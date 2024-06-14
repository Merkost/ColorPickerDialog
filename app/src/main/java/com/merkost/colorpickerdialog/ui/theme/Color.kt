package com.merkost.colorpickerdialog.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import com.merkost.colorpickerdialog.ColorFormat
import java.util.Locale

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

fun Color.toHex(): String {
    return String.format(
        "#%02x%02x%02x%02x",
        (alpha * 255).toInt(),
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    ).uppercase(Locale.ROOT)
}

fun Color.byFormat(format: ColorFormat) =when (format) {
    ColorFormat.RGBA -> "RGBA(${(this.red * 255).toInt()}, ${(this.green * 255).toInt()}, ${(this.blue * 255).toInt()}, ${"%.2f".format(this.alpha)})"
    ColorFormat.HEX -> "#${this.toHex()}"
    ColorFormat.HSLA -> {
        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(this.toArgb(), hsl)
        "HSLA(${hsl[0].toInt()}, ${"%.2f".format(hsl[1] * 100)}%, ${"%.2f".format(hsl[2] * 100)}%, ${"%.2f".format(this.alpha)})"
    }
}


fun Color.toHsl(): Triple<Float, Float, Float> {
    val (r, g, b) = Triple(this.red, this.green, this.blue)
    val max = listOf(r, g, b).maxOrNull() ?: 0f
    val min = listOf(r, g, b).minOrNull() ?: 0f
    val delta = max - min

    val hue = when {
        delta == 0f -> 0f
        max == r -> ((g - b) / delta) % 6
        max == g -> (b - r) / delta + 2
        max == b -> (r - g) / delta + 4
        else -> 0f
    }
    val hueDegrees = hue * 60f
    val lightness = (max + min) / 2f
    val saturation = if (delta == 0f) 0f else delta / (1 - kotlin.math.abs(2 * lightness - 1))

    return Triple(hueDegrees, saturation, lightness)
}

fun Color.getHue(): Float {
    val (hue, _, _) = this.toHsl()
    return hue
}