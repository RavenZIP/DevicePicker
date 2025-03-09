package com.ravenzip.devicepicker.common.model.device.specifications

import com.ravenzip.devicepicker.common.model.device.specifications.camera.BackCameraDto
import com.ravenzip.devicepicker.common.model.device.specifications.camera.FrontCameraDto

data class CameraDto(val back: BackCameraDto, val front: FrontCameraDto) {
    constructor() : this(BackCameraDto(), FrontCameraDto())
}
