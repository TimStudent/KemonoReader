package com.example.kemonoreaderv2

data class Booleans(
    val jpgState: Boolean,
    val pngState: Boolean,
    val mp4State: Boolean,
    val zipState: Boolean,
    val links: MutableList<String>
)
