package com.tkachuk.pet.mapper;

import com.tkachuk.pet.dto.NoteDto;
import com.tkachuk.pet.entity.Note;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class NoteMapper implements RowMapper<Note> {

    private final ModelMapper modelMapper;

    @Override
    public Note mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Note(
                resultSet.getLong("id"),
                resultSet.getString("text")
        );
    }

    public List<NoteDto> toDtoList(@NonNull List<Note> notes) {
        return notes.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public NoteDto toDto(@NonNull Note note) {
        return modelMapper.map(note, NoteDto.class);
    }

    public <F> Note toEntity(@NonNull F f) {
        return modelMapper.map(f, Note.class);
    }
}
