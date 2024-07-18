package validator


import org.junit.jupiter.api.Test
import storage.UrlStorage

class UniqueUrlKeyValidatorTest {

    //TODO add dependency Mockito or Mockk
    private val stubUrlStorage = object : UrlStorage {
        private val urlStorage = mutableMapOf<String, String>()

        override fun save(urlKey: String, originalUrl: String) {
            urlStorage[urlKey] = originalUrl
        }

        override fun getCount(): Int = throw NotImplementedError()

        override fun getOriginalUrl(urlKey: String): String? {
            return urlStorage[urlKey]
        }
    }

    private val validator: UrlKeyValidator = UniqueUrlKeyValidator(
        6,
        "abc",
        stubUrlStorage
    )

    //TODO name test methods in given when then style
    //TODO find and add more cases to test
    @Test
    fun `correct url`() {
        assert(validator.isValid("aaa"))
    }

    @Test
    fun `incorrect url (alphabet)`() {
        assert(!validator.isValid("aaad"))
    }

    @Test
    fun `incorrect url (size)`() {
        assert(!validator.isValid("aaaaaab"))
    }

    @Test
    fun `incorrect url (not unique)`() {
        stubUrlStorage.save("aaaaaab", "")
        assert(!validator.isValid("aaaaaab"))
    }
}