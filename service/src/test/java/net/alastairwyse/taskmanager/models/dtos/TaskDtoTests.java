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

package net.alastairwyse.taskmanager.models.dtos;

import java.util.UUID;
import java.util.Optional;

import net.alastairwyse.taskmanager.models.Task;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link TaskDto} class.
 */
public class TaskDtoTests {
    
    @Before
    public void setUp() {

    }

    @Test
    public void constructorWithTaskParameter_TaskParameterHasNonNullDueDate() {
        var expectedTaskDto = new TaskDto();
        expectedTaskDto.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        expectedTaskDto.setTitle("Do Christmas Shopping");
        expectedTaskDto.setDetail("Turkey, crackers, prawns, presents");
        expectedTaskDto.setDueDate(Optional.of(new DateOnlyDto(2023, 11, 27)));
        var testTask = new Task(expectedTaskDto);

        var testTaskDto = new TaskDto(testTask);

        assertEquals(expectedTaskDto.getId(), testTaskDto.getId());
        assertEquals(expectedTaskDto.getTitle(), testTaskDto.getTitle());
        assertEquals(expectedTaskDto.getDetail(), testTaskDto.getDetail());
        assertEquals(expectedTaskDto.getDueDate().get().getDay(), testTaskDto.getDueDate().get().getDay());
        assertEquals(expectedTaskDto.getDueDate().get().getMonth(), testTaskDto.getDueDate().get().getMonth());
        assertEquals(expectedTaskDto.getDueDate().get().getYear(), testTaskDto.getDueDate().get().getYear());
    }

    @Test
    public void constructorWithTaskParameter_TaskParameterHasNullDueDate() {
        var expectedTaskDto = new TaskDto();
        expectedTaskDto.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        expectedTaskDto.setTitle("Do Christmas Shopping");
        expectedTaskDto.setDetail("Turkey, crackers, prawns, presents");
        var testTask = new Task(expectedTaskDto);

        var testTaskDto = new TaskDto(testTask);

        assertEquals(expectedTaskDto.getId(), testTaskDto.getId());
        assertEquals(expectedTaskDto.getTitle(), testTaskDto.getTitle());
        assertEquals(expectedTaskDto.getDetail(), testTaskDto.getDetail());
        assertFalse(expectedTaskDto.getDueDate().isPresent());
    }
}
