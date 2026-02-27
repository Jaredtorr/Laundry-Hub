package com.example.mylavanderiapp.features.machines.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mylavanderiapp.core.ui.theme.*
import com.example.mylavanderiapp.features.auth.presentation.components.AuthTextField
import com.example.mylavanderiapp.features.machines.domain.entities.Machine
import com.example.mylavanderiapp.features.machines.domain.entities.MachineStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachineDialog(
    machine: Machine? = null,
    onDismiss: () -> Unit,
    onConfirm: (Machine) -> Unit
) {
    val isEditing = machine != null

    var name           by remember { mutableStateOf(machine?.name ?: "") }
    var capacity       by remember { mutableStateOf(machine?.capacity ?: "") }
    var location       by remember { mutableStateOf(machine?.location ?: "") }
    var selectedStatus by remember { mutableStateOf(machine?.status ?: MachineStatus.AVAILABLE) }
    var expanded       by remember { mutableStateOf(false) }

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
                // Ícono decorativo
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(BrandPale),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocalLaundryService,
                        contentDescription = null,
                        tint     = Brand,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Título y subtítulo
                Text(
                    text       = if (isEditing) "Editar Máquina" else "Nueva Máquina",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 20.sp,
                    color      = TextDark
                )
                Text(
                    text       = if (isEditing) "Modifica los datos de la máquina" else "Completa los datos para agregar",
                    fontFamily = Poppins,
                    fontSize   = 12.sp,
                    color      = TextMid,
                    modifier   = Modifier.padding(top = 4.dp, bottom = 22.dp)
                )

                // Nombre
                AuthTextField(
                    value         = name,
                    onValueChange = { name = it },
                    label         = "Nombre",
                    icon          = Icons.Outlined.LocalLaundryService
                )

                Spacer(Modifier.height(12.dp))

                // Capacidad
                AuthTextField(
                    value         = capacity,
                    onValueChange = { capacity = it },
                    label         = "Capacidad (ej: 8 kg)",
                    icon          = Icons.Outlined.Speed
                )

                Spacer(Modifier.height(12.dp))

                // Ubicación
                AuthTextField(
                    value         = location,
                    onValueChange = { location = it },
                    label         = "Ubicación (ej: Piso 1)",
                    icon          = Icons.Outlined.Place
                )

                // Dropdown de estado — solo al editar
                if (isEditing) {
                    Spacer(Modifier.height(12.dp))
                    MachineStatusDropdown(
                        selectedStatus = selectedStatus,
                        expanded       = expanded,
                        onExpandedChange = { expanded = !expanded },
                        onStatusSelected = { selectedStatus = it; expanded = false }
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Botón confirmar con gradiente igual que AuthGradientButton
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
                        onClick = {
                            if (name.isNotBlank() && capacity.isNotBlank()) {
                                onConfirm(
                                    Machine(
                                        id       = machine?.id ?: 0,
                                        name     = name,
                                        status   = if (isEditing) selectedStatus else MachineStatus.AVAILABLE,
                                        capacity = capacity,
                                        location = location.ifBlank { null }
                                    )
                                )
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text          = if (isEditing) "ACTUALIZAR" else "AGREGAR",
                            fontFamily    = Poppins,
                            fontWeight    = FontWeight.Bold,
                            fontSize      = 15.sp,
                            color         = Color.White,
                            letterSpacing = 1.5.sp
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Botón cancelar
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MachineStatusDropdown(
    selectedStatus: MachineStatus,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onStatusSelected: (MachineStatus) -> Unit
) {
    val statusLabel = when (selectedStatus) {
        MachineStatus.AVAILABLE   -> "Disponible"
        MachineStatus.OCCUPIED    -> "Ocupada"
        MachineStatus.MAINTENANCE -> "Mantenimiento"
    }

    ExposedDropdownMenuBox(
        expanded         = expanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            value         = statusLabel,
            onValueChange = {},
            readOnly      = true,
            label         = { Text("Estado", fontSize = 13.sp, fontFamily = Poppins) },
            leadingIcon   = {
                Icon(
                    Icons.Outlined.Info,
                    contentDescription = null,
                    tint     = Brand,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier      = Modifier.fillMaxWidth().menuAnchor(),
            shape         = RoundedCornerShape(14.dp),
            colors        = OutlinedTextFieldDefaults.colors(
                focusedBorderColor        = Brand,
                unfocusedBorderColor      = BrandPale,
                focusedLabelColor         = Brand,
                cursorColor               = Brand,
                focusedLeadingIconColor   = Brand,
                unfocusedLeadingIconColor = BrandLight
            )
        )
        ExposedDropdownMenu(
            expanded         = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier         = Modifier.background(SurfaceWhite)
        ) {
            listOf(
                MachineStatus.AVAILABLE   to "Disponible",
                MachineStatus.OCCUPIED    to "Ocupada",
                MachineStatus.MAINTENANCE to "Mantenimiento"
            ).forEach { (status, label) ->
                DropdownMenuItem(
                    text    = {
                        Text(label, fontFamily = Poppins, color = TextDark)
                    },
                    onClick = { onStatusSelected(status) }
                )
            }
        }
    }
}