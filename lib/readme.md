> ChatGPT generated 

# monet compose
 
## Overview
MonetCompose is a Compose desktop library that allows you to generate color palettes based on Kdrag0n's Monet Engine. The library works on Windows 10 and 11 and can retrieve the primary accent color and detect whether the user has enabled dark mode.

## Usage
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/ElisaMin/MonetCompose)](https://github.com/ElisaMin/MonetCompose/releases/latest)  
To use the library, simply add the following dependency to your Gradle or Kotlin DSL project:
```kts
implementation("me.heizi.monet-kdrag0n:compose-m3-windows-jna:<version>")
```
Replace`<version>`with the version you'd like to use. You can find the latest version in the release section.

## Example
Here's an example of using MonetCompose to generate a color scheme:

```kotlin
Kdrag0nTheme {
    // your composable content here
}
```
This will generate a MaterialTheme based on the detected primary accent color and dark mode setting.

## Platform Availability
Note that MonetCompose is currently only available on Windows. However, if you need MonetCompose for another platform, feel free to open an issue or submit a pull request on GitHub.

## License
This library is released under the MIT license. Please refer to the [LICENSE](LICENSE) file in this repository for more details.