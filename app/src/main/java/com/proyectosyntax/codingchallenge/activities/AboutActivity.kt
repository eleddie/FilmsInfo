package com.proyectosyntax.codingchallenge.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.proyectosyntax.codingchallenge.R
import android.content.Intent
import android.net.Uri
import kotlinx.android.synthetic.main.activity_about.*


class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(toolbarAbout)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun onIconClick(view: View) {
        when (view.id) {
            R.id.fabmail -> sendEmail()
            R.id.fabphone -> makeCall()
            R.id.fablinkedin -> openLinkedin()
        }
    }

    fun sendEmail() {
        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf("snchz26@gmail.com"))
        email.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.hire_you))
        email.type = "message/rfc822"
        startActivity(email)
    }

    fun makeCall() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:+584144193948")
        startActivity(intent)
    }

    fun openLinkedin() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/eduardosanchezg/"))
        startActivity(browserIntent)
    }
}
