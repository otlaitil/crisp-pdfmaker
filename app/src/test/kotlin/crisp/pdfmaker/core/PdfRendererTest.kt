package crisp.pdfmaker.core

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.Test
import kotlin.test.assertTrue

class PdfMakerTest {
    @Test
    fun `generates a pdf file successfully`() {
        val pdfMaker = PdfRenderer(
            Application.defaultTemplateAssetsPath(),
            useAccessibilityMode = false
        )

        val stream = ByteArrayOutputStream()
        val htmlContent = """
            <html>
                <body>
                    Hello World
                </body>
            </html>
        """.trimIndent()

        pdfMaker.renderPdf(htmlContent, stream)

        val generatedPdfText = extractPdfText(stream.toByteArray())

        assertTrue {
            generatedPdfText.contains(Regex("Hello World"))
        }
    }
}

private fun extractPdfText(pdfData: ByteArray): String {
    val pdfDocument = PDDocument.load(ByteArrayInputStream(pdfData))
    return PDFTextStripper().getText(pdfDocument)
}
