package com.mironov.hw1.activity

import android.Manifest
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mironov.hw1.R
import com.mironov.hw1.databinding.SecondActivityBinding
import com.mironov.hw1.model.Contact
import com.mironov.hw1.service.PickContactService

class SecondActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_CONTACTS = "contacts"

        const val EXTRA_PERCENT = "percent"

        const val ACTION_LOADING = "loading"

        const val ACTION_LOADED = "loaded"

        fun newIntent(context: Context) = Intent(context, SecondActivity::class.java)
    }

    private val localBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(this)
    }

    private lateinit var binding: SecondActivityBinding

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                ACTION_LOADING -> {
                    val percent = intent.getIntExtra(EXTRA_PERCENT, 0)
                    binding.progressBar.progress = percent
                }

                ACTION_LOADED -> {
                    val contacts =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            intent.getParcelableArrayListExtra(EXTRA_CONTACTS, Contact::class.java)
                                ?: arrayListOf()
                        } else {
                            intent.getParcelableArrayListExtra(EXTRA_CONTACTS) ?: arrayListOf()
                        }
                    saveContacts(contacts)
                }
            }
        }
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _->
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SecondActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentFilter = IntentFilter().apply {
            addAction(ACTION_LOADING)
            addAction(ACTION_LOADED)
        }
        localBroadcastManager.registerReceiver(receiver, intentFilter)
        binding.btn.setOnClickListener {
            startService()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        localBroadcastManager.unregisterReceiver(receiver)
    }

    private fun requestContactsPermission() {
        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            requestPermission.launch(Manifest.permission.READ_CONTACTS)
        } else {
            showContactsPermissionRationale()
        }
    }

    private fun showContactsPermissionRationale() {
        AlertDialog.Builder(this)
            .setTitle(R.string.permission_title)
            .setMessage(R.string.permission_description)
            .setPositiveButton(R.string.alert_button) { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )
                startActivity(intent)
            }
            .show()
    }

    private fun startService() {
        val permissionGranted = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
        if (permissionGranted) {
            Intent(this, PickContactService::class.java).apply {
                startService(this)
            }
            binding.btn.isEnabled = false
            binding.progressBar.isVisible = true
        } else {
            requestContactsPermission()
        }
    }

    private fun saveContacts(contacts: ArrayList<Contact>) {
        Intent().apply {
            putParcelableArrayListExtra(EXTRA_CONTACTS, contacts)
            setResult(RESULT_OK, this)
        }
        finish()
    }
}