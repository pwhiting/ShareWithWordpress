package com.example.sharewithwordpress

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import org.jsoup.Jsoup


class SharePhotoActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent?.action == "android.intent.action.SEND") {
            val myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val photoURL = intent.clipData?.getItemAt(0)?.coerceToText(this) ?: return
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
            val element=Jsoup.connect(photoURL.toString()).get().selectFirst("""[content*="https://lh3"]""")
            val bigURL=element.attr("content").toString()
            myClipboard?.primaryClip = ClipData.newPlainText(":text", shortcodeString(bigURL))
            startActivity(packageManager.getLaunchIntentForPackage("org.wordpress.android"))
        }
        finish()
    }
    private fun shortcodeString(url: String) = """[img src="$url"]"""
}


