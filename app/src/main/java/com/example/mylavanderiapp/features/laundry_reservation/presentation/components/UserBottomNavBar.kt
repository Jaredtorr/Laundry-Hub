package com.example.mylavanderiapp.features.laundry_reservation.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.LocalLaundryService
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mylavanderiapp.core.ui.theme.*

enum class UserNavDestination { MACHINES, MY_RESERVATIONS }

@Composable
fun UserBottomNavBar(
    current   : UserNavDestination,
    onNavigate: (UserNavDestination) -> Unit
) {
    NavigationBar(
        containerColor = SurfaceWhite,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = current == UserNavDestination.MACHINES,
            onClick  = { if (current != UserNavDestination.MACHINES) onNavigate(UserNavDestination.MACHINES) },
            icon = {
                Icon(
                    imageVector        = if (current == UserNavDestination.MACHINES) Icons.Filled.LocalLaundryService else Icons.Outlined.LocalLaundryService,
                    contentDescription = "Lavadoras",
                    modifier           = Modifier.size(24.dp)
                )
            },
            label  = { Text("Lavadoras", fontFamily = Poppins) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor   = Brand,
                selectedTextColor   = Brand,
                unselectedIconColor = TextMid,
                unselectedTextColor = TextMid,
                indicatorColor      = BrandPale
            )
        )

        NavigationBarItem(
            selected = current == UserNavDestination.MY_RESERVATIONS,
            onClick  = { if (current != UserNavDestination.MY_RESERVATIONS) onNavigate(UserNavDestination.MY_RESERVATIONS) },
            icon = {
                Icon(
                    imageVector        = if (current == UserNavDestination.MY_RESERVATIONS) Icons.Filled.ListAlt else Icons.Outlined.ListAlt,
                    contentDescription = "Mis reservas",
                    modifier           = Modifier.size(24.dp)
                )
            },
            label  = { Text("Mis reservas", fontFamily = Poppins) },
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