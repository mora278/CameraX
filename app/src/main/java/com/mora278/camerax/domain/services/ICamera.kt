package com.mora278.camerax.domain.services

import androidx.camera.view.PreviewView
import java.util.concurrent.ExecutorService

interface ICamera {
    fun startCamera(cameraExecutor: ExecutorService, viewFinder: PreviewView)
    fun takePhoto()
    fun captureVideo(onRecord: (isRecording: Boolean) -> Unit)
}