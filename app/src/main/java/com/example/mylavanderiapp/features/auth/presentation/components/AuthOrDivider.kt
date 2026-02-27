package com.example.mylavanderiapp.features.auth.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.TextMid

@Composable
fun AuthOrDivider() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = TextMid.copy(alpha = 0.2f))
        Text("  or  ", fontSize = 12.sp, color = TextMid)
        HorizontalDivider(modifier = Modifier.weight(1f), color = TextMid.copy(alpha = 0.2f))
    }
}