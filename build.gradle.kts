plugins {
    application
    val kotlinVersion = "1.5.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

application {
    val className = "top.harumill.getto.GettoMainKt"
    mainClass.set(className)
    mainClassName = className
}

group = "top.harumill"
version = "1.0"

repositories {
    maven { url = uri("https://mirrors.huaweicloud.com/repository/maven") }
    maven { url = uri("https://maven.aliyun.com/nexus/content/repositories/jcenter") }
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")

}

dependencies {
    val miraiVersion = "2.9.2"
    val ktorVersion = "1.6.7"
    val ktormVersion = "3.4.1"

    api("net.mamoe", "mirai-core-api", miraiVersion)     // 编译代码使用
    runtimeOnly("net.mamoe", "mirai-core", miraiVersion) // 运行时使用

    implementation(kotlin("stdlib"))

    implementation("org.xerial", "sqlite-jdbc", "3.8.11.2")

    // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
    implementation("org.apache.logging.log4j:log4j-core:2.17.1")

    // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    // https://mvnrepository.com/artifact/org.ktorm/ktorm-core
    implementation("org.ktorm:ktorm-core:$ktormVersion")

    // https://mvnrepository.com/artifact/io.ktor/ktor-client-core-jvm
    implementation("io.ktor:ktor-client-core:$ktorVersion")

    // https://mvnrepository.com/artifact/io.ktor/ktor-client-cio-jvm
    implementation("io.ktor:ktor-client-cio:$ktorVersion")

    // https://mvnrepository.com/artifact/io.ktor/ktor-client-json
    implementation("io.ktor:ktor-client-json:$ktorVersion")

    implementation("io.ktor:ktor-client-serialization:$ktorVersion")


}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions {
    freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    jvmTarget = "11"
}









