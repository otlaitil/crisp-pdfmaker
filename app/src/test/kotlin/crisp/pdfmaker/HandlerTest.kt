package crisp.pdfmaker

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlin.test.Test
import kotlin.test.assertEquals

class HandlerTest {
    private val fakeS3Uploader: IS3Uploader = mock {}

    private val mockLogger: LambdaLogger = mock {}
    private val mockContext: Context = mock {
        on { logger } doReturn mockLogger
    }

    @Test
    fun `handler works as expected`() {
        val fakePdfMaker: IPdfMaker = mock {}

        val body = mapOf(
            "template" to "test",
            "filename" to "test.pdf",
            "requestId" to "abc-123",
            "data" to mapOf(
                "name" to "Otto"
            )
        )

        val event = mapOf("body" to Gson().toJson(body))

        val handler = Handler(
            pdfMaker = fakePdfMaker,
            s3Uploader = fakeS3Uploader,
            s3BucketName = "test-bucket"
        )

        val actualResponse = handler.handleRequest(event, context = mockContext)

        val expectedResponse = mapOf(
            "body" to Gson().toJson(
                mapOf(
                    "bucket" to "test-bucket",
                    "key" to "test.pdf",
                    "requestId" to "abc-123"
                )
            ),
            "headers" to mapOf(
                "Content-Type" to "application/json"
            ),
            "isBase64Encoded" to "false",
            "statusCode" to "200"
        )


        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `returns an error response if template is not found`() {
        val fakePdfMaker: IPdfMaker = mock {}

        val body = mapOf(
            "template" to "nonexisting-template",
            "filename" to "test.pdf",
            "requestId" to "abc-123",
            "data" to mapOf(
                "test" to "data"
            )
        )

        val event = mapOf("body" to Gson().toJson(body))

        val handler = Handler(
            pdfMaker = fakePdfMaker,
            s3Uploader = fakeS3Uploader,
            s3BucketName = "test-bucket"
        )

        val actualResponse = handler.handleRequest(event, context = mockContext)

        val expectedResponse = mapOf(
            "body" to Gson().toJson(
                mapOf(
                    "error" to "Template not found.",
                    "requestId" to "abc-123"
                )
            ),
            "headers" to mapOf(
                "Content-Type" to "application/json"
            ),
            "isBase64Encoded" to "false",
            "statusCode" to "400"
        )


        assertEquals(expectedResponse, actualResponse)
    }
}
