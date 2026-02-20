plugins {
    kotlin("jvm") version "2.2.20"
    `java-library`
    `maven-publish`
    jacoco
}
jacoco {
    toolVersion = "0.8.12"
}

group = "io.github.dovalerio.thewire"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {

    // Coroutines (necessário para pipeline suspend)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.slf4j:slf4j-api:2.0.13")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.12.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.12.1")
    testImplementation("io.mockk:mockk:1.13.11")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }

    withSourcesJar()
    withJavadocJar()
}

kotlin {
    explicitApi()

    compilerOptions {
        allWarningsAsErrors.set(true)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                name.set("The Wire HTTP")
                description.set("A lightweight middleware-driven HTTP client for Kotlin.")
                url.set("https://github.com/your-user/the-wire-http")

                licenses {
                    license {
                        name.set("Apache License 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
            }
        }
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }

    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "**/dsl/**" // opcional: excluir DSL se quiser
                )
            }
        })
    )
}
tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.85".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}