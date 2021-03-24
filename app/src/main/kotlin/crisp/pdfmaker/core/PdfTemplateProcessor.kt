package crisp.pdfmaker.core

import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.exceptions.TemplateInputException
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ITemplateResolver
import org.thymeleaf.templateresolver.UrlTemplateResolver

class TemplateNotFoundException(message: String) : Exception(message)

class PdfTemplateProcessor(
    private val templatePath: String
) {
    private var engine: TemplateEngine = TemplateEngine()

    init {
        engine.addTemplateResolver(urlTemplateResolver())
    }

    fun process(template: String, data: Any): String {
        val context = Context()
        context.setVariable("data", data)

        try {
            return engine.process(template, context)
        } catch (e: TemplateInputException) {
            throw TemplateNotFoundException(e.message!!)
        }
    }

    private fun urlTemplateResolver(): ITemplateResolver {
        return UrlTemplateResolver().apply {
            templateMode = TemplateMode.HTML
            characterEncoding = "UTF-8"
            prefix = templatePath
            suffix = ".htm"
            order = 1
            checkExistence = true
            cacheTTLMs = 0
        }
    }
}
