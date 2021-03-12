package crisp.pdfmaker

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import java.io.ByteArrayOutputStream
import java.util.*

class Handler : RequestHandler<Map<String, Any>, Map<String, Any>> {
    override fun handleRequest(event: Map<String, Any>, context: Context): Map<String, Any> {
        context.logger.log("Got $event")

        val template = event["template"] as String
        val data = event["data"] as Map<String, String>

        val pdfMaker = PdfMaker()
        val stream = ByteArrayOutputStream()

        pdfMaker.makePdf(
            template,
            data,
            stream
        )

        val encoded = Base64.getEncoder().encode(stream.toByteArray())
        val base64EncodedPdfContent = String(encoded)

        return mapOf(
            "statusCode" to 200,
            "headers" to listOf(
                mapOf("Content-type" to "application/pdf")
            ),
            "body" to base64EncodedPdfContent,
            "isBase64Encoded" to true
        )
    }
}