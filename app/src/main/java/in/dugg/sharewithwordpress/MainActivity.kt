package `in`.dugg.sharewithwordpress

import android.support.v7.app.AppCompatActivity
import android.os.Bundle


// adb shell am start com.example.ShareWithWordpress/.SimpleActivity --es "Message" "hello!"


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}
