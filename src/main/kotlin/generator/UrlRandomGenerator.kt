package generator

import kotlin.random.Random

class UrlRandomGenerator : UrlGenerator {

    private val ALPHABET = "abcdeqwertyuiop"
    private val MAX_SIZE_URL = 6

    override fun generate(originalUrl: String): String {
        val result = StringBuilder()
        for (i in 0 until MAX_SIZE_URL) {
            result.append(ALPHABET[Random.nextInt(ALPHABET.length)])
        }
        return result.toString()
    }
}