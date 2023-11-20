package com.example.catatankufloatingroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.catatankufloatingroom.database.Note
import com.example.catatankufloatingroom.database.NoteDao
import com.example.catatankufloatingroom.database.NoteRoomDatabase
import com.example.catatankufloatingroom.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var mNoteDao: NoteDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)

        mNoteDao = db!!.noteDao()!!

        with(binding) {
            btnFloating.setOnClickListener {
                intent = Intent(this@MainActivity, PostActivity::class.java)
                startActivity(intent)
            }
            lvName.setOnItemClickListener { adapterView, view, i, id ->
                val item = adapterView.adapter.getItem(i) as Note
                val intent = Intent(this@MainActivity, UpdateActivity::class.java)
                intent.putExtra("id", item.id)
                intent.putExtra("title", item.title)
                intent.putExtra("description", item.description)
                intent.putExtra("date", item.date)
                startActivity(intent)
            }
        }
    }
    private fun getAllNotes() {
        mNoteDao.allNotes.observe(this) { notes ->
            val adapter: ArrayAdapter<Note> =
                ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1, notes)
            binding.lvName.adapter = adapter
        }
    }

    override fun onResume(){
        super.onResume()
        getAllNotes()
    }
}