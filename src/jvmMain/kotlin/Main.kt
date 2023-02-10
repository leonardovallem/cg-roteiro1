import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@Composable
@Preview
fun App() {
    val focusManager = LocalFocusManager.current

    val drawings = remember { mutableStateListOf<Offset>() }

    var showColorPicker by remember { mutableStateOf(false) }
    var x by remember { mutableStateOf("") }
    var y by remember { mutableStateOf("") }
    var color by remember { mutableStateOf(Color.Black) }
    val validPoint by remember {
        derivedStateOf { x.toFloatOrNull() != null && y.toFloatOrNull() != null }
    }


    fun addDrawing() {
        if (!validPoint) return

        drawings.add(Offset(x.toFloat(), y.toFloat()))
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
                    .weight(7f)
                    .border(2.dp, Color.Gray)
                    .pointerInput(Unit) {
                        detectDragGestures(matcher = PointerMatcher.Primary) {
                            drawings.add(it)
                        }
                    }
            ) {
                drawPoints(
                    drawings,
                    pointMode = PointMode.Points,
                    color = color,
                    strokeWidth = 3f
                )
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

                Button(
                    onClick = ::addDrawing,
                    enabled = validPoint
                ) {
                    Text("Desenhar")
                }

                OutlinedButton(
                    onClick = { showColorPicker = true },
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = color)
                ) {
                    Text("Cor")
                }

                Button(onClick = drawings::clear, enabled = drawings.isNotEmpty()) {
                    Text("Apagar")
                }
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
