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

package net.alastairwyse.taskmanager.validation;

import net.alastairwyse.taskmanager.models.dtos.NewTaskDto;
import net.alastairwyse.taskmanager.models.dtos.DateOnlyDto;

/**
 * Validator for instances of {@link NewTaskDto}
 */
public class NewTaskDtoValidator implements Validator<NewTaskDto, PropertyValidationResult> {
    
    /** Validator for {@link DateOnlyDto} instances. */
    protected DateOnlyDtoValidator dateOnlyDtoValidator;

    /**
     * Constructs a NewTaskDtoValidator.
     */
    public NewTaskDtoValidator() {

        dateOnlyDtoValidator = new DateOnlyDtoValidator();
    }

    @Override
    public PropertyValidationResult validate(NewTaskDto newTaskDto) {
        
        if (newTaskDto.getTitle() == null || newTaskDto.getTitle().isBlank()) {
            return new PropertyValidationResult(false, "The Title cannot be blank.", "Title");
        }
        if (newTaskDto.getDueDate().isPresent()) {
            ValidationResult dueDateValidationResult = dateOnlyDtoValidator.validate(newTaskDto.getDueDate().get());
            if (dueDateValidationResult.getIsValid() == false) {
                return new PropertyValidationResult(false, String.format("The DueDate failed to validate.  %s", dueDateValidationResult.getValidationError()), "DueDate");
            }
        }

        return new PropertyValidationResult(true);
    }
}
