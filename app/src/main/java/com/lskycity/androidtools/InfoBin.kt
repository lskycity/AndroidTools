package com.lskycity.androidtools

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by zhaofliu on 1/29/17.
 *
 * @author zhaofliu
 * @since 1/29/17
 */

data class InfoBin(var name: String?, var value: String?) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString(), parcel.readString())
    constructor() : this("", "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InfoBin> {
        override fun createFromParcel(parcel: Parcel): InfoBin {
            return InfoBin(parcel)
        }

        override fun newArray(size: Int): Array<InfoBin?> {
            return arrayOfNulls(size)
        }
    }
}
