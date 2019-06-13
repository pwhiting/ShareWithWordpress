package com.example.sharewithwordpress

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import org.jsoup.Jsoup

class SharePhotoActivity : AppCompatActivity() {

// should return below be replaced with finish()?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent?.action == INTENT_TRIGGER) {
            val myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val photoURL = intent.clipData?.getItemAt(0)?.coerceToText(this) ?: return
            if (photoURL.startsWith(PHOTO_PAGE_URL,true)) {
                StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
                val page = Jsoup.connect(photoURL.toString()).get()
                val embedString = when (page.selectFirst(GOOGLE_CONTENT_TYPE).attr(CONTENT_ATTR).toString()) {
                    "video" -> embedVideo(page.selectFirst(CSS_VIDEO).attr(CONTENT_ATTR).toString())
                    "google_photos:single_photo" -> embedPhoto(page.selectFirst(CSS_PHOTO).attr(CONTENT_ATTR).toString())
                    "google_photos:photo_album" -> embedAlbum(photoURL.toString())
                    else -> return
                }
                myClipboard?.primaryClip = ClipData.newPlainText(":text", embedString)
                startActivity(packageManager.getLaunchIntentForPackage(WORDPRESS))

            } else Toast.makeText(this, "can't parse $photoURL", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    private fun embedAlbum(url: String) = """[embed-google-photos-album link="$url"]"""
    private fun embedVideo(url: String) = """<div><video controls><source src=$url type="video/mp4"></video></div>"""
    private fun embedPhoto(url: String): String {
        val lessBigURL = url.substringBefore("=")
        return """[img src="$lessBigURL"]"""
    }

    companion object {
        const val CONTENT_ATTR = "content"
        const val GOOGLE_CONTENT_TYPE = """meta[property=og:type]"""
        const val CSS_VIDEO = """meta[property=og:video][content^=https://lh3]"""
        const val CSS_PHOTO = """meta[property=og:image][content^=https://lh3]"""
        const val WORDPRESS = "org.wordpress.android"
        const val INTENT_TRIGGER =  "android.intent.action.SEND"
        const val PHOTO_PAGE_URL = "https://photos.app.goo.gl/"
    }
}


