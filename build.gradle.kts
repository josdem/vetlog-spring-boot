buildscript {
  repositories {
    gradlePluginPortal()
  }
  dependencies {
    classpath("org.springframework.boot:spring-boot-gradle-plugin:3.4.4")
    classpath("org.flywaydb:flyway-mysql:11.4.0")
  }
}

plugins {
  id("org.springframework.boot") version "3.4.4"
  id("io.spring.dependency-management") version "1.1.7"
  id("org.flywaydb.flyway") version "11.4.0"
  id("org.sonarqube") version "6.1.0.5360"
  id("jacoco")
  id("java")
  id("com.diffplug.spotless") version "7.0.2"
  id("org.jetbrains.kotlin.jvm") version "2.1.20"
}

val gcpVersion by extra("6.1.1")
val retrofitVersion by extra("2.11.0")
val mockitoCoreVersion by extra("5.16.1")
val annotationsVersion by extra("26.0.2")
val jsonSmartVersion by extra("2.5.2")
val jaxbVersion by extra("2.3.1")
val cglibVersion by extra("3.3.0")
val mockitoKotlinVersion by extra("5.4.0")

group = "com.josdem.vetlog"
version = "2.4.4"

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

repositories {
  mavenCentral()
}

flyway {
  url = "jdbc:mysql://localhost:3306/vetlog"
}

sonar {
  properties {
    property("sonar.projectKey", "josdem_vetlog-spring-boot")
    property("sonar.organization", "josdem-io")
    property("sonar.host.url", "https://sonarcloud.io")
  }
}

spotless {
  java {
    target("**/*.java")
    targetExclude("**/build/**", "**/build-*/**")
    toggleOffOn()
    palantirJavaFormat()
    removeUnusedImports()
    trimTrailingWhitespace()
    endWithNewline()
  }
  kotlin {
    target("**/*.kt")
    targetExclude("**/build/**", "**/build-*/**")
    ktlint()
    trimTrailingWhitespace()
    endWithNewline()
  }
}

dependencies {
  // Spring Boot Core
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-aop")
  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")

  // External services and API integration
  implementation(platform("com.google.cloud:spring-cloud-gcp-dependencies:$gcpVersion"))
  implementation("com.google.cloud:spring-cloud-gcp-starter-storage")
  implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
  implementation("com.squareup.retrofit2:converter-jackson:$retrofitVersion")

  // Utility libraries (JSON, XML, Annotations)
  implementation("net.minidev:json-smart:$jsonSmartVersion")
  implementation("javax.xml.bind:jaxb-api:$jaxbVersion")
  implementation("org.jetbrains:annotations:$annotationsVersion")
  implementation("org.jetbrains.kotlin:kotlin-stdlib")

  // Database and ORM
  runtimeOnly("com.mysql:mysql-connector-j")

  // Compile-time dependencies
  compileOnly("org.projectlombok:lombok")
  annotationProcessor("org.projectlombok:lombok")

  // Test dependencies
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.security:spring-security-test")
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("org.mockito:mockito-core:$mockitoCoreVersion")
  testImplementation("cglib:cglib-nodep:$cglibVersion")
  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation(kotlin("test"))
  testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")
  testAnnotationProcessor("org.projectlombok:lombok")
  testCompileOnly("org.projectlombok:lombok")
}


jacoco {
  toolVersion = "0.8.11"
}

tasks.jacocoTestReport {
  reports {
    xml.required.set(true)
    html.required.set(true)
  }
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

kotlin {
  jvmToolchain(21)
}

tasks.withType<Test> {
  useJUnitPlatform()
  systemProperties.putAll(System.getProperties().map { it.key.toString() to it.value.toString() })
  dependsOn("spotlessApply")
}

tasks.withType<JavaExec> {
  systemProperties.putAll(System.getProperties().map { it.key.toString() to it.value.toString() })
  dependsOn("flywayMigrate")
}

springBoot {
  buildInfo()
}
