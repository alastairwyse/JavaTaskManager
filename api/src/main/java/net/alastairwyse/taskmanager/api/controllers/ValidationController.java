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

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import net.alastairwyse.taskmanager.models.dtos.NewTaskDto;
import net.alastairwyse.taskmanager.models.dtos.TaskDto;
import net.alastairwyse.taskmanager.validation.NewTaskDtoValidator;
import net.alastairwyse.taskmanager.validation.PropertyValidationResult;

/**
 * Controller which validates data sent to the API.
 */
@RestController
@RequestMapping("/api/validation")
@Tag(name = "Validation")
public class ValidationController {
    
    /**
     * Constructs a ValidationController.
     */
    public ValidationController() {
    }

    /**
     * Validates the specified {@link NewTaskDto}.
     * 
     * @param newTaskDto The {@link NewTaskDto} to validate.
     * @return The result of the validation.
     */
    @Operation(summary = "Validates a new task")
    @PostMapping("newTaskDto")
    @ApiResponse(responseCode = "200", description = "Validation completed")
    public PropertyValidationResult validateNewTaskDto(@RequestBody NewTaskDto newTaskDto) {

        var validator = new NewTaskDtoValidator();

        return validator.validate(newTaskDto);
    }

    /**
     * Validates the specified {@link TaskDto}.
     * 
     * @param taskDto The {@link TaskDto} to validate.
     * @return The result of the validation.
     */
    @Operation(summary = "Validates a task")
    @PostMapping("taskDto")
    @ApiResponse(responseCode = "200", description = "Validation completed")
    public PropertyValidationResult validateTaskDto(@RequestBody TaskDto taskDto) {

        var validator = new NewTaskDtoValidator();

        return validator.validate(taskDto);
    }
}
