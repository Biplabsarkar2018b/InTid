package com.elizabe.intid

import android.content.*
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.*

class IconSelectionAdapter(
    private val context: Context,
    private val icons: List<Int>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<IconSelectionAdapter.IconViewHolder>() {

    inner class IconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImageView: ImageView = itemView.findViewById(R.id.iconImageView)
        val textView:TextView = itemView.findViewById(R.id.appNameTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(icons[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rcv_item, parent, false)
        return IconViewHolder(view)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        val iconResId = icons[position]
        holder.iconImageView.setImageResource(iconResId)
        holder.textView.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return icons.size
    }
}
