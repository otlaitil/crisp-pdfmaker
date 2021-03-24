package crisp.pdfmaker.core

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import java.io.OutputStream

interface IPdfRenderer {
    fun renderPdf(htmlContent: String, out: OutputStream)
}

class PdfRenderer(
    private val assetsPath: String,
    private val useAccessibilityMode: Boolean = true
) : IPdfRenderer {

    override fun renderPdf(htmlContent: String, out: OutputStream) {
        val builder = PdfRendererBuilder().apply {
            useFastMode()
            usePdfUaAccessbility(useAccessibilityMode)
            withHtmlContent(htmlContent, assetsPath)
        }

        builder.toStream(out)
        builder.run()
    }
}
