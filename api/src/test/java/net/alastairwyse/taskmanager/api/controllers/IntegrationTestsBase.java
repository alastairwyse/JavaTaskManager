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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.util.Assert;

import net.alastairwyse.taskmanager.api.models.HttpErrorResponse;
import net.alastairwyse.taskmanager.models.dtos.NewTaskDto;
import net.alastairwyse.taskmanager.models.dtos.TaskDto;

import org.junit.Before;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Base for integration test classes.
 */
public abstract class IntegrationTestsBase {
    
    protected ObjectMapper objectMapper;

    @Before
    public void setUp() {

        objectMapper = new ObjectMapper();
    }

    /**
     * Asserts that a {@link JsonNode} contains a field with the specified name.
     * 
     * @param jsonNode The JSON node to check.
     * @param fieldName The name of the field.
     */
    protected void AssertJsonNodeContainsField(JsonNode jsonNode, String fieldName) {

        if (jsonNode.get(fieldName) == null)
            fail(String.format("%s did not contain a '%s' field", JsonNode.class.getSimpleName(), fieldName));
    }

    /**
     * Asserts that the specified {@link JsonNode} contains a string field with the specified value.
     * 
     * @param jsonNode The JSON node to check.
     * @param fieldName The name of the field.
     * @param fieldValue The value of the field.
     */
    protected void AssertJsonNodeContainsStringField(JsonNode jsonNode, String fieldName, String fieldValue) {

        AssertJsonNodeContainsField(jsonNode, fieldName);
        if (!jsonNode.get(fieldName).asText().equals(fieldValue))
            fail(String.format("%s field '%s' was expected to contain '%s' but contained '%s'", JsonNode.class.getSimpleName(), fieldName, fieldValue, jsonNode.get(fieldName).asText()));
    }

    /**
     * Asserts that the specified {@link JsonNode} contains an integer field with the specified value.
     * 
     * @param jsonNode The JSON node to check.
     * @param fieldName The name of the field.
     * @param fieldValue The value of the field.
     */
    protected void AssertJsonNodeContainsIntegerField(JsonNode jsonNode, String fieldName, int fieldValue) {

        AssertJsonNodeContainsField(jsonNode, fieldName);
        if (!jsonNode.get(fieldName).isInt())
            fail(String.format("%s field '%s' was expected to contain an integer value but contained '%s'", JsonNode.class.getSimpleName(), fieldName, jsonNode.get(fieldName).asText()));
        int actualValue = jsonNode.get(fieldName).asInt();
        if (fieldValue != actualValue)
            fail(String.format("%s field '%s' was expected to contain %d but contained %d", JsonNode.class.getSimpleName(), fieldName, fieldValue, actualValue));
    }

    /**
     * Asserts that the specified {@link JsonNode} contains a {@link NewTaskDto} with the specified properties.
     * 
     * @param jsonNode The JSON node to check.
     * @param title The expected task title.
     * @param detail The expected task detail.
     * @param dueDate The expected due date.
     */
    protected void AssertJsonNodeContainsHttpErrorResponse(JsonNode jsonNode, String code, String message) {

        AssertJsonNodeContainsStringField(jsonNode, "code", code);
        AssertJsonNodeContainsStringField(jsonNode, "message", message);
        Assert.isTrue(jsonNode.has("attributes"), String.format("%s was expected to contain an 'attributes' field, but did not.", HttpErrorResponse.class.getSimpleName()));
        Assert.isTrue(jsonNode.has("innerError"), String.format("%s was expected to contain an 'innerError' field, but did not.", HttpErrorResponse.class.getSimpleName()));
    }
    
    /**
     * Converts the specifed {@link NewTaskDto} to a JSON {@link ObjectNode}.
     * 
     * @param newTaskDto The {@link NewTaskDto} to convert.
     * @return An {@link ObjectNode} representing the task as JSON.
     */
    protected ObjectNode ConvertNewTaskDtoToJson(NewTaskDto newTaskDto) {

        ObjectNode returnNode = objectMapper.createObjectNode();
        JsonNode dueDateFieldValue;
        if (newTaskDto.getDueDate().isPresent() == true) {
            ObjectNode dueDateNode = objectMapper.createObjectNode();
            dueDateNode.put("year", newTaskDto.getDueDate().get().getYear());
            dueDateNode.put("month", newTaskDto.getDueDate().get().getMonth());
            dueDateNode.put("day", newTaskDto.getDueDate().get().getDay());
            dueDateFieldValue = dueDateNode;
        }
        else {
            dueDateFieldValue = objectMapper.nullNode();
        }
        returnNode.set("dueDate", dueDateFieldValue);
        returnNode.put("title", newTaskDto.getTitle());
        returnNode.put("detail", newTaskDto.getDetail());

        return returnNode;
    }

    /**
     * Converts the specifed {@link TaskDto} to a JSON {@link ObjectNode}.
     * 
     * @param taskDto The {@link TaskDto} to convert.
     * @return A {@link JsonNode} representing the task as JSON.
     */
    protected JsonNode ConvertTaskDtoToJson(TaskDto taskDto) {

        ObjectNode returnNode = ConvertNewTaskDtoToJson(taskDto);
        returnNode.put("id", taskDto.getId().toString());

        return returnNode;
    }
}
