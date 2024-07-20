package com.ravenzip.devicepicker.model.device.specifications

import com.ravenzip.devicepicker.model.device.specifications.camera.BackCamera
import com.ravenzip.devicepicker.model.device.specifications.camera.FrontCamera

data class Camera(val back: BackCamera, val front: FrontCamera) {
    constructor() : this(BackCamera(), FrontCamera())
}
