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

import android.animation.*
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView


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
        animator.disableViewDuringAnimation(fadeButton)

        animator.start()
    }

    private fun colorizer() {
        // we can animate any property as long as we tell ObjectAnimator how to access it
        // here we change the backgroundColor

        // this yields flashy result since colors are interpreted as Ints -> looks shitty
        //val animator = ObjectAnimator.ofInt(star.parent, "backgroundColor", Color.BLACK, Color.RED).start()
        // instead we want to use Argb to understand colors
        // it pins the target api level to 21 though
        val animator = ObjectAnimator.ofArgb(star.parent, "backgroundColor", Color.BLACK, Color.RED)
        // do the usual stuff
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.duration=500
        animator.disableViewDuringAnimation(colorizeButton)

        animator.start()


    }

    private fun shower() {
        val container = star.parent as ViewGroup
        val containerWidth = container.width
        val containerHeight = container.height
        var starW = star.width.toFloat()
        var starH = star.height.toFloat()

        // creating new View that can hold VectorDrawable
        val newStar = AppCompatImageView(this)
        // assign star
        newStar.setImageResource(R.drawable.ic_star)
        // set Layout parameter
        newStar.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)
        // adding the new View
        container.addView(newStar)

        // adding random size for new star
        newStar.scaleX = Math.random().toFloat() * 1.5f + .1f
        // same height and width
        newStar.scaleY = newStar.scaleX
        // cache the size of the new star
        starW *=newStar.scaleX
        starH *=newStar.scaleY

        // positioning:it should appear randomly somewhere from the left edge to the right edge
        // it can show half-way off the screen on the left (-starW / 2)
        // to half-way off the screen on the right (with the star positioned at (containerW - starW / 2)
        newStar.translationX = Math.random().toFloat() * containerWidth - starW / 2

        // make the star fall accelerating
        val mover = ObjectAnimator.ofFloat(newStar, View.TRANSLATION_Y,
            -starH, containerHeight + starH)
        mover.interpolator = AccelerateInterpolator(1f)
        // rotate linear
        val rotator = ObjectAnimator.ofFloat(newStar, View.ROTATION,
            (Math.random() * 1080).toFloat())
        rotator.interpolator = LinearInterpolator()

        // running the animations parallel
        val set = AnimatorSet()
        set.playTogether(mover, rotator)
        //random animation duration
        set.duration = (Math.random() * 1500 + 500).toLong()

        // remove the view at the end of the animation via listener
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                container.removeView(newStar)
            }
        })
        // start
        set.start()
    }

}
