package com.example.mylavanderiapp.features.auth.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun RegisterHeader(onNavigateToLogin: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(
                Brush.linearGradient(
                    colors = listOf(AccentCyan, Brand, BrandDark),
                    start = androidx.compose.ui.geometry.Offset(1400f, 0f),
                    end = androidx.compose.ui.geometry.Offset(0f, 800f)
                )
            )
    ) {
        Box(Modifier.size(160.dp).align(Alignment.TopStart).offset(x = (-50).dp, y = (-40).dp).clip(CircleShape).background(Color.White.copy(alpha = 0.08f)))
        Box(Modifier.size(90.dp).align(Alignment.BottomEnd).offset(x = 30.dp, y = 30.dp).clip(CircleShape).background(BrandDark.copy(alpha = 0.20f)))

        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 52.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.18f))
                    .padding(horizontal = 14.dp, vertical = 5.dp)
            ) {
                Text("NUEVA CUENTA", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 2.sp)
            }
            Spacer(Modifier.height(10.dp))
            Text("Crear Cuenta", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            Text("Únete a Laundry Hub hoy", fontSize = 12.sp, color = Color.White.copy(alpha = 0.75f), modifier = Modifier.padding(top = 4.dp))
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 48.dp, start = 20.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.18f))
                .clickable {
                    android.util.Log.d("RegisterHeader", "Back button clicked")
                    onNavigateToLogin()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.White, modifier = Modifier.size(20.dp))
        }
    }
}