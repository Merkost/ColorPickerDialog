package com.merkost.colorpickerdialog

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    trackBrush: Brush,
    thumbColor: Color,
    modifier: Modifier = Modifier,
    trackBackground: @Composable () -> Unit = {},
) {
    var trackWidth by remember { mutableStateOf(0f) }
    val thumbRadius = 12.dp
    val thumbDiameter = thumbRadius * 2

    Box(
        modifier = modifier
            .height(thumbDiameter)
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val x = change.position.x
                    val newValue =
                        ((x - thumbRadius.toPx()) / (trackWidth - thumbDiameter.toPx())) * (valueRange.endInclusive - valueRange.start) + valueRange.start
                    onValueChange(newValue.coerceIn(valueRange))
                }
            }
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    val newValue =
                        ((tapOffset.x - thumbRadius.toPx()) / (trackWidth - thumbDiameter.toPx())) * (valueRange.endInclusive - valueRange.start) + valueRange.start
                    onValueChange(newValue.coerceIn(valueRange))
                }
            }
            .onGloballyPositioned { coordinates ->
                trackWidth = coordinates.size.width.toFloat()
            }
    ) {
        // Track background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(50))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(trackBrush, RoundedCornerShape(50))
            )
        }

        trackBackground()

        // Thumb
        Canvas(
            modifier = Modifier
                .width(thumbDiameter)
                .height(thumbDiameter)
                .background(Color.Transparent)
        ) {
            val thumbCenter = Offset(
                x = thumbRadius.toPx() + (value - valueRange.start) / (valueRange.endInclusive - valueRange.start) * (trackWidth - thumbDiameter.toPx()),
                y = size.height / 2
            )
            drawCircle(
                color = thumbColor,
                radius = thumbRadius.toPx(),
                center = thumbCenter
            )
            drawCircle(
                color = Color.White,
                radius = thumbRadius.toPx() - 2,
                center = thumbCenter,
                style = Stroke(
                    width = 10f
                )
            )
        }
    }
}


@Composable
fun Circle(
    modifier: Modifier = Modifier,
    diameter: Dp = 24.dp,
    color: Color = Color.White,
) {
    Box(
        modifier = modifier
            .size(diameter)
            .background(color, CircleShape)
            .border(2.dp, Color.White, CircleShape)
    )
}


@Composable
fun HueSlider(
    hue: Float,
    onHueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val hueBrush = Brush.horizontalGradient(
        colors = (0..360 step 10).map { Color.hsv(it.toFloat(), 1f, 1f) }
    )

    CustomSlider(
        value = hue,
        onValueChange = onHueChange,
        valueRange = 0f..360f,
        trackBrush = hueBrush,
        thumbColor = Color.hsv(hue, 1f, 1f),
        modifier = modifier
    )
}


@Composable
fun CheckerboardBackground(modifier: Modifier = Modifier) {
    val squareSize = 6.dp
    val paint = Paint().apply {
        color = Color.Gray
    }
    val density = LocalDensity.current

    Canvas(modifier) {
        val canvasSize = with(density) { squareSize.toPx() }
        val step = canvasSize * 2
        for (y in 0..(this.size.height / step).toInt()) {
            for (x in 0..(size.width / step).toInt()) {
                val offsetX = x * step
                val offsetY = y * step
                drawRect(
                    color = Color.Gray,
                    topLeft = Offset(offsetX, offsetY),
                    size = Size(canvasSize, canvasSize)
                )
                drawRect(
                    color = Color.Gray,
                    topLeft = Offset(offsetX + canvasSize, offsetY + canvasSize),
                    size = Size(canvasSize, canvasSize)
                )
            }
        }
    }
}

@Composable
fun AlphaSlider(
    alpha: Float,
    color: Color,
    onAlphaChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val thumbRadius = 12.dp
    val thumbDiameter = thumbRadius * 2

    val thumbColor by remember(alpha) {
        derivedStateOf { color.copy(alpha = alpha) }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thumbDiameter)
    ) {

        val alphaBrush = Brush.horizontalGradient(
            colors = listOf(color.copy(alpha = 0f), color)
        )

        CustomSlider(
            value = alpha,
            onValueChange = onAlphaChange,
            valueRange = 0f..1f,
            trackBrush = alphaBrush,
            thumbColor = thumbColor,
            modifier = Modifier,
            trackBackground = {
                CheckerboardBackground(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.5f)
                )
            }
        )
    }
}