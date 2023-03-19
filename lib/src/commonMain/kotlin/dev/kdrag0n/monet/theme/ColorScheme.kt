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

    companion object {

        operator fun Int.rem(colorSwatch: ColorSwatch):Color
            = colorSwatch[this*10] ?: colorSwatch[0]!!
        operator fun Double.rem(colorSwatch: ColorSwatch):Color
            // convert to int for example 90.0-> 90 and then multiply by 10
            = colorSwatch[(this.toInt()*10)] ?: colorSwatch[0]!!

        fun ColorScheme.materialLight() = object : Material {

            // primary: Color = getMonetAccentColor(1, 700)
            override val primary: Color = 70 % accent1

            // onPrimary: Color = getMonetNeutralColor(1, 50)
            override val onPrimary: Color = 5 % neutral1

            // primaryContainer: Color = getMonetAccentColor(2, 100)
            override val primaryContainer: Color = 10 % accent2
            override val scrim: Color
                get() = TODO("Not yet implemented")

            // onPrimaryContainer: Color = getMonetAccentColor(1, 900)
            override val onPrimaryContainer: Color = 90 % accent1

            // inversePrimary: Color = getMonetAccentColor(1, 200)
            override val inversePrimary: Color = 20 % accent1

            // secondary: Color = getMonetAccentColor(2, 700)
            override val secondary: Color = 70 % accent2

            // onSecondary: Color = getMonetNeutralColor(1, 50)
            override val onSecondary: Color = 5 % neutral1

            // secondaryContainer: Color = getMonetAccentColor(2, 100)
            override val secondaryContainer: Color = 10 % accent2

            // onSecondaryContainer: Color = getMonetAccentColor(2, 900)
            override val onSecondaryContainer: Color = 90 % accent2

            // tertiary: Color = getMonetAccentColor(3, 600)
            override val tertiary: Color = 60 % accent3

            // onTertiary: Color = getMonetNeutralColor(1, 50)
            override val onTertiary: Color = 5 % neutral1

            // tertiaryContainer: Color = getMonetAccentColor(3, 100)
            override val tertiaryContainer: Color = 10 % accent3

            // onTertiaryContainer: Color = getMonetAccentColor(3, 900)
            override val onTertiaryContainer: Color = 90 % accent3

            // background: Color = getMonetNeutralColor(1, 50)
            override val background: Color = 5 % neutral1
            override val error: Color
                get() = TODO("Not yet implemented")
            override val errorContainer: Color
                get() = TODO("Not yet implemented")

            // onBackground: Color = getMonetNeutralColor(1, 900)
            override val onBackground: Color = 90 % neutral1
            override val onError: Color
                get() = TODO("Not yet implemented")
            override val onErrorContainer: Color
                get() = TODO("Not yet implemented")

            // surface: Color = getMonetNeutralColor(1, 50)
            override val surface: Color = 5 % neutral1
            override val surfaceTint: Color
                get() = TODO("Not yet implemented")

            // onSurface: Color = getMonetNeutralColor(1, 900)
            override val onSurface: Color = 90 % neutral1

            // surfaceVariant: Color = getMonetNeutralColor(2, 100)
            override val surfaceVariant: Color = 10 % neutral2

            // onSurfaceVariant: Color = getMonetNeutralColor(2, 700)
            override val onSurfaceVariant: Color = 70 % neutral2

            // inverseSurface: Color = getMonetNeutralColor(1, 800)
            override val inverseSurface: Color = 80 % neutral1

            // inverseOnSurface: Color = getMonetNeutralColor(2, 50)
            override val inverseOnSurface: Color = 5 % neutral2

            // outline: Color = getMonetAccentColor(2, 500)
            override val outline: Color = 50 % accent2
            override val outlineVariant: Color
                get() = TODO("Not yet implemented")
        }
        fun ColorScheme.materialDark() = object : Material {

            // primary: Color = getMonetAccentColor(1, 200),
            override val primary: Color = 20 % accent1

            // onPrimary: Color = getMonetAccentColor(1, 800),
            override val onPrimary: Color = 80 % accent1

            // primaryContainer: Color = getMonetAccentColor(1, 600),
            override val primaryContainer: Color = 60 % accent1
            override val scrim: Color
                get() = TODO("Not yet implemented")

            // onPrimaryContainer: Color = getMonetAccentColor(2, 100),
            override val onPrimaryContainer: Color = 10 % accent2

            // inversePrimary: Color = getMonetAccentColor(1, 600),
            override val inversePrimary: Color = 60 % accent1

            // secondary: Color = getMonetAccentColor(2, 200),
            override val secondary: Color = 20 % accent2

            // onSecondary: Color = getMonetAccentColor(2, 800),
            override val onSecondary: Color = 80 % accent2

            // secondaryContainer: Color = getMonetAccentColor(2, 700),
            override val secondaryContainer: Color = 70 % accent2

            // onSecondaryContainer: Color = getMonetAccentColor(2, 100),
            override val onSecondaryContainer: Color = 10 % accent2

            // tertiary: Color = getMonetAccentColor(3, 200),
            override val tertiary: Color = 20 % accent3

            // onTertiary: Color = getMonetAccentColor(3, 700),
            override val onTertiary: Color = 70 % accent3

            // tertiaryContainer: Color = getMonetAccentColor(3, 700),
            override val tertiaryContainer: Color = 70 % accent3

            // onTertiaryContainer: Color = getMonetAccentColor(3, 100),
            override val onTertiaryContainer: Color = 10 % accent3

            // background: Color = getMonetNeutralColor(1, 900),
            override val background: Color = 90 % neutral1
            override val error: Color
                get() = TODO("Not yet implemented")
            override val errorContainer: Color
                get() = TODO("Not yet implemented")

            // onBackground: Color = getMonetNeutralColor(1, 100),
            override val onBackground: Color = 10 % neutral1
            override val onError: Color
                get() = TODO("Not yet implemented")
            override val onErrorContainer: Color
                get() = TODO("Not yet implemented")

            // surface: Color = getMonetNeutralColor(1, 900),
            override val surface: Color = 90 % neutral1
            override val surfaceTint: Color
                get() = TODO("Not yet implemented")

            // onSurface: Color = getMonetNeutralColor(1, 100),
            override val onSurface: Color = 10 % neutral1

            // surfaceVariant: Color = getMonetNeutralColor(2, 700),
            override val surfaceVariant: Color = 70 % neutral2

            // onSurfaceVariant: Color = getMonetNeutralColor(2, 200),
            override val onSurfaceVariant: Color = 20 % neutral2

            // inverseSurface: Color = getMonetNeutralColor(1, 100),
            override val inverseSurface: Color = 10 % neutral1

            // inverseOnSurface: Color = getMonetNeutralColor(1, 800),
            override val inverseOnSurface: Color = 80 % neutral1

            // outline: Color = getMonetNeutralColor(2, 500),
            override val outline: Color = 50 % neutral2
            override val outlineVariant: Color
                get() = TODO("Not yet implemented")
        }

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