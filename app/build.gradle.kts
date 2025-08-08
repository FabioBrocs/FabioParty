plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

val osName = System.getProperty("os.name")
val platform = when {
    osName.startsWith("Windows") -> "win"
    osName.startsWith("Mac") -> "mac"
    else -> "linux"
}

dependencies {
    implementation("org.openjfx:javafx-base:21:$platform")
    implementation("org.openjfx:javafx-controls:21:$platform")
    implementation("org.openjfx:javafx-graphics:21:$platform")
    implementation("org.openjfx:javafx-fxml:21:$platform")
}

tasks.shadowJar {
    archiveBaseName.set("FabioParty")
    archiveVersion.set("")
    archiveClassifier.set("") // Removes "-all"
}

application {
    mainClass.set("personal.projectparty.app.MainCLI")
}
