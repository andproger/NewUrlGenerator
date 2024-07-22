import urlshorter.UrlShorter
import urlshorter.UrlShorterApi

fun main() {
    val urlShorter: UrlShorterApi = UrlShorter()
    val longUrl = "http://googgle.com/qwertyuiopasdfghjk"
    val shortUrl = urlShorter.generateUrl(longUrl)

    println("Short url: $shortUrl")
    val originalUrl = urlShorter.getOriginalUrl(shortUrl)
    println("Original url: $originalUrl")
}