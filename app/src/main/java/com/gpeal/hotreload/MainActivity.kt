package com.gpeal.hotreload

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import gpeal.hotreload.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        instructions1TextView.text = "Try modifying properties of this screen live!"
        instructions2TextView.text = """
adb shell am broadcast -a debug --es size 96x96
adb shell am broadcast -a debug --es color 0,0,255
adb shell am broadcast -a debug --ef rotation 45
adb shell am broadcast -a debug --es color 0,0,255 --ef rotation 45
adb shell am broadcast -a debug --es text "Hello\ World\!"
        """.trimIndent()
    }

    override fun onStart() {
        super.onStart()
        // This broadcast receiver will auto-register itself as a broadcast receiver.
        // You can pass an optional intentAction parameter.
        HotReloadReceiver(this) { intent ->
            // You can invoke this code block using `adb shell am broadcast ...`
            // You can use this for whatever you want. Here, we update a view's size, color, and text as an exmaple.
            if (intent.hasExtra("size")) {
                val (width, height) = intent.getStringExtra("size").split('x').map { it.toFloat().dp.toInt() }
                rectangleView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    this.width = width
                    this.height = height
                }
            }

            if (intent.hasExtra("color")) {
                val (r, g, b) = intent.getStringExtra("color").split(',').map(String::toInt)
                rectangleView.background = ColorDrawable(Color.argb(255, r, g, b))
            }

            if (intent.hasExtra("rotation")) {
                rectangleView.animate().rotation(intent.getFloatExtra("rotation", 0f)).start()
            }

            if (intent.hasExtra("text")) {
                liveText.text = intent.getStringExtra("text")
            }
        }
    }
}

val Number.dp get() = toFloat() * Resources.getSystem().displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT
