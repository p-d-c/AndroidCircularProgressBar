package com.example.rtw_android_challenge

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.ProgressBar


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        // Loading views
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val icon = findViewById<ImageView>(R.id.icon)

        // For tracking stroke
        var clapCount: Int = 0

        // For animating icon
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 500
        animator.addUpdateListener { animation ->
            val value = (animation.animatedValue as Float)
                .toFloat()
            icon.alpha = 1f
            icon.translationX = (150.0 * Math.sin(value * Math.PI)).toFloat()
            icon.translationY = (150.0 * Math.cos(value * Math.PI)).toFloat()
            icon.animate().interpolator = DecelerateInterpolator()
            icon.animate().alpha(0f)
        }

        // For updating stroke on click
        progressBar.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val scaleUpX = ObjectAnimator.ofFloat(
                            progressBar, "scaleX", 1.5f
                        )
                        val scaleUpY = ObjectAnimator.ofFloat(
                            progressBar, "scaleY", 1.5f
                        )
                        scaleUpX.duration = 250
                        scaleUpY.duration = 250
                        val scaleUp = AnimatorSet()
                        scaleUp.play(scaleUpX).with(scaleUpY)
                        scaleUp.start()
                    }
                    MotionEvent.ACTION_UP -> {
                        val scaleDownX = ObjectAnimator.ofFloat(
                            progressBar,
                            "scaleX", 1f
                        )
                        val scaleDownY = ObjectAnimator.ofFloat(
                            progressBar,
                            "scaleY", 1f
                        )
                        scaleDownX.duration = 250
                        scaleDownY.duration = 250
                        val scaleDown = AnimatorSet()
                        scaleDown.play(scaleDownX).with(scaleDownY)
                        scaleDown.start()

                        // Update once per click, not per event
                        clapCount++
                        if (clapCount > 1) {
                            progressBar.incrementProgressBy(1)
                        }

                        // Move icon
                        icon.visibility = View.VISIBLE
                        animator.start()
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
    }
}
