package com.example.notepad_api;

import com.example.notepad_api.models.Note;

import java.util.List;

public class NoteTest {

    public static void main(String[] args) {

        NoteManager manager = new NoteManager();

        Note note1 = new Note(
                "My first note",
                "I'm learning Java"
        );

        Note note2 = new Note(
                "My second note",
                "I'm learning Spring Boot"
        );

        manager.addNote(note1);
        manager.addNote(note2);

        boolean updated = manager.updateNote(
                1L,
                "Updated first note",
                "I just learned how to update objects"
        );

        System.out.println(updated);
        System.out.println(manager.getAllNotes());

        boolean updated2 = manager.updateNote(
                10L,
                "Doesn't matter",
                "This should fail"
        );
        System.out.println(updated2);

        boolean deleted = manager.deleteNote(1L);

        System.out.println("Deleted: " + deleted);
        System.out.println(manager.getAllNotes());
    }
}