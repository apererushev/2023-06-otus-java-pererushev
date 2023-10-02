import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id ("java")
    id ("com.github.johnrengelman.shadow")
}

dependencies {
    implementation("org.jetbrains:annotations")
    implementation("org.projectlombok:lombok")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("hw06-solid")
        archiveVersion.set("0.1")
        archiveClassifier.set("")
        manifest {
            attributes(mapOf("Main-Class" to "ru.otus.apererushev.Main"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}