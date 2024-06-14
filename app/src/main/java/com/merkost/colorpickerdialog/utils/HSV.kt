package com.merkost.colorpickerdialog.utils

import androidx.compose.ui.graphics.Color

data class HSV(val hue: Float, val saturation: Float, val value: Float) {
    fun toColor(): Color {
        val c = value * saturation
        val x = c * (1 - kotlin.math.abs((hue / 60) % 2 - 1))
        val m = value - c

        val (r, g, b) = when {
            hue < 60 -> Triple(c, x, 0f)
            hue < 120 -> Triple(x, c, 0f)
            hue < 180 -> Triple(0f, c, x)
            hue < 240 -> Triple(0f, x, c)
            hue < 300 -> Triple(x, 0f, c)
            else -> Triple(c, 0f, x)
        }

        return Color(
            red = (r + m).coerceIn(0f, 1f),
            green = (g + m).coerceIn(0f, 1f),
            blue = (b + m).coerceIn(0f, 1f)
        )
    }
}