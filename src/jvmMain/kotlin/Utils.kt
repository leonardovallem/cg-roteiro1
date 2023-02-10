import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt

fun Float.normalizeToHexString() = (this * 255f).roundToInt().toString(16)

fun Color.toHexString() = "#${alpha.normalizeToHexString()}${red.normalizeToHexString()}${green.normalizeToHexString()}${blue.normalizeToHexString()}"
