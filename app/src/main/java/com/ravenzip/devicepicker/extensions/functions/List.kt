package com.ravenzip.devicepicker.extensions.functions

fun <T> List<T>.convertToString(separator: String = ", ") =
    this.fold(
        initial = "",
        operation = { acc, item -> acc + (if (acc == "") "" else separator) + item.toString() })
