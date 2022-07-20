package com.example.weather.view.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.databinding.FragmentContactsBinding
import com.example.weather.utils.REQUEST_CODE_READ_CONTACTS

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding: FragmentContactsBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission() {
        val permission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
        PackageManager.PERMISSION_GRANTED
        if (permission == PackageManager.PERMISSION_GRANTED) {
            getContacts()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            AlertDialog.Builder(requireContext())
                .setTitle("Доступ к контактам")
                .setMessage(R.string.zadolbat)
                .setPositiveButton("Дать задолбать") { _, _ ->
                    permissionRequest(Manifest.permission.READ_CONTACTS)
                }
                .setNegativeButton("Не дать задолбать") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        } else {
            permissionRequest(Manifest.permission.READ_CONTACTS)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
            permissions.forEachIndexed { index, _ ->
                if (permissions[index] == Manifest.permission.READ_CONTACTS
                    && grantResults[index] == PackageManager.PERMISSION_GRANTED
                ) {
                    getContacts()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun permissionRequest(permission: String) {
        requestPermissions(arrayOf(permission), REQUEST_CODE_READ_CONTACTS)
    }

    @SuppressLint("Range")
    private fun getContacts() {
        val cursorWithContacts = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursorWithContacts?.let {
            for (i in 0 until it.count) {
                it.moveToPosition(i)
                val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val number =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                binding.linerContainer.addView(TextView(requireContext()).apply {
                    val nameAndNumber = "$name - $number"
                    text = nameAndNumber
                    textSize = 24F
                    setOnClickListener { startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${number}"))) }
                })
            }
        }
        cursorWithContacts?.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}