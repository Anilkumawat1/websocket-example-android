package com.anilkumawat.websocket

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var buttonConnect: Button
    private lateinit var editTextMessage: EditText
    private lateinit var editTextFname: EditText
    private lateinit var buttonSend: Button
    private lateinit var textViewMessage: TextView
    lateinit var mSocket : Socket
    val gson: Gson = Gson()
    var token1 = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.d(TAG, "Token: $token")
                     token1 = token
                    Toast.makeText(this, token, Toast.LENGTH_SHORT).show()

                } else {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }


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
            val setupData = setupData(name,token1)
            val jsonPayload = gson.toJson(setupData)
            mSocket.emit("setup",jsonPayload)
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