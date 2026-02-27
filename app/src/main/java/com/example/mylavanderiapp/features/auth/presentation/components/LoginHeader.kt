package com.example.mylavanderiapp.features.auth.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.AccentCyan
import com.example.mylavanderiapp.core.ui.theme.Brand
import com.example.mylavanderiapp.core.ui.theme.BrandDark

@Composable
fun LoginHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .background(
                Brush.linearGradient(
                    colors = listOf(BrandDark, Brand, AccentCyan),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(1400f, 800f)
                )
            )
    ) {
        Box(Modifier.size(200.dp).align(Alignment.TopEnd).offset(x = 70.dp, y = (-50).dp).clip(CircleShape).background(Color.White.copy(alpha = 0.07f)))
        Box(Modifier.size(120.dp).align(Alignment.BottomStart).offset(x = (-30).dp, y = 40.dp).clip(CircleShape).background(AccentCyan.copy(alpha = 0.15f)))
        Box(Modifier.size(60.dp).align(Alignment.TopStart).offset(x = 40.dp, y = 40.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.05f)))

        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f))
                    .border(1.5.dp, Color.White.copy(alpha = 0.30f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.LocalLaundryService, contentDescription = null, modifier = Modifier.size(42.dp), tint = Color.White)
            }
            Spacer(Modifier.height(14.dp))
            Text("LAUNDRY HUB", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, letterSpacing = 4.sp)
            Text("Sign in to manage your orders", fontSize = 12.sp, color = Color.White.copy(alpha = 0.72f), modifier = Modifier.padding(top = 5.dp))
        }
    }
}