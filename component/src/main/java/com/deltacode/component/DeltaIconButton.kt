package com.deltacode.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.deltacode.component.theme.TextMuted

/**
 * Reusable icon button with 48dp minimum touch target.
 *
 * @param icon Material icon vector.
 * @param contentDescription Accessibility label.
 * @param onClick Callback when tapped.
 * @param modifier Modifier for the button.
 * @param tint Icon tint color.
 */
@Composable
fun DeltaIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = TextMuted
) {
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
    )
}
