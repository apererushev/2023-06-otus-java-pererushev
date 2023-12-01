import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
    idea
    id("io.spring.dependency-management")
    id("org.springframework.boot") apply false
}

idea {
    project {
        languageLevel = IdeaLanguageLevel(17)
    }
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}


allprojects {
    group = "ru.otus.apererushev"

    repositories {
        mavenLocal()
        mavenCentral()
    }

    val guava: String by project
    val orgJetbrainsAnnotation: String by project
    val testing: String by project
    val apacheCommonsLang3: String by project
    val protobuf: String by project
    val jakarta: String by project
    val projectlombok: String by project
    val logback: String by project
    val testcontainersBom: String by project
    val slf4j: String by project

    apply(plugin = "io.spring.dependency-management")
    dependencyManagement {
        dependencies {
            imports {
                mavenBom(BOM_COORDINATES)
                mavenBom("org.testcontainers:testcontainers-bom:$testcontainersBom")
            }
            dependency("com.google.guava:guava:$guava")
            dependency("org.jetbrains:annotations:$orgJetbrainsAnnotation")
            dependency("org.testng:testng:$testing")
            dependency("org.apache.commons:commons-lang3:$apacheCommonsLang3")
            dependency("com.google.protobuf:protobuf-java-util:$protobuf")
            dependency("org.glassfish:jakarta.json:$jakarta")
            dependency("org.projectlombok:lombok:$projectlombok")
            //dependency("ch.qos.logback:logback-classic:$logback")
            dependency("org.slf4j:slf4j-api:$slf4j")
            //testImplementation("org.testcontainers:postgresql:1.19.1")

        }
    }
}


subprojects {
    plugins.apply(JavaPlugin::class.java)
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:all,-serial,-processing", "-Werror"))
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
        reports {
            junitXml.required.set(true)
            html.required.set(true)
        }
    }
}