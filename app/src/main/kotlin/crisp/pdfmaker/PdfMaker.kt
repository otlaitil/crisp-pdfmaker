package crisp.pdfmaker

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver
import java.io.OutputStream

interface IPdfMaker {
    fun makePdf(template: String, data: Map<String, Any>, out: OutputStream)
}

class PdfMaker : IPdfMaker {
    private var engine : TemplateEngine = TemplateEngine()

    init {
        engine.addTemplateResolver(templateResolver())
    }

    override fun makePdf(template: String, data: Map<String, Any>, out: OutputStream) {
        val context = Context()
        context.setVariable("data", data)

        val htmlContent = engine.process(template, context)

        val builder = PdfRendererBuilder().apply {
            useFastMode()
            withHtmlContent(htmlContent, null)
        }

        builder.toStream(out)
        builder.run()
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
