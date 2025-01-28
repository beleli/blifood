import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.4.2"
    id("org.sonarqube") version "4.4.0.3356"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    kotlin("plugin.jpa") version "1.8.22"
}

group = "br.com"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

val micrometerTracingVersion = "1.2.3"
val micrometerRegistryOtlpVersion = "1.13.4"
val openapiVersion = "2.2.0"
val jsonwebtokenVersion = "0.11.2"
val awsJavaSdkVersion = "1.12.664"
val secretsManagerVersion = "2.21.29"
val postgresqlVersion = "42.7.2"
val flywayCoreVersion = "9.0.0"
val kotestRunnerJUnit5Version = "5.8.0"
val mockkVersion = "1.13.9"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("io.micrometer:micrometer-tracing-bridge-brave:$micrometerTracingVersion")
    implementation("io.micrometer:micrometer-registry-otlp:$micrometerRegistryOtlpVersion")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$openapiVersion")

    implementation("io.jsonwebtoken:jjwt-api:$jsonwebtokenVersion")
    implementation("io.jsonwebtoken:jjwt-impl:$jsonwebtokenVersion")
    implementation("io.jsonwebtoken:jjwt-jackson:$jsonwebtokenVersion")

    implementation("com.amazonaws:aws-java-sdk-s3:$awsJavaSdkVersion")
    implementation("software.amazon.awssdk:secretsmanager:$secretsManagerVersion")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("org.flywaydb:flyway-core:$flywayCoreVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestRunnerJUnit5Version")
    testImplementation("io.kotest:kotest-assertions-core:$kotestRunnerJUnit5Version")
    testImplementation("io.kotest:kotest-property:$kotestRunnerJUnit5Version")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("com.h2database:h2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
    dependsOn("ktlintFormat")
}

tasks.withType<Test> {
    systemProperties(Pair("spring.profiles.active", "test"))
    environment("BLIFOOD_SECRETS_MANAGER_IMPL", "localstack")
    useJUnitPlatform()
}

tasks.withType<BootRun> {
    systemProperties(Pair("spring.profiles.active", "local"))
}

configure<KtlintExtension> {
    ignoreFailures.set(true)
    filter {
        include("**/kotlin/**")
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", "blifood-api")
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.token", "sqp_f60225cd023d93ebe360dccc87f93b1097c90355")
    }
}
