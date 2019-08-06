package com.example.qrcodereader

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        checkPermissions()
        initQRCamera()

    }

    private fun checkPermissions() {
        // already we got permission.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            qr_view.resume()
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 999)
        }
    }

    companion object {
        const val REQUEST_CAMERA_PERMISSION:Int = 1
    }

    @SuppressLint("WrongConstant")
    private fun initQRCamera() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        val isReadPermissionGranted = (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        val isWritePermissionGranted = (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        val isCameraPermissionGranted = (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)

        if (isReadPermissionGranted && isWritePermissionGranted && isCameraPermissionGranted) {
            openQRCamera() // ← カメラ起動
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CAMERA_PERMISSION)
        }
    }

    private fun openQRCamera() {
        qr_view.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                if (result != null) {
                    onPause()
                    val qrResult:TextView = findViewById(R.id.result)
                    qrResult.setText("$result")
                    val intent = Intent(applicationContext,ResultActivity::class.java)
                    intent.putExtra(EXTRA_MESSAGE,result.toString())
                    startActivity(intent)
                }
            }

            override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) { }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            REQUEST_CAMERA_PERMISSION -> { initQRCamera() }
        }
    }
}
