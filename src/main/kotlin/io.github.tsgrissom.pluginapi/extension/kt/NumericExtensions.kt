package io.github.tsgrissom.pluginapi.extension.kt

/**
 * Formats the Double to have the requisite number of fractional digits trailing the decimal separator.
 * @param n The number of fractional digits allowed to trail the decimal separator.
 * @return A new Double containing the requisite number of fractional digits trailing the decimal separator.
 */
fun Double.roundToDigits(n: Int) : Double =
    String.format("%.${n}f", this).toDouble()