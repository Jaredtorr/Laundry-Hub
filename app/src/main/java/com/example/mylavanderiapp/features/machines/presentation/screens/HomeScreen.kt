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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.auth.domain.entities.User
import com.example.mylavanderiapp.features.auth.domain.entities.UserRole
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.entities.MachineStatus
import com.example.mylavanderiapp.features.machines.presentation.components.MachineDialog
import com.example.mylavanderiapp.features.machines.presentation.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    user: User? = null,
    onLogout: () -> Unit,
    onReserveTurn: (Machine) -> Unit,
    onMyTurns: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val machines by viewModel.machines.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var machineToEdit by remember { mutableStateOf<Machine?>(null) }
    var machineToDelete by remember { mutableStateOf<Machine?>(null) }

    val isAdmin = user?.role == UserRole.ADMIN

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Lavandería - Máquinas",
                            fontWeight = FontWeight.Bold
                        )
                        if (user != null) {
                            Text(
                                text = if (isAdmin) "Administrador" else "Usuario",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Filled.Logout,
                            contentDescription = "Cerrar sesión",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryTealDark,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Button(
                    onClick = onMyTurns,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryTealDark
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccessTime,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "MIS TURNOS",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        floatingActionButton = {
            if (isAdmin) {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = PrimaryTealDark,
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Agregar máquina"
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(machines) { machine ->
                    MachineCard(
                        machine = machine,
                        isAdmin = isAdmin,
                        onReserve = { onReserveTurn(machine) },
                        onEdit = { machineToEdit = machine },
                        onDelete = { machineToDelete = machine }
                    )
                }
            }
        }
    }

    // Diálogo para agregar
    if (showAddDialog) {
        MachineDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { machine ->
                viewModel.addMachine(machine)
                showAddDialog = false
            }
        )
    }

    // Diálogo para editar
    if (machineToEdit != null) {
        MachineDialog(
            machine = machineToEdit,
            onDismiss = { machineToEdit = null },
            onConfirm = { machine ->
                viewModel.updateMachine(machine)
                machineToEdit = null
            }
        )
    }

    // Diálogo de confirmación para eliminar
    if (machineToDelete != null) {
        AlertDialog(
            onDismissRequest = { machineToDelete = null },
            containerColor = Color.White,
            icon = {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                    tint = Color(0xFFF44336),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "Eliminar Máquina",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("¿Estás seguro de que deseas eliminar ${machineToDelete?.name}?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteMachine(machineToDelete!!.id)
                        machineToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF44336)
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { machineToDelete = null }) {
                    Text("Cancelar", color = Color.Gray)
                }
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
        MachineStatus.AVAILABLE -> Color(0xFF4CAF50)
        MachineStatus.OCCUPIED -> Color(0xFFF44336)
        MachineStatus.MAINTENANCE -> Color(0xFFFF9800)
    }

    val statusText = when (machine.status) {
        MachineStatus.AVAILABLE -> "Disponible"
        MachineStatus.OCCUPIED -> "Ocupada"
        MachineStatus.MAINTENANCE -> "Mantenimiento"
    }

    val isReservable = machine.status == MachineStatus.AVAILABLE

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
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
                        Text(
                            text = machine.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Text(
                            text = "${machine.capacity} • ${machine.location}",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                color = statusColor,
                                shape = RoundedCornerShape(50)
                            )
                    )

                    Text(
                        text = statusText,
                        fontSize = 14.sp,
                        color = statusColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isAdmin) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar",
                            tint = PrimaryTealDark
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Eliminar",
                            tint = Color(0xFFF44336)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }

                Button(
                    onClick = onReserve,
                    enabled = isReservable,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryTealDark,
                        disabledContainerColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = if (!isAdmin) Modifier.fillMaxWidth() else Modifier
                ) {
                    Text(
                        text = if (isReservable) "Reservar" else "No disponible",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}