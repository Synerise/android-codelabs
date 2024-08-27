pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven(url = "https://jitpack.io")
        maven(url =  "https://pkgs.dev.azure.com/Synerise/AndroidSDK/_packaging/prod/maven/v1")
        google()
        mavenCentral()
    }
}

rootProject.name = "Synerise Android Integration App"
include(":app")
 