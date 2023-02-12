package components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SimpleButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Button(onClick = onClick, enabled = enabled, modifier = modifier) {
        leadingIcon?.let {
            it()
            Spacer(modifier = Modifier.width(12.dp))
        }

        Text(label)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DisappearingSimpleButton(
    label: String,
    onClick: () -> Unit,
    show: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    AnimatedContent(targetState = show) {
        if (it) SimpleButton(
            label = label,
            onClick = onClick,
            enabled = enabled,
            modifier = modifier
        )
    }
}