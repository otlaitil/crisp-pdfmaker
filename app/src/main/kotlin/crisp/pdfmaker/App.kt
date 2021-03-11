package crisp.pdfmaker

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import java.nio.file.Files
import java.nio.file.Paths
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver

fun main() {
    val engine = TemplateEngine()
    engine.addTemplateResolver(templateResolver())

    val context = Context()
    context.setVariable("name", "otto")

    val htmlContent = engine.process("test", context)

    val builder = PdfRendererBuilder().apply {
        useFastMode()
        withHtmlContent(htmlContent, null)
    }

    Files.newOutputStream(Paths.get("/pdf-output/file.pdf")).use { out ->
        builder.toStream(out)
        builder.run()
    }
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