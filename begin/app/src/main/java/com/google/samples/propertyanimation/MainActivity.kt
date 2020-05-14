/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.propertyanimation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView


class MainActivity : AppCompatActivity() {

    private lateinit var star: ImageView
    private lateinit var rotateButton: Button
    private lateinit var translateButton: Button
    private lateinit var scaleButton: Button
    private lateinit var fadeButton: Button
    private lateinit var colorizeButton: Button
    private lateinit var showerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        star = findViewById(R.id.star)
        rotateButton = findViewById<Button>(R.id.rotateButton)
        translateButton = findViewById<Button>(R.id.translateButton)
        scaleButton = findViewById<Button>(R.id.scaleButton)
        fadeButton = findViewById<Button>(R.id.fadeButton)
        colorizeButton = findViewById<Button>(R.id.colorizeButton)
        showerButton = findViewById<Button>(R.id.showerButton)

        rotateButton.setOnClickListener {
            rotater()
        }

        translateButton.setOnClickListener {
            translater()
        }

        scaleButton.setOnClickListener {
            scaler()
        }

        fadeButton.setOnClickListener {
            fader()
        }

        colorizeButton.setOnClickListener {
            colorizer()
        }

        showerButton.setOnClickListener {
            shower()
        }
    }

    // add extension function to disable buttons while animation is in progress
    private fun ObjectAnimator.disableViewDuringAnimation(view: View){
        // we are adding a Listener and use an adapter class
        // which provides default implementations of all of the listener methods)
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled=false
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled=true
            }
        })
    }

    private fun rotater() {
        val animator  = ObjectAnimator.ofFloat(star, View.ROTATION, -360f, 0f)
        // change the animation duration to one second
        animator.duration = 1000
        // deactivate rotate button while animation is running
        animator.disableViewDuringAnimation(rotateButton)
        animator.start()
    }

    private fun translater() {
        // we move/translate on x axis for 200px
        val animator = ObjectAnimator.ofFloat(
            star, View.TRANSLATION_X, 500f
        )
        // we want the animation to repeat back to the original position
        // repeatCount controls how many times it repeats after the first run
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE

        animator.disableViewDuringAnimation(translateButton)

        animator.start()

    }

    private fun scaler() {
        // we want to animate 2 properties at the same time
        // so we have to use PropertyValuesHolder
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)
        //ofPropertyValuesholder accepts any number of PropertyValuesHolder so all of them run parallel
        val animator = ObjectAnimator.ofPropertyValuesHolder(star, scaleX, scaleY)
        //reverse the animation
        animator.repeatCount=1
        animator.repeatMode= ObjectAnimator.REVERSE
        animator.duration=1000
        animator.disableViewDuringAnimation(scaleButton)
        animator.start()
    }

    private fun fader() {
        // using ALPHA: 0=invisible, 1 visible
        val animator = ObjectAnimator.ofFloat(
            star, View.ALPHA, 0f
        )
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.duration=1000
        animator.disableViewDuringAnimation(translateButton)

        animator.start()
    }

    private fun colorizer() {
    }

    private fun shower() {
    }

}
