package crisp.pdfmaker

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import java.io.ByteArrayOutputStream
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest

class Handler : RequestHandler<Map<String, Any>, Map<String, Any>> {
    private val pdfMaker = PdfMaker()
    private val s3 = S3Client.builder()
        .region(Region.EU_CENTRAL_1)
        .build()

    override fun handleRequest(event: Map<String, Any>, context: Context): Map<String, Any> {
        val logger = context.logger
        logger.log("Got $event")

        val s3BucketName = System.getenv("BUCKET_NAME")

        val body = event["body"] as Map<*, *>

        val requestId = body["request-id"] as String
        val filename = body["filename"] as String
        val template = body["template"] as String
        val data = body["data"] as Map<String, String>

        val outputStream = ByteArrayOutputStream()

        pdfMaker.makePdf(
            template,
            data,
            outputStream
        )

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(s3BucketName)
            .key(filename)
            .build()

        s3.putObject(
            putObjectRequest,
            RequestBody.fromBytes(outputStream.toByteArray())
        )

        return mapOf(
            "filename" to filename,
            "bucket" to s3BucketName,
            "key" to filename,
            "requestId" to requestId
        )
    }
}
