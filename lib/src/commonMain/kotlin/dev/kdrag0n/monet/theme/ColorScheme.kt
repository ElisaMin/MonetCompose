@file:Suppress("NOTHING_TO_INLINE")
package dev.kdrag0n.monet.theme

import dev.kdrag0n.colorkt.Color
import dev.kdrag0n.colorkt.cam.Zcam
import dev.kdrag0n.colorkt.cam.Zcam.Companion.toZcam
import dev.kdrag0n.colorkt.conversion.ConversionGraph.convert
import dev.kdrag0n.colorkt.gamut.LchGamut
import dev.kdrag0n.colorkt.gamut.LchGamut.clipToLinearSrgb
import dev.kdrag0n.colorkt.rgb.Srgb
import dev.kdrag0n.colorkt.tristimulus.CieXyz
import dev.kdrag0n.colorkt.tristimulus.CieXyzAbs.Companion.toAbs
import io.github.aakira.napier.Napier

typealias ColorSwatch = Map<Int, Color>

inline operator fun Int.rem(colorSwatch: ColorSwatch): Color = colorSwatch[this * 10] ?: colorSwatch[0]!!
inline operator fun Double.rem(colorSwatch: ColorSwatch): Color
// convert to int for example 90.0-> 90 and then multiply by 10
        = colorSwatch[(this.toInt() * 10)] ?: colorSwatch[0]!!

interface ColorScheme {
    val neutral1: ColorSwatch
    val neutral2: ColorSwatch

    val accent1: ColorSwatch
    val accent2: ColorSwatch
    val accent3: ColorSwatch

    interface Dynamic : ColorScheme {
        companion object {
            // Hue shift for the tertiary accent color (accent3), in degrees.
            // 60 degrees = shifting by a secondary color
            private const val ACCENT3_HUE_SHIFT_DEGREES = 60.0
            private fun transformColor(
                target: Zcam, seed: Zcam, reference: Zcam,
                cond: Zcam.ViewingConditions,
                accurateShades: Boolean = true
                // Keep target lightness.
            ): Color = target.lightness.let { lightness ->
                // Allow colorless gray and low-chroma colors by clamping.
                // To preserve chroma ratios, scale chroma by the reference (A-1 / N-1).
                // Zero reference chroma won't have chroma anyway, so use 0 to avoid a divide-by-zero
                val scaleC = reference.chroma.takeIf { it == 0.0 }
                // Non-zero reference chroma = possible chroma scale
                    ?: (seed.chroma.coerceIn(0.0, reference.chroma) / reference.chroma)
                // Use the seed color's hue, since it's the most prominent feature of the theme.
                val chroma = target.chroma * scaleC
                val hue = seed.hue

                val newColor = Zcam(
                    lightness = lightness,
                    chroma = chroma,
                    hue = hue,
                    viewingConditions = cond,
                )
                if (accurateShades) {
                    newColor.clipToLinearSrgb(LchGamut.ClipMethod.PRESERVE_LIGHTNESS)
                } else {
                    newColor.clipToLinearSrgb(LchGamut.ClipMethod.ADAPTIVE_TOWARDS_MID, alpha = 5.0)
                }
            }

            private fun transformSwatch(
                swatch: ColorSwatch,
                seed: Zcam,
                referenceSwatch: ColorSwatch,
                cond: Zcam.ViewingConditions,
                accurateShades: Boolean
            ): ColorSwatch {
                return swatch.map { (shade, color) ->
                    val target = color as? Zcam
                        ?: color.convert<CieXyz>().toAbs(cond.referenceWhite.y)
                            .toZcam(cond, include2D = false)
                    val reference = referenceSwatch[shade]!! as? Zcam
                        ?: color.convert<CieXyz>().toAbs(cond.referenceWhite.y)
                            .toZcam(cond, include2D = false)
                    val newLch = transformColor(target, seed, reference, cond, accurateShades)
                    val newSrgb = newLch.convert<Srgb>()

                    Napier.d("Transform: [$shade] $target => $newLch => ${newSrgb.toHex()}")
                    shade to newSrgb
                }.toMap()
            }

            operator fun invoke(
                targets: Target,
                seedColor: Color,
                chromaFactor: Double = 1.0,
                cond: Zcam.ViewingConditions,
                accurateShades: Boolean = true,
                complementColor: Color? = null,
            ) = seedColor.convert<CieXyz>()
                .toAbs(cond.referenceWhite.y)
                .toZcam(cond, include2D = false)
                .let { lch ->
                    lch.copy(chroma = lch.chroma * chromaFactor)
                }.run {
                    Napier.i("Seed color: ${seedColor.convert<Srgb>().toHex()} => $this")
                    this to copy(
                        hue = complementColor?.convert<CieXyz>()
                            ?.toAbs(cond.referenceWhite.y)
                            ?.toZcam(cond, include2D = false)?.hue
                            ?: (hue + ACCENT3_HUE_SHIFT_DEGREES)
                    )
                }.let { (neutral, accent3) ->
                    val accent = neutral.copy()
                    fun transformSwatch(swatch: ColorSwatch, seed: Zcam, referenceSwatch: ColorSwatch): ColorSwatch =
                        transformSwatch(swatch, seed, referenceSwatch, cond, accurateShades)
                    object : ColorScheme {

                        // Main accent color. Generally, this is close to the seed color.
                        override val accent1 = transformSwatch(targets.accent1, accent, targets.accent1)
                        // Secondary accent color. Darker shades of accent1.
                        override val accent2 = transformSwatch(targets.accent2, accent, targets.accent1)
                        // Tertiary accent color. Seed color shifted to the next secondary color via hue offset.
                        override val accent3 = transformSwatch(targets.accent3, accent3, targets.accent1)

                        // Main background color. Tinted with the seed color.
                        override val neutral1 = transformSwatch(targets.neutral1, neutral, targets.neutral1)
                        // Secondary background color. Slightly tinted with the seed color.
                        override val neutral2 = transformSwatch(targets.neutral2, neutral, targets.neutral1)
                    }
                }

            operator fun get(
                seedColor: Color, config: Monet.Config = Monet.Config.default
            ) = with(config) {
                invoke(
                    targets = Target.MaterialYou(
                        chromaFactor,
                        useLinearLightness,
                        cond
                    ),
                    seedColor = seedColor,
                    chromaFactor = chromaFactor,
                    cond = cond,
                    accurateShades = accurateShades,
                    complementColor = complementColor,
                )
            }

        }
    }


    // Helpers
    val neutralColors get() = Pair(neutral1, neutral2)
    val accentColors get() = Triple(accent1, accent2, accent3)

    fun materialLight() = object : Material {
        override val primary: Color = 70 % accent1
        override val onPrimary: Color = 5 % neutral1
        override val primaryContainer: Color = 10 % accent2

        //errors
        @Deprecated("gen by bot")
        override val scrim: Color = 10 % accent1
        override val onPrimaryContainer: Color = 90 % accent1
        override val inversePrimary: Color = 20 % accent1
        override val secondary: Color = 70 % accent2
        override val onSecondary: Color = 5 % neutral1
        override val secondaryContainer: Color = 10 % accent2
        override val onSecondaryContainer: Color = 90 % accent2
        override val tertiary: Color = 60 % accent3
        override val onTertiary: Color = 5 % neutral1
        override val tertiaryContainer: Color = 10 % accent3
        override val onTertiaryContainer: Color = 90 % accent3
        override val background: Color = 5 % neutral1
        override val onBackground: Color = 90 % neutral1
        @Deprecated("gen by bot")
        override val error: Color = 50 % accent3
        @Deprecated("gen by bot")
        override val errorContainer: Color = 10 % accent3
        @Deprecated("gen by bot")
        override val onError: Color = 5 % accent3
        @Deprecated("gen by bot")
        override val onErrorContainer: Color = 90 % accent3
        override val surface: Color = 5 % neutral1

        // errors
        @Deprecated("gen by bot")
        override val surfaceTint: Color = 10 % neutral1
        override val onSurface: Color = 90 % neutral1
        override val surfaceVariant: Color = 10 % neutral2
        override val onSurfaceVariant: Color = 70 % neutral2
        override val inverseSurface: Color = 80 % neutral1
        override val inverseOnSurface: Color = 5 % neutral2
        override val outline: Color = 50 % accent2

        // errors
        @Deprecated("gen by bot")
        override val outlineVariant: Color = 30 % accent2
    }
    fun materialDark() = object : Material {
        override val primary: Color = 20 % accent1
        override val onPrimary: Color = 80 % accent1
        override val primaryContainer: Color = 60 % accent1

        //errors
        @Deprecated("gen by bot")
        override val scrim: Color = 10 % accent1
        override val onPrimaryContainer: Color = 10 % accent2
        override val inversePrimary: Color = 60 % accent1
        override val secondary: Color = 20 % accent2
        override val onSecondary: Color = 80 % accent2
        override val secondaryContainer: Color = 70 % accent2
        override val onSecondaryContainer: Color = 10 % accent2
        override val tertiary: Color = 20 % accent3
        override val onTertiary: Color = 70 % accent3
        override val tertiaryContainer: Color = 70 % accent3
        override val onTertiaryContainer: Color = 10 % accent3
        override val background: Color = 90 % neutral1
        override val onBackground: Color = 10 % neutral1

        @Deprecated("gen by bot")
        override val error: Color = 50 % accent3
        @Deprecated("gen by bot")
        override val errorContainer: Color = 10 % accent3
        @Deprecated("gen by bot")
        override val onError: Color = 5 % accent3
        @Deprecated("gen by bot")
        override val onErrorContainer: Color = 90 % accent3
        @Deprecated("gen by bot")
        override val surfaceTint: Color = 10 % neutral1

        override val surface: Color = 90 % neutral1
        override val onSurface: Color = 10 % neutral1
        override val surfaceVariant: Color = 70 % neutral2
        override val onSurfaceVariant: Color = 20 % neutral2
        override val inverseSurface: Color = 10 % neutral1
        override val inverseOnSurface: Color = 80 % neutral1
        override val outline: Color = 50 % neutral2

        //errors
        @Deprecated("gen by bot")
        override val outlineVariant: Color = 30 % neutral2
    }

    /**
     * reflect to material 3 color scheme
     */
    interface Material {
        /**
         * The background color that appears behind scrollable content.
         */
        val background: Color

        /**
         * The error color is used to indicate errors in components, such as invalid text in a text field.
         */
        val error: Color

        /**
         * The preferred tonal color of error containers.
         */
        val errorContainer: Color

        /**
         * A color that contrasts well with inverseSurface.
         */
        val inverseOnSurface: Color

        /**
         * Color to be used as a "primary" color in places where the inverse color scheme is needed, such as the button on a SnackBar.
         */
        val inversePrimary: Color

        /**
         * A color that contrasts sharply with surface.
         */
        val inverseSurface: Color

        /**
         * Color used for text and icons displayed on top of the background color.
         */
        val onBackground: Color

        /**
         * Color used for text and icons displayed on top of the error color.
         */
        val onError: Color

        /**
         * The color (and state variants) that should be used for content on top of errorContainer.
         */
        val onErrorContainer: Color

        /**
         * Color used for text and icons displayed on top of the primary color.
         */
        val onPrimary: Color

        /**
         * The color (and state variants) that should be used for content on top of primaryContainer.
         */
        val onPrimaryContainer: Color

        /**
         * Color used for text and icons displayed on top of the secondary color.
         */
        val onSecondary: Color

        /**
         * The color (and state variants) that should be used for content on top of secondaryContainer.
         */
        val onSecondaryContainer: Color

        /**
         * Color used for text and icons displayed on top of the surface color.
         */
        val onSurface: Color

        /**
         * The color (and state variants) that can be used for content on top of surface.
         */
        val onSurfaceVariant: Color

        /**
         * Color used for text and icons displayed on top of the tertiary color.
         */
        val onTertiary: Color

        /**
         * The color (and state variants) that should be used for content on top of tertiaryContainer.
         */
        val onTertiaryContainer: Color

        /**
         * Subtle color used for boundaries.
         */
        val outline: Color

        /**
         * Utility color used for boundaries for decorative elements when strong contrast is not required.
         */
        val outlineVariant: Color

        /**
         * The primary color is the color displayed most frequently across your app’s screens and components.
         */
        val primary: Color

        /**
         * The preferred tonal color of containers.
         */
        val primaryContainer: Color

        /**
         * Color of a scrim that obscures content.
         */
        val scrim: Color

        /**
         * The secondary color provides more ways to accent and distinguish your product.
         */
        val secondary: Color

        /**
         * A tonal color to be used in containers.
         */
        val secondaryContainer: Color

        /**
         * The surface color that affect surfaces of components, such as cards, sheets, and menus.
         */
        val surface: Color

        /**
         * This color will be used by components that apply tonal elevation and is applied on top of surface.
         */
        val surfaceTint: Color

        /**
         * Another option for a color with similar uses of surface.
         */
        val surfaceVariant: Color

        /**
         * The tertiary color that can be used to balance primary and secondary colors, or bring heightened attention to an element such as an input field.
         */
        val tertiary: Color

        /**
         * A tonal color to be used in containers.
         */
        val tertiaryContainer: Color
    }
}