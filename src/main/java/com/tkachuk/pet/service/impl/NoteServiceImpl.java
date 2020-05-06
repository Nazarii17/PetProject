package com.tkachuk.pet.service.impl;

import com.tkachuk.pet.constant.ErrorMessage;
import com.tkachuk.pet.dto.NoteDto;
import com.tkachuk.pet.entity.Note;
import com.tkachuk.pet.exception.NoSuchEntityException;
import com.tkachuk.pet.exception.NotSavedException;
import com.tkachuk.pet.mapper.NoteMapper;
import com.tkachuk.pet.repository.jdbc.NoteRepo;
import com.tkachuk.pet.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepo noteRepo;
    private final NoteMapper noteMapper;

    @Autowired
    public NoteServiceImpl(NoteRepo noteRepo, NoteMapper noteMapper) {
        this.noteRepo = noteRepo;
        this.noteMapper = noteMapper;
    }

    /**
     * Returns a List of {@link NoteDto} which is representation of all {@link Note} from Db, with DTO pattern;
     * First method founds all {@link Note} from Db using {@link NoteRepo#findAll(),
     * then returns converted by {@link NoteMapper} a List of {@link NoteDto};
     *
     * @return List of {@link Note};
     */
    @Override
    public List<NoteDto> findAll() {
        List<Note> all = noteRepo.findAll();
        return noteMapper.toDtoList(all);

    }

    /**
     * Gets {@link Note} from Db,
     * or throws {@link NoSuchEntityException}, if a note with given id not exists;
     *
     * @param id - {@link Note} id;
     * @return - {@link Note} from Db;
     */
    private Note findNoteById(Long id) {
        return noteRepo.findById(id)
                .orElseThrow(() -> new NoSuchEntityException(ErrorMessage.NOTE_NOT_FOUND_BY_ID + id));
    }

    /**
     * Finds and returns an {@link NoteDto} with given id;
     * Using {@link NoteRepo} finds an entity from DB;
     * Then converts to {@link NoteDto} format and returns it;
     *
     * @param id - {@link Note#getId()} id;
     * @return - {@link NoteDto};
     */
    @Override
    public Optional<NoteDto> findById(Long id) {
        Note note = findNoteById(id);
        return Optional.of(noteMapper.toDto(note));
    }

    /**
     * Creates and saves {@link Note} using data of given{@link NoteDto};
     *
     * @param noteDto - {@link NoteDto} to save;
     * @return - The non-null value Optional<{{@link NoteDto}}>
     */
    @Override
    public Optional<NoteDto> create(NoteDto noteDto) {
        Note note = noteMapper.toEntity(noteDto);
        save(note);
        return Optional.of(noteMapper.toDto(note));
    }

    /**
     * Saves {@link Note} to Db;
     *
     * @param note - {@link Note} to save;
     * @return - saved {@link Note} with Id;
     */
    private Note save(Note note) {
        try {
            noteRepo.save(note);
        } catch (DataIntegrityViolationException e) {
            throw new NotSavedException(ErrorMessage.NOTE_NOT_SAVED);
        }
        return note;
    }

    /**
     * Deletes {@link Note} from Db by Id;
     *
     * @param id - {@link Note} id;
     * @return - 'true' if {@link Note} was deleted;
     */
    @Override
    public boolean deleteById(Long id) {
        if (isNoteExists(id)) {
            return noteRepo.deleteById(id);
        } else {
            return false;
        }
    }

    /**
     * Updates {@link Note} from DB;
     *
     * @param id   - {@link Note} id;
     * @param note - {@link NoteDto};
     * @return - updated {@link NoteDto};
     */
    @Override
    public Optional<NoteDto> update(Long id, NoteDto note) {
        Note noteFromDb = findNoteById(id);
        noteFromDb.setText(note.getText());
        noteRepo.update(noteFromDb);
        return Optional.of(noteMapper.toDto(noteFromDb));
    }

    /**
     * Checks whether {@link Note} with given id exists;
     *
     * @param id - {@link Note} id;
     * @return - true if {@link Note} exists;
     */
    private boolean isNoteExists(Long id) {
        return noteRepo.findById(id).isPresent();
    }

}
