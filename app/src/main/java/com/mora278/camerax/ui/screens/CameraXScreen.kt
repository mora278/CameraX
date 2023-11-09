package com.mora278.camerax.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FlashAuto
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.mora278.camerax.ui.screens.Permissions.REQUIRED_PERMISSIONS
import com.mora278.camerax.ui.viewmodels.CameraXViewModel
import java.util.concurrent.Executors

fun interface StartCamera {
    operator fun invoke()
}

@Composable
fun CameraXScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    context: Context = LocalContext.current,
    cameraXViewModel: CameraXViewModel,
) {
    val startCamera: StartCamera = remember {
        StartCamera {
            cameraXViewModel.startCamera(
                context = context,
                lifecycleOwner = lifecycleOwner
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CameraPreview(
            modifier = Modifier
                .fillMaxSize()
        ) {
            cameraXViewModel.setUpPreview(viewFinder = it)
        }
        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.CenterEnd)
                .padding(8.dp)
                .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.3f)),
            verticalArrangement = Arrangement.Center,
        ) {
            IconButton(
                onClick = {
                    cameraXViewModel.switchCamera()
                    startCamera.invoke()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = null
                )
            }
            IconButton(
                onClick = cameraXViewModel::changeFlashMode
            ) {
                Icon(
                    imageVector = when (cameraXViewModel.flashMode) {
                        ImageCapture.FLASH_MODE_AUTO -> Icons.Default.FlashAuto
                        ImageCapture.FLASH_MODE_ON -> Icons.Default.FlashOn
                        else -> Icons.Default.FlashOff
                    },
                    contentDescription = null
                )
            }
            IconButton(
                onClick = {
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ZoomIn,
                    contentDescription = null
                )
            }
            IconButton(
                onClick = {
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ZoomOut,
                    contentDescription = null
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    cameraXViewModel.takePhoto(context)
                }
            ) {
                Text(text = "Take photo")
            }
//            Button(
//                onClick = {
//                    cameraXViewModel.captureVideo(context)
//                }
//            ) {
//                Text(text = if (cameraXViewModel.isRecording) "Stop capture" else "Start capture")
//            }
        }
    }

    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if ((it.key in REQUIRED_PERMISSIONS) and it.value.not())
                    permissionGranted = false
            }
            if (permissionGranted.not()) {
                Toast.makeText(context, "Permissions request denied", Toast.LENGTH_SHORT).show()
            } else {
                startCamera.invoke()
            }
        }
    )

    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    cameraXViewModel.cameraExecutor = Executors.newSingleThreadExecutor()
                    if (allPermissionsGranted(context)) {
                        startCamera.invoke()
                    } else {
                        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
                    }
                } else if (event == Lifecycle.Event.ON_STOP) {
                    cameraXViewModel.cameraExecutor.shutdown()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )
}

@Composable
private fun CameraPreview(
    modifier: Modifier = Modifier,
    onView: (viewFinder: PreviewView) -> Unit,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            PreviewView(context)
                .apply {

                }.also {
                    onView.invoke(it)
                }
        }
    )
}

private fun allPermissionsGranted(context: Context) = REQUIRED_PERMISSIONS.all {
    ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
}

private object Permissions {
    val REQUIRED_PERMISSIONS = mutableListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
    ).apply {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }.toTypedArray()
}