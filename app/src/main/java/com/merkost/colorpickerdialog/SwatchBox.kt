package com.merkost.colorpickerdialog


import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.merkost.colorpickerdialog.ui.theme.hue
import com.merkost.colorpickerdialog.utils.HSV
import kotlin.math.max
import kotlin.math.min

@Composable
fun SwatchBox(
    modifier: Modifier,
    hue: Float,
    onColorChange: (saturation: Float, value: Float) -> Unit,
) {
    var selectedOffset by remember { mutableStateOf(Offset.Unspecified) }
    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(hue) {
        if (selectedOffset != Offset.Unspecified) {
            val (s, v) = getSaturationPoint(selectedOffset, boxSize)
            onColorChange(s, v)
        }
    }

    val blackGradientBrush = remember {
        Brush.verticalGradient(listOf(Color(0xffffffff), Color(0xff000000)))
    }
    val currentColorGradientBrush = remember(hue) {
        val hsv = HSV(hue, 1.0f, 1.0f)
        Brush.horizontalGradient(
            listOf(
                Color(0xffffffff),
                hsv.toColor()
            )
        )
    }

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val clampedOffset = offset.safeBox(boxSize.toSize())
                    selectedOffset = clampedOffset
                    val (s, v) = getSaturationPoint(offset, size)
                    onColorChange(s, v)
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val clampedOffset = change.position.safeBox(boxSize.toSize())
                    selectedOffset = clampedOffset
                    val (s, v) = getSaturationPoint(change.position, size)
                    onColorChange(s, v)
                }
            }
            .onGloballyPositioned { coordinates ->
                boxSize = coordinates.size
            }
    ) {
        drawRect(blackGradientBrush)
        drawRect(currentColorGradientBrush, blendMode = BlendMode.Modulate)
        if (selectedOffset != Offset.Unspecified) {
            drawCircle(
                color = Color.White,
                radius = 12.dp.toPx(),
                center = selectedOffset,
                style = Stroke(width = 10f)
            )
            drawCircle(
                color = Color.White,
                radius = 3.dp.toPx(),
                center = selectedOffset
            )
        }
    }
}

private fun getSaturationPoint(
    offset: Offset,
    size: IntSize
): Pair<Float, Float> {
    val (saturation, value) = getSaturationValueFromPosition(
        offset,
        size.toSize()
    )
    return saturation to value
}

/**
 * Given an offset and size, this function calculates a saturation and value amount based on that.
 *
 * @return new saturation and value
 */
private fun getSaturationValueFromPosition(offset: Offset, size: Size): Pair<Float, Float> {
    val width = size.width
    val height = size.height

    val newX = offset.x.coerceIn(0f, width)

    val newY = offset.y.coerceIn(0f, size.height)
    val saturation = 1f / width * newX
    val value = 1f - 1f / height * newY

    return saturation.coerceIn(0f, 1f) to value.coerceIn(0f, 1f)
}

private fun Offset.safeBox(boxSize: Size) = Offset(
    x = max(0f, min(x, boxSize.width)),
    y = max(0f, min(y, boxSize.height))
)