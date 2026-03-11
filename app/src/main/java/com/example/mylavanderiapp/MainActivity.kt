package com.example.mylavanderiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.example.mylavanderiapp.core.navigation.NavigationWrapper
import com.example.mylavanderiapp.core.ui.theme.MylavanderiappTheme
import com.example.mylavanderiapp.features.auth.presentation.navigation.AuthNavGraph
import com.example.mylavanderiapp.features.laundry_reservation.presentation.navigation.ReservationNavGraph
import com.example.mylavanderiapp.features.machines.presentation.navigation.MachinesNavGraph
import com.example.mylavanderiapp.features.maintenance.presentation.navigation.MaintenanceNavGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authNavGraph: AuthNavGraph
    @Inject
    lateinit var machinesNavGraph: MachinesNavGraph
    @Inject
    lateinit var reservationNavGraph: ReservationNavGraph
    @Inject
    lateinit var maintenanceNavGraph: MaintenanceNavGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MylavanderiappTheme {
                val googleSignInHelper = remember {
                    com.example.mylavanderiapp.core.auth.GoogleSignInHelper(this)
                }
                NavigationWrapper(
                    navGraphs = listOf(
                        authNavGraph,
                        machinesNavGraph,
                        reservationNavGraph,
                        maintenanceNavGraph
                    ),
                    googleSignInHelper = googleSignInHelper
                )
            }
        }
    }
}