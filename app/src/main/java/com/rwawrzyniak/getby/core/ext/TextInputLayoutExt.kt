package com.rwawrzyniak.getby.core.ext

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.markRequired() {
    hint = "$hint *"
}