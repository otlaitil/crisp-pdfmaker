package crisp.pdfmaker.core

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class PdfTemplateProcessorTest {
    private val templateProcessor = PdfTemplateProcessor(
        Application.defaultTemplatePath()
    )

    @Test
    fun `processes a template successfully`() {
        val template = "test"
        val data = mapOf("name" to "Otto")

        val htmlContent = templateProcessor.process(template, data)

        assertTrue {
            htmlContent.contains("Otto")
        }
    }

    @Test
    fun `throws an error if template is not found`() {
        val template = "nonexisting-template"
        val data = mapOf("data" to "empty")

        assertFailsWith<TemplateNotFoundException> {
            templateProcessor.process(template, data)
        }
    }
}
