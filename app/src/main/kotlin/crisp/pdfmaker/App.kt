package crisp.pdfmaker

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val pdfMaker = PdfMaker()

    val template = "hello"
    val data = mapOf("name" to "otto")

    Files.newOutputStream(Paths.get("/pdf-output/test.pdf")).use { out ->
        pdfMaker.makePdf(template, data, out)
    }
}
