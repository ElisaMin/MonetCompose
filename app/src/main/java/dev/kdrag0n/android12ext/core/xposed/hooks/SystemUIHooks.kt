package dev.kdrag0n.android12ext.core.xposed.hooks

import android.annotation.SuppressLint
import android.app.WallpaperColors
import android.app.WallpaperManager
import android.content.Context
import androidx.core.content.getSystemService
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import dev.kdrag0n.android12ext.core.xposed.hookMethod
import dev.kdrag0n.android12ext.monet.overlay.ThemeOverlayController
import dev.kdrag0n.android12ext.monet.theme.TargetColors
import timber.log.Timber

class SystemUIHooks(
    private val context: Context,
    private val lpparam: XC_LoadPackage.LoadPackageParam,
) {
    fun applyFeatureFlag(flag: String, enabled: Boolean) {
        val hook = object : XC_MethodReplacement() {
            override fun replaceHookedMethod(param: MethodHookParam) = enabled
        }

        try {
            lpparam.hookMethod(FEATURE_FLAGS_CLASS, hook, flag)
        } catch (e: NoSuchMethodException) {
            Timber.w("Feature flag does not exist: $flag")
        }
    }

    fun applyRoundedScreenshotBg() {
        val hook = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                param.thisObject.javaClass.getDeclaredField("DEBUG_COLOR").let {
                    it.isAccessible = true
                    it.setBoolean(null, false)
                }
            }
        }

        lpparam.hookMethod(
            "com.android.systemui.ScreenDecorations",
            hook,
            "updateColorInversion",
            Int::class.java
        )
    }

    fun applyPrivacyIndicators(enabled: Boolean) {
        val constructorHook = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                XposedHelpers.setBooleanField(param.thisObject, "micCameraAvailable", enabled)
                XposedHelpers.setBooleanField(param.thisObject, "locationAvailable", enabled)
            }
        }

        XposedHelpers.findAndHookConstructor(
            PRIVACY_CLASS,
            lpparam.classLoader,
            XposedHelpers.findClass("com.android.systemui.appops.AppOpsController", lpparam.classLoader),
            XposedHelpers.findClass("com.android.systemui.util.concurrency.DelayableExecutor", lpparam.classLoader),
            XposedHelpers.findClass("com.android.systemui.util.concurrency.DelayableExecutor", lpparam.classLoader),
            XposedHelpers.findClass("com.android.systemui.util.DeviceConfigProxy", lpparam.classLoader),
            XposedHelpers.findClass("com.android.systemui.settings.UserTracker", lpparam.classLoader),
            XposedHelpers.findClass("com.android.systemui.privacy.logging.PrivacyLogger", lpparam.classLoader),
            XposedHelpers.findClass("com.android.systemui.util.time.SystemClock", lpparam.classLoader),
            XposedHelpers.findClass("com.android.systemui.dump.DumpManager", lpparam.classLoader),
            constructorHook,
        )

        val flagHook = object : XC_MethodReplacement() {
            override fun replaceHookedMethod(param: MethodHookParam) = true
        }

        lpparam.hookMethod(
            "com.android.systemui.qs.QuickStatusBarHeaderController",
            flagHook,
            "getChipEnabled",
        )
    }

    fun applyThemeOverlayController(
        isGoogle: Boolean,
        chromaMultiplier: Double,
        multiColor: Boolean,
    ) {
        val controller = ThemeOverlayController(
            TargetColors(chromaMultiplier),
            chromaMultiplier,
            multiColor,
        )
        val clazz = if (isGoogle) THEME_CLASS_GOOGLE else THEME_CLASS_AOSP
        val wallpaperManager = context.getSystemService<WallpaperManager>()!!

        lpparam.hookMethod(clazz, object : XC_MethodReplacement() {
            override fun replaceHookedMethod(param: MethodHookParam): Any {
                return controller.getOverlay(param.args[0] as Int, param.args[1] as Int)
            }
        }, "getOverlay", Int::class.java, Int::class.java)

        lpparam.hookMethod(clazz, object : XC_MethodReplacement() {
            override fun replaceHookedMethod(param: MethodHookParam): Any {
                return controller.getNeutralColor(param.args[0] as WallpaperColors)
            }
        }, "getNeutralColor", WallpaperColors::class.java)

        lpparam.hookMethod(clazz, object : XC_MethodReplacement() {
            override fun replaceHookedMethod(param: MethodHookParam): Any {
                return controller.getAccentColor(param.args[0] as WallpaperColors)
            }
        }, "getAccentColor", WallpaperColors::class.java)

        // Quantization tweaks
        lpparam.hookMethod(THEME_CLASS_AOSP, object : XC_MethodHook() {
            // System UI has permission to draw the wallpaper
            @SuppressLint("MissingPermission")
            override fun beforeHookedMethod(param: MethodHookParam) {
                if (wallpaperManager.wallpaperInfo == null) {
                    // Static wallpaper: use custom quantizer
                    Timber.i("Extracting colors using custom quantizer")
                    val colors = WallpaperColors.fromDrawable(wallpaperManager.drawable)
                    XposedHelpers.setObjectField(param.thisObject, "mSystemColors", colors)
                }
            }
        }, "reevaluateSystemTheme", Boolean::class.java)
    }

    fun applySensorPrivacyToggles() {
        val hook = object : XC_MethodReplacement() {
            override fun replaceHookedMethod(param: MethodHookParam) = true
        }

        lpparam.hookMethod(
            "com.android.systemui.qs.tiles.MicrophoneToggleTile",
            hook,
            "isAvailable",
        )
        lpparam.hookMethod(
            "com.android.systemui.qs.tiles.CameraToggleTile",
            hook,
            "isAvailable",
        )
    }

    companion object {
        private const val FEATURE_FLAGS_CLASS = "com.android.systemui.statusbar.FeatureFlags"
        private const val GAME_ENTRY_CLASS = "com.google.android.systemui.gamedashboard.EntryPointController"
        private const val PRIVACY_CLASS = "com.android.systemui.privacy.PrivacyItemController"

        private const val THEME_CLASS_AOSP = "com.android.systemui.theme.ThemeOverlayController"
        private const val THEME_CLASS_GOOGLE = "com.google.android.systemui.theme.ThemeOverlayControllerGoogle"
    }
}
