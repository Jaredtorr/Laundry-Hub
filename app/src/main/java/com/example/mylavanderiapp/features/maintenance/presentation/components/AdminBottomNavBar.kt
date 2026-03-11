package com.example.mylavanderiapp.features.shared.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mylavanderiapp.core.ui.theme.*

enum class AdminNavDestination { HOME, MAINTENANCE }

@Composable
fun AdminBottomNavBar(
    current:    AdminNavDestination,
    onNavigate: (AdminNavDestination) -> Unit
) {
    NavigationBar(
        containerColor = SurfaceWhite,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = current == AdminNavDestination.HOME,
            onClick  = { if (current != AdminNavDestination.HOME) onNavigate(AdminNavDestination.HOME) },
            icon = {
                Icon(
                    imageVector        = if (current == AdminNavDestination.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Inicio",
                    modifier           = Modifier.size(24.dp)
                )
            },
            label  = { Text("Inicio", fontFamily = Poppins) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor   = Brand,
                selectedTextColor   = Brand,
                unselectedIconColor = TextMid,
                unselectedTextColor = TextMid,
                indicatorColor      = BrandPale
            )
        )

        NavigationBarItem(
            selected = current == AdminNavDestination.MAINTENANCE,
            onClick  = { if (current != AdminNavDestination.MAINTENANCE) onNavigate(AdminNavDestination.MAINTENANCE) },
            icon = {
                Icon(
                    imageVector        = if (current == AdminNavDestination.MAINTENANCE) Icons.Filled.Build else Icons.Outlined.Build,
                    contentDescription = "Mantenimiento",
                    modifier           = Modifier.size(24.dp)
                )
            },
            label  = { Text("Mantenimiento", fontFamily = Poppins) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor   = Brand,
                selectedTextColor   = Brand,
                unselectedIconColor = TextMid,
                unselectedTextColor = TextMid,
                indicatorColor      = BrandPale
            )
        )
    }
}