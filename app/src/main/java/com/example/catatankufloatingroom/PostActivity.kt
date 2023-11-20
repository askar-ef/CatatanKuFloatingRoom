package com.example.catatankufloatingroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.catatankufloatingroom.database.Note
import com.example.catatankufloatingroom.database.NoteDao
import com.example.catatankufloatingroom.database.NoteRoomDatabase
import com.example.catatankufloatingroom.databinding.ActivityPostBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding

    private lateinit var mNoteDao: NoteDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPostBinding.inflate(layoutInflater)

        executorService = Executors.newSingleThreadExecutor()

        val db = NoteRoomDatabase.getDatabase(this)
        mNoteDao = db!!.noteDao()!!

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        
        with(binding){
            btnPost.setOnClickListener{
                insert(
                    Note(
                        title = txtTitle.text.toString(),
                        description = txtDescription.text.toString(),
                        date = txtDate.text.toString()
                    )
                )
                intent = Intent(this@PostActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
    private fun insert(note: Note) {
        executorService.execute{mNoteDao.insert((note))}
    }
}