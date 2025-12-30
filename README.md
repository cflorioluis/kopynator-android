# Kopynator Android SDK

Official Android SDK for Kopynator.

## Installation

### 1. Add JitPack to your `settings.gradle.kts`
```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### 2. Add dependency
```kotlin
dependencies {
    implementation("com.github.cflorioluis:kopynator-android:1.0.0")
}
```

## Usage

### 🚀 Initialize
Initialize the SDK once, typically in your `Application` or `MainActivity`.

```kotlin
Kopynator.initialize(
    apiKey = "YOUR_API_KEY",
    locale = "en"
)

// Fetch latest translations from cloud
Kopynator.getInstance().fetchTranslations { success ->
    if (success) {
        // Ready to use
    }
}
```

### 💬 Translate
```kotlin
val title = Kopynator.getInstance().t("home.title", "Default Title")
```

## Publishing to JitPack
1. Push this package to a public GitHub repository.
2. Create a GitHub Release with a version tag (e.g., `1.0.0`).
3. Add the repo to [JitPack](https://jitpack.io).
