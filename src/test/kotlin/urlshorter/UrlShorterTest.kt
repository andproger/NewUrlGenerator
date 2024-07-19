package urlshorter

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import urlshorter.generator.UrlKeyGenerator
import urlshorter.storage.UrlStorage
import urlshorter.validator.UrlKeyValidator
import kotlin.test.BeforeTest
import kotlin.test.assertEquals


class UrlShorterTest {

    private val urlStorage: UrlStorage = mockk()
    private val urlKeyGenerator: UrlKeyGenerator = mockk()
    private val urlKeyValidator: UrlKeyValidator = mockk()
    private var options: UrlShorter.Options = UrlShorter.Options.default

    private val urlShorter = UrlShorter(
        urlStorage,
        urlKeyGenerator,
        urlKeyValidator,
        options
    )

    private var originalUrl = "http://googkle.com/asdasdas/asdasd/asd"
    private var generatedUrlKey = "aaa"
    private fun shortUrlByGeneratedUrlKey(
        host: String = options.host,
        urlKey: String = generatedUrlKey
    ) = "$host/$urlKey"

    @BeforeTest
    fun setup() {
        every { urlStorage.getCount() }.answers { 0 }
        every { urlStorage.save(any(), any()) }.answers { }
        every { urlKeyGenerator.generate(any()) }.answers { generatedUrlKey }
        every { urlKeyValidator.isValid(any()) }.answers { true }
    }

    @Test
    fun `option host and validation pass - generateUrl - right shortUrl once generated, once saved and returned`() {
        //WHEN
        val shortUrl = urlShorter.generateUrl(originalUrl)

        //THEN
        assertEquals(shortUrlByGeneratedUrlKey(), shortUrl)
        verify(exactly = 1) { urlKeyGenerator.generate(any()) }
        verify { urlStorage.save(generatedUrlKey, originalUrl) }
    }

    @Test
    fun `100 urls - generateUrl - not generated, not saved and returned ERROR`() {
        //GIVEN
        every { urlStorage.getCount() }.answers { 100 }

        //WHEN
        val shortUrl = urlShorter.generateUrl(originalUrl)

        //THEN
        assert(shortUrl.startsWith("ERROR"))
        assert(shortUrl.contains("limit"))
        verify(exactly = 0) { urlKeyGenerator.generate(any()) }
        verify(exactly = 0) { urlStorage.save(any(), any()) }
    }

    @Test
    fun `validation not pass - generateUrl - 5 times tried, not saved and returned ERROR`() {
        //GIVEN
        every { urlKeyValidator.isValid(any()) }.answers { false }

        //WHEN
        val shortUrl = urlShorter.generateUrl(originalUrl)

        //THEN
        assert(shortUrl.startsWith("ERROR"))
        verify(exactly = 5) { urlKeyGenerator.generate(any()) }
        verify(exactly = 5) { urlKeyValidator.isValid(any()) }
        verify(exactly = 0) { urlStorage.save(any(), any()) }
    }

    @Test
    fun `validation not pass 3 times - generateUrl - 4 times tried, 4th saved and returned`() {
        //GIVEN
        every { urlKeyValidator.isValid(any()) }.returnsMany(false, false, false, true)

        //WHEN
        val shortUrl = urlShorter.generateUrl(originalUrl)

        //THEN
        assertEquals(shortUrlByGeneratedUrlKey(), shortUrl)
        verify(exactly = 4) { urlKeyGenerator.generate(any()) }
        verify(exactly = 4) { urlKeyValidator.isValid(any()) }
        verify(exactly = 1) { urlStorage.save(generatedUrlKey, originalUrl) }
    }

    @Test
    fun `urlStorage returning originalUrl - getOriginalUrl - get from urlStorage called with urlKey`() {
        //GIVEN
        every { urlStorage.getOriginalUrl(any()) }.answers { originalUrl }
        val shortUrl = "http://example.com/bbb"

        //WHEN
        val result = urlShorter.getOriginalUrl(shortUrl)

        //THEN
        assertEquals(originalUrl, result)
        verify(exactly = 1) { urlStorage.getOriginalUrl("bbb") }
    }

    @Test
    fun `urlStorage returning null - getOriginalUrl - get from urlStorage called with urlKey and return null`() {
        //GIVEN
        every { urlStorage.getOriginalUrl(any()) }.answers { null }
        val shortUrl = "http://example.com/bbb"

        //WHEN
        val result = urlShorter.getOriginalUrl(shortUrl)

        //THEN
        assertEquals(null, result)
        verify(exactly = 1) { urlStorage.getOriginalUrl("bbb") }
    }
}