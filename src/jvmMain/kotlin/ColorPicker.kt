import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import kotlin.math.roundToInt

@Composable
fun ColorPicker(
    value: Color,
    onChange: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    var alpha by remember { mutableStateOf(value.alpha) }
    var red by remember { mutableStateOf(value.red) }
    var green by remember { mutableStateOf(value.green) }
    var blue by remember { mutableStateOf(value.blue) }

    val currentColor by remember {
        derivedStateOf { Color(red, green, blue, alpha) }
    }

    Dialog(
        onCloseRequest = onDismiss,
        title = "Select a color",
        state = rememberDialogState(height = 600.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Select a color",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                TextButton(
                    onClick = {
                        onChange(currentColor)
                        onDismiss()
                    },
                    modifier = Modifier.padding(start = 24.dp)
                ) {
                    Text("Save")
                }
            }

            Text(
                text = currentColor.toHexString(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(currentColor)
                    .padding(24.dp)
            )

            ColorSlider(value = alpha, onValueChange = { alpha = it }, channel = ColorChannel.Alpha)
            ColorSlider(value = red, onValueChange = { red = it }, channel = ColorChannel.Red)
            ColorSlider(value = green, onValueChange = { green = it }, channel = ColorChannel.Green)
            ColorSlider(value = blue, onValueChange = { blue = it }, channel = ColorChannel.Blue)
        }
    }
}

@Composable
fun ColorSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    channel: ColorChannel,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.padding(24.dp)) {
        Text(
            channel.symbol.toString(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.CenterStart)
        )

        Slider(
            value,
            onValueChange,
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(thumbColor = channel.color),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 36.dp)
                .width(maxWidth)
        )

        Text(
            text = (value * 255f).roundToInt().toString(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

enum class ColorChannel(val symbol: Char, val color: Color) {
    Red('R', Color.Red),
    Green('G', Color.Green),
    Blue('B', Color.Blue),
    Alpha('A', Color.Gray)
}