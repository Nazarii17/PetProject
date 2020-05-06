package com.tkachuk.pet.service;

import com.tkachuk.pet.dto.NoteDto;
import com.tkachuk.pet.entity.Note;
import com.tkachuk.pet.mapper.NoteMapper;
import com.tkachuk.pet.repository.jdbc.NoteRepo;

import java.util.List;
import java.util.Optional;

public interface NoteService {

    /**
     * Returns a List of {@link NoteDto} which is representation of all {@link Note} from Db, with DTO pattern;
     * First method founds all {@link Note} from Db using {@link NoteRepo#findAll(),
     * then returns converted by {@link NoteMapper} a List of {@link NoteDto};
     *
     * @return List of {@link Note};
     */
    List<NoteDto> findAll();

    /**
     * Finds and returns an {@link NoteDto} with given id;
     * Using {@link NoteRepo} finds an entity from DB;
     * Then converts to {@link NoteDto} format and returns it;
     *
     * @param id - {@link Note#getId()} id;
     * @return - {@link NoteDto};
     */
    Optional<NoteDto> findById(Long id);

    /**
     * Creates and saves {@link Note} using data of given{@link NoteDto};
     *
     * @param noteDto - {@link NoteDto} to save;
     * @return - The non-null value Optional<{{@link NoteDto}}>
     */
    Optional<NoteDto> create(NoteDto noteDto);

    /**
     * Updates {@link Note} from DB;
     *
     * @param id   - {@link Note} id;
     * @param note - {@link NoteDto};
     * @return - updated {@link NoteDto};
     */
    Optional<NoteDto> update(Long id, NoteDto note);

    /**
     * Deletes {@link Note} from Db by Id;
     *
     * @param id - {@link Note} id;
     * @return - 'true' if {@link Note} was deleted;
     */
    boolean deleteById(Long id);

}
