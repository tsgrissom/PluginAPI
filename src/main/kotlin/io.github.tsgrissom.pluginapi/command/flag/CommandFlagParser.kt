package io.github.tsgrissom.pluginapi.command.flag

import io.github.tsgrissom.pluginapi.extension.kt.equalsIc
import org.bukkit.Bukkit

/**
 * Should be able to accept any of the following argument combinations
 * command -fg
 * command --foo --goo
 */

private fun String.isInShortFlagFormat() : Boolean {
    return this != "-" && this.startsWith("-") && !this.startsWith("--")
}

private fun String.isInLongFlagFormat() : Boolean {
    return this.startsWith("--") && this != "--" && this != "-"
}

class CommandFlagParser(
    contextualArgs: Array<out String>,
    vararg validFlags: ValidCommandFlag
) {
    private val parsedFlags = mutableMapOf<String, Boolean>()
    private val unknownFlags = mutableSetOf<String>()

    fun wasPassed(qualifiedName: String) =
         parsedFlags[qualifiedName] ?: false

    fun wasPassed(flag: ValidCommandFlag) =
        parsedFlags[flag.qualifiedName] ?: false

    fun getParsedFlags() =
        this.parsedFlags
    fun getUnknownFlags() =
        this.unknownFlags
    fun hasUnknownFlags() =
        unknownFlags.isNotEmpty()
    fun getUnknownFlagsAsFormattedList() : String {
        if (!hasUnknownFlags())
            return "None"

        var str = String()

        for ((i, flag) in unknownFlags.withIndex()) {
            str += flag

            if (i != (unknownFlags.size - 1))
                str += ", "
        }

        return str
    }

    init {
        // Parse contextual arguments, validate for unknown or duplicate flags
        var shortFlagInput = String()
        val longFlagInput = mutableSetOf<String>()
        for (argument in contextualArgs) {
            if (argument.isInShortFlagFormat()) {
                val sans = argument.removePrefix("-")
                for (char in sans) {
                    var isValid = false
                    for (flag in validFlags) {
                        if (flag.getShortNameAsChar().equals(char, ignoreCase=false))
                            isValid = true
                    }

                    if (shortFlagInput.contains(char))
                        continue

                    if (!isValid) {
                        unknownFlags.add(char.toString())
                        continue
                    }

                    shortFlagInput += char
                }
            } else if (argument.isInLongFlagFormat()) {
                val sans = argument.removePrefix("--")

                var isValid = false
                for (flag in validFlags) {
                    if (flag.qualifiedName.equalsIc(sans))
                        isValid = true
                }

                if (!isValid) {
                    unknownFlags.add(sans)
                    continue
                }

                if (longFlagInput.contains(sans))
                    continue

                longFlagInput.add(sans)
            }
        }

        /*
         * For each valid flag we are looking for:
         * 1. Map it to false so that every flag has a default value
         * 2. If there are no contextual arguments, continue to next iteration
         * 3. If the assembled short flag input builder is not empty, check for the flag's short name as char, ignoring case
         *  - If this matched, notify of short flag being found and
         */
        for (flag in validFlags) {
            val qn = flag.qualifiedName

            parsedFlags[qn] = false

            if (contextualArgs.isEmpty())
                continue

            if (shortFlagInput.isNotEmpty() && shortFlagInput.contains(flag.getShortNameAsChar(), false)) {
                // Short flag detected
                parsedFlags[qn] = true
                continue
            }

            for (arg in contextualArgs) {
                if (!arg.startsWith("--"))
                    continue

                val sans = arg.removePrefix("--")
                if (qn.equalsIc(sans)) {
                    // Long flag detected
                    parsedFlags[qn] = true
                }
            }
        }
    }
}