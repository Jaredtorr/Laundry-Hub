package com.example.mylavanderiapp.features.machines.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.machines.domain.entities.Machine

@Composable
fun DeleteMachineDialog(
    machine: Machine,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor   = SurfaceWhite,
        shape            = RoundedCornerShape(24.dp),
        icon = {
            Icon(
                imageVector        = Icons.Filled.Warning,
                contentDescription = null,
                tint               = ErrorRed
            )
        },
        title = {
            Text(
                text       = "¿Eliminar máquina?",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                color      = TextDark
            )
        },
        text = {
            Text(
                text       = "¿Estás seguro de que deseas eliminar ${machine.name}?",
                fontFamily = Poppins,
                color      = TextMid
            )
        },
        confirmButton = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(ErrorRed)
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                TextButton(onClick = onConfirm) {
                    Text(
                        text       = "Eliminar",
                        color      = Color.White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text       = "Cancelar",
                    color      = TextMid,
                    fontFamily = Poppins
                )
            }
        }
    )
}