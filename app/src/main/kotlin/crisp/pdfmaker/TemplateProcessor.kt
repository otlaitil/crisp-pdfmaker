package crisp.pdfmaker

import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.exceptions.TemplateInputException
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver
import org.thymeleaf.templateresolver.UrlTemplateResolver

class TemplateNotFoundException(message: String) : Exception(message)

enum class ResolverType {
    CLASSLOADER, URL
}

class TemplateProcessor(
    type : ResolverType = ResolverType.CLASSLOADER
) {
    private var engine: TemplateEngine = TemplateEngine()

    init {
        if (type == ResolverType.CLASSLOADER) {
            engine.addTemplateResolver(classLoaderTemplateResolver())
        } else {
            engine.addTemplateResolver(urlTemplateResolver())
        }
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

    private fun urlTemplateResolver(): ITemplateResolver {
        return UrlTemplateResolver().apply {
            templateMode = TemplateMode.HTML
            characterEncoding = "UTF-8"
            order = 1
            checkExistence = true
            cacheTTLMs = 0
        }
    }
}
