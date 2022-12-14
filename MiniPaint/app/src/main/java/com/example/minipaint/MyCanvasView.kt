package com.example.minipaint

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import kotlin.math.abs

private const val STROKE_WIDTH = 12f

class MyCanvasView(context: Context): View(context) {

    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap

    private val drawColor = ResourcesCompat.getColor(resources, R.color.colorPaint,null)
    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorBackground,null)

    private val paint = Paint().apply{
        color = drawColor
//        Smooths out edges of what is drawn without affecting the shape
        isAntiAlias = true
//        Dithering affects how colors with higher-precision than the device are down sampled
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = STROKE_WIDTH
    }

    private var path = Path()

    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f

    private var currentX = 0f
    private var currentY = 0f

    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    private lateinit var frame: Rect

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if(::extraBitmap.isInitialized) extraBitmap.recycle()

        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)

        val inset = 40
        frame = Rect(inset, inset, width-inset, height -inset)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)
        canvas.drawRect(frame,paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when(event.action){
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE-> touchMove()
            MotionEvent.ACTION_UP-> touchUp()
        }

        return true
    }

    private fun touchUp() {
        path.reset()
    }

    private fun touchMove() {
//        Calc the distance that has been moved
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY-currentY)

//        if the distance was further than touch tolerance add a segement ot the path
        if(dx >= touchTolerance || dy >= touchTolerance){

//            quadTo creates a smoothly drawn line without corners from starting point
//            for the next segment to the endpoint of this segment
            path.quadTo(
                currentX,
                currentY,
                (motionTouchEventX + currentX) /2,
                (motionTouchEventY + currentY) / 2)

            currentX = motionTouchEventX
            currentY = motionTouchEventY

            extraCanvas.drawPath(path, paint)
        }
        invalidate()
    }

//    This method is called when the user first touches the screen
    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }
}