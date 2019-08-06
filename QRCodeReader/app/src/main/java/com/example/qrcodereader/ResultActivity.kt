package com.example.qrcodereader

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.TextView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val getqr = getIntent()
        val message = intent.extras.getString(AlarmClock.EXTRA_MESSAGE)

        val mapper = jacksonObjectMapper()
        val key = mapper.readValue<Key>(message)
        val text: TextView = findViewById(R.id.textView)
        text.setText(key.toString())
    }
}
