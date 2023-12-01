rootProject.name = "otusJava"

include("hw01-gradle")
include("hw02-generics")
include("hw03-annotation")
include("hw04-gc")
include("hw05-byteCodes")
include("hw07-structuralpatterns")
include("hw08-io")
include("hw09-jdbc:demo")
include("hw09-jdbc:homework")
include("hw10-jpql")

pluginManagement {
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings


    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
    }
}
