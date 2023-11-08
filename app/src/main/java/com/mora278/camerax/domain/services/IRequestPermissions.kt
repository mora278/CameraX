package com.mora278.camerax.domain.services

interface IRequestPermissions {
    fun requestCameraPermissions()
    fun allCameraPermissionsGranted(): Boolean
}