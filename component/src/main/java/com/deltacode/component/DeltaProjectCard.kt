package com.deltacode.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.deltacode.component.theme.BgElevated
import com.deltacode.component.theme.BgSurface
import com.deltacode.component.theme.TextMuted
import com.deltacode.component.theme.TextPrimary
import com.deltacode.component.theme.TextSecondary

/**
 * Project list item card for the dashboard.
 *
 * Displays project name, last-opened date, and size with
 * an overflow menu button.
 *
 * @param name Project display name.
 * @param dateText Formatted date string (e.g. "7/18/2026 4:28").
 * @param sizeText Formatted size string (e.g. "1.8M").
 * @param onClick Callback when the card body is tapped.
 * @param onMenuClick Callback when the overflow menu (⋯) is tapped.
 * @param modifier Modifier for the root layout.
 */
@Composable
fun DeltaProjectCard(
    name: String,
    dateText: String,
    sizeText: String,
    onClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(BgElevated)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail placeholder
        Spacer(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(BgSurface)
        )

        Spacer(modifier = Modifier.width(14.dp))

        // Project info
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                maxLines = 1
            )
            Text(
                text = dateText,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Text(
                text = sizeText,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }

        // Overflow menu
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Project options",
                tint = TextMuted
            )
        }
    }
}
