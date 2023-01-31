package io.orefice.chaos.monkey.application.controller;

import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.orefice.chaos.monkey.application.helpers.IntegrationTest;
import io.orefice.chaos.monkey.model.PodParameters;
import io.orefice.chaos.monkey.service.destroyer.DestroyerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class DestroyerControllerTest extends IntegrationTest {

    @MockBean
    private CoreV1Api api;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private DestroyerService destroyerService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void shouldReturnAPodDestroyedWhenFindIt() throws Exception {
        when(destroyerService.findAndDelete(new PodParameters("namespaceToDestroy", "fancyLabel")))
                .thenReturn(Optional.of("aPodDestroyed"));

        MvcResult result = mockMvc.perform(get("/destroy?namespace=namespaceToDestroy&label=fancyLabel"))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertThat(result.getResponse().getContentAsString()).isEqualTo("aPodDestroyed");
    }

    @Test
    void shouldReturn404WhenPodNotFound() throws Exception {
        when(destroyerService.findAndDelete(new PodParameters("namespaceToDestroy", "fancyLabel")))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/destroy?namespace=namespaceToDestroy&label=fancyLabel"))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}