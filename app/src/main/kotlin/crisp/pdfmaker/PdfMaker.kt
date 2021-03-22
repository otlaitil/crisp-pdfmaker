package crisp.pdfmaker

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import java.io.OutputStream

interface IPdfMaker {
    fun makePdf(htmlContent: String, out: OutputStream)
}

class PdfMaker(
    private val assetsPath: String,
    private val useAccessibilityMode: Boolean = true
) : IPdfMaker {

    override fun makePdf(htmlContent: String, out: OutputStream) {
        val builder = PdfRendererBuilder().apply {
            useFastMode()
            usePdfUaAccessbility(useAccessibilityMode)
            withHtmlContent(htmlContent, assetsPath)
        }

        builder.toStream(out)
        builder.run()
    }
}
