package com.example.sharewithwordpress

object Shared {
    val CSS = """[content*="https://lh3.googleusercontent.com"]"""
    val WORDPRESS = "org.wordpress.android"
    val photoPageUrlRegex = "https://photos.app.goo.gl/.*".toRegex()
    val photoUrlRegex = "(https://[^=]*)".toRegex()
}