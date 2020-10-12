package by.tska.drawApp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val strokes = mutableListOf<Stroke>()

    var changeableColor: Int = Color.RED

    init {
        val brush = Paint(ANTI_ALIAS_FLAG).apply {
            color = changeableColor
            textSize = 10f
            strokeWidth = 8f
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
        }

        strokes.add(Stroke(Path(), brush))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (s in strokes) {
            canvas.drawPath(s.path, s.paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val eventX = event.x
        val eventY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val paint = Paint(ANTI_ALIAS_FLAG).apply {
                    color = changeableColor
                    textSize = 10f
                    strokeWidth = 8f
                    style = Paint.Style.STROKE
                    strokeJoin = Paint.Join.ROUND
                }
                strokes.add(Stroke(Path(), paint))
                strokes.last().path.moveTo(eventX, eventY)
                return true
            }
            MotionEvent.ACTION_MOVE -> strokes.last().path.lineTo(eventX, eventY)
            else -> return false
        }
        postInvalidate()
        return true
    }

    fun resetPaths() {
        strokes.forEach { it.path.reset() }
        invalidate()
    }

    data class Stroke(val path: Path, val paint: Paint)
}