package dev.luteoos.scrumbet.android.util.composeUtil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.luteoos.scrumbet.android.R

@Composable
fun VisibilityToggle(modifier: Modifier = Modifier.height(IntrinsicSize.Min), initialState: Boolean, onClick: (isVisible: Boolean) -> Unit) {
    var isVisible by remember { mutableStateOf(initialState) }
    val shape = RoundedCornerShape(size = Size.xSmall())
    val defaultModifier = Modifier.fillMaxHeight()

    LaunchedEffect(key1 = initialState, block = {
        isVisible = initialState
    })

    Row(
        modifier = modifier
            .padding(0.dp)
            .background(MaterialTheme.colorScheme.surface, shape),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        TextButton(
            modifier =
            defaultModifier,
            colors = if (isVisible) ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary, containerColor = MaterialTheme.colorScheme.primary) else ButtonDefaults.textButtonColors(),
            shape = shape,
            onClick = {
                onClick(true)
                isVisible = true
            }
        ) {
            Text(modifier = Modifier.padding(horizontal = Size.regular(), vertical = 0.dp), text = stringResource(R.string.label_show), fontSize = TextSize.xxSmall())
        }
        TextButton(
            modifier =
            defaultModifier,
            colors = if (!isVisible) ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary, containerColor = MaterialTheme.colorScheme.primary) else ButtonDefaults.textButtonColors(),
            shape = shape,
            onClick = {
                onClick(false)
                isVisible = false
            }
        ) {
            Text(modifier = Modifier.padding(horizontal = Size.regular(), vertical = 0.dp), text = stringResource(R.string.label_hide), fontSize = TextSize.xxSmall())
        }
    }
}
