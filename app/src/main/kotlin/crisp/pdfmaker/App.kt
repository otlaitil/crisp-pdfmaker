package crisp.pdfmaker

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val builder = PdfRendererBuilder()
    builder.useFastMode()
    builder.withUri(template("test"))

    Files.newOutputStream(Paths.get("/tmp/file.pdf")).use { out ->
        builder.toStream(out)
        builder.run()
    }
}

private fun template(name: String): String {
    val uri = object {}.javaClass
        .classLoader
        .getResource("templates/${name}.htm")

    return uri.toString()
}
