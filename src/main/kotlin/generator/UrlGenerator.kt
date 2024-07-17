package generator

interface UrlGenerator {
    fun generate(originalUrl: String): String
}