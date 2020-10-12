package by.tska.drawApp

import android.content.ClipData
import android.graphics.Color
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.jaredrummler.android.colorpicker.ColorShape


class MainActivity : AppCompatActivity(), ColorPickerDialogListener {

    private lateinit var drawingView: DrawingView
    private lateinit var dropLayout: RelativeLayout
    private lateinit var params: RelativeLayout.LayoutParams
    private var editTextList = mutableListOf<View>()

    private lateinit var addText: Button
    private lateinit var colorButton: Button
    private lateinit var resetButton: Button
    private var dragListener: View.OnDragListener = View.OnDragListener { v, event ->
        val view: View = event.localState as View
        val vg = v.parent as ViewGroup
        val rl = vg.findViewById<View>(R.id.dropLayout) as RelativeLayout
        var x: Int
        var y: Int
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> params =
                    view.layoutParams as RelativeLayout.LayoutParams
            DragEvent.ACTION_DRAG_ENTERED -> {
                x = event.x.toInt()
                y = event.y.toInt()
            }
            DragEvent.ACTION_DRAG_EXITED -> {
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                x = event.x.toInt()
                y = event.y.toInt()
            }
            DragEvent.ACTION_DRAG_ENDED -> {
            }
            DragEvent.ACTION_DROP -> {
                val childCountDropped = rl.childCount
                println("Child Count of Views::::::Dropped$childCountDropped")
                x = event.x.toInt()
                y = event.y.toInt()
                params.leftMargin = x
                params.topMargin = y
                view.layoutParams = params
                view.visibility = View.VISIBLE
            }
            else -> {
            }
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawingView)
        dropLayout = findViewById(R.id.dropLayout)
        dropLayout.setOnDragListener(dragListener)
        dropLayout.setOnTouchListener(onTouchListener)
        addText = findViewById<View>(R.id.textButton) as Button
        addText.setOnClickListener{ v ->
            val relativeParent = v.parent as ViewGroup
            val editText = EditText(v.context)
            editText.id = View.generateViewId()
            editText.tag = "editText${editTextList.size}"
            editTextList.add(editText)
            relativeParent.addView(editText)
            editText.setOnLongClickListener { v ->
                val dragData = ClipData.newPlainText("", "")
                val shadowBuilder = View.DragShadowBuilder(v)
                v.startDrag(dragData, shadowBuilder, v, 0)
                v.visibility = View.INVISIBLE
                true
            }
        }

        colorButton = findViewById(R.id.colorButton)
        colorButton.setOnClickListener { createColorPickerDialog() }

        resetButton = findViewById(R.id.resetButton)
        resetButton.setOnClickListener {
            if (editTextList.isNotEmpty()) {
                for (i in editTextList.size - 1 downTo 0) {
                    findViewById<RelativeLayout>(R.id.onDraglayout).removeView(findViewById(editTextList[i].id))
                }
                editTextList.clear()
            }
            drawingView.resetPaths()
        }
    }

    override fun onColorSelected(dialogId: Int, color: Int) {
        drawingView.changeableColor = color
    }

    private fun createColorPickerDialog() {
        ColorPickerDialog.newBuilder()
            .setColor(Color.RED)
            .setDialogType(ColorPickerDialog.TYPE_PRESETS)
            .setAllowCustom(true)
            .setAllowPresets(true)
            .setColorShape(ColorShape.CIRCLE)
            .setDialogId(1)
            .show(this)
    }

    override fun onDialogDismissed(dialogId: Int) {}

}