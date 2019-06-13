package com.example.sharewithwordpress

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast


// adb shell am start -W -a android.intent.action.SEND -d "https://photos.app.goo.gl/ikgeQY3HHQ9tVbja6"

class ShareAlbumActivity :AppCompatActivity() {

    
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        //if (intent == null) return
//        if (intent?.action == "android.intent.action.SEND") {
//            val myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
//            //val photoURL = intent.data ?: return
//            val photoURL =intent.clipData?.getItemAt(0)?.coerceToText(this)?: return
//            if(photoPageUrlRegex.matches(photoURL)) {
//                myClipboard?.primaryClip = ClipData.newPlainText(":text", shortcodeString(photoURL.toString()))
//                Toast.makeText(this, "shortcode created", Toast.LENGTH_SHORT).show()
//                startActivity(packageManager.getLaunchIntentForPackage(Shared.WORDPRESS))
//            } else {
//                Toast.makeText(this, "can't parse $photoURL", Toast.LENGTH_SHORT).show()
//            }
//        }
//        finish()
//    }
//    private fun shortcodeString(url: String) = """[embed-google-photos-album link="$url"]"""
}