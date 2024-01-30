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

package net.alastairwyse.taskmanager.models;

import java.util.Optional;
import java.util.UUID;
import java.time.LocalDate;

import net.alastairwyse.taskmanager.validation.NewTaskDtoValidator;
import net.alastairwyse.taskmanager.validation.ValidationResult;
import net.alastairwyse.taskmanager.conversion.DateOnlyDtoConverter;
import net.alastairwyse.taskmanager.models.dtos.NewTaskDto;
import net.alastairwyse.taskmanager.models.dtos.TaskDto;

/**
 * A task managed by the TaskManager
 */
public class Task {
    
    /** A unique id for the task. */
    protected UUID id;
    /** The title of heading of the task. */
    protected String title;
    /** The detail of the task. */
    protected String detail;
    /** The optional due date of the task. */
    protected Optional<LocalDate> dueDate;
    
    /**
     * @return A unique id for the task.
     */
    public UUID getId() {
        return id;
    }
    
    /**
     * @return The title or heading of the task.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The detail of the task.
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @return The optional due date of the task.
     */
    public Optional<LocalDate> getDueDate() {
        return dueDate;
    }

    /**
     * Constructs a Task.
     * 
     * @param newTaskDto The {@link NewTaskDto} to create the task from.
     */
    public Task(NewTaskDto newTaskDto) {

        var validator = new NewTaskDtoValidator();
        ValidationResult validationResult = validator.validate(newTaskDto);
        if (validationResult.getIsValid() == false) {
            throw new IllegalArgumentException(String.format("Failed to create Task instance.  %s", validationResult.getValidationError()));
        }

        id = UUID.randomUUID();
        title = newTaskDto.getTitle();
        detail = newTaskDto.getDetail();
        if (newTaskDto.getDueDate().isPresent() == true) {
            var converter = new DateOnlyDtoConverter();
            try {
                dueDate = Optional.of(converter.convert(newTaskDto.getDueDate().get()));
            }
            catch (Exception e) {
                // This should not happen, since 'newTaskDto' was validated above
            }
        }
        else {
            dueDate = Optional.empty();
        }
    }

    /**
     * Constructs a Task.
     * 
     * @param taskDto The {@link TaskDto} to create the task from.
     */
    public Task(TaskDto taskDto)
    {
        this((NewTaskDto)taskDto);
        id = taskDto.getId();
    }
}
