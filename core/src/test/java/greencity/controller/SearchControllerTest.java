package greencity.controller;

import greencity.GreenCityApplication;
import greencity.service.LanguageService;
import greencity.service.SearchService;
import greencity.service.UserService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {SearchController.class})
@WithMockUser(username = "user")
@ContextConfiguration(classes = {GreenCityApplication.class})
class SearchControllerTest {
    static final String searchLink = "/search";
    static final Locale locale = Locale.ENGLISH;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    SearchService searchService;
    @MockBean
    LanguageService languageService;
    @MockBean
    ModelMapper modelMapper;
    @MockBean
    UserService userService;

    @Test
    @DirtiesContext
    void search_ValidArgs_StatusIsOk() throws Exception {
        String searchQuery = "title";

        when(languageService.findAllLanguageCodes()).thenReturn(List.of(locale.getLanguage()));

        mockMvc.perform(get(searchLink)
                        .param("searchQuery", searchQuery)
                        .locale(locale))
                .andExpect(status().isOk());

        verify(searchService).search(searchQuery, locale.getLanguage());
        verify(languageService).findAllLanguageCodes();
    }

    @Test
    @DirtiesContext
    void searchEcoNews_ValidArgs_StatusIsOk() throws Exception {
        String searchQuery = "Eco news title";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        when(languageService.findAllLanguageCodes()).thenReturn(List.of(locale.getLanguage()));

        mockMvc.perform(get(searchLink + "/econews?page=" + pageNumber + "&size=" + pageSize + "&sort=ASC")
                        .param("searchQuery", searchQuery)
                        .locale(locale))
                .andExpect(status().isOk());

        verify(searchService).searchAllNews(pageable, searchQuery, locale.getLanguage());
        verify(languageService).findAllLanguageCodes();
    }

}
