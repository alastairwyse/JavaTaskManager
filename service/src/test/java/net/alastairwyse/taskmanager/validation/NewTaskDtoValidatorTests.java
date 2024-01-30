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

import java.util.Optional;

import net.alastairwyse.taskmanager.models.dtos.DateOnlyDto;
import net.alastairwyse.taskmanager.models.dtos.NewTaskDto;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link NewTaskDtoValidator} class.
 */
public class NewTaskDtoValidatorTests {
        
    private NewTaskDtoValidator testNewTaskDtoValidator;

    @Before
    public void setUp() {

        testNewTaskDtoValidator = new NewTaskDtoValidator();
    }

    @Test
    public void validate_TitleNull() {

        var testNewTaskDto = new NewTaskDto();
        testNewTaskDto.setTitle(null);
        testNewTaskDto.setDetail("Turkey, crackers, prawns, presents");

        ValidationResult result = testNewTaskDtoValidator.validate(testNewTaskDto);

        assertEquals(false, result.getIsValid());
        assertEquals("The Title cannot be blank.", result.getValidationError());
    }

    @Test
    public void validate_TitleBlank() {

        var testNewTaskDto = new NewTaskDto();
        testNewTaskDto.setTitle(" ");
        testNewTaskDto.setDetail("Turkey, crackers, prawns, presents");

        ValidationResult result = testNewTaskDtoValidator.validate(testNewTaskDto);

        assertEquals(false, result.getIsValid());
        assertEquals("The Title cannot be blank.", result.getValidationError());
    }

    @Test
    public void validate_DueDateInvalid() {

        var testNewTaskDto = new NewTaskDto();
        testNewTaskDto.setTitle("Do Christmas Shopping");
        testNewTaskDto.setDetail("Turkey, crackers, prawns, presents");
        testNewTaskDto.setDueDate(Optional.of(new DateOnlyDto(2023, 12, 32)));

        ValidationResult result = testNewTaskDtoValidator.validate(testNewTaskDto);

        assertEquals(false, result.getIsValid());
        assertEquals("The DueDate failed to validate.  Year 2023, month 12, and day of month 32 could not be converted to a valid date.", result.getValidationError());
    }

    @Test
    public void validate() {

        var testNewTaskDto = new NewTaskDto();
        testNewTaskDto.setTitle("Do Christmas Shopping");
        testNewTaskDto.setDetail("Turkey, crackers, prawns, presents");
        testNewTaskDto.setDueDate(Optional.of(new DateOnlyDto(2023, 12, 20)));

        ValidationResult result = testNewTaskDtoValidator.validate(testNewTaskDto);

        assertEquals(true, result.getIsValid());
    }
}
