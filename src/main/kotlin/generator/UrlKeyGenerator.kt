package generator

interface UrlKeyGenerator {
    fun generate(originalUrl: String): String
}