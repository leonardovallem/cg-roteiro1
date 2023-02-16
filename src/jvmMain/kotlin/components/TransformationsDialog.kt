package components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import utils.Point
import utils.Transformation
import utils.Transformations.mirrorCenter
import utils.Transformations.mirrorX
import utils.Transformations.mirrorY
import utils.Transformations.rotate
import utils.Transformations.scale
import utils.Transformations.shearX
import utils.Transformations.shearY
import utils.Transformations.translate
import utils.isDigitOrMinus

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TransformationsDialog(
    onDismiss: () -> Unit,
    points: List<Point>,
    canvasSize: IntSize,
    onTransform: (List<Point>) -> Unit,
) {
    var selectedTransformation by remember { mutableStateOf<Transformation?>(null) }
    var xFactor by remember { mutableStateOf("") }
    var yFactor by remember { mutableStateOf("") }
    var factor by remember { mutableStateOf("") }

    val xFactorFloat by remember { derivedStateOf { xFactor.toFloatOrNull() } }
    val yFactorFloat by remember { derivedStateOf { yFactor.toFloatOrNull() } }
    val factorFloat by remember { derivedStateOf { factor.toFloatOrNull() } }

    Dialog(
        onCloseRequest = onDismiss,
        title = "Transformations",
        state = rememberDialogState(height = 600.dp)
    ) {
        AnimatedContent(targetState = selectedTransformation == null) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (it) items(Transformation.all) {
                    ListItem(
                        headlineText = { Text(it.name) },
                        leadingContent = { Icon(imageVector = it.icon, contentDescription = null) },
                        modifier = Modifier.clickable { selectedTransformation = it }
                    )
                } else {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            TextButton(onClick = {
                                selectedTransformation = null
                                xFactor = ""
                                yFactor = ""
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.ArrowBack,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Clear")
                            }

                            TextButton(
                                onClick = {
                                    onDismiss()
                                    selectedTransformation?.let { transformation ->
                                        onTransform(
                                            when (transformation) {
                                                Transformation.Scale -> points.map {
                                                    it.scale(
                                                        xFactor = xFactorFloat ?: 0f,
                                                        yFactor = yFactorFloat ?: 0f
                                                    )
                                                }

                                                Transformation.Translate -> points.map {
                                                    it.translate(
                                                        xFactor = xFactorFloat ?: 0f,
                                                        yFactor = yFactorFloat ?: 0f
                                                    )
                                                }

                                                Transformation.Rotate -> points.map {
                                                    it.rotate(factorFloat ?: 0f)
                                                }

                                                Transformation.ShearX -> points.map {
                                                    it.shearX(factorFloat ?: 0f)
                                                }

                                                Transformation.ShearY -> points.map {
                                                    it.shearY(factorFloat ?: 0f)
                                                }

                                                Transformation.MirrorX -> points.mirrorX(canvasSize.width)
                                                Transformation.MirrorY -> points.mirrorY(canvasSize.height)
                                                Transformation.MirrorCenter -> points.mirrorCenter(canvasSize)
                                            }
                                        )
                                    }
                                },
                                enabled = selectedTransformation?.let { type ->
                                    if (type.hasOneFactor()) factorFloat != null && factorFloat != 0f
                                    else if (type.hasTwoFactors()) listOf(
                                        xFactorFloat,
                                        yFactorFloat
                                    ).any { it != null && it != 0f }
                                    else true
                                } ?: false,
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Done,
                                    contentDescription = null
                                )

                                Spacer(modifier = Modifier.width(12.dp))
                                Text(selectedTransformation?.name ?: "Transform")
                            }
                        }
                    }

                    if (selectedTransformation?.hasTwoFactors() == true) item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(24.dp)
                        ) {
                            TextField(
                                value = xFactor,
                                onValueChange = { value ->
                                    if (value.all { it.isDigitOrMinus() }) xFactor = value
                                },
                                label = { Text("X Factor") },
                                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                                modifier = Modifier.weight(1f),
                            )

                            TextField(
                                value = yFactor,
                                onValueChange = { value ->
                                    if (value.all { it.isDigitOrMinus() }) yFactor = value
                                },
                                label = { Text("Y Factor") },
                                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                                modifier = Modifier.weight(1f),
                            )
                        }
                    } else if (selectedTransformation?.hasOneFactor() == true) item {
                        TextField(
                            value = factor,
                            onValueChange = { value ->
                                if (value.all { it.isDigitOrMinus() }) factor = value
                            },
                            label = { Text("Factor") },
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                        )
                    } else item {
                        Text(
                            text = "Apply now",
                            style = MaterialTheme.typography.labelLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        )
                    }
                }
            }
        }
    }
}