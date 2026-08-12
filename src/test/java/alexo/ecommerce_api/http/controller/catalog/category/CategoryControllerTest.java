package alexo.ecommerce_api.http.controller.catalog.category;

import alexo.ecommerce_api.configuration.jackson.JacksonConfig;
import alexo.ecommerce_api.service.internal.identity.authority.UserPrincipalService;
import alexo.ecommerce_api.service.internal.jwt.JwtService;
import alexo.ecommerce_api.http.response.ApiPayloadSerializer;
import alexo.ecommerce_api.service.internal.catalog.category.CategoryService;
import alexo.ecommerce_api.dto.service.internal.catalog.category.CategoryResponseDTO;
import alexo.ecommerce_api.dto.service.internal.catalog.category.create.CreateRequestDTO;
import alexo.ecommerce_api.dto.service.internal.catalog.category.update.CategoryUpdateRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@Import({ApiPayloadSerializer.class, JacksonConfig.class})
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private UserPrincipalService userPrincipalService;
    @MockitoBean
    private PasswordEncoder passwordEncoder;

    // ── POST /api/catalog/category ──────────────────────────────────────────────

    @Test
    @WithMockUser
    void createCategory_validRequest_returns200WithPayload() throws Exception {
        CreateRequestDTO request = new CreateRequestDTO("Electronics And Gadgets", null);
        CategoryResponseDTO response = new CategoryResponseDTO(1L, "Electronics And Gadgets", null);

        when(categoryService.createCategory(any(CreateRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/catalog/category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id").value(1))
                .andExpect(jsonPath("$.payload.treeName").value("Electronics And Gadgets"))
                .andExpect(jsonPath("$.payload.parentId").doesNotExist())
                .andExpect(jsonPath("$.errors").doesNotExist());
    }

    @Test
    @WithMockUser
    void createCategory_withParent_returnsParentIdInPayload() throws Exception {
        CreateRequestDTO request = new CreateRequestDTO("Mobile Phones Category", 10L);
        CategoryResponseDTO response = new CategoryResponseDTO(2L, "Electronics And Gadgets > Mobile Phones Category", 10L);

        when(categoryService.createCategory(any(CreateRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/catalog/category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.parentId").value(10))
                .andExpect(jsonPath("$.payload.treeName").value("Electronics And Gadgets > Mobile Phones Category"));
    }

    @Test
    @WithMockUser
    void createCategory_blankName_returns400WithErrors() throws Exception {
        String body = """
                {"name": "   ", "parentId": null}
                """;

        mockMvc.perform(post("/api/catalog/category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    @WithMockUser
    void createCategory_nameTooShort_returns400WithErrors() throws Exception {
        String body = """
                {"name": "Short", "parentId": null}
                """;

        mockMvc.perform(post("/api/catalog/category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    @WithMockUser
    void createCategory_nameTooLong_returns400WithErrors() throws Exception {
        String longName = "A".repeat(101);
        String body = "{\"name\": \"" + longName + "\", \"parentId\": null}";

        mockMvc.perform(post("/api/catalog/category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    void createCategory_unauthenticated_returns401() throws Exception {
        String body = """
                {"name": "Electronics And Gadgets", "parentId": null}
                """;

        mockMvc.perform(post("/api/catalog/category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());
    }

    // ── PUT /api/catalog/category ───────────────────────────────────────────────

    @Test
    @WithMockUser
    void updateCategory_validRequest_returns200WithPayload() throws Exception {
        // write JSON manually — JsonNullable.undefined() cannot be serialized via ObjectMapper without the nullable module
        String body = """
                {"categoryId": 1, "name": "New Category Name X"}
                """;
        CategoryResponseDTO response = new CategoryResponseDTO(1L, "New Category Name X", null);

        when(categoryService.updateCategory(any(CategoryUpdateRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/catalog/category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.id").value(1))
                .andExpect(jsonPath("$.payload.treeName").value("New Category Name X"))
                .andExpect(jsonPath("$.errors").doesNotExist());
    }

    @Test
    @WithMockUser
    void updateCategory_nullCategoryId_returns400WithErrors() throws Exception {
        String body = """
                {"categoryId": null, "name": "New Category Name X"}
                """;

        mockMvc.perform(put("/api/catalog/category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    @WithMockUser
    void updateCategory_nameTooShort_returns400WithErrors() throws Exception {
        String body = """
                {"categoryId": 1, "name": "Tiny"}
                """;

        mockMvc.perform(put("/api/catalog/category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    void updateCategory_unauthenticated_returns401() throws Exception {
        String body = """
                {"categoryId": 1, "name": "New Category Name X"}
                """;

        mockMvc.perform(put("/api/catalog/category")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());
    }
}
