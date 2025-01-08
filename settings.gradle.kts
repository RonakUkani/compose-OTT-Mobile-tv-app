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
        google()
        mavenCentral()
    }
}

rootProject.name = "OTT Sample"
include(":app")
include(":data")
include(":domain")
include(":feature:home:main")
include(":feature:home:mobile")
include(":feature:home:tv")
include(":feature:search:mobile")
include(":feature:account:mobile")
include(":feature:splash:mobile")
include(":navigation")
include(":core:common")
include(":feature:videoplayer")
include(":feature:splash:tv")
include(":feature:search:tv")
include(":feature:account:tv")
include(":feature:videos:mobile")
