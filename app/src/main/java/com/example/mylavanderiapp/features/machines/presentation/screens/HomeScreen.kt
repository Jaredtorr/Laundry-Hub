package com.example.mylavanderiapp.features.machines.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.PrimaryTealDark
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
    onLogout: () -> Unit,
    onMyTurns: () -> Unit
) {
    val uiState        by viewModel.uiState.collectAsState()
    val operationState by viewModel.operationState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showAddDialog  by remember { mutableStateOf(false) }
    var machineToEdit  by remember { mutableStateOf<Machine?>(null) }
    var machineToDelete by remember { mutableStateOf<Machine?>(null) }

    val isAdmin = true // reemplaza con tu lógica real de sesión

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
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Lavandería", fontWeight = FontWeight.Bold)
                        Text(if (isAdmin) "Modo Administrador" else "Modo Usuario", fontSize = 12.sp)
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Filled.Logout, contentDescription = "Salir", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryTealDark,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = Color.White) {
                Button(
                    onClick = onMyTurns,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryTealDark)
                ) {
                    Icon(Icons.Filled.AccessTime, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("MIS TURNOS")
                }
            }
        },
        floatingActionButton = {
            if (isAdmin) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = PrimaryTealDark
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Añadir", tint = Color.White)
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize().background(Color(0xFFF5F5F5))) {
            when (uiState) {
                is MachinesUIState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = PrimaryTealDark)
                }
                is MachinesUIState.Success -> {
                    val machines = (uiState as MachinesUIState.Success).machines
                    if (machines.isEmpty()) {
                        Text("No hay máquinas", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(machines) { machine ->
                                MachineCard(
                                    machine  = machine,
                                    isAdmin  = isAdmin,
                                    onReserve = {},
                                    onEdit   = { machineToEdit = machine },
                                    onDelete = { machineToDelete = machine }
                                )
                            }
                        }
                    }
                }
                is MachinesUIState.Error -> {
                    Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Error: ${(uiState as MachinesUIState.Error).message}", color = Color.Red)
                        Button(onClick = { viewModel.loadMachines() }) { Text("Reintentar") }
                    }
                }
                else -> {}
            }
        }
    }

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
            icon    = { Icon(Icons.Filled.Warning, contentDescription = null, tint = Color.Red) },
            title   = { Text("¿Eliminar máquina?") },
            text    = { Text("¿Estás seguro de que deseas eliminar ${machineToDelete?.name}?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteMachine(machineToDelete!!.id)
                        machineToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { machineToDelete = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun MachineCard(
    machine: Machine,
    isAdmin: Boolean,
    onReserve: () -> Unit,
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
    val isReservable = machine.status == MachineStatus.AVAILABLE

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocalLaundryService,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = PrimaryTealDark
                    )
                    Column {
                        Text(machine.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(
                            "${machine.capacity}${machine.location?.let { " • $it" } ?: ""}",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(12.dp).background(color = statusColor, shape = RoundedCornerShape(50)))
                    Text(statusText, fontSize = 14.sp, color = statusColor, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (isAdmin) {
                    IconButton(onClick = onEdit, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = PrimaryTealDark)
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color(0xFFF44336))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
                if (!isAdmin) {
                    Button(
                        onClick  = onReserve,
                        enabled  = isReservable,
                        colors   = ButtonDefaults.buttonColors(containerColor = PrimaryTealDark, disabledContainerColor = Color.LightGray),
                        shape    = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isReservable) "Reservar" else "No disponible", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}