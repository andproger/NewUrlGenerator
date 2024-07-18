package storage

class UrlMemoryStorage : UrlStorage {

    private val urlStorage = mutableMapOf<String, String>()

    override fun save(urlKey: String, originalUrl: String) {
        urlStorage[urlKey] = originalUrl
    }

    override fun getCount(): Int {
        return urlStorage.size
    }

    override fun getOriginalUrl(urlKey: String): String? {
        return urlStorage[urlKey]
    }
}