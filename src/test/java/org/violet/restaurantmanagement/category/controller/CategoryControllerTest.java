package org.violet.restaurantmanagement.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.violet.restaurantmanagement.RmaControllerTest;
import org.violet.restaurantmanagement.category.controller.mapper.CategoryCreateRequestToCreateCommandMapper;
import org.violet.restaurantmanagement.category.controller.mapper.CategoryUpdateRequestToUpdateCommandMapper;
import org.violet.restaurantmanagement.category.controller.request.CategoryCreateRequest;
import org.violet.restaurantmanagement.category.controller.request.CategoryListRequest;
import org.violet.restaurantmanagement.category.controller.request.CategoryUpdateRequest;
import org.violet.restaurantmanagement.category.exceptions.CategoryNotFoundException;
import org.violet.restaurantmanagement.category.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.category.service.CategoryService;
import org.violet.restaurantmanagement.category.service.command.CategoryCreateCommand;
import org.violet.restaurantmanagement.category.service.command.CategoryListCommand;
import org.violet.restaurantmanagement.category.service.command.CategoryUpdateCommand;
import org.violet.restaurantmanagement.category.service.domain.Category;
import org.violet.restaurantmanagement.common.model.PaginationBuilder;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.common.model.SortingBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest extends RmaControllerTest {

    @MockBean
    private CategoryService categoryService;

    private static final CategoryUpdateRequestToUpdateCommandMapper categoryUpdateRequestToUpdateCommandMapper = CategoryUpdateRequestToUpdateCommandMapper.INSTANCE;
    private static final CategoryCreateRequestToCreateCommandMapper categoryCreateRequestToCreateCommandMapper = CategoryCreateRequestToCreateCommandMapper.INSTANCE;

    private final static String BASE_URL = "/api/v1/category";


    @Test
    void givenValidCategoryListRequest_whenCategoriesFound_thenReturnSuccess() throws Exception {
        // Given
        CategoryListRequest.CategoryFilter mockCategoryFilter = CategoryListRequest.CategoryFilter.builder()
                .name("Category")
                .build();
        CategoryListRequest mockCategoryListRequest = CategoryListRequest.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(1)
                                .pageSize(3)
                                .build()
                )
                .sorting(
                        SortingBuilder.builder()
                                .desc()
                                .property("name")
                                .build()
                )
                .filter(mockCategoryFilter)
                .build();

        // When
        List<Category> mockCategories = new ArrayList<>();
        mockCategories.add(
                Category.builder()
                        .id(1L)
                        .name("category 1")
                        .status(CategoryStatus.ACTIVE)
                        .build()
        );
        mockCategories.add(
                Category.builder()
                        .id(2L)
                        .name("category 2")
                        .status(CategoryStatus.ACTIVE)
                        .build()
        );
        mockCategories.add(
                Category.builder()
                        .id(3L)
                        .name("category 3")
                        .status(CategoryStatus.ACTIVE)
                        .build()
        );

        RmaPage<Category> rmaPage = RmaPage.<Category>builder()
                .content(mockCategories)
                .pageNumber(mockCategoryListRequest.getPagination().getPageNumber())
                .pageSize(mockCategories.size())
                .totalPageCount(mockCategoryListRequest.getPagination().getPageNumber())
                .totalElementCount(mockCategories.size())
                .sortedBy(mockCategoryListRequest.getSorting())
                .filteredBy(mockCategoryFilter)
                .build();

        Mockito.when(categoryService.getAllCategories(Mockito.any(CategoryListCommand.class)))
                .thenReturn(rmaPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockCategoryListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageNumber")
                        .value(rmaPage.getPageNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageSize")
                        .value(rmaPage.getPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.totalPageCount")
                        .value(rmaPage.getTotalPageCount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.filteredBy.name")
                        .value(mockCategoryFilter.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));

        // Verify
        Mockito.verify(categoryService, Mockito.times(1))
                .getAllCategories(Mockito.any(CategoryListCommand.class));
    }

    @Test
    void givenValidCategoryListRequestWithoutFilter_whenCategoriesFound_thenReturnSuccess() throws Exception {
        // Given
        CategoryListRequest.CategoryFilter mockCategoryFilter = CategoryListRequest.CategoryFilter.builder()
                .name("Category")
                .build();
        CategoryListRequest mockCategoryListRequest = CategoryListRequest.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(1)
                                .pageSize(3)
                                .build()
                )
                .sorting(
                        SortingBuilder.builder()
                                .desc()
                                .property("name")
                                .build()
                )
                .build();

        // When
        List<Category> mockCategories = new ArrayList<>();
        mockCategories.add(
                Category.builder()
                        .id(1L)
                        .name("category 1")
                        .status(CategoryStatus.ACTIVE)
                        .build()
        );
        mockCategories.add(
                Category.builder()
                        .id(2L)
                        .name("category 2")
                        .status(CategoryStatus.ACTIVE)
                        .build()
        );
        mockCategories.add(
                Category.builder()
                        .id(3L)
                        .name("category 3")
                        .status(CategoryStatus.ACTIVE)
                        .build()
        );

        RmaPage<Category> rmaPage = RmaPage.<Category>builder()
                .content(mockCategories)
                .pageNumber(mockCategoryListRequest.getPagination().getPageNumber())
                .pageSize(mockCategories.size())
                .totalPageCount(mockCategoryListRequest.getPagination().getPageNumber())
                .totalElementCount(mockCategories.size())
                .sortedBy(mockCategoryListRequest.getSorting())
                .filteredBy(mockCategoryFilter)
                .build();

        Mockito.when(categoryService.getAllCategories(Mockito.any(CategoryListCommand.class)))
                .thenReturn(rmaPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockCategoryListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageNumber")
                        .value(rmaPage.getPageNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageSize")
                        .value(rmaPage.getPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.totalPageCount")
                        .value(rmaPage.getTotalPageCount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));

        // Verify
        Mockito.verify(categoryService, Mockito.times(1))
                .getAllCategories(Mockito.any(CategoryListCommand.class));
    }

    @Test
    void givenCategoryListRequestWithoutSorting_whenCategoriesFound_thenReturnSuccess() throws Exception {
        // Given
        CategoryListRequest.CategoryFilter mockCategoryFilter = CategoryListRequest.CategoryFilter.builder()
                .name("Category")
                .build();

        CategoryListRequest mockCategoryListRequest = CategoryListRequest.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(1)
                                .pageSize(2)
                                .build()
                )
                .filter(mockCategoryFilter)
                .build();

        // When
        List<Category> mockCategories = new ArrayList<>();
        mockCategories.add(
                Category.builder()
                        .id(1L)
                        .name("category 1")
                        .status(CategoryStatus.ACTIVE)
                        .build()
        );
        mockCategories.add(
                Category.builder()
                        .id(2L)
                        .name("category 2")
                        .status(CategoryStatus.ACTIVE)
                        .build()
        );


        RmaPage<Category> rmaPage = RmaPage.<Category>builder()
                .content(mockCategories)
                .pageNumber(mockCategoryListRequest.getPagination().getPageNumber())
                .pageSize(mockCategories.size())
                .totalPageCount(mockCategoryListRequest.getPagination().getPageNumber())
                .totalElementCount(mockCategories.size())
                .sortedBy(mockCategoryListRequest.getSorting())
                .filteredBy(mockCategoryFilter)
                .build();

        Mockito.when(categoryService.getAllCategories(
                Mockito.any(CategoryListCommand.class))
        ).thenReturn(rmaPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockCategoryListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageNumber")
                        .value(rmaPage.getPageNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageSize")
                        .value(rmaPage.getPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.totalPageCount")
                        .value(rmaPage.getTotalPageCount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.filteredBy.name")
                        .value(mockCategoryFilter.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));

        // Verify
        Mockito.verify(categoryService, Mockito.times(1))
                .getAllCategories(Mockito.any(CategoryListCommand.class));
    }

    @Test
    void givenValidCategoryListRequestWithoutSortingAndFiltering_whenCategoriesFound_thenReturnSuccess() throws Exception {
        // Given
        CategoryListRequest mockCategoryListRequest = CategoryListRequest.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(1)
                                .pageSize(2)
                                .build()
                )
                .build();

        // When
        List<Category> mockCategories = new ArrayList<>();
        mockCategories.add(
                Category.builder()
                        .id(1L)
                        .name("category 1")
                        .status(CategoryStatus.ACTIVE)
                        .build()
        );
        mockCategories.add(
                Category.builder()
                        .id(2L)
                        .name("category 2")
                        .status(CategoryStatus.ACTIVE)
                        .build()
        );

        RmaPage<Category> rmaPage = RmaPage.<Category>builder()
                .content(mockCategories)
                .pageNumber(mockCategoryListRequest.getPagination().getPageNumber())
                .pageSize(mockCategories.size())
                .totalPageCount(mockCategoryListRequest.getPagination().getPageNumber())
                .totalElementCount(mockCategories.size())
                .build();

        Mockito.when(categoryService.getAllCategories(
                Mockito.any(CategoryListCommand.class))
        ).thenReturn(rmaPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockCategoryListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageNumber")
                        .value(rmaPage.getPageNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.pageSize")
                        .value(rmaPage.getPageSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.totalPageCount")
                        .value(rmaPage.getTotalPageCount()))
                   .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));

        // Verify
        Mockito.verify(categoryService, Mockito.times(1))
                .getAllCategories(Mockito.any(CategoryListCommand.class));
    }

    @Test
    void givenInvalidCategoryListRequest_whenCategoriesNotFound_thenReturnBadRequest() throws Exception {
        // Given
        CategoryListRequest givenRequest = CategoryListRequest.builder().build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(givenRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(categoryService);
    }

    @Test
    void givenInvalidNegativePageSize_whenCategoriesNotFound_thenReturnBadRequest() throws Exception {
        // Given
        CategoryListRequest mockCategoryListRequest = CategoryListRequest.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(1)
                                .pageSize(-1)
                                .build()
                )
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockCategoryListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(categoryService);
    }

    @Test
    void givenInvalidNegativePageNumber_whenCategoriesNotFound_thenReturnBadRequest() throws Exception {
        // Given
        CategoryListRequest mockCategoryListRequest = CategoryListRequest.builder()
                .pagination(
                        PaginationBuilder.builder()
                                .pageNumber(-1)
                                .pageSize(1)
                                .build()
                )
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockCategoryListRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(categoryService);
    }

    @Test
    void givenValidCategoryId_whenCategoryFound_thenReturnSuccess() throws Exception {
        // Given
        Long categoryId = 1L;

        // When
        Category category = Category.builder()
                .id(categoryId)
                .name("Test")
                .status(CategoryStatus.ACTIVE)
                .createdAt(LocalDateTime.now()).build();

        Mockito.when(categoryService.getCategoryById(categoryId)).thenReturn(category);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", categoryId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.name")
                        .value(category.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));

        // Verify
        Mockito.verify(categoryService, Mockito.times(1)).getCategoryById(categoryId);
    }

    @Test
    void givenInvalidNegativeCategoryId_whenCategoryNotFound_thenReturnBadRequest() throws Exception {
        //Given
        Long categoryId = -1L;

        // When
        Mockito.doThrow(ConstraintViolationException.class)
                .when(categoryService)
                .getCategoryById(categoryId);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", categoryId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(categoryService);
    }

    @Test
    void givenInvalidCategoryId_whenCategoryNotFound_thenReturnNotFound() throws Exception {
        // Given
        Long categoryId = 999L;

        // When
        Mockito.when(categoryService.getCategoryById(categoryId))
                .thenThrow(new CategoryNotFoundException());

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", categoryId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify
        Mockito.verify(categoryService, Mockito.times(1)).getCategoryById(categoryId);
    }


    @Test
    void givenValidCategoryCreateRequest_whenCreateCategory_thenReturnsSuccess() throws Exception {
        // Given
        CategoryCreateRequest mockCategoryCreateRequest = new CategoryCreateRequest(
                "Test",
                CategoryStatus.ACTIVE
        );

        // When
        CategoryCreateCommand createCommand = categoryCreateRequestToCreateCommandMapper.map(mockCategoryCreateRequest);
        Mockito.doNothing().when(categoryService).createCategory(Mockito.any(CategoryCreateCommand.class));

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockCategoryCreateRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify
        Mockito.verify(categoryService, Mockito.times(1)).createCategory(createCommand);
    }

    @Test
    void givenInvalidsMaxSizeInput_whenCreateCategory_thenReturnBadRequest() throws Exception {
        // Given
        CategoryCreateRequest mockCategoryCreateRequest = new CategoryCreateRequest(
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry." +
                        " Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an " +
                        " Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an " +
                        "unknown printer took a galley of type and scrambled it to make a type specimen book. " ,
                CategoryStatus.ACTIVE
        );

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockCategoryCreateRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(categoryService);
    }

    @Test
    void givenInvalidCategoryCreateRequest_whenCategoryCantCreate_thenReturnBadRequest() throws Exception {
        // Given
        CategoryCreateRequest mockCategoryCreateRequest = new CategoryCreateRequest(
                null,
                null
        );

        // When
        Mockito.doThrow(new CategoryNotFoundException()).when(categoryService).createCategory(Mockito.any());

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockCategoryCreateRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(categoryService);
    }

    @Test
    void givenValidCategoryUpdateRequest_whenUpdateCategory_thenReturnSuccess() throws Exception {
        //Given
        Long categoryId = 1L;

        // When
        CategoryUpdateRequest mockCategoryUpdateRequest = new CategoryUpdateRequest(
                "UpdateTest",
                CategoryStatus.ACTIVE
        );

        CategoryUpdateCommand command = categoryUpdateRequestToUpdateCommandMapper.map(mockCategoryUpdateRequest);

        Mockito.doNothing().when(categoryService).updateCategory(categoryId, command);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(command)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));

        // Verify
        Mockito.verify(categoryService).updateCategory(categoryId, command);
    }

    @Test
    void givenInvalidCategoryUpdateRequest_whenCategoryUpdate_thenReturnBadRequest() throws Exception {
        // Given
        Long categoryId = 1L;

        //When
        CategoryUpdateRequest mockCategoryUpdateRequest = new CategoryUpdateRequest(
                null,
                null
        );
        CategoryUpdateCommand command = categoryUpdateRequestToUpdateCommandMapper.map(mockCategoryUpdateRequest);

        Mockito.doThrow(new CategoryNotFoundException()).when(categoryService)
                .updateCategory(categoryId, command);

        //Then
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(command)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(categoryService);
    }

    @Test
    void givenInvalidNegativeCategoryId_whenUpdateCategoryNotFound_thenReturnBadRequest() throws Exception {
        //Given
        Long categoryId = -1L;

        // When
        CategoryUpdateRequest mockCategoryUpdateRequest = new CategoryUpdateRequest(
                "UpdateTest",
                CategoryStatus.ACTIVE
        );
        CategoryUpdateCommand command = categoryUpdateRequestToUpdateCommandMapper.map(mockCategoryUpdateRequest);

        Mockito.doThrow(ConstraintViolationException.class)
                .when(categoryService)
                .updateCategory(categoryId, command);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/{id}", categoryId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(categoryService);
    }

    @Test
    void givenValidDeletedCategoryId_whenCategoryFound_thenReturnSuccess() throws Exception {
        // Given
        Long categoryId = 1L;

        // When
        Mockito.doNothing().when(categoryService).deleteCategory(categoryId);

        // Assert
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", categoryId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));

        // Verify
        Mockito.verify(categoryService).deleteCategory(categoryId);
    }

    @Test
    void givenInvalidDeleteId_whenDeleteCategoryNotFound_thenReturnCategoryNotFound() throws Exception {
        // Given
        Long categoryId = 100L;

        // When
        Mockito.doThrow(CategoryNotFoundException.class)
                .when(categoryService)
                .deleteCategory(categoryId);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", categoryId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify
        Mockito.verify(categoryService).deleteCategory(categoryId);
    }

    @Test
    void givenInvalidNegativeId_whenDeleteCategory_thenReturnBadRequest() throws Exception {
        // Given
        Long categoryId = -1L;

        // When
        Mockito.doThrow(ConstraintViolationException.class)
                .when(categoryService)
                .deleteCategory(categoryId);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/{id}", categoryId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify
        Mockito.verifyNoInteractions(categoryService);
    }
}