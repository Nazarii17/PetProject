package com.tkachuk.pet.repository.jdbc;

import com.tkachuk.pet.entity.Note;

import java.util.List;
import java.util.Optional;

public interface NoteRepo {

    List<Note> findAll();

    Optional<Note> findById(Long id);

    Note save(Note note);

    boolean deleteById(Long id);

    void update(Note noteFromDb);
}
