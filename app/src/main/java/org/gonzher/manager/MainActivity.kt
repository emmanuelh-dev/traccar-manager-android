package org.gonzher.manager

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.WebViewFragment
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager

class MainActivity : AppCompatActivity() {

    var pendingEventId: Long? = null

    private fun updateEventId(intent: Intent?) {
        intent?.getStringExtra("eventId")?.let { pendingEventId = it.toLongOrNull() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateEventId(intent)

        if (savedInstanceState == null) {
            initContent()
        }
    }

    private fun initContent() {
        // Simplemente iniciamos MainFragment directamente ya que la URL por defecto
        // estará definida en los recursos de la app
        fragmentManager.beginTransaction()
            .add(android.R.id.content, MainFragment())
            .commit()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        updateEventId(intent)
        LocalBroadcastManager.getInstance(this)
            .sendBroadcast(Intent(MainFragment.EVENT_EVENT))
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        fragmentManager.findFragmentById(android.R.id.content)
            ?.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        val fragment = fragmentManager.findFragmentById(android.R.id.content) as? WebViewFragment
        if (fragment?.webView?.canGoBack() == true) {
            fragment.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}