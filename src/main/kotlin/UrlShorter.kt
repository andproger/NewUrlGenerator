import generator.UrlGenerator
import generator.UrlRandomGenerator
import storage.UrlMemoryStorage
import storage.UrlStorage

class UrlShorter {

    private val urlStorage: UrlStorage = UrlMemoryStorage()
    private val urlGenerator: UrlGenerator = UrlRandomGenerator(urlStorage)

    fun generateUrl(originalUrl: String): String {
        return urlGenerator.generate(originalUrl)
    }

    fun getOriginalUrl(shortUrl: String): String? {
        return urlStorage.getOriginalUrl(shortUrl)
    }
}