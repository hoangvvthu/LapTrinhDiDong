package com.example.baitap07

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class BluetoothMainActivity : AppCompatActivity() {

    private lateinit var btnPaired: Button
    private lateinit var btnGiaLap: Button
    private lateinit var listDanhSach: ListView
    private var myBluetooth: BluetoothAdapter? = null
    private var pairedDevices: Set<BluetoothDevice>? = null

    companion object {
        const val REQUEST_BLUETOOTH_ENABLE = 1
        const val EXTRA_ADDRESS = "device_address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_main)

        btnPaired = findViewById(R.id.btnTimthietbi)
        btnGiaLap = findViewById(R.id.btnGiaLap)
        listDanhSach = findViewById(R.id.listTb)

        myBluetooth = BluetoothAdapter.getDefaultAdapter()

        btnPaired.setOnClickListener {
            if (myBluetooth == null) {
                Toast.makeText(this, "Thiết bị không hỗ trợ Bluetooth", Toast.LENGTH_SHORT).show()
            } else {
                checkPermissionsAndSearch()
            }
        }

        // Mở màn hình Giả lập Socket (Không cần Bluetooth thật)
        btnGiaLap.setOnClickListener {
            val intent = Intent(this, SocketActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkPermissionsAndSearch() {
        val permissions = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
        } else {
            permissions.add(Manifest.permission.BLUETOOTH)
            permissions.add(Manifest.permission.BLUETOOTH_ADMIN)
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        val missingPermissions = permissions.filter {
            ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isEmpty()) {
            if (!myBluetooth!!.isEnabled) {
                val turnBTon = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(turnBTon, REQUEST_BLUETOOTH_ENABLE)
            } else {
                pairedDevicesList()
            }
        } else {
            requestPermissionLauncher.launch(missingPermissions.toTypedArray())
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.entries.all { it.value }) pairedDevicesList()
    }

    @SuppressLint("MissingPermission")
    private fun pairedDevicesList() {
        pairedDevices = myBluetooth?.bondedDevices
        val list = ArrayList<String>()
        if (pairedDevices != null && pairedDevices!!.isNotEmpty()) {
            for (bt in pairedDevices!!) {
                list.add(bt.name + "\n" + bt.address)
            }
        } else {
            Toast.makeText(this, "Không tìm thấy thiết bị đã ghép đôi.", Toast.LENGTH_LONG).show()
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        listDanhSach.adapter = adapter
        listDanhSach.onItemClickListener = myListClickListener
    }

    private val myListClickListener = AdapterView.OnItemClickListener { _, v, _, _ ->
        val info = (v as TextView).text.toString()
        val address = info.substring(info.length - 17)
        val i = Intent(this, BlueControlActivity::class.java)
        i.putExtra(EXTRA_ADDRESS, address)
        startActivity(i)
    }
}
