package com.example.mylavanderiapp.features.laundry_reservation.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.*

@Composable
fun ReservationHeader(
    available: Int,
    occupied: Int,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(
                Brush.linearGradient(
                    colors = listOf(BrandDark, Brand, AccentCyan),
                    start  = Offset(0f, 0f),
                    end    = Offset(1400f, 800f)
                )
            )
    ) {
        Box(
            Modifier
                .size(180.dp)
                .align(Alignment.TopEnd)
                .offset(x = 60.dp, y = (-50).dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.07f))
        )
        Box(
            Modifier
                .size(100.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-30).dp, y = 30.dp)
                .clip(CircleShape)
                .background(AccentCyan.copy(alpha = 0.15f))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 52.dp, start = 24.dp, end = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Lavadoras 🧺",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize   = 22.sp,
                        color      = Color.White
                    )
                    Text(
                        "Aparta tu lavadora",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Normal,
                        fontSize   = 13.sp,
                        color      = Color.White.copy(alpha = 0.80f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                        .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onLogout) {
                        Icon(
                            Icons.Filled.Logout,
                            contentDescription = "Salir",
                            tint     = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ReservationStatChip(
                    label    = "$available Disponibles",
                    color    = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
                ReservationStatChip(
                    label    = "$occupied Ocupadas",
                    color    = ErrorRed,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}