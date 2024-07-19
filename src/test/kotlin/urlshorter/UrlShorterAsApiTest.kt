package urlshorter

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class UrlShorterAsApiTest {

    @Test
    fun `custom host - generateUrl once - returned short url and originalUrl`() {
        //GIVEN
        val host = "myhost.my"
        val urlShorter = UrlShorter(options = UrlShorter.Options.default.copy(host = host))
        val url = "http://google.com/sdfdsfsdf/sdfdsf"

        //WHEN
        val result = urlShorter.generateUrl(url)
        val originalUrl = urlShorter.getOriginalUrl(result)

        //THEN
        assertEquals(url, originalUrl)
        assert(result.startsWith(host))
        assert(result.length <= "$host/".length + UrlShorter.Defaults.URL_KEY_MAX_SIZE)
    }

    @Test
    fun `defaults and custom host - generateUrl 100 times - returned short urls & returned originalUrls`() {
        //GIVEN
        val count = 100
        val host = "myhost.my"

        //WHEN
        val testResults = testTimes(
            count = count,
            host = host,
            urlsStorageLimit = null
        )

        //THEN
        assert(testResults.allShortUrlsCorrect(host))
        assertEquals(testResults.urls, testResults.originalUrls)
    }

    @Test
    fun `default 100 for storage limit and custom host - generateUrl 200 times - returned 100 short urls and 100 ERRORs`() {
        //GIVEN
        val count = 200
        val host = "myhost.my"

        //WHEN
        val testResults = testTimes(
            count = count,
            host = host,
            urlsStorageLimit = 100
        )

        //THEN
        assert(
            testResults.shortUrlsWithOriginals.count { (shortUrl, _) ->
                shortUrl.startsWith(host)
                        && shortUrl.length <= "$host/".length + UrlShorter.Defaults.URL_KEY_MAX_SIZE
            } == 100
        )
    }

    @Test
    fun `1000 for storage and custom host - generateUrl 1000 times - returned 1000 short urls`() {
        //GIVEN
        val count = 1000
        val host = "myhost.my"

        //WHEN
        val testResults = testTimes(
            count = count,
            host = host,
            urlsStorageLimit = count
        )

        //THEN
        val allResultsCorrect = testResults.allShortUrlsCorrect(host)
        assert(allResultsCorrect)
        assertEquals(testResults.urls, testResults.originalUrls)
    }

    @Test
    fun `100_000 for storage and custom host - generateUrl 100_000 times - returned 100_000 short urls`() {
        //GIVEN
        val count = 100_000
        val host = "myhost.my"

        //WHEN
        val testResults = testTimes(
            count = count,
            host = host,
            urlsStorageLimit = count
        )

        //THEN
        val allResultsCorrect = testResults.allShortUrlsCorrect(host)
        assert(allResultsCorrect)
        assertEquals(testResults.urls, testResults.originalUrls)
    }

    //TODO write more test for different customizations

    private class TestResults(
        val urls: List<String>,
        val shortUrlsWithOriginals: List<Pair<String, String>>,
        val originalUrls: List<String?>
    ) {
        fun allShortUrlsCorrect(host: String): Boolean {
            return shortUrlsWithOriginals.all { (shortUrl, _) ->
                shortUrl.startsWith(host)
                        && shortUrl.length <= "$host/".length + UrlShorter.Defaults.URL_KEY_MAX_SIZE
            }
        }
    }

    private fun testTimes(
        count: Int,
        host: String,
        urlsStorageLimit: Int? = count
    ): TestResults {
        val urlShorter = UrlShorter(
            options = UrlShorter.Options.default.copy(
                host = host,
                urlsStorageLimit = urlsStorageLimit ?: UrlShorter.Defaults.URLS_STORAGE_LIMIT
            )
        )
        val urls = List(count) {
            "http://google.com/sdfdsfsdf/sdfdsf$it"
        }

        //WHEN
        val results = urls.map { url ->
            urlShorter.generateUrl(url) to url
        }
        val originalUrls = results.map { (shortUrl, _) ->
            urlShorter.getOriginalUrl(shortUrl)
        }
        return TestResults(
            urls,
            results,
            originalUrls
        )
    }
}