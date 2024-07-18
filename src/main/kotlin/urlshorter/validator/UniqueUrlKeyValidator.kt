package urlshorter.validator

import urlshorter.storage.UrlStorage

class UniqueUrlKeyValidator(
    private val maxUrlKeySize: Int,
    private val alphabet: String,
    private val urlStorage: UrlStorage,
) : UrlKeyValidator {
    override fun isValid(urlKey: String): Boolean {
        if (urlKey.length > maxUrlKeySize) return false
        if (urlKey.any { !alphabet.contains(it) }) return false
        if (urlStorage.getOriginalUrl(urlKey) != null) return false
        return true
    }
}