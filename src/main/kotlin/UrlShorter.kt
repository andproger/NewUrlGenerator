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
        //TODO move hole logic to separate class or refactor UrlShorter to be testable(unit tests)
        if (urlStorage.getCount() >= URLS_STORAGE_LIMIT) {
            return errorMessage("Saving limit $URLS_STORAGE_LIMIT exceeded.")
        }

        var tries = 0
        while (tries < MAX_GENERATE_TRIES) {
            tries++
            val shortUrl = urlGenerator.generate(originalUrl)
            if (uniqueShortUrlValidator.isValid(shortUrl)) {
                urlStorage.save(shortUrl, originalUrl)
                return shortUrl
            }
        }

        return errorMessage("Please Try latter")
    }

    fun getOriginalUrl(shortUrl: String): String? {
        return urlStorage.getOriginalUrl(shortUrl)
    }

    private fun errorMessage(message: String) = "ERROR: $message"

    companion object {
        private const val MAX_GENERATE_TRIES = 5
        private const val ALPHABET = "abcdeqwertyuiop"
        private const val MAX_SIZE_URL = 6
        private const val URLS_STORAGE_LIMIT = 100
    }
}