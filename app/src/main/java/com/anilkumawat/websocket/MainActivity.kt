package com.anilkumawat.websocket

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket

class MainActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var buttonConnect: Button
    private lateinit var editTextMessage: EditText
    private lateinit var editTextFname: EditText
    private lateinit var buttonSend: Button
    private lateinit var textViewMessage: TextView
    lateinit var mSocket : Socket
    val gson: Gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SocketHandler.setSocket()

        mSocket=SocketHandler.getSocket()

        mSocket.connect()
        // Initialize views
        editTextName = findViewById(R.id.editTextName)
        buttonConnect = findViewById(R.id.buttonConnect)
        editTextMessage = findViewById(R.id.editTextMessage)
        editTextFname = findViewById(R.id.editTextFname)
        buttonSend = findViewById(R.id.buttonSend)
        textViewMessage = findViewById(R.id.textViewMessage)


        mSocket.on("receive-message"){args ->
            if(args[0]!=null){
                val mes=gson.fromJson(args[0].toString(), messages::class.java)
                runOnUiThread {
                    textViewMessage.text = "Received Message: ${mes.mess}"
                }

            }

        }

        // Set click listeners for buttons
        buttonConnect.setOnClickListener {
            // Perform action when Connect button is clicked
            val name = editTextName.text.toString()
            mSocket.emit("setup",name)
            // Your logic for connecting
        }

        buttonSend.setOnClickListener {
            val message = editTextMessage.text.toString()
            val fname = editTextFname.text.toString()
            val name = editTextName.text.toString()
            val payload = messages(name,fname,message)
            val jsonPayload = gson.toJson(payload)
            mSocket.emit("private-message",jsonPayload)


        }
    }
}