pluginManagement {
    repositories {
        google()  // This ensures that Google plugins are available
        mavenCentral()  // Includes Maven Central for resolving dependencies
        gradlePluginPortal()  // Includes Gradle Plugin Portal
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)  // Ensures that repositories declared at the project level are prioritized
    repositories {
        google()  // Add Google's repository to resolve dependencies
        mavenCentral()  // Include Maven Central for resolving dependencies
    }
}

rootProject.name = "Two Step Verification"  // Define the project name
include(":app")  // Include the app module in the build
