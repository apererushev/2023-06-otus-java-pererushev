import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id ("java")
    id ("com.github.johnrengelman.shadow")
}

dependencies {
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("hw04-gc")
        archiveVersion.set("0.1")
        archiveClassifier.set("")
        manifest {
            attributes(mapOf("Main-Class" to "ru.calculator.CalcDemo"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}