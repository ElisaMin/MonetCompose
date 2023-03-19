package dev.kdrag0n.monet.theme

import dev.kdrag0n.colorkt.Color

typealias ColorSwatch = Map<Int, Color>

interface ColorScheme {
     val neutral1: ColorSwatch
     val neutral2: ColorSwatch

     val accent1: ColorSwatch
     val accent2: ColorSwatch
     val accent3: ColorSwatch

    // Helpers
    val neutralColors get() = Pair(neutral1, neutral2)
    val accentColors get() = Triple(accent1, accent2, accent3)

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
