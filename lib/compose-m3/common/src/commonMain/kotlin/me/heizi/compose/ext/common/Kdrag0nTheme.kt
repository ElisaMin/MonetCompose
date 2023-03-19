package me.heizi.compose.ext.common

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import dev.kdrag0n.colorkt.rgb.Rgb
import dev.kdrag0n.colorkt.rgb.Srgb
import dev.kdrag0n.monet.theme.ColorScheme.Companion.materialDark
import dev.kdrag0n.monet.theme.ColorScheme.Companion.materialLight
import dev.kdrag0n.monet.theme.Dynamic
import dev.kdrag0n.monet.theme.Monet
import dev.kdrag0n.colorkt.rgb.Srgb as Kolor
import dev.kdrag0n.monet.theme.ColorScheme as Kdrag0nColorScheme

@Composable
external fun systemIsDarkTheme(): Boolean
external fun systemSeekColor(): Kolor?
external fun monetConfig(): Monet.Config


@Composable
fun Kdrag0nTheme(
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    content: @Composable () -> Unit
) {
    val scheme = kdrag0nColorScheme
    MaterialTheme(
        shapes = shapes,
        typography = typography,
        colorScheme = scheme,
        content = content
    )
}

//create a seek color provider
val LocalSeekColorProvider: ProvidableCompositionLocal<Rgb> = staticCompositionLocalOf {
    systemSeekColor() ?: Kolor(0x01579B)
}

val kdrag0nColorScheme: ColorScheme
    @Composable get() = if (systemIsDarkTheme()) rememberKdrag0nDarkColorScheme() else rememberKdrag0nLightColorScheme()

@Composable
fun currentKdrag0nColorState(): State<Kdrag0nColorScheme> {
    val config by remember { mutableStateOf(monetConfig()) }
    return mutableStateOf(Dynamic[LocalSeekColorProvider.current, config])
}


@Composable
fun rememberKdrag0nColorState(): Kdrag0nColorScheme =
    remember(LocalSeekColorProvider.current) { currentKdrag0nColorState() }.value

fun rememberKdrag0nLightColorScheme(): ColorScheme = remember {
    rememberKdrag0nColorState().materialLight().run {
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

//            error = error.toComposeColor(),
//            onError = onError.toComposeColor(),
//            errorContainer = errorContainer.toComposeColor(),
//            onErrorContainer = onErrorContainer.toComposeColor(),

            background = background.toComposeColor(),
            onBackground = onBackground.toComposeColor(),

            surface = surface.toComposeColor(),
            onSurface = onSurface.toComposeColor(),
            surfaceVariant = surfaceVariant.toComposeColor(),
            onSurfaceVariant = onSurfaceVariant.toComposeColor(),
            // errors
//            surfaceTint = surfaceTint.toComposeColor(),
//            scrim = scrim.toComposeColor(),
//            outlineVariant = outlineVariant.toComposeColor(),

            outline = outline.toComposeColor(),
            inversePrimary = inversePrimary.toComposeColor(),
            inverseSurface = inverseSurface.toComposeColor(),
            inverseOnSurface = inverseOnSurface.toComposeColor(),
        )
    }
}



fun rememberKdrag0nDarkColorScheme(): ColorScheme = remember {
    rememberKdrag0nColorState().materialDark().run {
        darkColorScheme(
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

//            error = error.toComposeColor(),
//            onError = onError.toComposeColor(),
//            errorContainer = errorContainer.toComposeColor(),
//            onErrorContainer = onErrorContainer.toComposeColor(),

            background = background.toComposeColor(),
            onBackground = onBackground.toComposeColor(),

            surface = surface.toComposeColor(),
            onSurface = onSurface.toComposeColor(),
            surfaceVariant = surfaceVariant.toComposeColor(),
            onSurfaceVariant = onSurfaceVariant.toComposeColor(),
            // errors
//            surfaceTint = surfaceTint.toComposeColor(),
//            scrim = scrim.toComposeColor(),
//            outlineVariant = outlineVariant.toComposeColor(),

            outline = outline.toComposeColor(),
            inversePrimary = inversePrimary.toComposeColor(),
            inverseSurface = inverseSurface.toComposeColor(),
            inverseOnSurface = inverseOnSurface.toComposeColor(),
        )
    }
}

fun dev.kdrag0n.colorkt.Color.toComposeColor(): Color = when (this) {
    is Srgb -> Color(toRgb8())
    is Rgb -> Srgb(r, g, b).toComposeColor()
    else -> throw NotImplementedError("Not implemented for ${this::class.simpleName}")
}
