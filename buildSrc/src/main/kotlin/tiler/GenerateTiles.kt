package tiler

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.IIOImage
import javax.imageio.stream.ImageOutputStream
import java.awt.image.BufferedImage
import java.awt.RenderingHints
import kotlin.math.min

abstract class GenerateTiles : DefaultTask() {
    @TaskAction
    fun run() {
        val srcPath = "C:/git/p2e_map/composeApp/src/wasmJsMain/composeResources/drawable/oklyon.png"
        val outPath = "C:/git/p2e_map/composeApp/src/wasmJsMain/composeResources/drawable/tiles"
        val tile = 1080
        val levels = 1
        val format = "png"
        val quality = 1f

        File(outPath).deleteRecursively()

        val srcFile = File(srcPath)
        require(srcFile.exists()) { "Nie znaleziono pliku: $srcPath" }

        val original = ImageIO.read(srcFile) ?: error("Nie mogę wczytać obrazu: $srcPath")
        val baseW = original.width
        val baseH = original.height
        println("Źródło: ${srcFile.name} (${baseW}x${baseH}), tile=$tile, levels=$levels, format=$format, quality=$quality")
        println("Katalog wyjściowy: $outPath")

        fun scaleImage(src: BufferedImage, w: Int, h: Int, asPng: Boolean): BufferedImage {
            val type = if (asPng) BufferedImage.TYPE_INT_ARGB else BufferedImage.TYPE_INT_RGB
            val dst = BufferedImage(w, h, type)
            val g = dst.createGraphics()
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            if (!asPng) {
                g.color = java.awt.Color.WHITE
                g.fillRect(0, 0, w, h)
            }
            g.drawImage(src, 0, 0, w, h, null)
            g.dispose()
            return dst
        }

        fun writeImage(img: BufferedImage, file: File, fmt: String, q: Float) {
            file.parentFile.mkdirs()
            if (fmt == "jpg" || fmt == "jpeg") {
                val writers = ImageIO.getImageWritersByFormatName("jpg")
                val writer = writers.next()
                val ios: ImageOutputStream = ImageIO.createImageOutputStream(file)
                writer.output = ios
                val param = writer.defaultWriteParam
                if (param.canWriteCompressed()) {
                    param.compressionMode = ImageWriteParam.MODE_EXPLICIT
                    param.compressionQuality = q.coerceIn(0f, 1f)
                }
                val rgb = if (img.type == BufferedImage.TYPE_INT_RGB) img else {
                    val tmp = BufferedImage(img.width, img.height, BufferedImage.TYPE_INT_RGB)
                    val g = tmp.createGraphics()
                    g.color = java.awt.Color.WHITE
                    g.fillRect(0, 0, img.width, img.height)
                    g.drawImage(img, 0, 0, null)
                    g.dispose()
                    tmp
                }
                writer.write(null, IIOImage(rgb, null, null), param)
                ios.close(); writer.dispose()
            } else {
                ImageIO.write(img, "png", file)
            }
        }

        fun ceilDiv(a: Int, b: Int) = (a + b - 1) / b

        val asPng = format == "png"

        for (z in 0 until levels) {
            val scaleDen = 1 shl z
            val levelW = ceilDiv(baseW, scaleDen)
            val levelH = ceilDiv(baseH, scaleDen)
            println("Poziom $z -> ${levelW}x${levelH}")

            val levelImg = if (z == 0) original else scaleImage(original, levelW, levelH, asPng)

            val rows = ceilDiv(levelH, tile)
            val cols = ceilDiv(levelW, tile)
            for (row in 0 until rows) {
                val y0 = row * tile
                val th = min(tile, levelH - y0)
                for (col in 0 until cols) {
                    val x0 = col * tile
                    val tw = min(tile, levelW - x0)

                    // ✅ ZAWSZE płótno tile × tile (np. 256×256)
                    val tileImg = BufferedImage(
                        tile, tile,
                        if (asPng) BufferedImage.TYPE_INT_ARGB else BufferedImage.TYPE_INT_RGB
                    )
                    val g = tileImg.createGraphics()

                    // ✅ wypełnij tło (JPG: białe, PNG: przezroczyste)
                    if (!asPng) {
                        g.color = java.awt.Color.WHITE
                        g.fillRect(0, 0, tile, tile)
                    } else {
                        // przezroczyste – nic nie malujemy; ARGB ma alfa = 0
                    }

                    // ✅ narysuj wycinek w lewym-górnym rogu, NIE skalując
                    //    (pozostała część kafla zostaje wypełniona tłem)
                    g.drawImage(
                        levelImg,
                        0, 0, tw, th,              // doc: docelowy rect na kaflu
                        x0, y0, x0 + tw, y0 + th,  // src: wycinek ze źródła
                        null
                    )
                    g.dispose()

                    val outFile = File("$outPath/$z/$row/$col.$format")
                    writeImage(tileImg, outFile, format, quality)
                }
            }
        }

        println("Gotowe ✅  Wpisz w MapState: fullWidth=$baseW, fullHeight=$baseH")
    }
}
