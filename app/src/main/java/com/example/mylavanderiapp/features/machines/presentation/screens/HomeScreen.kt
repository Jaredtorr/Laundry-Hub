package com.example.mylavanderiapp.features.machines.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.entities.MachineStatus
import com.example.mylavanderiapp.features.machines.presentation.components.MachineDialog
import com.example.mylavanderiapp.features.machines.presentation.states.MachineOperationState
import com.example.mylavanderiapp.features.machines.presentation.states.MachinesUIState
import com.example.mylavanderiapp.features.machines.presentation.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onLogout: () -> Unit
) {
    val uiState        by viewModel.uiState.collectAsState()
    val operationState by viewModel.operationState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showAddDialog   by remember { mutableStateOf(false) }
    var machineToEdit   by remember { mutableStateOf<Machine?>(null) }
    var machineToDelete by remember { mutableStateOf<Machine?>(null) }

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    LaunchedEffect(operationState) {
        when (operationState) {
            is MachineOperationState.Success -> {
                snackbarHostState.showSnackbar((operationState as MachineOperationState.Success).message)
                viewModel.resetOperationState()
            }
            is MachineOperationState.Error -> {
                snackbarHostState.showSnackbar(
                    message = (operationState as MachineOperationState.Error).message,
                    duration = SnackbarDuration.Long,
                    withDismissAction = true
                )
                viewModel.resetOperationState()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Brand,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir máquina", tint = Color.White)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(BgLight)
        ) {
            // Header con gradiente igual que login
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(BrandDark, Brand, AccentCyan),
                            start  = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end    = androidx.compose.ui.geometry.Offset(1400f, 800f)
                        )
                    )
            ) {
                // Burbujas decorativas
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

                // Contenido del header
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
                                "Lavandería 🧺",
                                fontFamily = Poppins,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize   = 22.sp,
                                color      = Color.White
                            )
                            Text(
                                "Panel de administración",
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Normal,
                                fontSize   = 13.sp,
                                color      = Color.White.copy(alpha = 0.80f)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Campana — lógica de notificaciones pendiente
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f))
                                    .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(onClick = { /* TODO: notificaciones */ }) {
                                    Icon(
                                        Icons.Filled.Notifications,
                                        contentDescription = "Notificaciones",
                                        tint     = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            // Logout
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
                    }

                    Spacer(Modifier.height(16.dp))

                    // Resumen de máquinas
                    val machines = (uiState as? MachinesUIState.Success)?.machines ?: emptyList()
                    val available   = machines.count { it.status == MachineStatus.AVAILABLE }
                    val occupied    = machines.count { it.status == MachineStatus.OCCUPIED }
                    val maintenance = machines.count { it.status == MachineStatus.MAINTENANCE }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MachineStatChip("$available Disponibles", Color(0xFF4CAF50), Modifier.weight(1f))
                        MachineStatChip("$occupied Ocupadas",     Color(0xFFF44336), Modifier.weight(1f))
                        MachineStatChip("$maintenance Mant.",     Color(0xFFFF9800), Modifier.weight(1f))
                    }
                }
            }

            // Contenido principal sobre el header
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn(tween(550)) + slideInVertically(
                    initialOffsetY = { 140 },
                    animationSpec  = tween(550, easing = EaseOutCubic)
                ),
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 200.dp)
                ) {
                    Card(
                        modifier  = Modifier.fillMaxSize(),
                        shape     = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                        colors    = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp, vertical = 24.dp)
                        ) {
                            Text(
                                "Máquinas registradas",
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 16.sp,
                                color      = TextDark,
                                modifier   = Modifier.padding(bottom = 16.dp)
                            )

                            when (uiState) {
                                is MachinesUIState.Loading -> {
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator(color = Brand)
                                    }
                                }
                                is MachinesUIState.Success -> {
                                    val machines = (uiState as MachinesUIState.Success).machines
                                    if (machines.isEmpty()) {
                                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                            Text(
                                                "No hay máquinas registradas",
                                                fontFamily = Poppins,
                                                color      = TextMid
                                            )
                                        }
                                    } else {
                                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                            items(machines) { machine ->
                                                MachineCard(
                                                    machine  = machine,
                                                    onEdit   = { machineToEdit = machine },
                                                    onDelete = { machineToDelete = machine }
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
                                                (uiState as MachinesUIState.Error).message,
                                                fontFamily = Poppins,
                                                color      = ErrorRed
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(14.dp))
                                                    .background(Brush.horizontalGradient(listOf(BrandDark, Brand, AccentCyan)))
                                                    .padding(horizontal = 24.dp, vertical = 12.dp)
                                            ) {
                                                TextButton(onClick = { viewModel.loadMachines() }) {
                                                    Text(
                                                        "Reintentar",
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
                    }
                }
            }
        }
    }

    // Dialogs
    if (showAddDialog) {
        MachineDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = {
                viewModel.addMachine(it)
                showAddDialog = false
            }
        )
    }

    if (machineToEdit != null) {
        MachineDialog(
            machine   = machineToEdit,
            onDismiss = { machineToEdit = null },
            onConfirm = {
                viewModel.updateMachine(it)
                machineToEdit = null
            }
        )
    }

    if (machineToDelete != null) {
        AlertDialog(
            onDismissRequest = { machineToDelete = null },
            containerColor   = SurfaceWhite,
            shape            = RoundedCornerShape(24.dp),
            icon    = { Icon(Icons.Filled.Warning, contentDescription = null, tint = ErrorRed) },
            title   = {
                Text(
                    "¿Eliminar máquina?",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    color      = TextDark
                )
            },
            text = {
                Text(
                    "¿Estás seguro de que deseas eliminar ${machineToDelete?.name}?",
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
                    TextButton(onClick = {
                        viewModel.deleteMachine(machineToDelete!!.id)
                        machineToDelete = null
                    }) {
                        Text("Eliminar", color = Color.White, fontFamily = Poppins, fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { machineToDelete = null }) {
                    Text("Cancelar", color = TextMid, fontFamily = Poppins)
                }
            }
        )
    }
}

@Composable
fun MachineStatChip(label: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.18f))
            .border(1.dp, Color.White.copy(alpha = 0.30f), RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
            Text(
                label,
                fontFamily = Poppins,
                fontSize   = 11.sp,
                fontWeight = FontWeight.Bold,
                color      = Color.White
            )
        }
    }
}

@Composable
fun MachineCard(
    machine: Machine,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val statusColor = when (machine.status) {
        MachineStatus.AVAILABLE   -> Color(0xFF4CAF50)
        MachineStatus.OCCUPIED    -> Color(0xFFF44336)
        MachineStatus.MAINTENANCE -> Color(0xFFFF9800)
    }
    val statusText = when (machine.status) {
        MachineStatus.AVAILABLE   -> "Disponible"
        MachineStatus.OCCUPIED    -> "Ocupada"
        MachineStatus.MAINTENANCE -> "Mantenimiento"
    }

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = BgLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BrandPale),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.LocalLaundryService,
                        contentDescription = null,
                        tint     = Brand,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Column {
                    Text(
                        machine.name,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 15.sp,
                        color      = TextDark
                    )
                    Text(
                        "${machine.capacity}${machine.location?.let { " • $it" } ?: ""}",
                        fontFamily = Poppins,
                        fontSize   = 12.sp,
                        color      = TextMid
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(statusColor, CircleShape)
                        )
                        Text(
                            statusText,
                            fontFamily = Poppins,
                            fontSize   = 11.sp,
                            color      = statusColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = Brand)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = ErrorRed)
                }
            }
        }
    }
}