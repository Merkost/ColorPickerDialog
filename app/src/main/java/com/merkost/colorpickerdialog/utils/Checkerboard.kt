package com.merkost.colorpickerdialog.utils

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Checkerboard(
    modifier: Modifier = Modifier,
    size: Dp = 8.dp,
    color1: Color = Color.LightGray,
    color2: Color = Color.White,
) {
    Canvas(modifier = modifier) {
        val sizePx = size.toPx()
        val numColumns = (sizePx / size.value).toInt()
        val numRows = (sizePx / size.value).toInt()

        for (row in 0..numRows) {
            for (col in 0..numColumns) {
                val color = if ((row + col) % 2 == 0) color1 else color2
                drawRect(
                    color = color,
                    topLeft = Offset(col * sizePx, row * sizePx),
                    size = Size(sizePx, sizePx)
                )
            }
        }
    }
}
