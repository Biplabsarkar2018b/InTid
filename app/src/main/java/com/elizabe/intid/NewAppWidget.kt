package com.elizabe.intid

import android.app.*
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.*
import android.net.*
import android.os.Handler
import android.os.Looper
import android.util.*
import android.widget.RemoteViews
import com.elizabe.intid.data.*
import kotlinx.coroutines.*

class NewAppWidget : AppWidgetProvider() {
    companion object {
        const val ACTION_UPDATE_WIDGET = "com.intid.UPDATE_WIDGET"
        const val ACTION_UPDATE_ALL_WIDGET = "com.intid.UPDATE_ALL_WIDGET"
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // Calling update function to initially update the widget
        updateWidgetWithShuffledData(context, appWidgetManager, appWidgetIds)

        // Schedule a periodic update
        handler.postDelayed(object : Runnable {
            override fun run() {
                // Update the widget with shuffled data
                updateWidgetWithShuffledData(context, appWidgetManager, appWidgetIds)
                handler.postDelayed(this, 10 * 1000) // 10 seconds
            }
        }, 10 * 1000) // 10 seconds
    }

    private fun updateWidgets(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(context, NewAppWidget::class.java)
        )
        val intent = Intent(context, NewAppWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        context.sendBroadcast(intent)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action == ACTION_UPDATE_WIDGET) {
            // Update the widget with shuffled data
            updateWidgetWithShuffledData(context!!, AppWidgetManager.getInstance(context), null)
        }else if(intent?.action== ACTION_UPDATE_ALL_WIDGET){
            updateWidgets(context!!)
        }
    }

    fun showSomething(){
        Log.d("Hola","Ding Dong")
    }

    private fun updateWidgetWithShuffledData(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray?) {
        GlobalScope.launch {
            // Load data from Room database here
            val iconData = loadIconDataFromDatabase(context)

            // Create a RemoteViews object for the widget layout
            val views = RemoteViews(context.packageName, R.layout.new_app_widget)

            // Update ImageView widgets with loaded data
            updateImageView(views, iconData,context)

            // Update the widget
            if (appWidgetIds != null) {
                appWidgetManager.updateAppWidget(appWidgetIds, views)
            }
        }
    }

    private suspend fun loadIconDataFromDatabase(context: Context): List<IconWithData> {
        return withContext(Dispatchers.IO) {
            // Load the icon data from Room database
            val iconDb = AppDatabaseIcon.getDatabase(context)
            iconDb.iconDao().getAll()
        }
    }

    private fun updateImageView(views: RemoteViews, iconData: List<IconWithData>,context: Context) {
        // Update ImageView widgets with data
        val iconImageViewIds = intArrayOf(
            R.id.one, R.id.two, R.id.three, R.id.four
        )

        views.setImageViewResource(R.id.one,R.drawable.add)
        views.setImageViewResource(R.id.two,R.drawable.add)
        views.setImageViewResource(R.id.three,R.drawable.add)
        views.setImageViewResource(R.id.four,R.drawable.add)

        val intent = Intent(context, IconActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        views.setOnClickPendingIntent(R.id.one, pendingIntent)
        views.setOnClickPendingIntent(R.id.two, pendingIntent)
        views.setOnClickPendingIntent(R.id.three, pendingIntent)
        views.setOnClickPendingIntent(R.id.four, pendingIntent)


        // Ensure that it has enough data to fill all ImageViews
        // Shuffle the list of icon data to randomize the order
        val shuffledIconData = iconData.shuffled()

        // Create a sublist with the first 4 randomly shuffled icons
        val selectedIcons = shuffledIconData.take(4)

        // Update the RemoteViews with the selected icon resources
        for (i in iconImageViewIds.indices) {
            if (i < selectedIcons.size) {
//                val iconResourceId = selectedIcons[i].iconResourceId
//                // Update the RemoteViews with the loaded icon resource
//                views.setImageViewResource(iconImageViewIds[i], iconResourceId!!)
                val iconResourceId = selectedIcons[i].iconResourceId
                // Update the RemoteViews with the loaded icon resource
                views.setImageViewResource(iconImageViewIds[i], iconResourceId!!)

                val webUrl = selectedIcons[i].appUrl // Replace with the URL you want to open
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(iconImageViewIds[i], pendingIntent)
            }
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
