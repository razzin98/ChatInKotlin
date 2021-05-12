package com.example.aleczad.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aleczad.R
import com.example.aleczad.ui.ui.shoutbox_II.ShoutboxIIFragment

class Shoutbox_II : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shoutbox__i_i_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ShoutboxIIFragment.newInstance())
                .commitNow()
        }
    }
}
