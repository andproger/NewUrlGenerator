package storage

class UrlMemoryStorage : UrlStorage {

    private val urlStorage = mutableMapOf<String, String>()

    override fun save(shortUrl: String, originalUrl: String) {
        urlStorage[shortUrl] = originalUrl
    }

    override fun getOriginalUrl(shortUrl: String): String? {
        return urlStorage[shortUrl]
    }
}