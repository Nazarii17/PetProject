package com.tkachuk.pet.controller.rest.v1;

import com.tkachuk.pet.dto.OrganizationDto;
import com.tkachuk.pet.dto.OrganizationPhotoDto;
import com.tkachuk.pet.dto.OrganizationProfileDto;
import com.tkachuk.pet.entity.User;
import com.tkachuk.pet.exception.NoSuchEntityException;
import com.tkachuk.pet.service.OrganizationPhotoService;
import com.tkachuk.pet.service.OrganizationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/v1/organizations")
@Slf4j
@AllArgsConstructor
public class OrganizationRestController {
    private final OrganizationService organizationService;
    private final OrganizationPhotoService organizationPhotoService;

    @GetMapping("/exception-test")
    public void testFilter() {
        throw new NoSuchEntityException("Ops, something went wrong, isn't it?");
    }

    @GetMapping
    public ResponseEntity<List<OrganizationDto>> getAll() {
        List<OrganizationDto> resultList = organizationService.findAll();
        return !resultList.isEmpty()
                ? ResponseEntity.ok(resultList)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationProfileDto> getOne(@PathVariable Long id) {
        Optional<OrganizationProfileDto> resultOptional = organizationService.findOrganizationProfileById(id);
        return resultOptional
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/")
    public ResponseEntity<OrganizationDto> create(@AuthenticationPrincipal User user,
                                                  @Valid OrganizationDto organization) {
        Optional<OrganizationDto> resultOptional = organizationService.create(user, organization);
        return resultOptional.map(ResponseEntity::ok).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OrganizationDto> deleteById(@PathVariable("id") Long id) {
        boolean result = organizationService.deleteById(id);
        return result
                ? ResponseEntity.ok(new OrganizationDto())
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationProfileDto> update(@PathVariable("id") Long id,
                                                         @AuthenticationPrincipal User user,
                                                         @Valid @RequestBody OrganizationProfileDto organization) { //TODO postman check @RequestBody?????
        Optional<OrganizationProfileDto> resultOptional = organizationService.update(id, user, organization);
        return resultOptional.map(ResponseEntity::ok).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}/logo")
    public ResponseEntity<OrganizationDto> updateLogo(@PathVariable("id") Long id,
                                                      @RequestParam("file") MultipartFile photo) {
        Optional<OrganizationDto> resultOptional = organizationService.changeLogo(id, photo);
        return resultOptional.map(ResponseEntity::ok).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}/logo")
    public ResponseEntity<OrganizationDto> deleteLogo(@PathVariable("id") Long id) {
        Optional<OrganizationDto> resultOptional = organizationService.deleteLogo(id);
        return resultOptional.map(ResponseEntity::ok).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/photos")
    public ResponseEntity<Set<OrganizationPhotoDto>> getAllOrganizationPhotos(@PathVariable Long id) {
        Set<OrganizationPhotoDto> resultOptional = organizationService.findAllPhotosByOrgId(id);
        return !resultOptional.isEmpty()
                ? ResponseEntity.ok(resultOptional)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/photos/{photoId}")
    public ResponseEntity<OrganizationPhotoDto> deletePhotoById(@PathVariable("photoId") Long photoId) {
        boolean result = organizationService.deletePhotoById(photoId);
        return result
                ? ResponseEntity.ok(new OrganizationPhotoDto())
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/photos/{photoId}")
    public ResponseEntity<OrganizationPhotoDto> updatePhoto(@PathVariable("photoId") Long id,
                                                            @RequestParam("file") MultipartFile file) {
        Optional<OrganizationPhotoDto> resultOptional = organizationService.updatePhoto(id, file);
        return resultOptional.map(ResponseEntity::ok).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/photos/all")
    public ResponseEntity<Set<OrganizationPhotoDto>> addPhotos(@RequestParam(value = "organizationId") Long id,
                                                               @RequestParam("organizationPhotos") MultipartFile[] photos) {
        Set<OrganizationPhotoDto> result = organizationService.addNewPhotos(id, photos);
        return !result.isEmpty()
                ? ResponseEntity.ok(result)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

