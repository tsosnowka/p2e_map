package tiler

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.CopyOption
import java.nio.file.SimpleFileVisitor
import java.nio.file.StandardCopyOption
import java.nio.file.attribute.BasicFileAttributes

abstract class MoveDistribution : DefaultTask() {
    @get:Internal
    val definedRootDir: File = project.rootProject.projectDir

    @TaskAction
    fun run() {
        moveDistribution(definedRootDir)
    }

    fun moveDistribution(rootFile: File) {
        val targetDirFile = File(rootFile,"docs")
        if (targetDirFile.exists()) targetDirFile.deleteRecursively()
        else targetDirFile.mkdir()
        val distDirFile = File(rootFile,"composeApp/build/dist/wasmJs/productionExecutable")
        if (distDirFile.exists().not()) return
        copyFolder(distDirFile.toPath(), targetDirFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        val process = ProcessBuilder("git", "add", ".")
            .directory(rootFile)
            .inheritIO()
            .start()

        val exitCode = process.waitFor()
        if (exitCode != 0) error("ERROR: git add exit code: $exitCode)")
        else println("SUCCESS: Distribution files added")
    }

    @Suppress("WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
    @Throws(IOException::class)
    private fun copyFolder(source: Path, target: Path, vararg options: CopyOption?) {
        Files.walkFileTree(source, object : SimpleFileVisitor<Path?>() {
            @Throws(IOException::class)
            override fun preVisitDirectory(dir: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                Files.createDirectories(target.resolve(source.relativize(dir!!).toString()))
                return FileVisitResult.CONTINUE
            }

            @Throws(IOException::class)
            override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                Files.copy(file!!, target.resolve(source.relativize(file).toString()), *options)
                return FileVisitResult.CONTINUE
            }
        })
    }
}