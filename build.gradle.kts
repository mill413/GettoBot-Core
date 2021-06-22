plugins {
    application
    val kotlinVersion = "1.4.32"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

application {
    val className = "top.harumill.getto.StartGettoKt"
    mainClass.set(className)
    mainClassName = className
}

group = "top.harumill"
version = "1.0"

repositories {
    maven { url =uri("https://mirrors.huaweicloud.com/repository/maven") }
    maven { url =uri("https://maven.aliyun.com/nexus/content/repositories/jcenter")}
    maven { url =uri("https://dl.bintray.com/kotlin/kotlin-eap")}
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")

}

dependencies {
    val miraiVersion = "2.6.5"
    implementation(kotlin("stdlib"))
    testImplementation("junit", "junit", "4.12")
    api("net.mamoe", "mirai-core", miraiVersion)
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.32")
    implementation("org.xerial", "sqlite-jdbc", "3.8.11.2")
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}









