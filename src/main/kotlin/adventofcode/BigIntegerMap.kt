package adventofcode

import java.math.BigInteger

class BigIntegerMap<KEY>(val default: BigInteger = BigInteger.ZERO): HashMap<KEY, BigInteger>() {

    fun add(k: KEY, value: BigInteger) {
        compute(k) {_, v ->
            if (v == null) {
                default + value
            } else {
                v + value
            }
        }
    }

    fun inc(k: KEY) {
        add(k, BigInteger.ONE)
    }

    fun dec(k: KEY) {
        add(k, -BigInteger.ONE)
    }

    override fun get(key: KEY): BigInteger {
        val value = super.get(key)
        if (value == null) {
            return default
        }
        return value
    }


}
