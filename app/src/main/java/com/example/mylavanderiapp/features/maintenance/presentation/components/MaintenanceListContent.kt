package com.example.mylavanderiapp.features.maintenance.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.maintenance.domain.entities.MaintenanceRecord
import com.example.mylavanderiapp.features.maintenance.presentation.states.MaintenanceUIState

@Composable
fun MaintenanceListContent(
    uiState: MaintenanceUIState,
    selectedTab: Int,
    onResolve: (MaintenanceRecord) -> Unit,
    onDelete: (MaintenanceRecord) -> Unit,
    onRetry: () -> Unit
) {
    when (uiState) {
        is MaintenanceUIState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Brand)
            }
        }
        is MaintenanceUIState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(uiState.message, fontFamily = Poppins, color = ErrorRed)
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = onRetry) {
                        Text("Reintentar", fontFamily = Poppins, color = Brand)
                    }
                }
            }
        }
        is MaintenanceUIState.Success -> {
            val filtered = if (selectedTab == 0)
                uiState.records.filter { !it.isResolved }
            else
                uiState.records.filter { it.isResolved }

            if (filtered.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text       = if (selectedTab == 0) "Sin registros activos" else "Sin registros resueltos",
                        fontFamily = Poppins,
                        fontSize   = 14.sp,
                        color      = TextMid
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filtered) { record ->
                        MaintenanceCard(
                            record    = record,
                            onResolve = { onResolve(record) },
                            onDelete  = { onDelete(record) }
                        )
                    }
                }
            }
        }
        else -> {}
    }
}