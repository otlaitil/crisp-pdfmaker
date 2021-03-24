package crisp.pdfmaker.web

import crisp.pdfmaker.core.TemplateNotFoundException
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.exceptions.TemplateInputException
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver

class WebTemplateProcessor() {
    private var engine: TemplateEngine = TemplateEngine()

    init {
        engine.addTemplateResolver(classLoaderTemplateResolver())
    }

    fun process(template: String, data: Any): String {
        val context = Context()
        context.setVariable("data", data)

        try {
            return engine.process(template, context)
        } catch (e: TemplateInputException) {
            throw TemplateNotFoundException("Template $template not found.")
        }
    }

    private fun classLoaderTemplateResolver(): ITemplateResolver {
        return ClassLoaderTemplateResolver().apply {
            suffix = ".htm"
            templateMode = TemplateMode.HTML
            characterEncoding = "UTF-8"
            order = 1
            checkExistence = true
        }
    }
}
