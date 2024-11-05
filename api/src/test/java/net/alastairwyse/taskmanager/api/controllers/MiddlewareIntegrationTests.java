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

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import net.alastairwyse.taskmanager.TaskManager;
import net.alastairwyse.taskmanager.api.TaskManagerApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Integration tests custom middleware components (e.g. custom error handling).
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = TaskManagerApi.class)
@AutoConfigureMockMvc
public class MiddlewareIntegrationTests extends IntegrationTestsBase {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private TaskManager mockTaskManager;

    private ObjectMapper objectMapper;
    
    @Before
    public void setUp() {

        objectMapper = new ObjectMapper();
    }

    @Test
    public void nonJsonAcceptHeader() throws Exception {

        MvcResult result = mvc.perform(get("/api/v1/task")
                .accept(MediaType.APPLICATION_PDF))
            .andExpect(status().isNotAcceptable())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn();

        JsonNode jsonResult = objectMapper.readTree(result.getResponse().getContentAsString());
        AssertJsonNodeContainsHttpErrorResponse(jsonResult, "ContentTypeNotAcceptable", "'Accept' header did not contain an acceptable content type.  Acceptable values are '*/*, application/json'.");
    }

    @Test
    public void invalidUrlPath() throws Exception {

        MvcResult result = mvc.perform(get("/api/v1/invalid")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn();

        JsonNode jsonResult = objectMapper.readTree(result.getResponse().getContentAsString());
        AssertJsonNodeContainsHttpErrorResponse(jsonResult, NoResourceFoundException.class.getSimpleName(), "No resource exists at URL 'api/v1/invalid'.");
        assertTrue(jsonResult.get("attributes") instanceof ArrayNode); 
        ArrayNode attributes = (ArrayNode)jsonResult.get("attributes");
        assertEquals(2, attributes.size());
        assertTrue(attributes.get(0).has("ResourcePath"));
        assertEquals("api/v1/invalid", attributes.get(0).get("ResourcePath").asText());
        assertTrue(attributes.get(1).has("HttpMethod"));
        assertEquals("GET", attributes.get(1).get("HttpMethod").asText());
    }

    @Test
    public void unsupportedHttpMethod() throws Exception {

        MvcResult result = mvc.perform(patch("/api/v1/task")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isMethodNotAllowed())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn();

        JsonNode jsonResult = objectMapper.readTree(result.getResponse().getContentAsString());
        AssertJsonNodeContainsHttpErrorResponse(jsonResult, HttpRequestMethodNotSupportedException.class.getSimpleName(), "Request method 'PATCH' is not supported");
        assertTrue(jsonResult.get("attributes") instanceof ArrayNode); 
        ArrayNode attributes = (ArrayNode)jsonResult.get("attributes");
        assertEquals(1, attributes.size());
        assertTrue(attributes.get(0).has("HttpMethod"));
        assertEquals("PATCH", attributes.get(0).get("HttpMethod").asText());
    }

    @Test
    public void invalidUrlParameter() throws Exception {

        MvcResult result = mvc.perform(get("/api/v1/task/123")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andReturn();

        JsonNode jsonResult = objectMapper.readTree(result.getResponse().getContentAsString());
        AssertJsonNodeContainsHttpErrorResponse(jsonResult, MethodArgumentTypeMismatchException.class.getSimpleName(), "Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; Invalid UUID string: 123");
        assertTrue(jsonResult.get("attributes") instanceof ArrayNode); 
        ArrayNode attributes = (ArrayNode)jsonResult.get("attributes");
        assertEquals(1, attributes.size());
        assertTrue(attributes.get(0).has("ArgumentName"));
        assertEquals("id", attributes.get(0).get("ArgumentName").asText());
        assertTrue(jsonResult.get("innerError") instanceof JsonNode); 
        JsonNode innerError = (JsonNode)jsonResult.get("innerError");
        AssertJsonNodeContainsHttpErrorResponse(innerError, IllegalArgumentException.class.getSimpleName(), "Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; Invalid UUID string: 123");
    }

    @Test
    public void missingBodyParameter() throws Exception {

        MvcResult result = mvc.perform(post("/api/v1/task")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andReturn();

        JsonNode jsonResult = objectMapper.readTree(result.getResponse().getContentAsString());
        AssertJsonNodeContainsStringField(jsonResult, "code", HttpMessageNotReadableException.class.getSimpleName());
        assertTrue(jsonResult.get("message").asText().startsWith("Required request body is missing:"));
    }
}
