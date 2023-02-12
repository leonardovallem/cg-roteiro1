import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import components.ColorPicker
import components.Counter
import components.DisappearingSimpleButton
import components.SimpleButton
import components.TransformationsDialog
import utils.MouseEvent
import utils.Point
import utils.draw
import utils.toHexString
import utils.transform
import utils.withColor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
@Preview
fun App() {
    val focusManager = LocalFocusManager.current

    var mouseEvent by remember { mutableStateOf(MouseEvent.Up) }
    var drawings by remember { mutableStateOf(emptyList<Point>()) }
    var currentPixels by remember { mutableStateOf(0) }

    var showColorPicker by remember { mutableStateOf(false) }
    var showTransformationsDialog by remember { mutableStateOf(false) }

    var pixelScale by remember { mutableStateOf(1) }
    var color by remember { mutableStateOf(Color.Black) }

    var x by remember { mutableStateOf("") }
    var y by remember { mutableStateOf("") }
    val validPoint by remember {
        derivedStateOf { x.toFloatOrNull() != null && y.toFloatOrNull() != null }
    }

    fun addDrawing() {
        if (!validPoint) return

        drawings = drawings + Point(x, y, color)
        x = ""
        y = ""
    }

    fun clearLastDrawing() {
        drawings = drawings.dropLast(currentPixels)
    }

    if (showColorPicker) ColorPicker(
        value = color,
        onChange = { color = it },
        onDismiss = { showColorPicker = false }
    )

    if (showTransformationsDialog) TransformationsDialog(
        onDismiss = { showTransformationsDialog = false }
    ) { transformation, xFactor, yFactor ->
        drawings = drawings.transform(transformation, xFactor, yFactor)
    }

    LaunchedEffect(mouseEvent) {
        if (mouseEvent == MouseEvent.Down) currentPixels = 0
    }

    MaterialTheme {
        Row(
            modifier = Modifier.fillMaxSize()
                .onPreviewKeyEvent {
                    when {
                        it.key == Key.Tab -> {
                            focusManager.moveFocus(FocusDirection.Next)
                            true
                        }

                        it.isCtrlPressed && it.key == Key.Enter -> {
                            addDrawing()
                            true
                        }

                        else -> false
                    }
                }
        ) {
            Canvas(
                modifier = Modifier
                    .padding(16.dp)
                    .border(1.dp, Color.Gray)
                    .padding(1.dp)
                    .weight(7f)
                    .fillMaxHeight()
                    .onPointerEvent(PointerEventType.Release) { mouseEvent = MouseEvent.Up }
                    .onPointerEvent(PointerEventType.Press) { mouseEvent = MouseEvent.Down }
                    .onPointerEvent(PointerEventType.Move) {
                        if (mouseEvent == MouseEvent.Down) {
                            drawings = drawings + it.calculateCentroid().withColor(color)
                            currentPixels++
                        }
                    }
                    // TODO make it work
                    .onPreviewKeyEvent {
                        if (it.isCtrlPressed && it.key == Key.Z) {
                            clearLastDrawing()
                            true
                        } else false
                    }
            ) {
                draw(drawings, pixelScale = pixelScale.toFloat())
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .weight(3f)
                    .padding(24.dp)
            ) {
                Counter(
                    value = pixelScale,
                    onValueChange = { pixelScale = it },
                    label = "Pixel scale"
                )

                Text(
                    text = "Add pixel",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextField(
                        value = x,
                        onValueChange = { x = it },
                        label = { Text("X") },
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        modifier = Modifier.weight(1f),
                    )
                    TextField(
                        value = y,
                        onValueChange = { y = it },
                        label = { Text("Y") },
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        modifier = Modifier.weight(1f),
                    )
                }

                DisappearingSimpleButton(
                    label = "Desenhar pixel",
                    onClick = ::addDrawing,
                    show = validPoint
                )

                Spacer(modifier = Modifier.weight(1f))

                SimpleButton(label = "Cor", onClick = { showColorPicker = true }) {
                    Icon(
                        imageVector = Icons.Rounded.Palette,
                        contentDescription = "Current color: ${color.toHexString()}",
                        tint = color,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }

                DisappearingSimpleButton(
                    label = "Transformar",
                    onClick = { showTransformationsDialog = true },
                    show = drawings.isNotEmpty()
                )

                DisappearingSimpleButton(
                    label = "Apagar",
                    onClick = { drawings = emptyList() },
                    show = drawings.isNotEmpty()
                )
            }
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Roteiro 1"
    ) {
        App()
    }
}
