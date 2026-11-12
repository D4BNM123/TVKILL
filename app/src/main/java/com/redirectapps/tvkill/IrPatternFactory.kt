package com.redirectapps.tvkill

/**
 * Converts decoded Flipper-IRDB protocol records into the timing units used by
 * Pattern.java. Pattern.java converts these carrier-cycle units back to
 * microseconds for Android ConsumerIrManager.
 *
 * Protocol layouts follow Flipper Zero's public infrared encoder definitions.
 */
object IrPatternFactory {
    private fun pattern(frequency: Int, timingsUs: List<Int>): Pattern {
        val values = IntArray(timingsUs.size + 2)
        values[0] = 1
        values[1] = 1
        for (i in timingsUs.indices) {
            values[i + 2] = Math.round(timingsUs[i] * frequency / 1000000.0).toInt()
        }
        return Pattern(frequency, values)
    }

    private fun appendBits(out: MutableList<Int>, value: Long, count: Int, mark: Int, zero: Int, one: Int, space: Int) {
        for (bit in 0 until count) {
            out.add(mark)
            out.add(if (((value shr bit) and 1L) != 0L) one else zero)
        }
    }

    /** NEC, 8-bit address + inverted address + 8-bit command + inverted command. */
    fun nec(address: Int, command: Int): Pattern {
        val out = arrayListOf(9000, 4500)
        val bytes = intArrayOf(address and 0xFF, (address.inv()) and 0xFF, command and 0xFF, command.inv() and 0xFF)
        for (byte in bytes) appendBits(out, byte.toLong(), 8, 560, 560, 1690, 560)
        out.add(560)
        out.add(40000)
        return pattern(38000, out)
    }

    /** NEC extended, 16-bit address and 16-bit command, little-endian bytes. */
    fun necExt(addressLow: Int, addressHigh: Int, commandLow: Int, commandHigh: Int): Pattern {
        val out = arrayListOf(9000, 4500)
        val bytes = intArrayOf(addressLow, addressHigh, commandLow, commandHigh)
        for (byte in bytes) appendBits(out, (byte and 0xFF).toLong(), 8, 560, 560, 1690, 560)
        out.add(560)
        out.add(40000)
        return pattern(38000, out)
    }

    /** Samsung32, address byte repeated, followed by command and inverted command. */
    fun samsung32(address: Int, command: Int): Pattern {
        val out = arrayListOf(4500, 4500)
        val bytes = intArrayOf(address and 0xFF, address and 0xFF, command and 0xFF, command.inv() and 0xFF)
        for (byte in bytes) appendBits(out, byte.toLong(), 8, 560, 560, 1690, 560)
        out.add(560)
        out.add(40000)
        return pattern(38000, out)
    }

    /** Sony SIRC 12-bit: 7 command bits followed by 5 address bits, LSB first. */
    fun sirc12(address: Int, command: Int): Pattern {
        val out = arrayListOf(2400, 600)
        appendBits(out, command.toLong(), 7, 600, 600, 1200, 600)
        appendBits(out, address.toLong(), 5, 600, 600, 1200, 600)
        out.add(45000)
        return pattern(40000, out)
    }

    /** RCA: 4-bit address, 8-bit command, inverted address, inverted command. */
    fun rca(address: Int, command: Int): Pattern {
        val a = address and 0x0F
        val c = command and 0xFF
        val word = a.toLong() or (c.toLong() shl 4) or
            (((a.inv()) and 0x0F).toLong() shl 12) or
            (((c.inv()) and 0xFF).toLong() shl 16)
        val out = arrayListOf(4000, 4000)
        appendBits(out, word, 24, 500, 1000, 2000, 1000)
        out.add(40000)
        return pattern(38000, out)
    }
}
