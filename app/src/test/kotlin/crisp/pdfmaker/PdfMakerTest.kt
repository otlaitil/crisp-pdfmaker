package crisp.pdfmaker

import kotlin.test.Test
import java.io.ByteArrayInputStream
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.ByteArrayOutputStream
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class PdfMakerTest {
    @Test
    fun `generates a pdf file successfully`() {
        val pdfMaker = PdfMaker()

        val template = "templates/test"
        val data = mapOf("name" to "otto")

        val stream = ByteArrayOutputStream()

        pdfMaker.makePdf(template, data, stream)

        val generatedPdfText = extractPdfText(stream.toByteArray())

        assertTrue {
            generatedPdfText.contains(Regex("otto"))
        }
    }

    @Test
    fun `returns an error if template is not found`() {
        val pdfMaker = PdfMaker()

        val template = "nonexisting-template"
        val data = mapOf("data" to "empty")

        val stream = ByteArrayOutputStream()

        assertFailsWith<TemplateNotFoundException> {
            pdfMaker.makePdf(template, data, stream)
        }
    }
}

private fun extractPdfText(pdfData: ByteArray): String {
    val pdfDocument = PDDocument.load(ByteArrayInputStream(pdfData))
    return PDFTextStripper().getText(pdfDocument)
}
