package storage

interface UrlStorage {
    fun save(shortUrl: String, originalUrl: String)

    fun getOriginalUrl(shortUrl: String): String?
}