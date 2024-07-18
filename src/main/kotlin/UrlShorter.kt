import generator.UrlGenerator
import generator.UrlRandomGenerator
import storage.UrlMemoryStorage
import storage.UrlStorage
import validator.UniqueShortUrlValidator
import validator.UrlValidator

class UrlShorter {

    private val urlStorage: UrlStorage = UrlMemoryStorage()
    private val urlGenerator: UrlGenerator = UrlRandomGenerator(MAX_SIZE_URL, ALPHABET)
    private val uniqueShortUrlValidator: UrlValidator = UniqueShortUrlValidator(MAX_SIZE_URL, ALPHABET, urlStorage)

    fun generateUrl(originalUrl: String): String {
        var tries = 0
        while (tries < MAX_GENERATE_TRIES) {
            tries++
            val shortUrl = urlGenerator.generate(originalUrl)
            if (uniqueShortUrlValidator.isValid(shortUrl)) {
                urlStorage.save(shortUrl, originalUrl)
                return shortUrl
            }
        }
        //TODO as failed type or throw error
        return "ERROR: try latter"
    }

    fun getOriginalUrl(shortUrl: String): String? {
        return urlStorage.getOriginalUrl(shortUrl)
    }

    companion object {
        private const val MAX_GENERATE_TRIES = 5
        private const val ALPHABET = "abcdeqwertyuiop"
        private const val MAX_SIZE_URL = 6
    }
}