package crisp.pdfmaker

import io.javalin.Javalin
import org.yaml.snakeyaml.Yaml
import java.io.ByteArrayOutputStream
import java.io.File

// process pdf template views and load assets from disk
private val assetsPath = File("template-assets").toURI().toString()
private var pdfMaker = PdfMaker(assetsPath)
private val templateProcessor: TemplateProcessor = TemplateProcessor(ResolverType.URL)

// web ui: process html views from classpath
private var appTemplateProcessor = TemplateProcessor(ResolverType.CLASSLOADER)

fun main() {
    val app = Javalin.create().start(7000)

    app.get("/") { ctx -> ctx.html(index()) }
    app.get("/view-pdf") { ctx ->
        ctx
            .result(viewPdf(ctx.queryParam("template-path")))
            .contentType("application/pdf")
    }

    app.exception(Exception::class.java) { e, ctx ->
        ctx.html(e.stackTraceToString())
    }
}

private fun index(): String {
    var availableTemplates = mutableListOf<String>()

    File("/templates/").walkBottomUp().forEach {
        if (it.extension == "htm") {
            availableTemplates.add(it.parentFile.name)
        }
    }

    return appTemplateProcessor.process(
        "web/index",
        mapOf(
            "templates" to availableTemplates
        )
    )
}

private fun viewPdf(name: String?): ByteArray {
    val template = File("templates/$name/$name.htm")
    val templatePath = template.toURI().toString()

    val dataYaml = File("templates/$name/$name.yml")

    val yaml = Yaml()
    val data: Map<String, Any> = yaml.load(dataYaml.inputStream())

    val output = ByteArrayOutputStream()

    val htmlContent = templateProcessor.process(templatePath, data)

    pdfMaker.makePdf(
        htmlContent,
        output
    )

    return output.toByteArray()
}
