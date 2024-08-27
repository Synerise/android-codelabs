package com.synerise.integration.app.main.permissions

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationPermission(
    permissionState: PermissionState,
    onPermissionGranted: () -> Unit,
    onPermissionRefused: () -> Unit
) {
    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = { onPermissionRefused() },
        permissionNotAvailableContent = { onPermissionRefused() }
    ) {
        onPermissionGranted()
    }
}