package validator

interface UrlValidator {
    fun isValid(url: String): Boolean
}