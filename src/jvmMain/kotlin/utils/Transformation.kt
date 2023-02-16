package utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Flip
import androidx.compose.material.icons.rounded.OpenWith
import androidx.compose.material.icons.rounded.PhotoSizeSelectSmall
import androidx.compose.material.icons.rounded.Rotate90DegreesCcw
import androidx.compose.material.icons.rounded.Style
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.IntSize
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

object Transformations {
    fun Point.scale(xFactor: Float, yFactor: Float) = copy(x = x * xFactor, y = y * yFactor)
    fun Point.translate(xFactor: Float, yFactor: Float) = copy(x = x + xFactor, y = y + yFactor)
    fun Point.rotate(angle: Float): Point {
        val (sin, cos) = sin(angle) to cos(angle)
        return copy(x = (+x * cos) - (y * sin), y = (+x * sin) + (y * cos))
    }

    fun Point.shearX(factor: Float) = copy(x = x + (factor * y))
    fun Point.shearY(factor: Float) = copy(y = y + (factor * x))

    fun List<Point>.mirrorX(width: Int) = map { it.copy(x = abs(it.x - width)) }

    fun List<Point>.mirrorY(height: Int) = map { it.copy(y = abs(it.y - height)) }

    fun List<Point>.mirrorCenter(size: IntSize) = map {
        it.copy(x = abs(it.x - size.width), y = abs(it.y - size.height))
    }
}

enum class Transformation {
    Scale, Translate, Rotate, MirrorX, MirrorY, MirrorCenter, ShearX, ShearY;

    val icon: ImageVector
        get() = with(Icons.Rounded) {
            when (this@Transformation) {
                Scale -> PhotoSizeSelectSmall
                Translate -> OpenWith
                Rotate -> Rotate90DegreesCcw
                MirrorX, MirrorY, MirrorCenter -> Flip
                ShearX, ShearY -> Style
            }
        }

    fun hasTwoFactors() = this == Scale || this == Translate
    fun hasOneFactor() = this in listOf(Rotate, ShearX, ShearY)

    companion object {
        val all = values().toList()
    }
}
