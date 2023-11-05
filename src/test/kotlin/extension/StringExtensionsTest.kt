package extension

import PAPIPluginTest
import io.github.tsgrissom.pluginapi.extension.kt.*
import org.bukkit.ChatColor
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class StringExtensionsTest : PAPIPluginTest() {

    @DisplayName("Does String#dequoted when passed a quoted String not equal the original String?")
    @Test
    fun doesDequotedQuotedStrNeqOriginalQuotedStr() {
        val cases = arrayOf(
            "\"Some text within quotes\"",
            "'Some more text within quotes'"
        )
        cases.forEach { assertNotEquals(it.dequoted(), it) }
    }

    @DisplayName("Does String#dequoted when passed a non-quoted String equal the original String?")
    @Test
    fun doesDequotedNonQuotedStrEqOriginalStr() {
        val cases = arrayOf(
            "Some text within quotes\"",
            "\"Some text with a leading quote",
            "'Some more text within quotes",
            "Some text with a trailing apostrophe'"
        )
        cases.forEach { assertEquals(it.dequoted(), it) }
    }

    @DisplayName("Does a non-percentage String value fail to match the percentage regular expression?")
    @Test
    fun doesNonPercentStrNotMatchRegex() {
        val cases = arrayOf("10", "%10", "10 %")
        cases.forEach { assertFalse(it.isPercentage()) }
    }

    @DisplayName("Does a Percentage as a String Value Succeed in Matching the Percentage Regular Expression")
    @Test
    fun doesPercentStrMatchRegex() {
        val cases = arrayOf("10%", "0.01%")
        cases.forEach { assertTrue(it.isPercentage()) }
    }

    @DisplayName("Do various forms of chat color expressed in Strings all contain ChatColor?")
    @Suppress("DEPRECATION")
    @Test
    fun doesStrWithMixedColorTypesContainsChatColor() {
        val cases = arrayOf(
            "&aText with untranslated color codes",
            "&bSome text with translated color codes".translateColor(),
            "${ChatColor.RED}Text with interpolated ChatColor enumeration"
        )
        cases.forEach { assertTrue(it.containsChatColor()) }
    }

    @DisplayName("Does String prepended with an untranslated color code then String#translateAndStripColorCodes equal the original String?")
    @Test
    fun doesStrPrependedWithUntranslatedColorCodesThenTranslatedAndStrippedEqOriginalStr() {
        val original = "Some text that will have a chat color prepended"
        val pre = "&a&l"
        val combinedThenTranslatedAndStripped = "$pre$original".translateAndStripColorCodes()
        assertEquals(combinedThenTranslatedAndStripped, original)
    }

    @DisplayName("Does String consisting of only color codes and ChatColors passed to String#isOnlyColorCodes equal true?")
    @Suppress("DEPRECATION")
    @Test
    fun doesStrConsistingOfOnlyColorCodesMatchIsOnlyColorCodes() {
        val cases = arrayOf(
            "&b&l&m",
            ChatColor.RED.toString(),
            ChatColor.GREEN.toString() + ChatColor.BOLD.toString()
        )
        cases.forEach { assertTrue(it.isOnlyColorCodes()) }
    }

    @DisplayName("Does String consisting of both ChatColors and text passed to String#isOnlyColorCodes equal false?")
    @Suppress("DEPRECATION")
    @Test
    fun doesSubstantialStrWithColorNotMatchIsOnlyCodes() {
        val cases = arrayOf(
            "&bThis is a colored string with substance",
            "${ChatColor.RED}This is another string"
        )
        cases.forEach { assertFalse(it.isOnlyColorCodes()) }
    }

    @DisplayName("Does String#startsAndEndsWithSameChar(ignoreCase) when passed mixed-capitalization leading and trailing character palindromes always equal true?")
    @Test
    fun isStartsAndEndsIcTruthyOnPalindromeWithMixedCapitalization() {
        val cases = arrayOf("civiC", "Madam", "leveL")
        cases.forEach {
            assertTrue(it.startsAndEndsWithSameChar(ignoreCase=true))
        }
    }

    @DisplayName("Does String#resolveChatColor when passed single-character color codes not equal null?")
    @Test
    fun doesResolveChatColorSucceedOnSingleCharColorCodes() {
        val cases = "0123456789abcdef"
        for (char in cases) {
            assertNotNull("$char".resolveChatColor())
        }
    }

    @DisplayName("Does String#resolveChatColor when passed invalid single-character color codes equal null?")
    @Test
    fun doesResolveChatColorFailOnInvalidSingleCharColorCodes() {
        val cases = "ghijp"
        for (char in cases) {
            assertNull("$char".resolveChatColor())
        }
    }

    @DisplayName("Does String#resolveChatColor when passed qualified input color codes not equal null?")
    @Test
    fun doesResolveChatColorSucceedOnQualifiedColorCodes() {
        val cases = arrayOf("&a", "&l", "§b", "§k")
        cases.forEach {
            assertNotNull(it.resolveChatColor())
        }
    }

    @DisplayName("Does String#resolveChatColor when passed invalid qualified input color codes equal null?")
    @Test
    fun doesResolveChatColorFailOnInvalidQualifiedColorCodes() {
        val cases = arrayOf("&g", "&h", "§i", "§j")
        cases.forEach {
            assertNull(it.resolveChatColor())
        }
    }
}