package utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.OpenInFull
import androidx.compose.material.icons.rounded.OpenWith
import androidx.compose.material.icons.rounded.Rotate90DegreesCcw
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Transformation(val transform: Point.(xFactor: Float, yFactor: Float) -> Point) {
    object Scale : Transformation({ xFactor, yFactor -> copy(x = x * xFactor, y = y * yFactor) })
    object Translate : Transformation({ xFactor, yFactor -> copy(x = x + xFactor, y = y + yFactor) })
    object Rotate : Transformation({ xFactor, yFactor -> TODO() })

    val name: String
        get() = when (this) {
            Scale -> "Scale"
            Translate -> "Translate"
            Rotate -> "Rotate"
        }

    val icon: ImageVector
        get() = with(Icons.Rounded) {
            when (this@Transformation) {
                Scale -> OpenInFull
                Translate -> OpenWith
                Rotate -> Rotate90DegreesCcw
            }
        }

    companion object {
        val all by lazy { listOf(Scale, Translate, Rotate) }
    }
}