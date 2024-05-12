package com.anilkumawat.websocket



import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import java.time.LocalDateTime

object SocketHandler  {

    lateinit var mSocket: Socket
    val gson: Gson = Gson()


    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket("http://192.168.1.19:3000")
        } catch (e: URISyntaxException) {

        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }


}