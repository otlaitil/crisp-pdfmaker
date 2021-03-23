package crisp.pdfmaker.core

import java.io.ByteArrayOutputStream
import kotlin.test.Test
import kotlin.test.assertTrue

internal class ApplicationTest {
    private val application = Application()

    @Test
    fun makePdf() {
        val output = ByteArrayOutputStream()
        application.makePdf("test", mapOf("name" to "Otto"), output)
        assertTrue {
            output.toByteArray().isNotEmpty()
        }
    }
}
