# 问题

## 1、jpackageImage 打包失败

~~~java
org.gradle.api.tasks.TaskExecutionException: Execution failed for task ':jpackageImage'.
    Caused by: org.gradle.process.internal.ExecException: Process 'command 'D:\DevlopEnv\JDK\jdk-21.0.4/bin/jpackage.exe'' finished with non-zero exit value 1
        at org.beryx.jlink.impl.JPackageImageTaskImpl.execute(JPackageImageTaskImpl.groovy:56)
        at org.beryx.jlink.JPackageImageTask.jpackageTaskAction(JPackageImageTask.groovy:87)
~~~

主要原因是 Gradle 配置文件中的 version 版本号打包到的平台有冲突，需要删掉 SNAPSHOT 



## 2、配置文件实例

~~~java
plugins {
    id "application"
    id "java"
    id "org.beryx.jlink" version "2.26.0"
    id "org.openjfx.javafxplugin" version "0.1.0"
    id "com.gluonhq.gluonfx-gradle-plugin" version "1.0.22"
}

group "com.icuxika"
version "1.0.0"

repositories {
    mavenCentral()
}

application {
    applicationName = "JavaFXSample"
    mainModule.set("sample")
    mainClass.set("com.icuxika.MainApp")
    applicationDefaultJvmArgs = [
            // ZGC
            "-XX:+UseZGC",
            // 当遇到空指针异常时显示更详细的信息
            "-XX:+ShowCodeDetailsInExceptionMessages",
            "-Dsun.java2d.opengl=true",
            // 不添加此参数，打包成exe后，https协议的网络图片资源无法加载
            "-Dhttps.protocols=TLSv1.1,TLSv1.2"
    ]
}

javafx {
    version = "21"
    modules = ["javafx.controls", "javafx.graphics", "javafx.fxml", "javafx.swing", "javafx.media", "javafx.web"]
}

dependencies {
    implementation("io.github.palexdev:materialfx:11.16.1")
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("ch.qos.logback:logback-classic:1.5.3")
}

jlink {
    options.set(List.of("--strip-debug", "--compress", "zip-9", "--no-header-files", "--no-man-pages"))

    launcher {
        name = application.applicationName
        imageName.set(application.applicationName)
    }

    imageZip.set(project.file("${project.getLayout().getBuildDirectory().get()}/image-zip/JavaFXSample.zip"))

    jpackage {
        outputDir = "build-package"
        imageName = application.applicationName
        skipInstaller = false
        installerName = application.applicationName
        appVersion = version.toString()

        if (org.gradle.internal.os.OperatingSystem.current().windows) {
            icon = "src/main/resources/application.ico"
            installerOptions += ["--win-dir-chooser", "--win-menu", "--win-shortcut", "--win-menu-group", application.applicationName]
        }
        if (org.gradle.internal.os.OperatingSystem.current().macOsX) {
            icon = "src/main/resources/application.icns"
        }
        if (org.gradle.internal.os.OperatingSystem.current().linux) {
            icon = "src/main/resources/application.png"
            installerType = "deb"
            installerOptions += ["--linux-deb-maintainer", "icuxika@outlook.com", "--linux-menu-group", application.applicationName, "--linux-shortcut"]
        }
    }
}

tasks.withType(JavaCompile).configureEach {
    options.encoding = "UTF-8"
}

// https://github.com/gluonhq/substrate/issues/1232
// 修改 icon 不要改变文件名称 src\windows\assets\icon.ico
mainClassName = "com.icuxika.MainApp"
gluonfx {
    bundlesList = [
            "LanguageResource",
            "LanguageResource_en",
            "LanguageResource_zh_CN"
    ]
    if (org.gradle.internal.os.OperatingSystem.current().macOsX) {
        compilerArgs = ["-Dsvm.platform=org.graalvm.nativeimage.Platform\$MACOS_AMD64"]
    }
}

task printDependentJarsList {
    doLast {
        println("------------------------------------------------------------")
        println(configurations.getByName("runtimeClasspath").asPath)
        println("------------------------------------------------------------")
    }
}

// 拷贝项目依赖的 jar 包到指定目录
task copyDependencies() {
    doLast {
        copy {
            from(configurations.getByName("runtimeClasspath"))
            into("${project.getLayout().getBuildDirectory().get()}/modules")
        }
    }
}
~~~

