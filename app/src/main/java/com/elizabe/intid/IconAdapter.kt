package com.elizabe.intid

import android.content.*
import android.net.*
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.*
import com.elizabe.intid.data.*

class IconAdapter(private val context: Context) :
    RecyclerView.Adapter<IconAdapter.IconViewHolder>() {

    private var iconList: List<IconWithData> = emptyList()

    fun setIcons(icons: List<IconWithData>) {
        iconList = icons
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rcv_item, parent, false)
        return IconViewHolder(view)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        val icon = iconList[position]
        holder.bind(icon)
    }

    override fun getItemCount(): Int {
        return iconList.size
    }

    inner class IconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconImageView: ImageView = itemView.findViewById(R.id.iconImageView)
        private val appNameTextView:TextView = itemView.findViewById(R.id.appNameTextView)
        fun bind(icon: IconWithData) {
            // Load the icon image from resources based on icon's resId
            val iconResId = icon.iconResourceId
            val text = icon.appName
            appNameTextView.text = text
            iconImageView.setImageResource(iconResId!!)

            // Set a click listener to handle icon click actions
            itemView.setOnClickListener {
                // Handle the icon click here, e.g., open the associated app or URL
                val url = icon.appUrl

                if (url != null) {
                    if (url.isNotEmpty()) {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(browserIntent)
                    } else {
                        // Handle cases when the URL is empty or there's no URL to open

                    }
                }
            }
        }
    }
}