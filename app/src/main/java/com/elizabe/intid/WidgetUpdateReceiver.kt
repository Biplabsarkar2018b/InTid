package com.elizabe.intid

import android.content.*

class WidgetUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val updateIntent = Intent(it, NewAppWidget::class.java)
            updateIntent.action = NewAppWidget.ACTION_UPDATE_WIDGET
            it.sendBroadcast(updateIntent)
        }
    }
}