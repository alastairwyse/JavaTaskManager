/*
 * Copyright 2023 Alastair Wyse (https://github.com/alastairwyse/JavaTaskManager/)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.alastairwyse.taskmanager.api.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import net.alastairwyse.taskmanager.TaskManager;
import net.alastairwyse.taskmanager.api.TaskManagerApi;
import net.alastairwyse.taskmanager.models.dtos.DateOnlyDto;
import net.alastairwyse.taskmanager.models.dtos.NewTaskDto;
import net.alastairwyse.taskmanager.models.dtos.TaskDto;
import net.alastairwyse.taskmanager.models.Task;
import net.alastairwyse.taskmanager.models.TaskDoesntExistException;

import org.junit.Before;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.runner.*;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

/**
 * Integration tests for the TaskController.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = TaskManagerApi.class)
@AutoConfigureMockMvc
public class TaskControllerIntegrationTests extends IntegrationTestsBase {
    
    @Autowired
    private MockMvc mvc;
    @MockBean
    private TaskManager mockTaskManager;
    @Captor
    private ArgumentCaptor<NewTaskDto> newTaskDtoCaptor;
    @Captor
    private ArgumentCaptor<TaskDto> taskDtoCaptor;
    @Captor
    private ArgumentCaptor<Task> taskCaptor;

    private NewTaskDto testNewTaskDto;
    private TaskDto testTaskDto;
    private Task testTask1;
    private Task testTask2;

    @Before
    @Override
    public void setUp() {

        super.setUp();
        testNewTaskDto = new NewTaskDto();
        testNewTaskDto.setTitle("Do Christmas Shopping");
        testNewTaskDto.setDetail("Turkey, crackers, prawns, presents");
        testNewTaskDto.setDueDate(Optional.of(new DateOnlyDto(2023, 12, 18)));
        testTask1 = new Task(testNewTaskDto);
        testTaskDto = new TaskDto(testTask1);
        var testTask2Dto = new TaskDto();
        testTask2Dto.setId(UUID.randomUUID());
        testTask2Dto.setTitle("Apply for leave");
        testTask2Dto.setDetail("First week of January");
        testTask2Dto.setDueDate(Optional.empty());
        testTask2 = new Task(testTask2Dto);
    }

    @Test
    public void createTask() throws Exception {

        JsonNode newTaskDtoJson = ConvertNewTaskDtoToJson(testNewTaskDto);
        var returnTask = new Task(testNewTaskDto);
        Mockito.when(mockTaskManager.createTask(newTaskDtoCaptor.capture())).thenReturn(returnTask);

        MvcResult result = mvc.perform(post("/api/task")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTaskDtoJson.toString()))
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn();

        verify(mockTaskManager, times(1)).createTask(any(NewTaskDto.class));
        assertEquals(testNewTaskDto.getTitle(), newTaskDtoCaptor.getValue().getTitle());
        assertEquals(testNewTaskDto.getDetail(), newTaskDtoCaptor.getValue().getDetail());
        assertEquals(testNewTaskDto.getDueDate().get().getDay(), newTaskDtoCaptor.getValue().getDueDate().get().getDay());
        assertEquals(testNewTaskDto.getDueDate().get().getMonth(), newTaskDtoCaptor.getValue().getDueDate().get().getMonth());
        assertEquals(testNewTaskDto.getDueDate().get().getYear(), newTaskDtoCaptor.getValue().getDueDate().get().getYear());
        JsonNode jsonResult = objectMapper.readTree(result.getResponse().getContentAsString());
        AssertJsonNodeContainsNewTaskDto(jsonResult, testNewTaskDto.getTitle(), testNewTaskDto.getDetail(), testNewTaskDto.getDueDate());
        assertEquals(returnTask.getId().toString(), jsonResult.get("id").asText());
    }

    @Test
    public void updateTask() throws Exception {

        JsonNode taskDtoJson = ConvertTaskDtoToJson(testTaskDto);

        mvc.perform(put("/api/task")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskDtoJson.toString()))
            .andExpect(status().isOk());

        verify(mockTaskManager, times(1)).updateTask(taskCaptor.capture());
        assertEquals(testTaskDto.getId(), taskCaptor.getValue().getId());
        assertEquals(testTaskDto.getTitle(), taskCaptor.getValue().getTitle());
        assertEquals(testTaskDto.getDetail(), taskCaptor.getValue().getDetail());
        assertEquals(testTaskDto.getDueDate().get().getDay(), taskCaptor.getValue().getDueDate().get().getDayOfMonth());
        assertEquals(testTaskDto.getDueDate().get().getMonth(), taskCaptor.getValue().getDueDate().get().getMonthValue());
        assertEquals(testTaskDto.getDueDate().get().getYear(), taskCaptor.getValue().getDueDate().get().getYear());
    }

    @Test
    public void updateTask_TaskWithIdDoesntExist() throws Exception {

        JsonNode taskDtoJson = ConvertTaskDtoToJson(testTaskDto);
        String mockExceptionMessage = String.format("Task with id '%s' doesn't exist.", testTaskDto.getId().toString());
        var mockException = new TaskDoesntExistException(mockExceptionMessage);
        doThrow(mockException).when(mockTaskManager).updateTask(any(Task.class));

        MvcResult result = mvc.perform(put("/api/task")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskDtoJson.toString()))
            .andExpect(status().isNotFound())
            .andReturn();

        verify(mockTaskManager, times(1)).updateTask(any(Task.class));
        JsonNode jsonResult = objectMapper.readTree(result.getResponse().getContentAsString());
        AssertJsonNodeContainsHttpErrorResponse(jsonResult, TaskDoesntExistException.class.getSimpleName(), mockExceptionMessage);
    }

    @Test
    public void getTasks() throws Exception {

        var testTasks = new ArrayList<Task>();
        testTasks.add(testTask1);
        testTasks.add(testTask2);
        Mockito.when(mockTaskManager.getAllTasks()).thenReturn(testTasks);

        MvcResult result = mvc.perform(get("/api/task")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn();
    
        verify(mockTaskManager, times(1)).getAllTasks();
        JsonNode jsonResult = objectMapper.readTree(result.getResponse().getContentAsString());
        ArrayNode jsonResultArray = (ArrayNode)jsonResult;
        assertEquals(2, jsonResultArray.size());
        AssertJsonNodeContainsTask(jsonResultArray.get(0), testTask1.getId(), testTask1.getTitle(), testTask1.getDetail(), testTask1.getDueDate());
        AssertJsonNodeContainsTask(jsonResultArray.get(1), testTask2.getId(), testTask2.getTitle(), testTask2.getDetail(), testTask2.getDueDate());
    }

    @Test
    public void deleteTask() throws Exception {
        
        JsonNode taskDtoJson = ConvertTaskDtoToJson(testTaskDto);

        mvc.perform(delete("/api/task")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskDtoJson.toString()))
            .andExpect(status().isOk());

        verify(mockTaskManager).deleteTask(taskCaptor.capture());
        assertEquals(testTaskDto.getId(), taskCaptor.getValue().getId());
        assertEquals(testTaskDto.getTitle(), taskCaptor.getValue().getTitle());
        assertEquals(testTaskDto.getDetail(), taskCaptor.getValue().getDetail());
        assertEquals(testTaskDto.getDueDate().get().getDay(), taskCaptor.getValue().getDueDate().get().getDayOfMonth());
        assertEquals(testTaskDto.getDueDate().get().getMonth(), taskCaptor.getValue().getDueDate().get().getMonthValue());
        assertEquals(testTaskDto.getDueDate().get().getYear(), taskCaptor.getValue().getDueDate().get().getYear());
    }

    @Test
    public void deleteTask_TaskWithIdDoesntExist() throws Exception {
        
        JsonNode taskDtoJson = ConvertTaskDtoToJson(testTaskDto);
        String mockExceptionMessage = String.format("Task with id '%s' doesn't exist.", testTaskDto.getId().toString());
        var mockException = new TaskDoesntExistException(mockExceptionMessage);
        doThrow(mockException).when(mockTaskManager).deleteTask(any(Task.class));

        MvcResult result = mvc.perform(delete("/api/task")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskDtoJson.toString()))
            .andExpect(status().isNotFound())
            .andReturn();

        verify(mockTaskManager, times(1)).deleteTask(any(Task.class));
        JsonNode jsonResult = objectMapper.readTree(result.getResponse().getContentAsString());
        AssertJsonNodeContainsHttpErrorResponse(jsonResult, TaskDoesntExistException.class.getSimpleName(), mockExceptionMessage);
    }

    @Test
    public void getTask() throws Exception {
        
        Mockito.when(mockTaskManager.getTask(testTask1.getId())).thenReturn(testTask1);

        MvcResult result = mvc.perform(get("/api/task/" + testTask1.getId().toString())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn();

        verify(mockTaskManager, times(1)).getTask(testTask1.getId());
        JsonNode jsonResult = objectMapper.readTree(result.getResponse().getContentAsString());
        AssertJsonNodeContainsTask(jsonResult, testTask1.getId(), testTask1.getTitle(), testTask1.getDetail(), testTask1.getDueDate());
    }

    @Test
    public void getTask_TaskWithIdDoesntExist() throws Exception {

        String mockExceptionMessage = String.format("Task with id '%s' doesn't exist.", testTaskDto.getId().toString());
        var mockException = new TaskDoesntExistException(mockExceptionMessage);
        doThrow(mockException).when(mockTaskManager).getTask(testTask1.getId());

        MvcResult result = mvc.perform(get("/api/task/" + testTask1.getId().toString())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andReturn();

        verify(mockTaskManager, times(1)).getTask(testTask1.getId());
        JsonNode jsonResult = objectMapper.readTree(result.getResponse().getContentAsString());
        AssertJsonNodeContainsHttpErrorResponse(jsonResult, TaskDoesntExistException.class.getSimpleName(), mockExceptionMessage);
    }

    //#region Private/Protected Methods

    /**
     * Asserts that a {@link JsonNode} contains the specified {@link DateOnlyDto}.
     * 
     * @param jsonNode The JSON node to check.
     * @param dueDate The expected due date.
     */
    private void AssertJsonNodeContainsLocalDate(JsonNode jsonNode, Optional<LocalDate> dueDate) {

        if (dueDate.isPresent()) {
            AssertJsonNodeContainsIntegerField(jsonNode, "year", dueDate.get().getYear());
            AssertJsonNodeContainsIntegerField(jsonNode, "month", dueDate.get().getMonthValue());
            AssertJsonNodeContainsIntegerField(jsonNode, "day", dueDate.get().getDayOfMonth());
        }
        else {
            assertTrue(jsonNode instanceof NullNode, String.format("%s %s was expected to contain null but contained '%s'", LocalDate.class.getSimpleName(), JsonNode.class.getSimpleName(), jsonNode.asText()));
        }
    }

    /**
     * Asserts that a {@link JsonNode} contains the specified {@link DateOnlyDto}.
     * 
     * @param jsonNode The JSON node to check.
     * @param dueDate The expected due date.
     */
    private void AssertJsonNodeContainsDateOnlyDto(JsonNode jsonNode, Optional<DateOnlyDto> dueDate) {

        if (dueDate.isPresent()) {
            AssertJsonNodeContainsIntegerField(jsonNode, "year", dueDate.get().getYear());
            AssertJsonNodeContainsIntegerField(jsonNode, "month", dueDate.get().getMonth());
            AssertJsonNodeContainsIntegerField(jsonNode, "day", dueDate.get().getDay());
        }
        else {
            assertTrue(jsonNode instanceof NullNode, String.format("%s %s was expected to contain null but contained '%s'", DateOnlyDto.class.getSimpleName(), JsonNode.class.getSimpleName(), jsonNode.asText()));
        }
    }

    /**
     * Asserts that the specified {@link JsonNode} contains a {@link NewTaskDto} with the specified properties.
     * 
     * @param jsonNode The JSON node to check.
     * @param title The expected task title.
     * @param detail The expected task detail.
     * @param dueDate The expected due date.
     */
    private void AssertJsonNodeContainsNewTaskDto(JsonNode jsonNode, String title, String detail, Optional<DateOnlyDto> dueDate) {

        AssertJsonNodeContainsStringField(jsonNode, "title", title);
        AssertJsonNodeContainsStringField(jsonNode, "detail", detail);
        AssertJsonNodeContainsDateOnlyDto(jsonNode.get("dueDate"), dueDate);
    }
    
    /**
     * Asserts that the specified {@link JsonNode} contains a {@link NewTaskDto} with the specified properties.
     * 
     * @param jsonNode The JSON node to check.
     * @param title The expected task title.
     * @param detail The expected task detail.
     * @param dueDate The expected due date.
     */
    private void AssertJsonNodeContainsTaskDto(JsonNode jsonNode, UUID id, String title, String detail, Optional<DateOnlyDto> dueDate) {

        AssertJsonNodeContainsNewTaskDto(jsonNode, title, detail, dueDate);
        AssertJsonNodeContainsStringField(jsonNode, "id", id.toString());
    }
    
    /**
     * Asserts that the specified {@link JsonNode} contains a {@link NewTaskDto} with the specified properties.
     * 
     * @param jsonNode The JSON node to check.
     * @param title The expected task title.
     * @param detail The expected task detail.
     * @param dueDate The expected due date.
     */
    private void AssertJsonNodeContainsTask(JsonNode jsonNode, UUID id, String title, String detail, Optional<LocalDate> dueDate) {

        AssertJsonNodeContainsStringField(jsonNode, "id", id.toString());
        AssertJsonNodeContainsStringField(jsonNode, "title", title);
        AssertJsonNodeContainsStringField(jsonNode, "detail", detail);
        AssertJsonNodeContainsLocalDate(jsonNode.get("dueDate"), dueDate);
    }
    
    //#endregion
}
