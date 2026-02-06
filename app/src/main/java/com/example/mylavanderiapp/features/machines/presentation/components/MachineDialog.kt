package com.example.mylavanderiapp.features.machines.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.PrimaryTealDark
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.entities.MachineStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachineDialog(
    machine: Machine? = null,
    onDismiss: () -> Unit,
    onConfirm: (Machine) -> Unit
) {
    var name by remember { mutableStateOf(machine?.name ?: "") }
    var capacity by remember { mutableStateOf(machine?.capacity ?: "8 kg") }
    var location by remember { mutableStateOf(machine?.location ?: "Piso 1") }
    var selectedStatus by remember { mutableStateOf(machine?.status ?: MachineStatus.AVAILABLE) }
    var expanded by remember { mutableStateOf(false) }

    val isEditing = machine != null

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text(
                text = if (isEditing) "Editar Máquina" else "Agregar Máquina",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Nombre
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    placeholder = { Text("Ej: Lavadora 1") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.LocalLaundryService,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Capacidad
                OutlinedTextField(
                    value = capacity,
                    onValueChange = { capacity = it },
                    label = { Text("Capacidad") },
                    placeholder = { Text("Ej: 8 kg") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Speed,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Ubicación
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Ubicación") },
                    placeholder = { Text("Ej: Piso 1") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Place,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Estado (Dropdown)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = when (selectedStatus) {
                            MachineStatus.AVAILABLE -> "Disponible"
                            MachineStatus.OCCUPIED -> "Ocupada"
                            MachineStatus.MAINTENANCE -> "Mantenimiento"
                        },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Estado") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = null
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Disponible") },
                            onClick = {
                                selectedStatus = MachineStatus.AVAILABLE
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Ocupada") },
                            onClick = {
                                selectedStatus = MachineStatus.OCCUPIED
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Mantenimiento") },
                            onClick = {
                                selectedStatus = MachineStatus.MAINTENANCE
                                expanded = false
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val newMachine = Machine(
                            id = machine?.id ?: System.currentTimeMillis().toString(),
                            name = name,
                            status = selectedStatus,
                            capacity = capacity,
                            location = location
                        )
                        onConfirm(newMachine)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryTealDark
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (isEditing) "Actualizar" else "Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        }
    )
}