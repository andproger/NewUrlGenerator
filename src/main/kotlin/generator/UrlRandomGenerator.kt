package generator

import storage.UrlStorage
import kotlin.random.Random

class UrlRandomGenerator(
    private val urlStorage: UrlStorage
) : UrlGenerator {

    private val ALPHABET = "abcdeqwertyuiop"
    private val MAX_SIZE_URL = 6

    override fun generate(originalUrl: String): String {
        val result = StringBuilder()
        for (i in 0 until MAX_SIZE_URL) {
            result.append(ALPHABET[Random.nextInt(ALPHABET.length)])
        }
        urlStorage.save(result.toString(), originalUrl)
        return result.toString()
    }
}