package urlshorter.storage

interface UrlStorage {
    fun save(urlKey: String, originalUrl: String)

    fun getCount(): Int

    fun getOriginalUrl(urlKey: String): String?
}