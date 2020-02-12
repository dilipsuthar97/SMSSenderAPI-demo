package com.dilipsuthar.smssendapi

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    private var sharedPref: SharedPreferences? = null
    private val ROUTE_SWITCH_ACTIVE = "route_switch_active"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = this.getSharedPreferences(packageName, Context.MODE_PRIVATE)

        val mService = Service.getService(applicationContext)
        var route = ""

        if (sharedPref?.getBoolean(ROUTE_SWITCH_ACTIVE, false)!!) {
            tv_route.text = "Selected route: 4"
            switch_route.isChecked = true
            route = "4"
        } else {
            tv_route.text = "Selected route: 1"
            switch_route.isChecked = false
            route = "1"
        }

        switch_route.setOnCheckedChangeListener { compoundButton, isChecked ->

            if (isChecked) {
                tv_route.text = "Selected route: 4"
                sharedPref!!.edit().putBoolean(ROUTE_SWITCH_ACTIVE, true).apply()
            } else {
                tv_route.text = "Selected route: 1"
                sharedPref!!.edit().putBoolean(ROUTE_SWITCH_ACTIVE, false).apply()
            }
        }

        button.setOnClickListener {
            val mobiles = fieldMobileNum.text.toString()
            val message = fieldMessage.text.toString()
            val senderId = fieldSenderId.text.toString()

            if (mobiles.isEmpty() && message.isEmpty() && senderId.isEmpty()) {
                fieldMobileNum.error = "Enter number"
                fieldMessage.error = "Enter message"
                fieldSenderId.error = "Enter sender ID"
            } else if (mobiles.isEmpty() && message.isEmpty()) {
                fieldMobileNum.error = "Enter number"
                fieldMessage.error = "Enter message"
            } else if(message.isEmpty() && senderId.isEmpty()) {
                fieldMessage.error = "Enter message"
                fieldSenderId.error = "Enter sender ID"
            } else if (mobiles.isEmpty() && senderId.isEmpty()) {
                fieldMobileNum.error = "Enter number"
                fieldSenderId.error = "Enter sender ID"
            } else if (mobiles.isEmpty())
                fieldMobileNum.error = "Enter number"
            else if (message.isEmpty())
                fieldMessage.error = "Enter message"
            else if (senderId.isEmpty())
                fieldSenderId.error = "Enter sender ID"
            else if (senderId.length < 6)
                fieldSenderId.error = "Enter 6 latter ID"
            else
                mService.requestSms(mobiles, message, route, senderId)
        }

    }
}
