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

import net.alastairwyse.taskmanager.models.dtos.DateOnlyDto;
import net.alastairwyse.taskmanager.models.dtos.TaskDto;

import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link Task} class.
 */
public class TaskTests {
    
    @Before
    public void setUp() {

    }

    @Test
    public void constructorWithTaskDtoParameter_TaskDtoParameterHasInvalidDueDate() {

        var expectedTaskDto = new TaskDto();
        expectedTaskDto.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        expectedTaskDto.setTitle("Do Christmas Shopping");
        expectedTaskDto.setDetail("Turkey, crackers, prawns, presents");
        expectedTaskDto.setDueDate(Optional.of(new DateOnlyDto(0, 0, 0)));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> 
        {
            var testTask = new Task(expectedTaskDto);
        });

        assertTrue(e.getMessage().contains("Failed to create Task instance.  The DueDate failed to validate.  Year 0, month 0, and day of month 0 could not be converted to a valid date."));
    }
}
