package com.example.notepad_api.repositories;

import com.example.notepad_api.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {

}
