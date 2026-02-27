package com.example.mylavanderiapp.features.laundry_reservation.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.machines.domain.entities.Machine

@Composable
fun ReservationConfirmDialog(
    machine: Machine,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier  = Modifier.fillMaxWidth(),
            shape     = RoundedCornerShape(28.dp),
            colors    = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 18.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(BrandPale),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.LocalLaundryService,
                        contentDescription = null,
                        tint     = Brand,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    "¿Apartar lavadora?",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 20.sp,
                    color      = TextDark
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "Vas a apartar ${machine.name}${machine.location?.let { " en $it" } ?: ""}. Esta acción cambiará su estado a ocupada.",
                    fontFamily = Poppins,
                    fontSize   = 13.sp,
                    color      = TextMid,
                    textAlign  = TextAlign.Center,
                    modifier   = Modifier.padding(bottom = 24.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.horizontalGradient(listOf(BrandDark, Brand, AccentCyan))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick  = onConfirm,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            "SÍ, APARTAR",
                            fontFamily    = Poppins,
                            fontWeight    = FontWeight.Bold,
                            fontSize      = 15.sp,
                            color         = Color.White,
                            letterSpacing = 1.5.sp
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                TextButton(
                    onClick  = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Cancelar",
                        fontFamily = Poppins,
                        fontSize   = 14.sp,
                        color      = TextMid
                    )
                }
            }
        }
    }
}