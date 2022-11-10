package com.nmc.myapplication.extension

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Currency conversion utility function.
 */
fun UsdBaseToOtherCurrency(
    amount: Double,
    map: Map<String, Double>,
    base: String
): Map<String, Double> {
    var ans = map.filter { base.equals(it.key) }[base]
    var result = HashMap<String, Double>();
    ans?.let {
        map.entries.forEach {
            var bigDecimal = BigDecimal(amount * it.value / ans)
            result.put(it.key, bigDecimal.setScale(2, RoundingMode.HALF_UP).toDouble())
        }
    }
    return result
}