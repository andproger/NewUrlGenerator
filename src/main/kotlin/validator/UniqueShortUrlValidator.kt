package validator

import storage.UrlStorage

class UniqueShortUrlValidator(
    private val maxUrlSize: Int,
    private val alphabet: String,
    private val urlStorage: UrlStorage,
) : UrlValidator {
    override fun isValid(url: String): Boolean {
        if (url.length > maxUrlSize) return false
        if (url.any { !alphabet.contains(it) }) return false
        if (urlStorage.getOriginalUrl(url) != null) return false
        return true
    }
}