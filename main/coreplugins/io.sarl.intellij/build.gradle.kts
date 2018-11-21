import org.jetbrains.grammarkit.tasks.GenerateLexer
import org.gradle.api.tasks.SourceSet
import org.gradle.api.JavaVersion.VERSION_1_8
import org.jetbrains.grammarkit.tasks.GenerateParser
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "io.sarl"
version = "0.9.0-SNAPSHOT"

plugins {
    idea
    java
    kotlin("jvm") version "1.3.10"
    id("org.jetbrains.intellij") version "0.3.12"
    id("org.jetbrains.grammarkit") version "2018.2.1"
}

idea {
    module {

    }
}

allprojects {
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

    sourceSets {
        getByName("main").java.srcDirs("src/main/gen")
    }
}

project(":") {

    intellij {
        pluginName = "intellij-sarl"
    }

    sourceSets["main"].java.srcDirs.add(file("src/main/gen"))

    val generateSarlLexer = task<GenerateLexer>("generateSarlLexer") {
        source = "src/main/grammars/SarlLexer.flex"
        targetDir = "src/main/java/io/sarl/intellij/parser"
        targetClass = "_SarlLexer"
        purgeOldFiles = true
    }

    val generateSarlParser = task<GenerateParser>("generateSarlParser") {
        source = "src/main/grammars/SarlParser.bnf"
        targetRoot = "src/main/java"
        pathToParser = "/io/sarl/intellij/parser/SarlParser.java"
        pathToPsiRoot = "/io/sarl/intellij/psi"
        purgeOldFiles = true
    }

    tasks.withType<KotlinCompile> {
        dependsOn(generateSarlLexer, generateSarlParser)
    }
}