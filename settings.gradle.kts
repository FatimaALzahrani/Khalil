pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        jcenter()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}

rootProject.name = "khalil"
include(":app")
//project(":sceneform").projectDir=new File("sceneformsrc/sceneform")
//
//include (":sceneformux")
//project(":sceneformux").projectDir=new File("sceneformux/ux")