package components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Counter(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    range: IntRange = 1..Int.MAX_VALUE
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        label?.let {
            Text(text = it)
            Spacer(modifier = Modifier.weight(1f))
        }

        IconButton(onClick = { onValueChange(value - 1) }, enabled = value > range.first) {
            Icon(imageVector = Icons.Rounded.Remove, contentDescription = "Decrement")
        }

        Text(text = value.toString())

        IconButton(onClick = { onValueChange(value + 1) }, enabled = value < range.last) {
            Icon(imageVector = Icons.Rounded.Add, contentDescription = "Increment")
        }
    }
}