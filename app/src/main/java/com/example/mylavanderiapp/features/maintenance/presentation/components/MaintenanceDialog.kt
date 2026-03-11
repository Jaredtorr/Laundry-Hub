package com.example.mylavanderiapp.features.maintenance.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.maintenance.domain.entities.MaintenanceRecord
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceDialog(
    machines: List<Machine>,
    onDismiss: () -> Unit,
    onConfirm: (MaintenanceRecord) -> Unit
) {
    var selectedMachine by remember { mutableStateOf(machines.firstOrNull()) }
    var description     by remember { mutableStateOf("") }
    var expanded        by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val today = "${calendar.get(Calendar.DAY_OF_MONTH)} ${
        arrayOf("Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic")[calendar.get(Calendar.MONTH)]
    } ${calendar.get(Calendar.YEAR)}"

    AlertDialog(
        onDismissRequest = onDismiss,
        shape            = RoundedCornerShape(20.dp),
        title = {
            Text(
                text       = "Nuevo mantenimiento",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp,
                color      = TextDark
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ExposedDropdownMenuBox(
                    expanded         = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value         = selectedMachine?.name ?: "Sin máquinas disponibles",
                        onValueChange = {},
                        readOnly      = true,
                        label         = { Text("Seleccionar máquina", fontFamily = Poppins) },
                        trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier      = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded         = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        machines.forEach { machine ->
                            DropdownMenuItem(
                                text    = { Text(machine.name, fontFamily = Poppins) },
                                onClick = {
                                    selectedMachine = machine
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value         = description,
                    onValueChange = { description = it },
                    label         = { Text("Descripción del problema", fontFamily = Poppins) },
                    placeholder   = { Text("Describa el problema...", fontFamily = Poppins) },
                    minLines      = 3,
                    modifier      = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value         = today,
                    onValueChange = {},
                    readOnly      = true,
                    label         = { Text("Fecha", fontFamily = Poppins) },
                    modifier      = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val machine = selectedMachine
                    if (machine != null && description.isNotBlank()) {
                        onConfirm(
                            MaintenanceRecord(
                                id          = 0,
                                machineId   = machine.id,   // ID real de la máquina
                                machineName = machine.name,
                                description = description,
                                startDate   = today,
                                daysElapsed = 0,
                                isResolved  = false
                            )
                        )
                    }
                },
                colors  = ButtonDefaults.buttonColors(containerColor = Brand),
                shape   = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar", fontFamily = Poppins, color = androidx.compose.ui.graphics.Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", fontFamily = Poppins, color = TextMid)
            }
        }
    )
}