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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import utils.Transformation

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TransformationsDialog(
    onDismiss: () -> Unit,
    onTransform: (transformation: Transformation, xFactor: Float, yFactor: Float) -> Unit,
) {
    var selectedTransformation by remember { mutableStateOf<Transformation?>(null) }
    var xFactor by remember { mutableStateOf("") }
    var yFactor by remember { mutableStateOf("") }

    val xFactorFloat by remember { derivedStateOf { xFactor.toFloatOrNull() } }
    val yFactorFloat by remember { derivedStateOf { yFactor.toFloatOrNull() } }

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
                                    selectedTransformation?.let {
                                        onTransform(it, yFactorFloat ?: 0f, yFactorFloat ?: 0f)
                                    }
                                },
                                enabled = listOf(xFactorFloat, yFactorFloat).any {
                                    it != null && it != 0f
                                },
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

                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(24.dp)
                        ) {
                            TextField(
                                value = xFactor,
                                onValueChange = { value ->
                                    if (value.all { it.isDigit() }) xFactor = value
                                },
                                label = { Text("X Factor") },
                                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                                modifier = Modifier.weight(1f),
                            )

                            TextField(
                                value = yFactor,
                                onValueChange = { value ->
                                    if (value.all { it.isDigit() }) yFactor = value
                                },
                                label = { Text("Y Factor") },
                                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
        }
    }
}