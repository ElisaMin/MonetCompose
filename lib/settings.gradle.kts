rootProject.name = "monet-kdrag0n"

// Include project compose-m3
include(":compose-m3")
include(":compose-m3:common")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs")
    }
}
