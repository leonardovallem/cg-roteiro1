package utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class Point(val x: Float, val y: Float, val color: Color) {
    constructor(x: String, y: String, color: Color) : this(x.toFloat(), y.toFloat(), color)

    fun toOffset() = Offset(x, y)
}