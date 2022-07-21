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
import com.example.weather.utils.REQUEST_CODES
import com.example.weather.view.weatherlist.CitiesListFragment

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
        if (permission == PackageManager.PERMISSION_GRANTED) {
            getContacts()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            AlertDialog.Builder(requireContext())
                .setTitle("Доступ к контактам и звонкам")
                .setMessage(R.string.message)
                .setPositiveButton("Дать") { _, _ ->
                    permissionRequest()
                }
                .setNegativeButton("Не дать") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        } else {
            permissionRequest()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODES) {
            permissions.forEachIndexed { index, _ ->
                if (permissions[index] == Manifest.permission.READ_CONTACTS
                    && grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                    getContacts()
                } else if (permissions[index] == Manifest.permission.READ_CONTACTS
                    && grantResults[index] == PackageManager.PERMISSION_DENIED) {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, CitiesListFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun permissionRequest() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS,Manifest.permission.CALL_PHONE), REQUEST_CODES)
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
                    setOnClickListener { startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:${number}"))) }
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