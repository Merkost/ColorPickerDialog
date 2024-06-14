package com.merkost.colorpickerdialog

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import com.merkost.colorpickerdialog.ui.theme.hue
import com.merkost.colorpickerdialog.utils.HSV

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun ColorPicker(
    onColorChange: (Color) -> Unit
) {
    var color by remember { mutableStateOf(Color.Unspecified) }
    var colorFormat by remember { mutableStateOf(ColorFormat.RGBA) }

    var hue by remember { mutableFloatStateOf(0f) }
    var alpha by remember { mutableFloatStateOf(1f) }

    var selectedColor by remember(color) { mutableStateOf(color) }
    val selectedColorFormat by remember(hue) {
        derivedStateOf { selectedColor.byFormat(colorFormat) }
    }



    Column {
        SwatchBox(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp)),
            hue = hue,
            onColorChange = { s, v ->
                val newColor = HSV(hue, s, v).toColor()
                selectedColor = newColor
                onColorChange(newColor)
            }
        )

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            if (selectedColor == Color.Unspecified) {
                Text(text = "Select a color", style = MaterialTheme.typography.bodySmall)
            } else {
                Text(text = selectedColorFormat, style = MaterialTheme.typography.bodySmall)
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        HueSlider(
            hue = hue,
            onHueChange = {
                hue = it
                color = (Color.hsv(hue, 1f, 1f))
                Log.d("ColorPicker", "Color preset selected: $color, hue: $it")

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
            hue = it.hue

        })
        Spacer(modifier = Modifier.height(16.dp))
        ColorValueDisplay(
            format = colorFormat
        ) { newFormat ->
            colorFormat = newFormat
        }
    }
}