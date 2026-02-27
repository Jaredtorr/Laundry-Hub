package com.example.mylavanderiapp.features.machines.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.presentation.states.MachinesUIState

@Composable
fun MachineListContent(
    uiState: MachinesUIState,
    onEdit: (Machine) -> Unit,
    onDelete: (Machine) -> Unit,
    onRetry: () -> Unit
) {
    when (uiState) {
        is MachinesUIState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Brand)
            }
        }

        is MachinesUIState.Success -> {
            val machines = uiState.machines
            if (machines.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text       = "No hay máquinas registradas",
                        fontFamily = Poppins,
                        color      = TextMid
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(machines) { machine ->
                        MachineCard(
                            machine  = machine,
                            onEdit   = { onEdit(machine) },
                            onDelete = { onDelete(machine) }
                        )
                    }
                }
            }
        }

        is MachinesUIState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text       = uiState.message,
                        fontFamily = Poppins,
                        color      = ErrorRed
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.horizontalGradient(listOf(BrandDark, Brand, AccentCyan))
                            )
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        TextButton(onClick = onRetry) {
                            Text(
                                text       = "Reintentar",
                                color      = Color.White,
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        else -> {}
    }
}