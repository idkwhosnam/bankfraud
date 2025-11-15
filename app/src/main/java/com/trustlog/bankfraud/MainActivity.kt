package com.trustlog.bankfraud

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.trustlog.bankfraud.databinding.ActivityMainBinding
import com.trustlog.bankfraud.sensor.SensorCollectionManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        handleMotionEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    private fun handleMotionEvent(event: MotionEvent) {
        val viewId = currentFocus?.resources?.getResourceEntryName(currentFocus?.id ?: -1)
            ?: "activity_root"
        SensorCollectionManager.logTouch(viewId, event)
    }
}
