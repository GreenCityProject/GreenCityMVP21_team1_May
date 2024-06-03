package greencity.controller;


import greencity.dto.category.CategoryDto;
import greencity.service.CategoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    CategoryController categoryController;

    private static final String categoryLink = "/category";

    private MockMvc mockMvc;


    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @DisplayName("Category Controller Post request to create new Category Test")
    void saveCategory() throws Exception {
        String name = "Category Name";


        CategoryDto categoryDto = CategoryDto.builder()
                .name(name)
                .build();

        String json = "{\n" +
                "  \"name\": \""+ name +"\"\n" +
                 "}";

        System.out.println(json);
        mockMvc.perform(post(categoryLink)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(categoryService).save(categoryDto);
    }

    @Test
    @DisplayName("Category Controller Get request to get all Categories list")
    void findAllCategory() throws Exception {
        mockMvc.perform(get(categoryLink))
                .andExpect(status().isOk());
        verify(categoryService).findAllCategoryDto();

    }
}