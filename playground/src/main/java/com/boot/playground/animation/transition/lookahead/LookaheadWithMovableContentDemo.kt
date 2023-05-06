package com.boot.playground.animation.transition.lookahead

import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentWithReceiverOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.layout.intermediateLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import com.boot.designsystem.transition.DeferredAnimation
import com.boot.playground.animation.transition.fancy.AnimatedDotsDemo
import com.boot.playground.animation.transition.statetransition.InfiniteProgress
import com.boot.playground.animation.transition.statetransition.InfinitePulsingHeart

@Composable
@Preview
fun LookaheadWithMovableContentDemo() {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		var isSingleColumn by remember { mutableStateOf(true) }

		Column(
			Modifier
				.padding(100.dp)
				.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Row(modifier = Modifier.clickable {
				isSingleColumn = true
			}, verticalAlignment = Alignment.CenterVertically) {
				RadioButton(isSingleColumn, { isSingleColumn = true })
				Text("Single Column")
			}
			Row(modifier = Modifier.clickable {
				isSingleColumn = false
			}, verticalAlignment = Alignment.CenterVertically) {
				RadioButton(!isSingleColumn, { isSingleColumn = false })
				Text("Double Column")
			}
		}

		val items = remember {
			colors.mapIndexed { id, color ->
				movableContentWithReceiverOf<LookaheadScope, Float> { weight ->
					Box(
						Modifier
							.padding(15.dp)
							.height(80.dp)
							.fillMaxWidth(weight)
							.animateBoundsInScope()
							.background(color, RoundedCornerShape(20)),
						contentAlignment = Alignment.Center
					) {
						when (id) {
							0 -> CircularProgressIndicator(color = Color.White)
							1 -> Box(Modifier.graphicsLayer {
								scaleX = 0.5f
								scaleY = 0.5f
								translationX = 100f
							}) {
								AnimatedDotsDemo()
							}

							2 -> Box(Modifier.graphicsLayer {
								scaleX = 0.5f
								scaleY = 0.5f
							}) { InfinitePulsingHeart() }

							else -> InfiniteProgress()
						}
					}
				}
			}
		}
		Box(Modifier.fillMaxSize()) {
			LookaheadScope {
				if (isSingleColumn) {
					Column(
						Modifier.fillMaxSize(),
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						items.forEach {
							it(0.8f)
						}
					}
				} else {
					Row {
						Column(Modifier.weight(1f)) {
							items.forEachIndexed { id, item ->
								if (id % 2 == 0) {
									item(1f)
								}
							}
						}
						Column(Modifier.weight(1f)) {
							items.forEachIndexed { id, item ->
								if (id % 2 != 0) {
									item(1f)
								}
							}
						}
					}
				}
			}
		}
	}
}

context (LookaheadScope)
fun Modifier.animateBoundsInScope(): Modifier = composed {
	val sizeAnim = remember { DeferredAnimation(IntSize.VectorConverter) }
	val offsetAnim = remember { DeferredAnimation(IntOffset.VectorConverter) }
	this.intermediateLayout { measurable, _ ->
		sizeAnim.updateTarget(
			lookaheadSize,
			spring()
		)
		measurable.measure(
			Constraints.fixed(
				sizeAnim.value!!.width,
				sizeAnim.value!!.height
			)
		)
			.run {
				layout(width, height) {
					coordinates?.let {
						val target =
							lookaheadScopeCoordinates.localLookaheadPositionOf(it)
								.round()
						offsetAnim.updateTarget(target, spring())
						val current = lookaheadScopeCoordinates.localPositionOf(
							it,
							Offset.Zero
						).round()
						val (x, y) = offsetAnim.value!! - current
						place(x, y)
					} ?: place(0, 0)
				}
			}
	}
}

private val colors = listOf(
	Color(0xffff6f69),
	Color(0xffffcc5c),
	Color(0xff264653),
	Color(0xff2a9d84)
)
