@file:Suppress("unused", "DuplicatedCode", "FunctionName")
package me.heizi.compose.ext.monet.common

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import dev.kdrag0n.colorkt.rgb.Rgb
import dev.kdrag0n.monet.theme.ColorScheme.Dynamic
import dev.kdrag0n.monet.theme.Monet
import dev.kdrag0n.colorkt.rgb.Srgb as Kolor
import dev.kdrag0n.monet.theme.ColorScheme as Kdrag0nColorScheme

expect val kdrag0nProvider:Kdrag0nProvider
//private inline val provider get() = kdrag0nProvider

interface Kdrag0nProvider {
    fun isSystemDarkTheme():Boolean
    fun systemColor():Color?
    val config:Monet.Config
    fun getScheme(
        color:Color = systemColor() ?: Color(0x01579B),
        dark:Boolean = isSystemDarkTheme(),
        config: Monet.Config = this.config
    ) = Dynamic[Kolor(color.toArgb()),config].let {
        if (dark) it.materialDark() else it.materialLight()
    }
}

@Composable
fun rememberProvider(): Kdrag0nProvider {
    val state by mutableStateOf(kdrag0nProvider)
    return remember { state }
}

@Composable
fun Kdrag0nTheme(
    isDark: Boolean = kdrag0nProvider.isSystemDarkTheme(),
    baseScheme: ColorScheme = remember(isDark) { if (isDark) darkColorScheme() else lightColorScheme() },
    config: Monet.Config = kdrag0nProvider.config,
    seek: Color = kdrag0nProvider.systemColor() ?: Color(0x01579B),
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    content: @Composable () -> Unit
) {
    var scheme = baseScheme

    LaunchedEffect(seek,isDark) {
        scheme = baseScheme.modifyFrom(
            Dynamic[Kolor(seek.toArgb()),config].let {
                if (isDark) it.materialDark() else it.materialLight()
            }
        )
    }
    MaterialTheme(
        shapes = shapes,
        typography = typography,
        colorScheme = scheme,
        content = content
    )
}
@Composable
fun Kdrag0nTheme(
    provider: Kdrag0nProvider = rememberProvider(),
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    content: @Composable () -> Unit
) = Kdrag0nTheme(
    isDark = provider.isSystemDarkTheme(),
//    baseScheme = if (provider.isSystemDarkTheme()) darkColorScheme() else lightColorScheme(),
    config = provider.config,
    seek = provider.systemColor() ?: Color(0x01579B),
    shapes = shapes,
    typography = typography,
    content = content
)

//@Suppress("NOTHING_TO_INLINE")
//@Deprecated("not best but use it", ReplaceWith("Kdrag0nProvider"),DeprecationLevel.HIDDEN)
//inline fun Kdrag0nColorScheme.Material.toM3Scheme(isDark:Boolean)
//    = if (isDark) this.toM3Scheme(darkColorScheme()) else toM3Scheme(lightColorScheme())

fun ColorScheme.modifyFrom(kdrag0nScheme:Kdrag0nColorScheme.Material) = kdrag0nScheme.run {
    copy(
        primary = primary.toComposeColor(),
        onPrimary = onPrimary.toComposeColor(),
        primaryContainer = primaryContainer.toComposeColor(),
        onPrimaryContainer = onPrimaryContainer.toComposeColor(),

        secondary = secondary.toComposeColor(),
        onSecondary = onSecondary.toComposeColor(),
        secondaryContainer = secondaryContainer.toComposeColor(),
        onSecondaryContainer = onSecondaryContainer.toComposeColor(),

        tertiary = tertiary.toComposeColor(),
        onTertiary = onTertiary.toComposeColor(),
        tertiaryContainer = tertiaryContainer.toComposeColor(),
        onTertiaryContainer = onTertiaryContainer.toComposeColor(),

        background = background.toComposeColor(),
        onBackground = onBackground.toComposeColor(),

        surface = surface.toComposeColor(),
        onSurface = onSurface.toComposeColor(),
        surfaceVariant = surfaceVariant.toComposeColor(),
        onSurfaceVariant = onSurfaceVariant.toComposeColor(),


        outline = outline.toComposeColor(),
        inversePrimary = inversePrimary.toComposeColor(),
        inverseSurface = inverseSurface.toComposeColor(),
        inverseOnSurface = inverseOnSurface.toComposeColor(),
    )
}
@Suppress("NOTHING_TO_INLINE")
@Deprecated("not best but use it", ReplaceWith("lightM3Scheme"),DeprecationLevel.HIDDEN)
inline fun Kdrag0nColorScheme.Material.toM3Scheme(
    baseScheme:ColorScheme = lightColorScheme()
) = ColorScheme(
    primary = primary.toComposeColor(),
    onPrimary = onPrimary.toComposeColor(),
    primaryContainer = primaryContainer.toComposeColor(),
    onPrimaryContainer = onPrimaryContainer.toComposeColor(),

    secondary = secondary.toComposeColor(),
    onSecondary = onSecondary.toComposeColor(),
    secondaryContainer = secondaryContainer.toComposeColor(),
    onSecondaryContainer = onSecondaryContainer.toComposeColor(),

    tertiary = tertiary.toComposeColor(),
    onTertiary = onTertiary.toComposeColor(),
    tertiaryContainer = tertiaryContainer.toComposeColor(),
    onTertiaryContainer = onTertiaryContainer.toComposeColor(),

    background = background.toComposeColor(),
    onBackground = onBackground.toComposeColor(),

    surface = surface.toComposeColor(),
    onSurface = onSurface.toComposeColor(),
    surfaceVariant = surfaceVariant.toComposeColor(),
    onSurfaceVariant = onSurfaceVariant.toComposeColor(),


    outline = outline.toComposeColor(),
    inversePrimary = inversePrimary.toComposeColor(),
    inverseSurface = inverseSurface.toComposeColor(),
    inverseOnSurface = inverseOnSurface.toComposeColor(),

    // base
    outlineVariant = baseScheme.outlineVariant,
    scrim = baseScheme.scrim,
    surfaceTint = baseScheme.surfaceTint,
    error = baseScheme.error,
    onError = baseScheme.onError,
    errorContainer = baseScheme.errorContainer,
    onErrorContainer = baseScheme.onErrorContainer,
)

fun Kdrag0nColorScheme.Material.lightM3Scheme(): ColorScheme = run {
    lightColorScheme(
        primary = primary.toComposeColor(),
        onPrimary = onPrimary.toComposeColor(),
        primaryContainer = primaryContainer.toComposeColor(),
        onPrimaryContainer = onPrimaryContainer.toComposeColor(),

        secondary = secondary.toComposeColor(),
        onSecondary = onSecondary.toComposeColor(),
        secondaryContainer = secondaryContainer.toComposeColor(),
        onSecondaryContainer = onSecondaryContainer.toComposeColor(),

        tertiary = tertiary.toComposeColor(),
        onTertiary = onTertiary.toComposeColor(),
        tertiaryContainer = tertiaryContainer.toComposeColor(),
        onTertiaryContainer = onTertiaryContainer.toComposeColor(),

        background = background.toComposeColor(),
        onBackground = onBackground.toComposeColor(),

        surface = surface.toComposeColor(),
        onSurface = onSurface.toComposeColor(),
        surfaceVariant = surfaceVariant.toComposeColor(),
        onSurfaceVariant = onSurfaceVariant.toComposeColor(),


        outline = outline.toComposeColor(),
        inversePrimary = inversePrimary.toComposeColor(),
        inverseSurface = inverseSurface.toComposeColor(),
        inverseOnSurface = inverseOnSurface.toComposeColor(),
    )
}
fun Kdrag0nColorScheme.Material.darkM3Scheme(): ColorScheme = run {
    lightColorScheme(
        primary = primary.toComposeColor(),
        onPrimary = onPrimary.toComposeColor(),
        primaryContainer = primaryContainer.toComposeColor(),
        onPrimaryContainer = onPrimaryContainer.toComposeColor(),

        secondary = secondary.toComposeColor(),
        onSecondary = onSecondary.toComposeColor(),
        secondaryContainer = secondaryContainer.toComposeColor(),
        onSecondaryContainer = onSecondaryContainer.toComposeColor(),

        tertiary = tertiary.toComposeColor(),
        onTertiary = onTertiary.toComposeColor(),
        tertiaryContainer = tertiaryContainer.toComposeColor(),
        onTertiaryContainer = onTertiaryContainer.toComposeColor(),

        background = background.toComposeColor(),
        onBackground = onBackground.toComposeColor(),

        surface = surface.toComposeColor(),
        onSurface = onSurface.toComposeColor(),
        surfaceVariant = surfaceVariant.toComposeColor(),
        onSurfaceVariant = onSurfaceVariant.toComposeColor(),


        outline = outline.toComposeColor(),
        inversePrimary = inversePrimary.toComposeColor(),
        inverseSurface = inverseSurface.toComposeColor(),
        inverseOnSurface = inverseOnSurface.toComposeColor(),
    )
}

fun dev.kdrag0n.colorkt.Color.toComposeColor(): Color = when (this) {
    is Rgb -> Color(r.toFloat(), g.toFloat(),b.toFloat())
    else -> throw NotImplementedError("Not implemented for ${this::class.simpleName}")
}