package com.boot.designsystem.theme.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

object AppTheme {
  val colors: AppColors
    @Composable @ReadOnlyComposable get() = LocalColors.current

  val typography: AppTypography
    @Composable @ReadOnlyComposable get() = LocalTypography.current

  val dimensions: AppDimensions
    @Composable @ReadOnlyComposable get() = LocalDimensions.current
}

@Composable
fun AppTheme(
  //  colors: AppColors = if (isSystemInDarkTheme()) AppTheme.colors else AppTheme.colors,
  colors: AppColors = AppTheme.colors,
  typography: AppTypography = AppTheme.typography,
  dimensions: AppDimensions = AppTheme.dimensions,
  content: @Composable () -> Unit
) {
  // Explicitly creating a new object here so we don't mutate the initial [colors]
  // provided, and overwrite the values set in it.
  val rememberedColors =
    remember { colors.copy() }.apply { updateColorsFrom(colors) }
  CompositionLocalProvider(
    LocalColors provides rememberedColors,
    LocalDimensions provides dimensions,
    LocalTypography provides typography,
  ) { content() }
}
