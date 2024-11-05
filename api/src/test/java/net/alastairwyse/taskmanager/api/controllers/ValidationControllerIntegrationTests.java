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

import java.util.Optional;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import net.alastairwyse.taskmanager.api.TaskManagerApi;
import net.alastairwyse.taskmanager.models.Task;
import net.alastairwyse.taskmanager.models.dtos.DateOnlyDto;
import net.alastairwyse.taskmanager.models.dtos.NewTaskDto;
import net.alastairwyse.taskmanager.models.dtos.TaskDto;
import net.alastairwyse.taskmanager.validation.PropertyValidationResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Integration tests for the ValidationController.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = TaskManagerApi.class)
@AutoConfigureMockMvc
public class ValidationControllerIntegrationTests extends IntegrationTestsBase {
    
    @Autowired
    private MockMvc mvc;

    private NewTaskDto testNewTaskDto;
    private TaskDto testTaskDto;

    @Before
    @Override
    public void setUp() {

        super.setUp();
        testNewTaskDto = new NewTaskDto();
        testNewTaskDto.setTitle("Do Christmas Shopping");
        testNewTaskDto.setDetail("Turkey, crackers, prawns, presents");
        testNewTaskDto.setDueDate(Optional.of(new DateOnlyDto(2023, 12, 18)));
        testTaskDto = new TaskDto(new Task(testNewTaskDto));
    }

    @Test
    public void validateNewTaskDto() throws Exception {
        
        testNewTaskDto.setTitle("");
        JsonNode newTaskDtoJson = ConvertNewTaskDtoToJson(testNewTaskDto);

        MvcResult result = mvc.perform(post("/api/v1/validation/newTaskDto")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTaskDtoJson.toString()))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn();

        JsonNode jsonResult = objectMapper.readTree(result.getResponse().getContentAsString());
        AssertJsonNodeContainsPropertyValidationResult(jsonResult, false, "The Title cannot be blank.", "Title");
    }

    @Test
    public void validateTaskDto() throws Exception {

        testTaskDto.setTitle("");
        JsonNode taskDtoJson = ConvertTaskDtoToJson(testTaskDto);

        MvcResult result = mvc.perform(post("/api/v1/validation/taskDto")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskDtoJson.toString()))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn();

        JsonNode jsonResult = objectMapper.readTree(result.getResponse().getContentAsString());
        AssertJsonNodeContainsPropertyValidationResult(jsonResult, false, "The Title cannot be blank.", "Title");        
    }

    //#region Private/Protected Methods

    /**
     * Asserts that the specified {@link JsonNode} contains a {@link PropertyValidationResult} with the specified properties.
     * 
     * @param jsonNode The JSON node to check.
     * @param isValid The expected 'valid' property'.
     * @param validationError The expected validation error.
     * @param propertyName The expected property name.
     */
    private void AssertJsonNodeContainsPropertyValidationResult(JsonNode jsonNode, boolean isValid, String validationError, String propertyName) {

        assertTrue(jsonNode.has("isValid"), String.format("%s %s was expected to contain an 'isValid' field but did not", PropertyValidationResult.class.getSimpleName(), JsonNode.class.getSimpleName()));
        assertTrue(jsonNode.get("isValid").isBoolean(), String.format("%s %s was expected to contain an 'isValid' field with a boolean vvalue, but value was not a boolean", PropertyValidationResult.class.getSimpleName(), JsonNode.class.getSimpleName()));
        assertEquals(isValid, jsonNode.get("isValid").asBoolean());
        AssertJsonNodeContainsStringField(jsonNode, "validationError", validationError);
        AssertJsonNodeContainsStringField(jsonNode, "propertyName", propertyName);
    }

    //#endregion
}
