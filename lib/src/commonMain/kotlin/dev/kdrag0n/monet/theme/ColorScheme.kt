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

    // Material Color support
    interface Material {

    }
}
