package urlshorter

import urlshorter.generator.UrlKeyGenerator
import urlshorter.generator.UrlKeyRandomGenerator
import urlshorter.storage.UrlMemoryStorage
import urlshorter.storage.UrlStorage
import urlshorter.validator.UniqueUrlKeyValidator
import urlshorter.validator.UrlKeyValidator

class UrlShorter(
    private val urlStorage: UrlStorage = Defaults.memoryStorage(),
    private val urlKeyGenerator: UrlKeyGenerator = Defaults.randomGenerator(),
    private val uniqueUrlKeyValidator: UrlKeyValidator = Defaults.validator(urlStorage),
    private val options: Options = Options.default
) {

    fun generateUrl(originalUrl: String): String {
        if (urlStorage.getCount() >= options.urlsStorageLimit) {
            return errorMessage("Saving limit ${options.urlsStorageLimit} exceeded.")
        }

        var tries = 0
        while (tries < options.generateTriesLimit) {
            tries++
            val urlKey = urlKeyGenerator.generate(originalUrl)
            if (uniqueUrlKeyValidator.isValid(urlKey)) {
                urlStorage.save(urlKey, originalUrl)
                return "${options.host}/$urlKey"
            }
        }

        return errorMessage("Please Try latter")
    }

    fun getOriginalUrl(shortUrl: String): String? {
        val urlKey = shortUrl.split("/").last()
        return urlStorage.getOriginalUrl(urlKey)
    }

    private fun errorMessage(message: String) = "ERROR: $message"

    data class Options(
        val generateTriesLimit: Int,
        val host: String,
        val urlsStorageLimit: Int
    ) {
        companion object {
            val default = Options(
                generateTriesLimit = Defaults.GENERATE_TRIES_LIMIT,
                host = Defaults.HOST_ADDRESS,
                urlsStorageLimit = Defaults.URLS_STORAGE_LIMIT
            )
        }
    }

    class Defaults {
        companion object {
            const val GENERATE_TRIES_LIMIT = 5
            const val HOST_ADDRESS = "http://example.com"
            const val URLS_STORAGE_LIMIT = 100
            const val ENG_ALPHABET = "abcdeqwrtyuiop"
            const val URL_KEY_MAX_SIZE = 6

            fun memoryStorage() = UrlMemoryStorage()

            fun randomGenerator() = UrlKeyRandomGenerator(
                URL_KEY_MAX_SIZE,
                ENG_ALPHABET
            )

            fun validator(urlStorage: UrlStorage) = UniqueUrlKeyValidator(
                URL_KEY_MAX_SIZE,
                ENG_ALPHABET,
                urlStorage
            )
        }
    }
}