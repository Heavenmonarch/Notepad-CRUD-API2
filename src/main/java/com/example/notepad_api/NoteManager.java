package com.example.notepad_api;

import com.example.notepad_api.models.Note;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class NoteManager {
    private ArrayList<Note> notes = new ArrayList<>();

    private Long nextId = 1L;

    public void addNote(Note note){
        note.setId(nextId);
        nextId++;
        notes.add(note);

    }

    public List<Note> getAllNotes(){
        return notes;
    }

    public boolean updateNote(Long id, String title, String content){
        if (id <= 0){
            return false;
        }
        Note note = this.findNoteById(id);
        if (note == null){
            return false;
        }
        note.setTitle(title);
        note.setContent(content);
        return true;
    }

    public boolean deleteNote(Long id){
        if (id <= 0 ) {
            return false;
        }
        Note note = this.findNoteById(id);
        if (note == null){
            return false;
        }
        notes.remove(note);
        return true;
    }

    public Note findNoteById(Long id){
        for (int i = 0; i < notes.size(); i++){
            Note note = notes.get(i);

            if (note.getId().equals(id)){
                return note;
            }
        }
        return null;
    }
}
