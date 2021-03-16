package crisp.pdfmaker

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import java.io.OutputStream
import kotlin.test.Test
import kotlin.test.assertEquals

class FakePdfMaker : IPdfMaker {
    override fun makePdf(template: String, data: Map<String, Any>, out: OutputStream) {
    }
}

class FakeS3Uploader : IS3Uploader {
    override fun upload(s3BucketName: String, filename: String, bytes: ByteArray) {
    }
}

class HandlerTest {

    @Test
    fun `handler works as expected`() {
        val fakePdfMaker = FakePdfMaker()
        val fakeS3Uploader = FakeS3Uploader()

        val mockLogger: LambdaLogger = mock {}
        val mockContext: Context = mock {
            on { logger } doReturn mockLogger
        }

        val body = mapOf(
            "template" to "hello",
            "filename" to "test.pdf",
            "request-id" to "abc-123",
            "data" to mapOf(
                "test" to "data"
            )
        )

        val event = mapOf("body" to body)

        val handler = Handler(
            pdfMaker = fakePdfMaker,
            s3Uploader = fakeS3Uploader,
            s3BucketName = "test-bucket"
        )

        val actualResponse = handler.handleRequest(event, context = mockContext)

        val expectedResponse = mapOf(
            "bucket" to "test-bucket",
            "key" to "test.pdf",
            "request-id" to "abc-123"
        )

        assertEquals(actualResponse, expectedResponse)
    }
}
