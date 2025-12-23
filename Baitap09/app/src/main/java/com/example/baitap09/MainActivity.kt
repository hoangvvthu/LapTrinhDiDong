package com.example.baitap09

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject
import java.net.URISyntaxException

class MainActivity : AppCompatActivity() {

    private lateinit var socket: Socket
    private val messages = mutableListOf<ChatMessage>()
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var rvChat: RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button
    
    // ID cố định của điện thoại (không đổi khi khởi động lại)
    private lateinit var deviceId: String

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        rvChat = findViewById(R.id.rvChat)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)

        chatAdapter = ChatAdapter(messages)
        rvChat.adapter = chatAdapter

        setupSocket()

        btnSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun setupSocket() {
        try {
            val opts = IO.Options()
            opts.forceNew = true
            opts.reconnection = true
            opts.transports = arrayOf("websocket")

            socket = IO.socket("https://f9421f5f-b762-4adb-9f61-a51a128e6417-00-2rytmc1ecx0vh.pike.replit.dev:3000", opts)
            
            socket.on(Socket.EVENT_CONNECT) {
                val data = JSONObject()
                data.put("deviceId", deviceId)
                data.put("name", "Khách hàng " + deviceId.takeLast(4).uppercase())
                socket.emit("register_customer", data)
                Log.d("SOCKET", "Đã kết nối với ID: $deviceId")
            }

            // Nhận lịch sử chat từ Server khi vừa kết nối
            socket.on("history_sync") { args ->
                val history = args[0] as JSONArray
                runOnUiThread {
                    messages.clear()
                    for (i in 0 until history.length()) {
                        val obj = history.getJSONObject(i)
                        messages.add(ChatMessage(obj.getString("message"), obj.getString("sender")))
                    }
                    chatAdapter.notifyDataSetChanged()
                    rvChat.scrollToPosition(messages.size - 1)
                }
            }

            socket.on("message") { args ->
                if (args.isNotEmpty()) {
                    val data = args[0] as JSONObject
                    if (data.getString("sender") == "manager") {
                        runOnUiThread {
                            messages.add(ChatMessage(data.getString("message"), "manager"))
                            chatAdapter.notifyItemInserted(messages.size - 1)
                            rvChat.scrollToPosition(messages.size - 1)
                        }
                    }
                }
            }
            
            socket.connect()
        } catch (e: URISyntaxException) { e.printStackTrace() }
    }

    private fun sendMessage() {
        val text = etMessage.text.toString().trim()
        if (text.isNotEmpty()) {
            val data = JSONObject()
            data.put("sender", "customer")
            data.put("deviceId", deviceId) // Gửi kèm ID cố định
            data.put("message", text)

            socket.emit("message", data)
            
            messages.add(ChatMessage(text, "customer"))
            chatAdapter.notifyItemInserted(messages.size - 1)
            rvChat.scrollToPosition(messages.size - 1)
            etMessage.text.clear()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::socket.isInitialized) socket.disconnect()
    }
}
