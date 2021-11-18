package com.bertalandancs.otpflickrsearcher.ui.main.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ThumbnailImage(val id: Long, val url: String) : Parcelable {

    override fun toString(): String {
        return "ThumbnailImage(id=$id)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ThumbnailImage

        if (id != other.id) return false
        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + url.hashCode()
        return result
    }
}
