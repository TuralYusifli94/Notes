package com.demo.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.demo.notes.databinding.ActivityAddNoteBinding;

public class AddNoteActivity extends AppCompatActivity {
    private ActivityAddNoteBinding binding;
    private NotesDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = NotesDatabase.getInstance(this);

        binding.buttonSaveNote.setOnClickListener(view -> {
            String title = binding.editTextTItle.getText().toString();
            String description = binding.editTextDescription.getText().toString();
            String daysOfWeek = binding.spinnerDaysOfWeek.getSelectedItem().toString();
            if (isFilled(title, description)) {
                Note note = new Note(title, description, daysOfWeek);
//                without roomdatabase
//                MainActivity.notes.add(note);
                database.notesDao().insertNote(note);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "have ful field", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isFilled(String title, String description) {
        return !title.isEmpty() && !description.isEmpty();
    }
}