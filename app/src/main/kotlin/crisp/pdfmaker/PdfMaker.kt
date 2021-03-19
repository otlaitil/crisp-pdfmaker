package crisp.pdfmaker

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import java.io.File
import java.io.OutputStream

interface IPdfMaker {
    fun makePdf(template: String, data: Map<String, Any>, out: OutputStream)
}

class PdfMaker(
    private val templateProcessor: TemplateProcessor = TemplateProcessor()
) : IPdfMaker {

    override fun makePdf(template: String, data: Map<String, Any>, out: OutputStream) {
        val htmlContent = templateProcessor.process(template, data)

        val builder = PdfRendererBuilder().apply {
            useFastMode()
            usePdfUaAccessbility(true)
            withHtmlContent(htmlContent, File("/template-assets").toURI().toString())
        }

        builder.toStream(out)
        builder.run()
    }
}
