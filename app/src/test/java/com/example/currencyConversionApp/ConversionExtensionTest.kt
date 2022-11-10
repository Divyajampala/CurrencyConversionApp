package com.example.currencyConversionApp

import android.content.Context
import com.nmc.myapplication.extension.UsdBaseToOtherCurrency
import org.junit.Test
import io.mockk.MockKAnnotations
import org.junit.Assert.*
import io.mockk.mockk
import org.junit.Before

/**
 * ContextExtension.kt Test cases.
 *
 */
class ConversionExtensionTest {

    @Test
    fun UsdBaseToOtherCurrencyTest() {
        var map = HashMap<String, Double>()
        map.put("USB", 1.0)
        map.put("ASB", 6.0)
        map.put("BSB", 4.0)
        map.put("CSB", 3.0)
        map.put("DSB", 4.0)
        map.put("ESB", 7.0)

        var expectedResult = HashMap<String, Double>()
        expectedResult.put("ASB", 10.0)
        expectedResult.put("BSB", 6.67)
        expectedResult.put("DSB", 6.67)
        expectedResult.put("CSB", 5.0)
        expectedResult.put("USB", 1.67)
        expectedResult.put("ESB", 11.67)
        assertEquals(expectedResult, UsdBaseToOtherCurrency(10.0, map, "ASB"))
    }
}