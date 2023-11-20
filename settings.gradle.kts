pluginManagement {
    repositories {
        gradlePluginPortal()
        maven (url ="https://maven.aliyun.com/repository/google")
        maven ( url ="https://maven.aliyun.com/repository/public")
        maven ( url ="https://maven.aliyun.com/repository/jcenter")
        maven ( url ="https://maven.aliyun.com/nexus/content/groups/public")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven (url ="https://maven.aliyun.com/repository/google")
        maven ( url ="https://maven.aliyun.com/repository/public")
        maven ( url ="https://maven.aliyun.com/repository/jcenter")
        maven ( url ="https://maven.aliyun.com/nexus/content/groups/public")
        google()
        mavenCentral()
    }
}

rootProject.name = "My Application"
include(":app")
