import generator.UrlGenerator
import generator.UrlRandomGenerator
import storage.UrlMemoryStorage
import storage.UrlStorage

class UrlShorter {

    private val urlStorage: UrlStorage = UrlMemoryStorage()
    private val urlGenerator: UrlGenerator = UrlRandomGenerator()

    fun generateUrl(originalUrl: String): String {
        val shortUrl = urlGenerator.generate(originalUrl)
        urlStorage.save(shortUrl, originalUrl)
        return shortUrl
    }

    fun getOriginalUrl(shortUrl: String): String? {
        return urlStorage.getOriginalUrl(shortUrl)
    }
}