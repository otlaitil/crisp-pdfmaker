package crisp.pdfmaker.core

import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest

interface IS3Uploader {
    fun upload(filename: String, bytes: ByteArray)
    fun s3BucketName() : String
}

class S3Uploader(private val s3BucketName: String) : IS3Uploader {
    private val s3 = S3Client.builder()
        .region(Region.EU_CENTRAL_1)
        .build()

    override fun s3BucketName(): String {
        return s3BucketName
    }
    override fun upload(filename: String, bytes: ByteArray) {
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
