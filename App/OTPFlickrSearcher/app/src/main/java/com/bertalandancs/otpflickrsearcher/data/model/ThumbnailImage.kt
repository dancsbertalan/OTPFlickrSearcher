package com.bertalandancs.otpflickrsearcher.data.model

data class ThumbnailImage(
    val serverId: Int,
    val photoId: Int,
    val secret: String,
    val size: Char = 'm'
) {
    val url: String
        get() {
            return "https://live.staticflickr.com/$serverId/${photoId}_${secret}_$size.jpg"
        }
}