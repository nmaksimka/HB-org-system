plugins {
    base
    id("org.springframework.boot") version "3.4.5" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

allprojects {
    group = "com.example.birthday"
    version = "0.1.0-SNAPSHOT"
}

subprojects {
    pluginManager.withPlugin("java") {
        dependencies {
            add("compileOnly", "org.projectlombok:lombok:1.18.38")
            add("annotationProcessor", "org.projectlombok:lombok:1.18.38")
            add("implementation", "org.mapstruct:mapstruct:1.6.3")
            add("annotationProcessor", "org.mapstruct:mapstruct-processor:1.6.3")
            add("annotationProcessor", "org.projectlombok:lombok-mapstruct-binding:0.2.0")
            add("testCompileOnly", "org.projectlombok:lombok:1.18.38")
            add("testAnnotationProcessor", "org.projectlombok:lombok:1.18.38")
        }
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}
