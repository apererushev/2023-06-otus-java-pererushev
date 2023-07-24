import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id ("com.github.johnrengelman.shadow")
}

dependencies {
    implementation("org.jetbrains:annotations")
    testImplementation ("org.junit.jupiter:junit-jupiter-api")
    testImplementation ("org.junit.jupiter:junit-jupiter-engine")
    testImplementation ("org.assertj:assertj-core")
    testImplementation ("org.mockito:mockito-core")
    testImplementation ("org.mockito:mockito-junit-jupiter")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("hw02-generics")
        archiveVersion.set("0.1")
        archiveClassifier.set("")
    }

    build {
        dependsOn(shadowJar)
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }


    test {
        systemProperty("file.encoding", "UTF-8")
        useJUnitPlatform()
    }
}



