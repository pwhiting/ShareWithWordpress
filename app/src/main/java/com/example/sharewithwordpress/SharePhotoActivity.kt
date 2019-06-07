package com.example.sharewithwordpress

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import org.jsoup.Jsoup

class SharePhotoActivity : AppCompatActivity() {


    private fun shortcodeString(url: String) = """[img src="$url"]"""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent?.action == "android.intent.action.SEND") {
            val myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val photoURL = intent.clipData?.getItemAt(0)?.coerceToText(this) ?: return
            if (Shared.photoPageUrlRegex.matches(photoURL)) {
                StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
                val element = Jsoup.connect(photoURL.toString()).get().selectFirst(Shared.CSS)
                val bigURL = element.attr("content").toString()
                val (lessBigURL) = Shared.photoUrlRegex.find(bigURL)!!.destructured
                myClipboard?.primaryClip = ClipData.newPlainText(":text", shortcodeString(lessBigURL))
                startActivity(packageManager.getLaunchIntentForPackage(Shared.WORDPRESS))
            } else {
                Toast.makeText(this, "can't parse $photoURL", Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }

}


