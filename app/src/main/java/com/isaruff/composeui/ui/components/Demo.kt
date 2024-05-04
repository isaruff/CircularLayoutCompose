package com.isaruff.composeui.ui.components

import androidx.annotation.FloatRange
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun Demo(data: List<Int>) {

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(10_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )
    CircularLayout(
        modifier = Modifier
            .graphicsLayer {
            rotationZ = rotation
        },
        radius = 300f
    ) {
        data.forEach { value ->
            Trapezoid(
                scaleYTarget = value.toFloat() * Random.nextFloat() / 1.5f,
                scaleXMin = Random.nextFloat() * 2
            )
        }
    }
}

val trapezoidShape = TrapezoidShape(0.2f)

@Composable
private fun Trapezoid(
    modifier: Modifier = Modifier,
    scaleYTarget: Float,
    scaleXMin: Float,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sound_tracker_infinite_transition")

    val scaleY by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = scaleYTarget.coerceAtMost(1.6f),
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "sound_tracker_rotator_anim"
    )

    val scaleX by infiniteTransition.animateFloat(
        initialValue = scaleXMin,
        targetValue = 1.5f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )
    Box(modifier = modifier
        .graphicsLayer {
            alpha = 0.5f
            this.scaleY = scaleY
            this.scaleX = scaleX
        }
        .size(height = 120.dp, width = 40.dp)
        .clip(trapezoidShape)
        .drawWithCache {
            onDrawWithContent {
                drawRect(Color.Gray)
            }
        })
}


class TrapezoidShape(
    @FloatRange(from = 0.0, to = 1.0) private val baseRatio: Float
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: Density
    ): Outline {
        val sidePieceRatio = (1 - baseRatio) / 2f

        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width * sidePieceRatio, size.height)
            lineTo(size.width * (sidePieceRatio + baseRatio), size.height)
            lineTo(size.width, 0f)
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
@Preview(showBackground = true)
fun DemoPrev() {
    val dummyData = remember {
        List(60) { Random.nextInt().coerceIn(1..3) }
    }

    Demo(data = dummyData)
}