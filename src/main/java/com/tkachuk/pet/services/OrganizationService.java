package com.tkachuk.pet.services;

import com.tkachuk.pet.entities.Organization;
import com.tkachuk.pet.entities.OrganizationType;
import com.tkachuk.pet.entities.User;
import com.tkachuk.pet.repositories.OrganizationRepo;
import com.tkachuk.pet.utils.ControllerUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrganizationService {

    @Value("${upload.path}")
    private String uploadPath;

    private final OrganizationRepo organizationRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public OrganizationService(OrganizationRepo organizationRepo, ModelMapper modelMapper) {
        this.organizationRepo = organizationRepo;
        this.modelMapper = modelMapper;
    }

    public List<Organization> findAll() {
        return organizationRepo.findAll();
    }

    public Organization findByName(String name) {
        return organizationRepo.findByName(name);
    }

    public Organization getOne(Long id) {
        return organizationRepo.getOne(id);
    }

    // Todo!!!
    @Transactional
    public void update(Organization organization) {
        //....
        organizationRepo.save(organization);
    }

    public void save(Organization organization) {
        organizationRepo.save(organization);
    }

    /**
     * Saves an organization with Valid author, and file;
     *
     * @param user         - user from current session;
     * @param organization - value from UI form, made by user;
     * @param file         - (image) logo of an organization;
     * @throws IOException - ;
     */
    public void save(@AuthenticationPrincipal User user,
                     @Valid Organization organization,
                     @RequestParam("file") MultipartFile file) throws IOException {
        organization.setAuthor(user);
        saveLogo(organization, file);
        save(organization);
    }

    public void saveLogo(@Valid Organization organization,
                         @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            organization.setLogo(resultFilename);
        }
    }

    public void delete(long id) {
        organizationRepo.deleteById(id);
    }

    public void update(@PathVariable("id") Long id,
                       @Valid Organization organization,
                       @RequestParam("file") MultipartFile file) throws IOException {
        Organization organizationToSave = getOne(id);
        organizationToSave.setName(organization.getName());
        organizationToSave.setWebsite(organization.getWebsite());
        organizationToSave.setAddress(organization.getAddress());
        organizationToSave.setPhoneNumber(organization.getPhoneNumber());
        organizationToSave.setRating(organization.getRating());
        organizationToSave.setDescription(organization.getDescription());
        saveLogo(organizationToSave, file);
        organizationToSave.setOrganizationTypes(organization.getOrganizationTypes());
        save(organizationToSave);
    }

    /**
     * Returns to user an AdditionPage with errors, to give an user a chance fix the problems
     * and fill up addition correctly;
     *
     * @param organization  -
     * @param bindingResult
     * @param model
     * @return
     */
    public String getAdditionPageWithErrors(@Valid Organization organization,
                                            BindingResult bindingResult,
                                            Model model) {
        Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
        model.mergeAttributes(errorsMap);
        model.addAttribute("organization", organization);
        model.addAttribute("types", OrganizationType.values());
        return "organizationAdd";
    }

}
