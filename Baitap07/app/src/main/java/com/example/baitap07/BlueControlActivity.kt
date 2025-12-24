package com.example.baitap07

import android.Manifest
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.util.UUID
import kotlin.concurrent.thread

class BlueControlActivity : AppCompatActivity() {

    private lateinit var btnTb1: ImageButton
    private lateinit var btnTb2: ImageButton
    private lateinit var btnDis: ImageButton
    private lateinit var txt1: TextView
    private lateinit var txtMAC: TextView

    private var myBluetooth: BluetoothAdapter? = null
    private var btSocket: BluetoothSocket? = null
    private var isBtConnected = false
    private var address: String? = null
    private var progress: ProgressDialog? = null

    companion object {
        // UUID chuẩn cho kết nối SPP (Serial Port Profile)
        val myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        address = intent.getStringExtra(BluetoothMainActivity.EXTRA_ADDRESS)
        setContentView(R.layout.activity_control)

        btnTb1 = findViewById(R.id.btnTb1)
        btnTb2 = findViewById(R.id.btnTb2)
        txt1 = findViewById(R.id.textV1)
        txtMAC = findViewById(R.id.textViewMAC)
        btnDis = findViewById(R.id.btnDisc)

        txtMAC.text = address

        connectBT()

        btnTb1.setOnClickListener { thietTbi1() }
        btnTb2.setOnClickListener { thietTbi2() }
        btnDis.setOnClickListener { disconnect() }
    }

    private fun connectBT() {
        progress = ProgressDialog.show(this, "Đang kết nối...", "Xin vui lòng đợi!!!")
        thread {
            var connectSuccess = true
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter()
                    val dispositivo = myBluetooth?.getRemoteDevice(address)
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                        btSocket = dispositivo?.createInsecureRfcommSocketToServiceRecord(myUUID)
                        BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                        btSocket?.connect()
                    }
                }
            } catch (e: IOException) {
                connectSuccess = false
            }

            runOnUiThread {
                if (!connectSuccess) {
                    msg("Kết nối thất bại ! Kiểm tra thiết bị.")
                    finish()
                } else {
                    msg("Kết nối thành công.")
                    isBtConnected = true
                }
                progress?.dismiss()
            }
        }
    }

    private fun thietTbi1() {
        sendSignal("1", "A", 1, btnTb1, "Thiết bị 1")
    }

    private fun thietTbi2() {
        // Sửa lại lệnh '2' và 'B' cho khớp với code Arduino
        sendSignal("2", "B", 2, btnTb2, "Thiết bị 2")
    }

    private fun sendSignal(onCmd: String, offCmd: String, deviceNum: Int, button: ImageButton, name: String) {
        if (btSocket != null) {
            try {
                val isCurrentlyOn = if (deviceNum == 1) flaglamp1 == 1 else flaglamp2 == 1
                if (!isCurrentlyOn) {
                    if (deviceNum == 1) flaglamp1 = 1 else flaglamp2 = 1
                    button.setBackgroundResource(android.R.drawable.btn_star_big_on)
                    btSocket?.outputStream?.write(onCmd.toByteArray())
                    txt1.text = "$name đang bật"
                } else {
                    if (deviceNum == 1) flaglamp1 = 0 else flaglamp2 = 0
                    button.setBackgroundResource(android.R.drawable.btn_star_big_off)
                    btSocket?.outputStream?.write(offCmd.toByteArray())
                    txt1.text = "$name đang tắt"
                }
            } catch (e: IOException) {
                msg("Lỗi gửi lệnh")
            }
        }
    }

    private var flaglamp1 = 0
    private var flaglamp2 = 0

    private fun disconnect() {
        try { btSocket?.close() } catch (e: IOException) { }
        finish()
    }

    private fun msg(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    }
}
