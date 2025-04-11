import java.io.FileInputStream
import java.util.Properties

val keystoreProperties = mutableListOf<Properties>()
enum class BuildVariants(val arg: String) {
    AOT("aot"), //使用AOT编译
    NATIVE("native"), //使用原生编译
    TEST("test"), //使用虚拟机数据库本地测试
    NATIVE_TEST("native_test"), //使用虚拟机数据库本地原生测试
}
val buildVariant = properties["buildVariant"] ?: BuildVariants.AOT.arg

prepareConfig()
loadConfig()

val libs = extensions.getByName("libs") as org.gradle.accessors.dm.LibrariesForLibs

val KOTLIN_VERSION = libs.versions.kotlin.get()
val SPRING_BOOT_VERSION = libs.versions.springBoot.get()
val MYBATIS_PLUS_VERSION = libs.versions.mybatisPlus.get()

plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    kotlin("plugin.spring") version libs.versions.kotlin.get()
    war
    id("org.springframework.boot") version libs.versions.springBoot.get()
    id("io.spring.dependency-management") version "1.1.7"
    id("org.graalvm.buildtools.native") version "0.10.6"
}

group = "cc.nyanyanya"
//version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-lang3:3.17.0")
    implementation("commons-io:commons-io:2.18.0")

    implementation("jakarta.mail:jakarta.mail-api:2.1.3")

    implementation("org.springframework.boot", "spring-boot-starter-jdbc", SPRING_BOOT_VERSION)
    implementation("org.springframework.boot", "spring-boot-starter-web", SPRING_BOOT_VERSION)
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$KOTLIN_VERSION")
//    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.4")
    implementation("org.springframework.boot", "spring-boot-starter-actuator", SPRING_BOOT_VERSION)
    developmentOnly("org.springframework.boot", "spring-boot-devtools", SPRING_BOOT_VERSION)
    runtimeOnly("org.postgresql:postgresql:42.7.5")
    providedRuntime("org.springframework.boot", "spring-boot-starter-tomcat", SPRING_BOOT_VERSION)
    implementation("org.springframework.boot", "spring-boot-starter-mail", SPRING_BOOT_VERSION)
    implementation("org.springframework.boot", "spring-boot-starter-webflux", SPRING_BOOT_VERSION)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$KOTLIN_VERSION")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.4")
//    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$KOTLIN_VERSION")
    if (buildVariant != BuildVariants.NATIVE.arg || buildVariant != BuildVariants.NATIVE_TEST.arg) {
        testImplementation("org.springframework.boot", "spring-boot-starter-test", SPRING_BOOT_VERSION)
//        testImplementation("org.mybatis.spring.boot", "mybatis-spring-boot-starter-test:3.0.4", SPRING_BOOT_VERSION)
    }

    implementation("com.baomidou", "mybatis-plus-jsqlparser", MYBATIS_PLUS_VERSION)
    implementation("com.baomidou", "mybatis-plus", MYBATIS_PLUS_VERSION)
    implementation("com.baomidou", "mybatis-plus-spring-boot3-starter", MYBATIS_PLUS_VERSION)
    implementation("com.github.yulichang:mybatis-plus-join-boot-starter:1.5.3")

    implementation("com.fasterxml.uuid:java-uuid-generator:5.1.0")
    implementation("com.squareup.okio:okio:3.10.2")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

if (buildVariant != BuildVariants.NATIVE.arg || buildVariant != BuildVariants.NATIVE_TEST.arg) {
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}



fun prepareConfig() {
    var databaseKeystorePropertiesFileName = "database-keystore.properties"
    var emailKeystorePropertiesFileName = "email-keystore.properties"

    when (buildVariant) {
        BuildVariants.AOT.arg, BuildVariants.NATIVE.arg -> {
            databaseKeystorePropertiesFileName = "database-keystore.properties"
        }
        BuildVariants.TEST.arg, BuildVariants.NATIVE_TEST.arg -> {
            databaseKeystorePropertiesFileName = "test-database-keystore.properties"
        }
    }

    val databaseKeystorePropertiesFile = rootProject.file(databaseKeystorePropertiesFileName)
    val databaseKeystoreProperties = Properties()
    keystoreProperties.add(databaseKeystoreProperties)
    val emailKeystorePropertiesFile = rootProject.file(emailKeystorePropertiesFileName)
    val emailKeystoreProperties = Properties()
    keystoreProperties.add(emailKeystoreProperties)

    try {
        FileInputStream(databaseKeystorePropertiesFile).use { inputStream ->
            databaseKeystoreProperties.load(inputStream)
        }
    } catch (ignored: Exception) {
    }
    try {
        FileInputStream(emailKeystorePropertiesFile).use { inputStream ->
            emailKeystoreProperties.load(inputStream)
        }
    } catch (ignored: Exception) {
    }
}

fun loadConfig() {
    tasks.withType<ProcessResources> {
        with(
            copySpec {
                duplicatesStrategy = DuplicatesStrategy.INCLUDE

                from("src/main/resources")
                include("**/application.yml")
                keystoreProperties.forEach { props ->
                    props.forEach { prop ->
                        if (prop.value != null) {
                            filter { it.replace("@${prop.key}@", prop.value.toString()) }
                            filter { it.replace("@project.${prop.key}@", prop.value.toString()) }
                        }
                    }
                }
            }
        )
    }
}