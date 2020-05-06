package com.tkachuk.pet.repository.jdbc.impl;

import com.tkachuk.pet.constant.ErrorMessage;
import com.tkachuk.pet.entity.Note;
import com.tkachuk.pet.exception.NoSuchEntityException;
import com.tkachuk.pet.mapper.NoteMapper;
import com.tkachuk.pet.repository.jdbc.NoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class NoteRepoImpl implements NoteRepo {

    private final JdbcTemplate jdbcTemplate;
    private final NoteMapper noteMapper;

    @Autowired
    public NoteRepoImpl(JdbcTemplate jdbcTemplate, NoteMapper noteMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.noteMapper = noteMapper;
    }

    @Override
    public List<Note> findAll() {
        String sqlQuery = "SELECT * FROM note";
        return jdbcTemplate.query(sqlQuery, noteMapper);
    }

    @Override
    public Optional<Note> findById(Long id) {
        String sqlQuery = "SELECT * FROM note where id =?";
        Note note;
        try {
           note = jdbcTemplate.queryForObject(sqlQuery, noteMapper, id);
        } catch (DataAccessException e) {
            throw new NoSuchEntityException(ErrorMessage.NOTE_NOT_FOUND_BY_ID + id, e);
        }
        return Optional.ofNullable(note);
    }

    @Override
    public Note save(Note note) {
        String sqlQuery = "insert into note(text) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, note.getText());
            return stmt;
        }, keyHolder);
        note.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return note;
    }

    @Override
    public boolean deleteById(Long id) {
        String sqlQuery = "delete from note where id = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public void update(Note note) {
        String sqlQuery = "update note set text = ? where id = ?";
        jdbcTemplate.update(sqlQuery, note.getText(), note.getId());
    }

}
