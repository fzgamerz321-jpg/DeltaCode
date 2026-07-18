package com.deltacode.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import com.deltacode.component.theme.AccentPrimary
import com.deltacode.component.theme.TextPrimary
import kotlinx.coroutines.delay

/**
 * Animated typing text that reveals characters one-by-one
 * with a blinking cursor at the end.
 *
 * @param text Full text to reveal.
 * @param typingDelayMs Delay between each character in milliseconds.
 * @param style Text style applied to the animated text.
 * @param modifier Modifier for the root layout.
 */
@Composable
fun DeltaTypingText(
    text: String,
    modifier: Modifier = Modifier,
    typingDelayMs: Long = 80L,
    style: TextStyle = MaterialTheme.typography.displayLarge,
    textColor: androidx.compose.ui.graphics.Color = TextPrimary
) {
    var charIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(text) {
        charIndex = 0
        for (i in text.indices) {
            delay(typingDelayMs)
            charIndex = i + 1
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "cursor_blink")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 530, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursor_alpha"
    )

    Row(modifier = modifier) {
        Text(
            text = text.take(charIndex),
            style = style,
            color = textColor
        )
        if (charIndex < text.length || charIndex == text.length) {
            Text(
                text = "│",
                style = style,
                color = AccentPrimary,
                modifier = Modifier.alpha(cursorAlpha)
            )
        }
    }
}
