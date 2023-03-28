> ChatGPT generated readme.md  

# MonetCompose  
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/ElisaMin/MonetCompose)](https://github.com/ElisaMin/MonetCompose/releases/latest)   

MonetCompose is a Compose desktop library that allows you to generate color palettes based on Kdrag0n's Monet Engine. The library can be implemented in two different ways - `windows` and `window-styler` - depending on the user's preferences and requirements. The library is available on GitHub Package.  

## Submodules
versions updated : 0.0.1-alpha02
### `windows`
`windows` submodule provides a default implementation that works for Windows 10 and 11 with the `Kdrag0nTheme` API.

To use this submodule, add the following dependency to your Gradle or Kotlin DSL project:
```kts
implementation("me.heizi.monet-kdrag0n:compose-m3-windows:<version>")
```
Replace `<version>` with the version you'd like to use. You can download the package from GitHub Package.

### `windows-jna`
The `windows-jna` submodule uses JNA to call Windows system APIs to retrieve the system color. To use this submodule, add the following dependency to your Gradle or Kotlin DSL project:
^```kts
implementation("me.heizi.monet-kdrag0n:compose-m3-windows-jna:<version>")
^```
Replace `<version>` with the version you'd like to use. You can download the package from GitHub Package.

The `windows-jna` submodule is built on top of the JNA library, which enables Java code to call native code. You can learn more about JNA on the official JNA website.

### `window-styler`
`window-styler` submodule uses the `ComposeWindowStyler` library to extend the functionality of the `windows` submodule with the `MonetWindow` API.

To use this submodule, add the following dependency to your Gradle or Kotlin DSL project:
```kts
implementation("me.heizi.monet-kdrag0n:compose-m3-window-styler:<version>")
```
Replace `<version>` with the version you'd like to use. You can download the package from GitHub Package.

The `window-styler` submodule is built on top of the `ComposeWindowStyler` library, which provides API for theming and styling Compose desktop window decorations. You can learn more about `ComposeWindowStyler` on the [official library page](https://github.com/MayakaApps/ComposeWindowStyler).

## Usage

### `windows` or `JNA` Submodule

Here's an example of using MonetCompose with the `Kdrag0nTheme` API:

```kotlin
Kdrag0nTheme {
// your composable content here
}
```

### `window-styler` Submodule

Here's an example of using MonetCompose with the `MonetWindow` API:

```kotlin
// compose window
MonetWindow.of {
    Kdrag0nTheme { 
    // your composable content here
    }
}
```

In the `MonetWindow` API, you can change the `MaterialTheme` to any other Compose theme you prefer. The `it` parameter in `MaterialTheme` provides the generated palette from Monet Engine.

## Platform Availability
MonetCompose is currently only available on Windows. However, if you need MonetCompose for another platform, feel free to open an issue or submit a pull request on GitHub.

## License
This library is released under the MIT license. Please refer to the [LICENSE](LICENSE) file in this repository for more details.
