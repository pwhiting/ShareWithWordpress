package com.example.sharewithwordpress

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import org.jsoup.Jsoup

class SharePhotoActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent?.action == "android.intent.action.SEND") {
            val myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val photoURL = intent.clipData?.getItemAt(0)?.coerceToText(this) ?: return
            if (photoPageUrlRegex.matches(photoURL)) {
                StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
                val page = Jsoup.connect(photoURL.toString()).get()
                var embedString: String? = null
                when (page.selectFirst(GOOGLE_CONTENT_TYPE).attr(CONTENT).toString()) {
                    "video" ->
                        embedString=makeVideoEmbed(page.selectFirst(CSS_VIDEO).attr(CONTENT).toString())
                    "google_photos:single_photo" ->
                        embedString=makePhotoEmbed(page.selectFirst(CSS_PHOTO).attr(CONTENT).toString())
                    "google_photos:photo_album" ->
                        embedString=makeAlbumEmbed(photoURL.toString())
                }
                if(embedString != null) {
                    myClipboard?.primaryClip = ClipData.newPlainText(":text", embedString)
                    startActivity(packageManager.getLaunchIntentForPackage(WORDPRESS))
                }
            } else {
                Toast.makeText(this, "can't parse $photoURL", Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }

    private fun makeAlbumEmbed(url: String) = """[embed-google-photos-album link="$url"]"""
    private fun makePhotoEmbed(url: String): String {
        val (lessBigURL) = photoUrlRegex.find(url)!!.destructured
        return """[img src="$lessBigURL"]"""
    }
    private fun makeVideoEmbed(url: String) = """<div><video controls><source src=$url type="video/mp4"></video></div>"""

    companion object {
        val CONTENT = "content"
        val GOOGLE_CONTENT_TYPE = """meta[property=og:type]"""
        val CSS_VIDEO = """meta[property=og:video][content^=https://lh3]"""
        val CSS_PHOTO = """meta[property=og:image][content^=https://lh3]"""
        val WORDPRESS = "org.wordpress.android"
        val photoPageUrlRegex = "https://photos.app.goo.gl/.*".toRegex()
        val photoUrlRegex = "(https://[^=]*)".toRegex()
    }
}


