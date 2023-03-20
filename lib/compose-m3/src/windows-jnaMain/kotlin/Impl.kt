package me.heizi.compose.ext.monet.common

import dev.kdrag0n.colorkt.rgb.Srgb
import dev.kdrag0n.monet.theme.Monet

actual fun systemIsDarkTheme(): Boolean
    = TODO()
actual fun systemSeekColor(): Srgb?
    = TODO()
actual fun monetConfig(): Monet.Config
    = Monet.Config.default