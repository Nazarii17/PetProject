package com.tkachuk.pet.controller.rest.v1;

import com.tkachuk.pet.dto.NoteDto;
import com.tkachuk.pet.service.NoteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/notes")
@Slf4j
@AllArgsConstructor
public class NoteRestController {
    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<List<NoteDto>> getAll() {
        List<NoteDto> resultList = noteService.findAll();
        return !resultList.isEmpty()
                ? ResponseEntity.ok(resultList)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDto> getOne(@PathVariable Long id) {
        Optional<NoteDto> resultOptional = noteService.findById(id);
        return resultOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/")
    public ResponseEntity<NoteDto> create(@RequestBody @Valid NoteDto noteDto) {
        Optional<NoteDto> resultOptional = noteService.create(noteDto);
        return resultOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDto> update(@PathVariable("id") Long id,
                                          @RequestBody @Valid NoteDto note) {//TODO postman check @RequestBody?
        Optional<NoteDto> resultOptional = noteService.update(id, note);
        return resultOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NoteDto> deleteById(@PathVariable("id") Long id) {
        boolean result = noteService.deleteById(id);
        return result
                ? ResponseEntity.ok(new NoteDto())
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
