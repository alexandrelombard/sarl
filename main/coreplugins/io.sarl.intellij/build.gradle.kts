import org.jetbrains.grammarkit.tasks.GenerateLexer
import org.gradle.api.tasks.SourceSet
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.gradle.api.JavaVersion.VERSION_1_8
import org.jetbrains.grammarkit.tasks.GenerateParser
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "io.sarl"
version = "0.9.0-SNAPSHOT"

buildscript {
    val kotlinVersion = "1.2.50"

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", kotlinVersion))
    }
}

plugins {
    idea
    kotlin("jvm") version "1.2.50"
    id("org.jetbrains.intellij") version "0.3.12"
    id("org.jetbrains.grammarkit") version "2018.2.1"
}

idea {
    module {

    }
}

allprojects {
    apply {
        plugin("idea")
        plugin("kotlin")
        plugin("org.jetbrains.grammarkit")
        plugin("org.jetbrains.intellij")
    }

    repositories {
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
        version = "2018.2.4"
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = VERSION_1_8
        targetCompatibility = VERSION_1_8
    }

    java.sourceSets["main"].java {
        srcDir("src/main/gen")
    }
    //the<SourceSetContainer>()["main"].java.srcDirs("src/main/gen")
}



project(":") {
    val generateSarlLexer = task<GenerateLexer>("generateSarlLexer") {
        source = "src/main/grammars/SarlLexer.flex"
        targetDir = "src/main/gen/io/sarl/intellij/lexer"
        targetClass = "_SarlLexer"
        purgeOldFiles = true
    }

    val generateSarlParser = task<GenerateParser>("generateSarlParser") {
        source = "src/main/grammars/SarlParser.bnf"
        targetRoot = "src/main/gen"
        pathToParser = "/io/sarl/intellij/parser/SarlParser.java"
        pathToPsiRoot = "/io/sarl/intellij/psi"
        purgeOldFiles = true
    }

    tasks.withType<KotlinCompile> {
        dependsOn(
                generateSarlLexer,
                generateSarlParser
        )
    }
}