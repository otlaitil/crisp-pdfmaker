package crisp.pdfmaker

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import java.io.ByteArrayOutputStream

class Handler : RequestHandler<Map<String, Any>, Map<String, Any>> {
    private val pdfMaker = PdfMaker()
    private val s3Uploader = S3Uploader()

    override fun handleRequest(event: Map<String, Any>, context: Context): Map<String, Any> {
        val logger = context.logger
        logger.log("Got $event")

        val s3BucketName = System.getenv("BUCKET_NAME")

        val body = event["body"] as Map<*, *>

        val requestId = body["request-id"] as String
        val filename = body["filename"] as String
        val template = body["template"] as String
        val data = body["data"] as Map<String, Any>

        val outputStream = ByteArrayOutputStream()

        pdfMaker.makePdf(
            template,
            data,
            outputStream
        )

        s3Uploader.upload(s3BucketName, filename, outputStream.toByteArray())

        return mapOf(
            "filename" to filename,
            "bucket" to s3BucketName,
            "key" to filename,
            "requestId" to requestId
        )
    }
}
