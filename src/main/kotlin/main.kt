import urlshorter.UrlShorter

fun main() {
    val urlShorter = UrlShorter()
    val longUrl = "http://googgle.com/qwertyuiopasdfghjk"
    val shortUrl = urlShorter.generateUrl(longUrl)

    println("Short url: $shortUrl")
    val originalUrl = urlShorter.getOriginalUrl(shortUrl)
    println("Original url: $originalUrl")
}