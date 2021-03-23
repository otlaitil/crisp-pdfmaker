package crisp.pdfmaker.lambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.google.gson.Gson
import crisp.pdfmaker.core.Application
import crisp.pdfmaker.core.TemplateNotFoundException
import java.io.ByteArrayOutputStream

data class Event(
    val filename: String,
    val requestId: String,
    val template: String,
    val data: Map<String, Any>
)

class Handler(private val application: Application = Application()) :
    RequestHandler<Map<String, Any>, Map<String, Any>> {
    override fun handleRequest(request: Map<String, Any>, context: Context): Map<String, Any> {
        val logger = context.logger
        logger.log("Got $request")

        val event = Gson().fromJson(request["body"] as String, Event::class.java)
        val outputStream = ByteArrayOutputStream()

        try {
            application.makePdf(event.template, event.data, outputStream)
        } catch (e: TemplateNotFoundException) {
            logger.log(e.message)
            return errorResponse("Template not found.", event.requestId, 400)
        }

        application.uploadPdf(event.filename, outputStream.toByteArray())

        return successResponse(application.s3BucketName(), event.filename, event.requestId)
    }

    private fun successResponse(s3BucketName: String, filename: String, requestId: String): Map<String, Any> {
        val jsonBody = Gson().toJson(
            mapOf(
                "bucket" to s3BucketName,
                "key" to filename,
                "requestId" to requestId
            )
        )

        return mapOf(
            "body" to jsonBody,
            "headers" to mapOf(
                "Content-Type" to "application/json"
            ),
            "isBase64Encoded" to "false",
            "statusCode" to "200"
        )
    }

    private fun errorResponse(error: String, requestId: String, statusCode: Int): Map<String, Any> {
        val jsonBody = Gson().toJson(
            mapOf(
                "error" to error,
                "requestId" to requestId
            )
        )

        return mapOf(
            "body" to jsonBody,
            "headers" to mapOf(
                "Content-Type" to "application/json"
            ),
            "isBase64Encoded" to "false",
            "statusCode" to statusCode.toString()
        )
    }
}
