package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import server.database.ExpenseRepository;
import commons.Expense;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExpenseController.class)
public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpenseRepository expenseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Expense> expenseList;

    @BeforeEach
    void setUp() {
        Expense expense1 = new Expense(1, null, "Lunch", 20.0, null, null);
        Expense expense2 = new Expense(2, null, "Coffee", 5.0, null, null);
        expenseList = Arrays.asList(expense1, expense2);

        given(expenseRepository.findAll()).willReturn(expenseList);
        given(expenseRepository.findById(1L)).willReturn(Optional.of(expense1));
    }

    @Test
    void getAllExpenses_ShouldReturnAllExpenses() throws Exception {
        mockMvc.perform(get("/api/expense"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(expenseList.size())))
                .andExpect(jsonPath("$[0].purchase", is(expenseList.get(0).getPurchase())));
    }

    @Test
    void getExpenseById_WhenExpenseExists_ShouldReturnExpense() throws Exception {
        mockMvc.perform(get("/api/expense/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.purchase", is("Lunch")));
    }

    @Test
    void getExpenseById_WhenExpenseDoesNotExist_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/expense/{id}", 99))
                .andExpect(status().isNotFound());
    }

    @Test
    void createExpense_ShouldAddExpenseAndReturnCreatedExpense() throws Exception {
        Expense newExpense = new Expense(3, null, "Snacks", 15.0, null, null);
        given(expenseRepository.save(newExpense)).willReturn(newExpense);

        mockMvc.perform(post("/api/expense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newExpense)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purchase", is("Snacks")));
    }

    @Test
    void updateExpense_WhenExpenseExists_ShouldUpdateAndReturnExpense() throws Exception {
        Expense updatedExpense = new Expense(1, null, "Updated Lunch", 25.0, null, null);
        given(expenseRepository.save(updatedExpense)).willReturn(updatedExpense);

        mockMvc.perform(put("/api/expense/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedExpense)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purchase", is("Updated Lunch")));
    }

    @Test
    void deleteExpense_WhenExpenseExists_ShouldDeleteExpense() throws Exception {
        mockMvc.perform(delete("/api/expense/{id}", 1))
                .andExpect(status().isOk());
    }

    
}