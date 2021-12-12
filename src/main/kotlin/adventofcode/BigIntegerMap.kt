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

    fun inc(k: KEY): BigIntegerMap<KEY> {
        add(k, BigInteger.ONE)
        return this
    }

    fun dec(k: KEY): BigIntegerMap<KEY> {
        add(k, -BigInteger.ONE)
        return this
    }

    fun copyOf(): BigIntegerMap<KEY> {
        val new = BigIntegerMap<KEY>(this.default)
        new.putAll(this)
        return new
    }

    override fun get(key: KEY): BigInteger {
        val value = super.get(key)
        if (value == null) {
            return default
        }
        return value
    }


}
