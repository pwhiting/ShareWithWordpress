package `in`.dugg.sharewithwordpress

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.coroutines.*
import org.jsoup.Jsoup

class SharePhotoActivity : AppCompatActivity(),CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildShortcode()
    }

    private fun buildShortcode () = launch {
        if (intent?.action != INTENT_TRIGGER) return@launch
        val myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
        val url = intent.clipData?.getItemAt(0)?.coerceToText(this@SharePhotoActivity)?.toString() ?: return@launch
        if (!url.startsWith(PHOTO_PAGE_URL, true)) {
            Toast.makeText(this@SharePhotoActivity, "can't parse $url", Toast.LENGTH_SHORT).show()
            return@launch
        }
        val page = withContext(Dispatchers.IO) { Jsoup.connect(url).get() }
        val shortcode = when (page.selectFirst(GOOGLE_CONTENT_TYPE).attr(CONTENT_ATTR).toString()) {
            ALBUM -> embed("album", page.selectFirst(CSS_PHOTO).attr(CONTENT_ATTR).toString().substringBefore("="),url)
            VIDEO -> embed("video", page.selectFirst(CSS_VIDEO).attr(CONTENT_ATTR).toString(), url)
            PHOTO -> embed("photo", page.selectFirst(CSS_PHOTO).attr(CONTENT_ATTR).toString().substringBefore("="),url)
            else -> return@launch
        }
        myClipboard?.primaryClip = ClipData.newPlainText(":text", shortcode)
        startActivity(packageManager.getLaunchIntentForPackage(WORDPRESS))
        finish()
    }

    companion object {
        const val CONTENT_ATTR = "content"
        const val GOOGLE_CONTENT_TYPE = """meta[property=og:type]"""
        const val CSS_VIDEO = """meta[property=og:video][content^=https://lh3]"""
        const val CSS_PHOTO = """meta[property=og:image][content^=https://lh3]"""
        const val WORDPRESS = "org.wordpress.android"
        const val INTENT_TRIGGER =  "android.intent.action.SEND"
        const val PHOTO_PAGE_URL = "https://photos.app.goo.gl/"
        const val VIDEO = "video"
        const val ALBUM = "google_photos:photo_album"
        const val PHOTO = "google_photos:single_photo"
    }

    private fun embed(type: String, src: String, url: String) = """[embed-google-$type src="$src" click="$url"]"""
    private fun embed(type: String, src: String) = """[embed-google-$type src="$src"]"""

}


