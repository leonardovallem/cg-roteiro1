package utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.roundToInt

fun Float.normalizeToHexString() = (this * 255f).roundToInt().toString(16)

fun Color.toHexString() = "#${alpha.normalizeToHexString()}${red.normalizeToHexString()}${green.normalizeToHexString()}${blue.normalizeToHexString()}"

fun Offset.withColor(color: Color) = Point(x, y, color)

fun DrawScope.draw(pixels: List<Point>, pixelScale: Float = 1f) = pixels.forEach {
    drawPoints(
        points = listOf(it.toOffset()),
        pointMode = PointMode.Points,
        color = it.color,
        strokeWidth = pixelScale
    )
}
