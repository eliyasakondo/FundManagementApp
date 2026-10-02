package com.eliyas.fundmanagementapp.presentation.common

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {

    private val banglaDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')

    /**
     * Formats financial double amounts with currency symbol according to selected locale.
     * English: ৳1,500.00
     * Bangla: ৳১,৫০০.০০
     */
    fun format(amount: Double, isBangla: Boolean = true): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale.US)
        val formattedEnglish = formatter.format(amount).replace("$", "৳")

        return if (isBangla) {
            convertToBanglaDigits(formattedEnglish)
        } else {
            formattedEnglish
        }
    }

    /**
     * Converts ASCII digits in string to Bengali digits.
     */
    fun convertToBanglaDigits(input: String): String {
        val builder = StringBuilder()
        for (char in input) {
            if (char in '0'..'9') {
                builder.append(banglaDigits[char - '0'])
            } else {
                builder.append(char)
            }
        }
        return builder.toString()
    }
}
