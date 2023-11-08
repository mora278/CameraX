package com.mora278.camerax.data.repositories

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.mora278.camerax.MainActivity
import com.mora278.camerax.domain.services.IRequestPermissions

class RequestPermissionsImpl(
    private val context: Context
): IRequestPermissions {
//    private val activityResultLauncher =
//        context.registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissions ->
//            var permissionGranted = true
//            permissions.entries.forEach {
//                if ((it.key in REQUIRED_PERMISSIONS) and it.value.not())
//                    permissionGranted = false
//            }
//            if (permissionGranted.not()) {
//                Toast.makeText(baseContext, "Permissions request denied", Toast.LENGTH_SHORT).show()
//            } else {
//                startCamera()
//            }
//
//        }

    override fun requestCameraPermissions() {

    }

    override fun allCameraPermissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val TAG = "CameraX"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS = mutableListOf(
            android.Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }
}