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

