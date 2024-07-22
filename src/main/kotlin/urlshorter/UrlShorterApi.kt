package urlshorter

interface UrlShorterApi {
    fun generateUrl(originalUrl: String): String

    fun getOriginalUrl(shortUrl: String): String?
}