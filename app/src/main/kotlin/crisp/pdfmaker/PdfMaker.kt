package crisp.pdfmaker

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.exceptions.TemplateInputException
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver
import java.io.OutputStream

interface IPdfMaker {
    fun makePdf(template: String, data: Map<String, Any>, out: OutputStream) : PdfResult
}

data class PdfResult(val success : Boolean, val error : String? = null)

class PdfMaker : IPdfMaker {
    private var engine : TemplateEngine = TemplateEngine()

    init {
        engine.addTemplateResolver(templateResolver())
    }

    override fun makePdf(template: String, data: Map<String, Any>, out: OutputStream) : PdfResult {
        val context = Context()
        context.setVariable("data", data)

        var htmlContent : String

        try {
            htmlContent = engine.process(template, context)
        } catch (e: TemplateInputException) {
            return PdfResult(false, "Template not found.")
        }

        val builder = PdfRendererBuilder().apply {
            useFastMode()
            withHtmlContent(htmlContent, null)
        }

        builder.toStream(out)
        builder.run()

        return PdfResult(true)
    }

    private fun templateResolver(): ITemplateResolver {
        return ClassLoaderTemplateResolver().apply {
            prefix = "templates/"
            suffix = ".htm"
            templateMode = TemplateMode.HTML
            characterEncoding = "UTF-8"
            order = 1
            checkExistence = true
        }
    }
}
