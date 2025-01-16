pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("http://maven.aliyun.com/nexus/content/groups/public/") {
            isAllowInsecureProtocol=true
        }
        maven {
            setUrl("https://maven.aliyun.com/repository/google/")
        }
        maven {
            setUrl("https://maven.aliyun.com/repository/public/")
        }
        maven {
            setUrl("https://maven.aliyun.com/repository/gradle-plugin/")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("http://maven.aliyun.com/nexus/content/groups/public/") {
            isAllowInsecureProtocol=true
        }
        maven {
            setUrl("https://maven.aliyun.com/repository/google/")
        }
        maven {
            setUrl("https://maven.aliyun.com/repository/public/")
        }
        maven {
            setUrl("https://maven.aliyun.com/repository/gradle-plugin/")
        }
    }
}

rootProject.name = "RoomDemo"
include(":app")
 