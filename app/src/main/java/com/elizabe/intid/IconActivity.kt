package com.elizabe.intid

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.*
import androidx.recyclerview.widget.*
import com.elizabe.intid.data.*
import com.elizabe.intid.databinding.ActivityIconBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*

class IconActivity : AppCompatActivity() {
    private lateinit var iconDb:AppDatabaseIcon
    lateinit var binding:ActivityIconBinding
    private lateinit var rcv:RecyclerView
    private lateinit var adapter:IconAdapter
    val availableIcons: List<Int> = listOf(
        R.drawable.facebook,
        R.drawable.google_drive,
        R.drawable.google_maps,
        R.drawable.google_pay,
        R.drawable.google_photos,
        R.drawable.google_play,
        R.drawable.google_search,
        R.drawable.twitter,
        R.drawable.whatsapp,
        R.drawable.youtube
    )

    val availableGoto : List<String> = listOf(
        "facebook",
        "drive",
        "maps",
        "GPay",
        "photos",
        "Play Store",
        "Search",
        "X",
        "Whatsapp",
        "YouTube"
    )

    val availableURL : List<String> = listOf(
        "https://www.facebook.com/",
        "https://drive.google.com/",
        "https://www.google.com/maps",
        "https://pay.google.com/",
        "https://photos.google.com/",
        "https://play.google.com/store",
        "https://www.google.com/",
        "https://www.x.com",
        "https://www.whatsapp.com/",
        "https://www.youtube.com/"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIconBinding.inflate(layoutInflater)
        setContentView(binding.root)

        iconDb = AppDatabaseIcon.getDatabase(this)
        adapter = IconAdapter(this)
        rcv = binding.storedIconsRCV
        rcv.adapter = adapter
        binding.fabAdd.setOnClickListener {
//            writeData()
            showIconSelectionDialog()
        }
        binding.fabDelete.setOnClickListener {
            deleteAllData()
        }

        loadStoredIcons(adapter = adapter)

    }

    private fun loadStoredIcons(adapter: IconAdapter) {
        GlobalScope.launch(Dispatchers.IO) {
            val icons = iconDb.iconDao().getAll()
            withContext(Dispatchers.Main) {
                adapter.setIcons(icons)
            }
        }
    }

    private fun writeData(){
        val iconWithData = IconWithData(null,3,"kcfd","ikcdf")
        GlobalScope.launch(Dispatchers.IO){
            iconDb.iconDao().insert(iconWithData)
        }
        Toast.makeText(this,"Successfully written", Toast.LENGTH_SHORT).show()
    }

    private fun deleteAllData(){
        GlobalScope.launch(Dispatchers.IO){
            iconDb.iconDao().deleteAll()
            loadStoredIcons(adapter)
        }


    }

    private fun showIconSelectionDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_icon_selection, null)
        val iconRecyclerView = dialogView.findViewById<RecyclerView>(R.id.iconRecyclerView)

        var dialog: AlertDialog? = null  // Variable to hold the dialog

        val iconAdapter = IconSelectionAdapter(this, availableIcons) { selectedIconResId ->
            val index = availableIcons.indexOf(selectedIconResId)
            val iconWithData = IconWithData(null, selectedIconResId, availableGoto[index], availableURL[index])
            GlobalScope.launch(Dispatchers.IO) {
                iconDb.iconDao().insert(iconWithData)
                loadStoredIcons(adapter = adapter)

                // Check if the dialog is not null and dismiss it
                dialog?.dismiss()
            }

//            Toast.makeText(this, "Icon selected and stored!", Toast.LENGTH_SHORT).show()
        }

        iconRecyclerView.adapter = iconAdapter
        iconRecyclerView.layoutManager = GridLayoutManager(this, 3) // Adjust the number of columns as needed.

        dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setNegativeButton("Cancel") { _, _ ->
                dialog?.dismiss()
            }
            .show()
    }

}