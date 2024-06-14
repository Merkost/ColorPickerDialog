package com.merkost.colorpickerdialog

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.merkost.colorpickerdialog.ui.theme.byFormat
import com.merkost.colorpickerdialog.ui.theme.getHue

@Composable
fun ColorPicker(
    onColorChange: (Color) -> Unit
) {
    var color by remember { mutableStateOf(Color.White) }
    var colorFormat by remember { mutableStateOf(ColorFormat.RGBA) }

    var selectedColor by remember(color) { mutableStateOf(color) }
    val selectedColorFormat by remember(selectedColor, colorFormat) {
        mutableStateOf(selectedColor.byFormat(colorFormat))
    }

    var hue by remember { mutableFloatStateOf(0f) }
    var alpha by remember { mutableFloatStateOf(1f) }

    Column {
        SwatchBox(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp)),
            color = color,
            onColorChange = {
                Log.d("ColorPicker", "Color changed: $it")
                selectedColor = it
                onColorChange(it)
            }
        )

        Text(text = selectedColorFormat, style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(16.dp))
        HueSlider(
            hue = hue,
            onHueChange = {
                hue = it
                color = (Color.hsv(hue, 1f, 1f))
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        AlphaSlider(
            alpha = alpha,
            color = color,
            onAlphaChange = {
                alpha = it
                Log.d("ColorPicker", "Alpha changed: $alpha")
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        ColorPresets(onColorSelected = {
            color = it
            hue = it.getHue()

        })
        Spacer(modifier = Modifier.height(16.dp))
        ColorValueDisplay(
            format = colorFormat
        ) { newFormat ->
            colorFormat = newFormat
        }
    }
}