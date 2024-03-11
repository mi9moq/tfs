package com.mironov.hw1.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.ContactsContract
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.mironov.hw1.activity.SecondActivity
import com.mironov.hw1.model.Contact
import kotlin.concurrent.thread

class PickContactService : Service() {

    private val localBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val contacts = loadContacts()
        thread {
            for (i in 1..10) {
                Thread.sleep(250)
                Intent(SecondActivity.ACTION_LOADING).apply {
                    putExtra(SecondActivity.EXTRA_PERCENT, i * 10)
                    localBroadcastManager.sendBroadcast(this)
                }
            }
            Intent(SecondActivity.ACTION_LOADED).apply {
                putParcelableArrayListExtra(SecondActivity.EXTRA_CONTACTS, contacts)
                localBroadcastManager.sendBroadcast(this)
            }
        }
        stopSelf()
        return START_NOT_STICKY
    }

    private fun loadContacts(): ArrayList<Contact> {

        val contacts = mutableListOf<Contact>()

        val cursor = application.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        while (cursor?.moveToNext() == true) {
            val contactName = cursor.getString(
                cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)
            )

            val phoneIndex = cursor.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.NUMBER
            )

            val phoneNumber = cursor.getString(phoneIndex)

            val contact = Contact(
                phone = phoneNumber,
                name = contactName
            )

            contacts.add(contact)
        }

        cursor?.close()
        return ArrayList(contacts)
    }
}