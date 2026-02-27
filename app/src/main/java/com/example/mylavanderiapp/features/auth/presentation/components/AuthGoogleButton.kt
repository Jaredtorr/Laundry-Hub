package com.example.mylavanderiapp.features.auth.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.TextDark
import androidx.compose.foundation.Image
import com.example.mylavanderiapp.R
@Composable
fun AuthGoogleButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_google),
            contentDescription = "Google",
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text("Continue with Google", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
    }
}