import generator.UrlKeyGenerator
import generator.UrlKeyRandomGenerator
import storage.UrlMemoryStorage
import storage.UrlStorage
import validator.UniqueUrlKeyValidator
import validator.UrlKeyValidator

class UrlShorter {

    private val urlStorage: UrlStorage = UrlMemoryStorage()
    private val urlKeyGenerator: UrlKeyGenerator = UrlKeyRandomGenerator(MAX_URL_KEY_SIZE, ALPHABET)
    private val uniqueUrlKeyValidator: UrlKeyValidator = UniqueUrlKeyValidator(MAX_URL_KEY_SIZE, ALPHABET, urlStorage)

    fun generateUrl(originalUrl: String): String {
        //TODO move hole logic to separate class or refactor UrlShorter to be testable(unit tests)
        if (urlStorage.getCount() >= URLS_STORAGE_LIMIT) {
            return errorMessage("Saving limit $URLS_STORAGE_LIMIT exceeded.")
        }

        var tries = 0
        while (tries < MAX_GENERATE_TRIES) {
            tries++
            val urlKey = urlKeyGenerator.generate(originalUrl)
            if (uniqueUrlKeyValidator.isValid(urlKey)) {
                urlStorage.save(urlKey, originalUrl)
                return "$HOST_ADDRESS/$urlKey"
            }
        }

        return errorMessage("Please Try latter")
    }

    fun getOriginalUrl(shortUrl: String): String? {
        val urlKey = shortUrl.split("/").last()
        return urlStorage.getOriginalUrl(urlKey)
    }

    private fun errorMessage(message: String) = "ERROR: $message"

    companion object {
        private const val MAX_GENERATE_TRIES = 5
        private const val ALPHABET = "abcdeqwrtyuiop"
        private const val HOST_ADDRESS = "http://example.com"
        private const val MAX_URL_KEY_SIZE = 6
        private const val URLS_STORAGE_LIMIT = 100
    }
}