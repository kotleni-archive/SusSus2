package com.maksimov.caria.ui.utils

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.core.animation.addListener
import androidx.core.view.setMargins

class SpinningWheel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    View(context, attrs, defStyleAttr, defStyleRes) {

    private var items: List<Item>? = null
    private var animator: ValueAnimator? = null
    private var currentIndex = 0
    private var currentOffset = 0f

    fun setState(items: List<Item>, index: Int) {
        check(items.isNotEmpty()) { "Items must be not empty " }
        check(index >= 0 && index < items.size) { "Wrong index" }

        this.items = items
        currentIndex = index
    }


    override fun onDraw(canvas: Canvas) {
        val items = items ?: return

        fun Item.draw(verticalOffset: Int) {
            drawable.apply {
                setBounds((width.toFloat() / 4).toInt(), verticalOffset +(width.toFloat() / 7).toInt(), width - (width.toFloat() / 4).toInt(), verticalOffset + ((width + height).toFloat() / 5).toInt())
                draw(canvas)
            }
        }


        val mid = height / 2f
        val topMid = mid - width / 2f - currentOffset * width

        // Draw master picture
        items[currentIndex].draw(topMid.toInt())
        // end draw master


        var currentTopMid = topMid + width + SPACING_PX
        var currentTempIndex = currentIndex

        // Draw bottom of master
        while (currentTopMid <= height) {
            //get next index
            currentTempIndex = (currentTempIndex + 1) % items.size
            items[currentTempIndex].draw(currentTopMid.toInt())

            currentTopMid += width + SPACING_PX
        }


        // Draw up of master
        currentTempIndex = currentIndex
        currentTopMid = topMid - width - SPACING_PX
        while (currentTopMid + width >= 0) {
            //get next index
            currentTempIndex = (currentTempIndex - 1)
            if (currentTempIndex < 0) currentTempIndex += items.size

            items[currentTempIndex].draw(currentTopMid.toInt())

            currentTopMid -= width + SPACING_PX
        }
    }

    fun spin(count: Int, duration: Long = 10_000L, onCompleteListener: () -> Unit = {}) {
        val items = items ?: error("fuck this")
        animator?.cancel()
        animator = ValueAnimator
            .ofFloat(currentIndex.toFloat(), currentIndex + count.toFloat())
            .apply {
                this.duration = duration
                interpolator = AccelerateDecelerateInterpolator()

                addUpdateListener {
                    var currentValue = it.animatedValue as Float
                    while (currentValue < 0) currentValue += items.size

                    currentIndex = currentValue.toInt() % items.size
                    currentOffset = currentValue - currentValue.toInt()

                    invalidate()
                }

                addListener(onEnd = { onCompleteListener() })

                start()
            }
    }

    fun getMaster(): Item {
        val items = items ?: error("fuck this")
        return items[currentIndex]
    }

    fun getUp(): Item {
        val items = items ?: error("fuck this")
        return items[(currentIndex + 1) % items.size]
    }

    fun getDown(): Item {
        val items = items ?: error("fuck this")
        var currentTempIndex = (currentIndex - 1)
        if (currentTempIndex < 0) currentTempIndex += items.size
        return items[currentTempIndex]
    }

    interface Item {
        val drawable: Drawable
    }

    companion object {
        private const val SPACING_PX = 0
    }
}