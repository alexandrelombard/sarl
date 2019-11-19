import org.gradle.api.JavaVersion.VERSION_1_8

group = "io.sarl"
version = "0.11.0-SNAPSHOT"

plugins {
    idea
    java
    kotlin("jvm") version "1.3.20"
    id("org.jetbrains.intellij") version "0.4.12"
}

idea {
    module {

    }
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }

    idea {
        module {
            generatedSourceDirs.add(file("src/main/gen"))
        }
    }

    intellij {
        updateSinceUntilBuild = false
        instrumentCode = true
        version = "2019.2.3"
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = VERSION_1_8
        targetCompatibility = VERSION_1_8
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/gen")
    }
}

project(":") {
    intellij {
        pluginName = "intellij-sarl"
    }

    sourceSets["main"].java.srcDirs.add(file("src/main/gen"))
}

dependencies {
    //implementation("org.antlr:antlr:3.5.2")
    implementation("org.eclipse.xtext:org.eclipse.xtext:2.19.0")
    implementation("org.eclipse.xtext:org.eclipse.xtext.util:2.19.0")
    implementation("org.eclipse.xtext:org.eclipse.xtext.xbase.lib:2.19.0")
    implementation("org.eclipse.xtend:org.eclipse.xtend.core:2.19.0")
    implementation("org.eclipse.xtend:org.eclipse.xtend.lib:2.19.0")
    implementation ("io.sarl.lang:io.sarl.lang:0.11.0-SNAPSHOT")
    implementation ("io.sarl.lang:io.sarl.lang.core:0.11.0-SNAPSHOT")
}