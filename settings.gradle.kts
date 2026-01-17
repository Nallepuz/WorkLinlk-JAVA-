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
/*dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                username = "mapbox"
                password = "sk.eyJ1IjoibmFsbG9VwdXoiLCJhIjoia21raTZkeGFtMHA1ODNkcGZpZnkxeHh4ciJ9.QEC6N34MjY9gE6L_q-yPGw"
            }
        }
    }
}*/
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                username = "mapbox"
                password = "sk.eyJ1IjoibmFsbGVwdXoiLCJhIjoiY21raTZkeGFtMHA1ODNkcGZpZnkxeHh4ciJ9.QEC6N34MjY9gE6L_q-yPGw"
            }
        }
    }
}

rootProject.name = "WorkLink"
include(":app")
 