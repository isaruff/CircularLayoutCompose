package com.isaruff.composeui.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.Layout
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


@Composable
fun CircularLayout(
    modifier: Modifier = Modifier,
    radius: Float? = null,
    content: @Composable () -> Unit
) {
    Layout(modifier = modifier,
        content = content,
        measurePolicy = { measurables, constraints ->
            val placeables =
                measurables.map { it.measure(constraints.copy(minWidth = 0, minHeight = 0)) }

            val maxChildWidth = placeables.maxOf { it.width }
            val maxChildHeight = placeables.maxOf { it.height }
            val maxChildrenDimension = maxOf(maxChildHeight, maxChildWidth)

            val centerOffset = Offset(
                (constraints.maxWidth - maxChildWidth) / 2f,
                (constraints.maxHeight - maxChildHeight) / 2f
            )
            val mRadius = radius ?: ((minOf(
                constraints.maxHeight,
                constraints.maxWidth
            ) - maxChildrenDimension) / 2f)


            layout(width = constraints.maxWidth, height = constraints.maxHeight) {
                placeables.forEachIndexed { index, placeable ->
                    val angleInRadians = 2 * Math.PI / measurables.size * index
                    val x = centerOffset.x - mRadius * cos(angleInRadians).toFloat()
                    val y = centerOffset.y - mRadius * sin(angleInRadians).toFloat()

                    placeable.placeWithLayer(x = x.roundToInt(), y = y.roundToInt()) {
                        rotationZ = Math.toDegrees(angleInRadians).toFloat() - 90f
                    }
                }
            }
        }
    )
}


