package crisp.pdfmaker.core

import java.io.File
import java.io.OutputStream

class Application(
    private val pdfRenderer: IPdfRenderer,
    private val s3Uploader: IS3Uploader,
    private val pdfTemplateProcessor: PdfTemplateProcessor
) {

    constructor() : this(
        PdfRenderer(defaultTemplateAssetsPath()),
        S3Uploader(defaultS3BucketName()),
        PdfTemplateProcessor(defaultTemplatePath())
    )

    companion object {
        fun defaultS3BucketName(): String {
            return System.getenv("BUCKET_NAME")
        }

        fun defaultTemplateAssetsPath(): String {
            val templateLocation = System.getenv("TEMPLATE_LOCATION")

            return if (templateLocation == "classpath") {
                val classLoader = Application::class.java.classLoader
                classLoader.getResource("template-assets/").toString()
            } else {
                File("../template-assets/").toURI().toString()
            }
        }

        fun defaultTemplatePath(): String {
            val templateLocation = System.getenv("TEMPLATE_LOCATION")

            return if (templateLocation == "classpath") {
                val classLoader = Application::class.java.classLoader
                classLoader.getResource("templates/").toString()
            } else {
                File("../templates/").toURI().toString()
            }
        }
    }

    fun makePdf(template: String, data: Map<String, Any>, output: OutputStream) {
        val htmlContent = pdfTemplateProcessor.process(template, data)

        pdfRenderer.renderPdf(
            htmlContent,
            output
        )
    }

    fun uploadPdf(filename: String, bytes: ByteArray) {
        s3Uploader.upload(filename, bytes)
    }

    fun s3BucketName(): String {
        return s3Uploader.s3BucketName()
    }
}
