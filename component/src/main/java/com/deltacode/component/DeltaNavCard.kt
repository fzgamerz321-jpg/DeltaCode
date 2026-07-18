package com.deltacode.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.deltacode.component.theme.AccentPrimary
import com.deltacode.component.theme.BgElevated
import com.deltacode.component.theme.BgSurface
import com.deltacode.component.theme.TextMuted
import com.deltacode.component.theme.TextPrimary

/**
 * Navigation card used in the dashboard nav row.
 *
 * Displays an icon and label. Highlights with the accent color
 * when [isSelected] is true.
 *
 * @param label Display label for the card.
 * @param icon Material icon vector.
 * @param isSelected Whether this card is currently active.
 * @param onClick Callback when the card is tapped.
 * @param modifier Modifier for the root layout.
 */
@Composable
fun DeltaNavCard(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) AccentPrimary.copy(alpha = 0.15f) else BgSurface,
        animationSpec = tween(durationMillis = 200),
        label = "nav_card_bg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) AccentPrimary else BgElevated,
        animationSpec = tween(durationMillis = 200),
        label = "nav_card_border"
    )
    val iconTint by animateColorAsState(
        targetValue = if (isSelected) AccentPrimary else TextMuted,
        animationSpec = tween(durationMillis = 200),
        label = "nav_card_icon_tint"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) TextPrimary else TextMuted,
        animationSpec = tween(durationMillis = 200),
        label = "nav_card_text"
    )

    Column(
        modifier = modifier
            .widthIn(min = 90.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = textColor,
            maxLines = 1
        )
    }
}
