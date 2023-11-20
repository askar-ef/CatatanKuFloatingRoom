package com.example.catatankufloatingroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.catatankufloatingroom.database.Note
import com.example.catatankufloatingroom.database.NoteDao
import com.example.catatankufloatingroom.database.NoteRoomDatabase
import com.example.catatankufloatingroom.databinding.ActivityUpdateBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var mNoteDao: NoteDao
    private lateinit var executorService: ExecutorService

    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityUpdateBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNoteDao = db!!.noteDao()!!


        val bundle: Bundle? = intent.extras
        id = bundle!!.getInt("id") ?: 0
        val title = bundle!!.getString("title") ?: ""
        val description = bundle!!.getString("description") ?: ""
        val date = bundle!!.getString("date") ?: ""

        with(binding){
            txtTitle.setText(title)
            txtDescription.setText(description)
            txtDate.setText(date)

            btnUpdate.setOnClickListener{
                update(
                    Note(
                        id = id,
                        title = txtTitle.getText().toString(),
                        description = txtDescription.getText().toString(),
                        date = txtDate.getText().toString()
                    )
                )
                id = 0
                intent = Intent(this@UpdateActivity, MainActivity::class.java)
                startActivity(intent)
            }

            btnDelete.setOnClickListener{
                delete(
                    Note(
                        id = id,
                        title = title,
                        description = description,
                        date = date
                    )
                )
                id = 0
                intent = Intent(this@UpdateActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
    private fun update(note: Note) {
        executorService.execute {mNoteDao.update(note)}
    }
    private fun delete(note: Note) {
        executorService.execute {mNoteDao.delete(note)}
    }


}