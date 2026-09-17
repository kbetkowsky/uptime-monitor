package com.kbetkowski.uptimemonitor.controller;

import com.kbetkowski.uptimemonitor.entity.MonitoredSite;
import com.kbetkowski.uptimemonitor.service.CheckService;
import com.kbetkowski.uptimemonitor.service.SiteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SiteController.class)
public class SiteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SiteService siteService;

    @MockitoBean
    private CheckService checkService;

    @Test
    void shouldReturn409WhenUrlAlreadyExists() throws Exception {
        when(siteService.create(any(), any())).thenThrow(new IllegalStateException("Url already exists"));

        mockMvc.perform(post("/sites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {"name": "Test", "url": "https://test.com"}
                        """))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturn201WhenUrlIsCorrect() throws Exception {
        when(siteService.create(any(), any())).thenReturn(new MonitoredSite("Test", "https://test.com"));

        mockMvc.perform(post("/sites")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "Test", "url": "https://test.com"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.url").value("https://test.com"))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    void shouldReturn400WhenUrlIsBad() throws Exception {
        mockMvc.perform(post("/sites")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "Test", "url": ""}
                        """))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
