import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.DrawScope

data class Point(val x: Float, val y: Float, val color: Color) {
    constructor(x: String, y: String, color: Color) : this(x.toFloat(), y.toFloat(), color)

    fun toOffset() = Offset(x, y)
}

fun DrawScope.draw(pixels: List<Point>, pixelScale: Float = 1f) = pixels.forEach {
    drawPoints(
        points = listOf(it.toOffset()),
        pointMode = PointMode.Points,
        color = it.color,
        strokeWidth = pixelScale
    )
}
