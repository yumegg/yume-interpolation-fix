plugins {
    java
    id("net.fabricmc.fabric-loom")
}

repositories {
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:${providers.gradleProperty("minecraft_version").get()}")
    implementation("net.fabricmc:fabric-loader:${providers.gradleProperty("loader_version").get()}")
    implementation(platform("net.fabricmc.fabric-api:fabric-api-bom:${providers.gradleProperty("fabric_api_version").get()}"))
    include(platform("net.fabricmc.fabric-api:fabric-api-bom:${providers.gradleProperty("fabric_api_version").get()}"))
    implementation("net.fabricmc.fabric-api:fabric-api-base")
    implementation("net.fabricmc.fabric-api:fabric-resource-loader-v1")
    include("net.fabricmc.fabric-api:fabric-api-base") {
        isTransitive = false
    }
    include("net.fabricmc.fabric-api:fabric-resource-loader-v1") {
        isTransitive = false
    }
    compileOnly("org.jetbrains:annotations:26.1.0")

    testImplementation(platform("org.junit:junit-bom:6.1.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(platform("org.mockito:mockito-bom:5.24.0"))
    testImplementation("org.mockito:mockito-core")
}

tasks.test {
    useJUnitPlatform()
}

tasks.processResources {
    val version = version
    inputs.property("version", version)
    filesMatching("fabric.mod.json") {
        expand("version" to version)
    }
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25

    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

tasks.jar {
    val projectName = project.name
    inputs.property("projectName", projectName)
    from("LICENSE") {
        rename { "${it}_$projectName" }
    }
}
