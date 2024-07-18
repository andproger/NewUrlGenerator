package generator

import kotlin.random.Random

class UrlKeyRandomGenerator(
    private val maxUrlKeySize: Int,
    private val alphabet: String
) : UrlKeyGenerator {

    override fun generate(originalUrl: String): String {
        val result = StringBuilder()
        for (i in 0 until maxUrlKeySize) {
            result.append(alphabet[Random.nextInt(alphabet.length)])
        }
        return result.toString()
    }
}