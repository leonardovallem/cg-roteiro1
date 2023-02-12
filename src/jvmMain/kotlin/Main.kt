import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import components.ColorPicker
import components.DisappearingSimpleButton
import components.SimpleButton

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
)
@Composable
@Preview
fun App() {
    val focusManager = LocalFocusManager.current

    val drawings = remember { mutableStateListOf<Point>() }

    var showColorPicker by remember { mutableStateOf(false) }
    var x by remember { mutableStateOf("") }
    var y by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(Color.Black) }
    val validPoint by remember {
        derivedStateOf { x.toFloatOrNull() != null && y.toFloatOrNull() != null }
    }

    fun addDrawing() {
        if (!validPoint) return

        drawings.add(Point(x, y, color))
        x = ""
        y = ""
    }

    if (showColorPicker) ColorPicker(
        value = color,
        onChange = { color = it },
        onDismiss = { showColorPicker = false }
    )

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
//                    .pointerInput(Unit) {
//                        detectDragGestures(matcher = PointerMatcher.Primary) {
//                            drawings.add(it)
//                        }
//                    }
            ) {
                draw(drawings)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .weight(3f)
                    .padding(24.dp)
            ) {
                TextField(value = x,
                    onValueChange = { x = it },
                    label = { Text("X") }
                )
                TextField(value = y,
                    onValueChange = { y = it },
                    label = { Text("Y") }
                )

                Spacer(modifier = Modifier.weight(1f))

                SimpleButton(
                    label = "Desenhar",
                    onClick = ::addDrawing,
                    enabled = validPoint
                )

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
                    onClick = {},
                    show = drawings.isNotEmpty()
                )

                DisappearingSimpleButton(
                    label = "Apagar",
                    onClick = drawings::clear,
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
