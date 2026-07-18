package com.deltacode.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.deltacode.component.theme.AccentPrimary
import com.deltacode.component.theme.BgSurface
import com.deltacode.component.theme.TextMuted
import com.deltacode.component.theme.TextPrimary

/**
 * Sort option chip used in the sort bar.
 *
 * @param label Display text (e.g. "Time", "Name", "Size").
 * @param isSelected Whether this chip is the active sort.
 * @param onClick Callback when tapped.
 * @param modifier Modifier for the chip.
 */
@Composable
fun DeltaSortChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) AccentPrimary else BgSurface,
        animationSpec = tween(200),
        label = "sort_chip_bg"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) TextPrimary else TextMuted,
        animationSpec = tween(200),
        label = "sort_chip_text"
    )

    Text(
        text = if (isSelected) "$label ↓" else label,
        style = MaterialTheme.typography.labelLarge,
        color = textColor,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    )
}

/**
 * Horizontal sort bar containing sort chips and a "Manage" action.
 *
 * @param options List of sort option labels.
 * @param selectedIndex Index of the currently active sort.
 * @param onSortSelected Callback with the index of the newly selected sort.
 * @param onManageClick Callback when "Manage" is tapped.
 * @param modifier Modifier for the bar.
 */
@Composable
fun DeltaSortBar(
    options: List<String>,
    selectedIndex: Int,
    onSortSelected: (Int) -> Unit,
    onManageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AccentPrimary.copy(alpha = 0.1f))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEachIndexed { index, label ->
                DeltaSortChip(
                    label = label,
                    isSelected = index == selectedIndex,
                    onClick = { onSortSelected(index) }
                )
            }
        }

        Text(
            text = "Manage",
            style = MaterialTheme.typography.labelLarge,
            color = AccentPrimary,
            modifier = Modifier
                .clickable(onClick = onManageClick)
                .padding(8.dp)
        )
    }
}
