package generator

import kotlin.random.Random

class UrlRandomGenerator(
    private val maxUrlSize: Int,
    private val alphabet: String
) : UrlGenerator {

    override fun generate(originalUrl: String): String {
        val result = StringBuilder()
        for (i in 0 until maxUrlSize) {
            result.append(alphabet[Random.nextInt(alphabet.length)])
        }
        return result.toString()
    }
}