package crisp.pdfmaker

import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest

class S3Uploader {
    private val s3 = S3Client.builder()
        .region(Region.EU_CENTRAL_1)
        .build()

    fun upload(s3BucketName: String, filename: String, bytes: ByteArray) {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(s3BucketName)
            .key(filename)
            .build()

        s3.putObject(
            putObjectRequest,
            RequestBody.fromBytes(bytes)
        )
    }
}
