package com.tkachuk.pet.controller.rest.v1;

import com.tkachuk.pet.dto.OrganizationDto;
import com.tkachuk.pet.dto.OrganizationProfileDto;
import com.tkachuk.pet.entity.User;
import com.tkachuk.pet.service.OrganizationPhotoService;
import com.tkachuk.pet.service.OrganizationService;
import com.tkachuk.pet.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(OrganizationRestController.class)
@Slf4j
public class OrganizationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    OrganizationService organizationService;

    @MockBean
    UserService userService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    OrganizationPhotoService organizationPhotoService;

    @MockBean
    User user;

    @Test
    @WithMockUser(roles = "USER")
    public void testUpdate() throws Exception {
        OrganizationProfileDto org = OrganizationProfileDto.builder()
                .name("Org Name")
                .website("www.organization.com")
                .description("Some text").build();

        BDDMockito.given(organizationService.update(Mockito.anyLong(), Mockito.any(), Mockito.any(OrganizationProfileDto.class)))
                .willReturn(Optional.of(org));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/v1/organizations/2")
                        .content("{\"name\":\"My Organization\",\"website\":\"org1.web.com\",\"description\":\"1234556678\",\"rating\":200}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetAll() throws Exception {
        OrganizationDto org = OrganizationDto.builder()
                .name("Org Name")
                .website("www.organization.com")
                .description("Some text").build();

        BDDMockito.given(organizationService.findAll())
                .willReturn(Arrays.asList(org));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/v1/organizations/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetOne() throws Exception {
        OrganizationProfileDto org = OrganizationProfileDto.builder()
                .name("Org Name")
                .website("www.organization.com")
                .description("Some text").build();

        BDDMockito.given(organizationService.findOrganizationProfileById(Mockito.anyLong()))
                .willReturn(Optional.of(org));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/v1/organizations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
