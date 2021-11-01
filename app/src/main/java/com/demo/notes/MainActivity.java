package com.demo.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.demo.notes.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final ArrayList<Note> notes = new ArrayList<>();
    private ActivityMainBinding binding;
    private NotesAdapter adapter;
    private MainViewModel viewModel;
//    private NotesDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        database = NotesDatabase.getInstance(this);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        adapter = new NotesAdapter(notes);
//        binding.recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
//        binding.recyclerViewNotes.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewNotes.setAdapter(adapter);
        getData();
        adapter.setOnNoteClickListener(new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(int position) {
                Toast.makeText(MainActivity.this, "position number" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(int position) {
                remove(position);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @
                    NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                remove(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewNotes);

        binding.buttonAddNote.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddNoteActivity.class);
            startActivity(intent);
        });
    }

    private void remove(int position) {
//        Note note = notes.get(position);
//        database.notesDao().deleteNote(note);
        Note note = adapter.getNotes().get(position);
        viewModel.deleteNote(note);

    }

    private void getData() {
//        LiveData<List<Note>> notesFromDB = database.notesDao().getAllNotes();
        LiveData<List<Note>> notesFromDB = viewModel.getNotes();
        notesFromDB.observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notesFromLiveData) {
//                notes.clear();
//                notes.addAll(notesFromLiveData);
//                adapter.notifyDataSetChanged();
                adapter.setNotes(notesFromLiveData);
            }
        });

    }

}