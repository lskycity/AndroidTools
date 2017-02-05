package com.lskycity.androidtools;

import android.util.Pair;

/**
 * Created by zhaofliu on 1/29/17.
 *
 * @author zhaofliu
 * @since 1/29/17
 */

public class InfoBin{
    public String name;
    public String value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InfoBin bin = (InfoBin) o;

        if (name != null ? !name.equals(bin.name) : bin.name != null) return false;
        return value != null ? value.equals(bin.value) : bin.value == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InfoBin{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}' +"\n";
    }
}
