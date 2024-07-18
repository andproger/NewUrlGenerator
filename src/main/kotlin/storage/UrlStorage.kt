package storage

interface UrlStorage {
    fun save(shortUrl: String, originalUrl: String)

    fun getCount(): Int

    fun getOriginalUrl(shortUrl: String): String?
}