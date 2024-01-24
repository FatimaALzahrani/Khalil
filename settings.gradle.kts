pluginManagement {
    repositories {
        google()
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

rootProject.name = "khalil"
include(":app")
//project(":sceneform").projectDir=new File("sceneformsrc/sceneform")
//
//include (":sceneformux")
//project(":sceneformux").projectDir=new File("sceneformux/ux")