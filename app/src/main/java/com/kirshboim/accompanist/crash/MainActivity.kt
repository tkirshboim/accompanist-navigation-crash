@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialNavigationApi::class)

package com.kirshboim.accompanist.crash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val stateChangeEnabled = remember { mutableStateOf(false) }
            val sheetState = rememberModalBottomSheetState(stateChangeEnabled)
            val bottomSheetNavigator = remember(sheetState) { BottomSheetNavigator(sheetState) }
            val navController = rememberNavController(bottomSheetNavigator)
            val backStackEntry =
                with(navController) { currentBackStackEntryFlow.collectAsState(currentBackStackEntry) }
            LaunchedEffect(backStackEntry.value) { stateChangeEnabled.value = false }

            ModalBottomSheetLayout(bottomSheetNavigator) {
                NavHost(navController = navController, startDestination = "xyz") {
                    composable("xyz") {}
                }
            }
        }
    }
}

// Inline this function to work around the crash.
@Composable
private fun rememberModalBottomSheetState(stateChangeEnabled: MutableState<Boolean>) =
    rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { stateChangeEnabled.value }
    )

