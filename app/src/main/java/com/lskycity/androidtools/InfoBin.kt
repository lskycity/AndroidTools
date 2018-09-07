package com.lskycity.androidtools

import android.util.Pair

/**
 * Created by zhaofliu on 1/29/17.
 *
 * @author zhaofliu
 * @since 1/29/17
 */

class InfoBin {
    var name: String? = null
    var value: String? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val bin = o as InfoBin?

        if (if (name != null) name != bin!!.name else bin!!.name != null) return false
        return if (value != null) value == bin.value else bin.value == null

    }

    override fun hashCode(): Int {
        var result = if (name != null) name!!.hashCode() else 0
        result = 31 * result + if (value != null) value!!.hashCode() else 0
        return result
    }

    override fun toString(): String {
        return "InfoBin{" +
                "name='" + name + '\''.toString() +
                ", value='" + value + '\''.toString() +
                '}'.toString() + "\n"
    }
}
