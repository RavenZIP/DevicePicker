package com.ravenzip.devicepicker.common.utils.extension

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.getValue

inline fun <reified T> Iterable<DataSnapshot>.convertToClass(): List<T> {
    val data = mutableListOf<T>()
    this.forEach {
        val item = it.getValue<T>()
        if (item !== null) {
            data.add(item)
        }
    }
    return data
}
