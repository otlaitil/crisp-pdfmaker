package crisp.pdfmaker

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import java.io.ByteArrayOutputStream

class Handler(
    private val pdfMaker: IPdfMaker = PdfMaker(),
    private val s3Uploader: IS3Uploader = S3Uploader(),
    private val s3BucketName: String = System.getenv("BUCKET_NAME")
) : RequestHandler<Map<String, Any>, Map<String, Any>> {

    override fun handleRequest(event: Map<String, Any>, context: Context): Map<String, Any> {
        val logger = context.logger
        logger.log("Got $event")

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
            "bucket" to s3BucketName,
            "key" to filename,
            "request-id" to requestId
        )
    }
}
