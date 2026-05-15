package com.example.projectconnect.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PunkTitle(
    text: String,
    modifier: Modifier = Modifier,
    fontSizeSp: Int = 30,
    shadowBlur: Float = 9f,
    glitchOffsetDp: Int = 1
) {
    Box(modifier = modifier) {
        Text(
            text = text,
            modifier = Modifier.offset(x = (-glitchOffsetDp).dp, y = glitchOffsetDp.dp),
            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.55f),
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = fontSizeSp.sp),
            fontWeight = FontWeight.Black
        )
        Text(
            text = text,
            modifier = Modifier.offset(x = glitchOffsetDp.dp, y = (-1).dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.52f),
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = fontSizeSp.sp),
            fontWeight = FontWeight.Black
        )
        Text(
            text = text,
            color = Color(0xFFEAD7FF),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = fontSizeSp.sp,
                shadow = Shadow(
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.38f),
                    offset = Offset(0f, 0f),
                    blurRadius = shadowBlur
                )
            ),
            fontWeight = FontWeight.Black
        )
    }
}
