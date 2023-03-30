package dev.kdrag0n.monet.theme

import dev.kdrag0n.colorkt.cam.Zcam
import dev.kdrag0n.colorkt.data.Illuminants
import dev.kdrag0n.colorkt.rgb.Srgb
import dev.kdrag0n.colorkt.tristimulus.CieXyzAbs.Companion.toAbs
import dev.kdrag0n.colorkt.ucs.lab.CieLab

object Monet {

    data class Config(
        val cond: Zcam.ViewingConditions,
        val complementColor: Srgb? = null,
        val chromaFactor: Double = 1.0,
        val accurateShades: Boolean = true,
        val useLinearLightness: Boolean = false,
    ) {

        companion object {
            @Suppress("MemberVisibilityCanBePrivate")
            val Default = Config()
        }

        constructor(
            // For all models
            chromaFactor: Double = 1.0,
            accurateShades: Boolean = true,
            // zero means not use,
            complementColorHex: Int = 0,
//            // ZCAM only
            whiteLuminance: Double = 199.52623149688787,
            useLinearLightness: Boolean = false,
        ) : this(
            createZcamViewingConditions(whiteLuminance), complementColorHex.takeIf { it != 0 }?.let { Srgb(it) },
            chromaFactor = chromaFactor,
            accurateShades = accurateShades,
            useLinearLightness = useLinearLightness,
        )
    }

    fun createZcamViewingConditions(whiteLuminance: Double) = Zcam.ViewingConditions(
        surroundFactor = Zcam.ViewingConditions.SURROUND_AVERAGE,
        // sRGB
        adaptingLuminance = 0.4 * whiteLuminance,
        // Gray world
        backgroundLuminance = CieLab(
            L = 50.0,
            a = 0.0,
            b = 0.0,
        ).toXyz().y * whiteLuminance,
        referenceWhite = Illuminants.D65.toAbs(whiteLuminance),
    )
}