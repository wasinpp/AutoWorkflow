package com.boot.playground.animation.transition.lookahead

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boot.designsystem.transition.animateBounds
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun LookaheadWithSubcompose() {
	Column {
		LookaheadScope {
			val isWide by produceState(initialValue = true) {
				while (true) {
					delay(1000)
					value = !value
				}
			}
			var shouldAnimate by remember {
				mutableStateOf(false)
			}
			Button(onClick = { shouldAnimate = !shouldAnimate }) {
				Text(if (shouldAnimate) "Stop animating bounds" else "Animate bounds")
			}
			SubcomposeLayout(
				Modifier
					.background(colors[3])
					.conditionallyAnimateBounds(shouldAnimate)
			) {
				val constraints = it.copy(minWidth = 0)
				val placeable = subcompose(0) {
					Box(
						Modifier
							.conditionallyAnimateBounds(
								shouldAnimate,
								Modifier
									.width(if (isWide) 150.dp else 70.dp)
									.requiredHeight(400.dp)
							)
							.background(colors[0])
					)
				}[0].measure(constraints)

				val rightPlaceable = subcompose(1) {
					Box(
						Modifier
							.conditionallyAnimateBounds(
								shouldAnimate,
								Modifier
									.width(if (isWide) 150.dp else 70.dp)
									.requiredHeight(400.dp)
							)
							.background(colors[1])
					)
				}[0].measure(constraints)

				val totalWidth = placeable.width + rightPlaceable.width + 150
				layout(totalWidth, placeable.height) {
					val bottomPlaceable = subcompose(2) {
						Box(
							Modifier
								.width(totalWidth.toDp())
								.conditionallyAnimateBounds(
									shouldAnimate,
									Modifier.height(if (isWide) 150.dp else 70.dp)
								)
								.background(colors[2])
						)
					}[0].measure(constraints)

					placeable.place(50, 0)
					bottomPlaceable.place(0, placeable.height - bottomPlaceable.height - 50)
					rightPlaceable.place(placeable.width + 100, 0)
				}
			}
		}
	}
}

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.conditionallyAnimateBounds(
	shouldAnimate: Boolean,
	modifier: Modifier = Modifier
) = if (shouldAnimate) this.animateBounds(modifier) else this.then(modifier)

private val colors = listOf(
	Color(0xffff6f69),
	Color(0xffffcc5c),
	Color(0xff2a9d84),
	Color(0xff264653)
)
