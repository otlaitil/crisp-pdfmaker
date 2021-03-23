package crisp.pdfmaker.web

import crisp.pdfmaker.core.Application
import io.javalin.Javalin
import org.yaml.snakeyaml.Yaml
import java.io.ByteArrayOutputStream
import java.io.File

private val webTemplateProcessor = WebTemplateProcessor()

class Web(private val application: Application = Application()) {
    fun start() {
        val app = Javalin.create().start(7000)

        app.get("/") { ctx -> ctx.html(index()) }
        app.get("/view-pdf") { ctx ->
            ctx
                .result(viewPdf(ctx.queryParam("template")))
                .contentType("application/pdf")
        }

        app.exception(Exception::class.java) { e, ctx ->
            ctx.html(e.stackTraceToString())
        }
    }

    private fun index(): String {
        var availableTemplates = mutableListOf<String>()

        File("../templates/").walkBottomUp().forEach {
            if (it.extension == "htm") {
                availableTemplates.add(it.parentFile.name)
            }
        }

        return webTemplateProcessor.process(
            "web/index",
            mapOf(
                "templates" to availableTemplates
            )
        )
    }

    private fun viewPdf(name: String?): ByteArray {
        val template = "$name/$name"
        val dataYamlFile = File("../templates/$template.yml")

        val yaml = Yaml()
        val data: Map<String, Any> = yaml.load(dataYamlFile.inputStream())

        val output = ByteArrayOutputStream()

        application.makePdf(template, data, output)

        return output.toByteArray()
    }
}
