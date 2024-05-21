package greencity.controller;

import greencity.converters.UserArgumentResolver;
import greencity.service.CategoryService;
import greencity.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.param;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    CategoryController categoryController;

    private static final String categoryLink = "/category";

    private MockMvc mockMvc;


//    @Mock
//    private UserService userService;
//    @Mock
//    private ModelMapper modelMapper;
//
//

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
        String nameUa = "Категорія";
        int id = 5;

         String json = "{\n" +
                 "  \"name\": \""+ name +"\",\n" +
                 "  \"nameUa\": \" " + nameUa + "\",\n" +
                 "  \"parentCategoryId\": " + id + "\n" +
                 "}";

        System.out.println(json);
        mockMvc.perform(post(categoryLink)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        assert categoryService.findAllCategoryDto() != null;
    }

    @Test
    @DisplayName("Category Controller Get request to get all Categories list")
    void findAllCategory() throws Exception {
        mockMvc.perform(get(categoryLink))
                .andExpect(status().isOk());
        verify(categoryService).findAllCategoryDto();

    }
}