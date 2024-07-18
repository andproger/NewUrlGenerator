package validator

interface UrlKeyValidator {
    fun isValid(urlKey: String): Boolean
}