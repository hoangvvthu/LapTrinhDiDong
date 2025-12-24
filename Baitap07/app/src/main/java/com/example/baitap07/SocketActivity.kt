package com.example.baitap07

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.net.URISyntaxException

class SocketActivity : AppCompatActivity() {

    private lateinit var imgLight: ImageView
    private lateinit var tvStatus: TextView
    private lateinit var btnOn: Button
    private lateinit var btnOff: Button
    private var mSocket: Socket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_socket)

        initViews()
        initSocket()
    }

    private fun initViews() {
        val toolbar: Toolbar = findViewById(R.id.toolbarSocket)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        imgLight = findViewById(R.id.imgLight)
        tvStatus = findViewById(R.id.tvStatus)
        btnOn = findViewById(R.id.btnOn)
        btnOff = findViewById(R.id.btnOff)
        
        btnOn.setOnClickListener { attemptSend("1") }
        btnOff.setOnClickListener { attemptSend("0") }
    }

    private fun initSocket() {
        try {
            mSocket = IO.socket("https://socket-io-chat.now.sh")
        } catch (e: URISyntaxException) {
            Log.e("SocketIO", "Lỗi URL")
        }

        mSocket?.on(Socket.EVENT_CONNECT) {
            runOnUiThread {
                tvStatus.text = "Trạng thái: Đã kết nối Server"
                Toast.makeText(this, "Kết nối thành công!", Toast.LENGTH_SHORT).show()
            }
        }

        mSocket?.on(Socket.EVENT_CONNECT_ERROR) {
            runOnUiThread { tvStatus.text = "Trạng thái: Chạy Offline (Demo)" }
        }

        mSocket?.connect()
    }

    private fun attemptSend(command: String) {
        // Cập nhật giao diện ngay lập tức (Simulation Mode)
        if (command == "1") {
            imgLight.setImageResource(android.R.drawable.btn_star_big_on)
            tvStatus.text = "Trạng thái: Đèn đang BẬT"
        } else {
            imgLight.setImageResource(android.R.drawable.btn_star_big_off)
            tvStatus.text = "Trạng thái: Đèn đang TẮT"
        }

        // Gửi lệnh lên Server qua Socket (đúng yêu cầu bài tập)
        if (mSocket?.connected() == true) {
            val data = JSONObject().apply {
                put("device_id", "light_01")
                put("command", command)
            }
            mSocket?.emit("control_device", data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket?.disconnect()
    }
}
