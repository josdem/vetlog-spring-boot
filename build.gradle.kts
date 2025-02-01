buildscript {
  repositories {
    gradlePluginPortal()
  }
  dependencies {
    classpath("org.springframework.boot:spring-boot-gradle-plugin:3.4.2")
    classpath("org.flywaydb:flyway-mysql:11.2.0")
  }
}

plugins {
  id("org.springframework.boot") version "3.4.1"
  id("io.spring.dependency-management") version "1.1.7"
  id("org.flywaydb.flyway") version "11.2.0"
  id("org.sonarqube") version "6.0.0.5145"
  id("jacoco")
  id("java")
  id("com.diffplug.spotless") version "7.0.2"
  id("org.jetbrains.kotlin.jvm") version "2.0.0"
}

val gcpVersion by extra("5.10.0")
val retrofitVersion by extra("2.11.0")
val mockitoCoreVersion by extra("5.15.2")

group = "com.josdem.vetlog"
version = "2.2.5"

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
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-aop")
  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("com.google.cloud:spring-cloud-gcp-starter-storage")
  implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
  implementation("com.squareup.retrofit2:converter-jackson:$retrofitVersion")
  implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.jetbrains:annotations:24.0.1")
  implementation(platform("com.google.cloud:spring-cloud-gcp-dependencies:$gcpVersion"))
  compileOnly("org.projectlombok:lombok")
  annotationProcessor("org.projectlombok:lombok")
  runtimeOnly("com.mysql:mysql-connector-j")
  implementation("net.minidev:json-smart:2.4.9")
  implementation("javax.xml.bind:jaxb-api:2.3.1")
  testImplementation("cglib:cglib-nodep:3.2.4")
  testCompileOnly("org.projectlombok:lombok")
  testAnnotationProcessor("org.projectlombok:lombok")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.security:spring-security-test")
  testImplementation("io.projectreactor:reactor-test")
  testImplementation("org.mockito:mockito-core:$mockitoCoreVersion")
  implementation("org.jetbrains.kotlin:kotlin-stdlib")

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
