package crisp.pdfmaker

import io.javalin.Javalin
import org.yaml.snakeyaml.Yaml
import java.io.ByteArrayOutputStream
import java.io.File

// process html views for the app from classloader
private var appTemplateProcessor = TemplateProcessor(ResolverType.CLASSLOADER)

// process pdf template views from disk
private var pdfMaker = PdfMaker(TemplateProcessor(ResolverType.URL))

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
    val template = File("/templates/$name/$name.htm")
    val templateUri = template.toURI()

    val dataYaml = File("/templates/$name/$name.yml")

    val yaml = Yaml()
    val data: Map<String, Any> = yaml.load(dataYaml.inputStream())

    val output = ByteArrayOutputStream()

    pdfMaker.makePdf(
        templateUri.toString(),
        data,
        output
    )

    return output.toByteArray()
}
