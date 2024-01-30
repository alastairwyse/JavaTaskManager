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

package net.alastairwyse.taskmanager;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import net.alastairwyse.taskmanager.models.dtos.DateOnlyDto;
import net.alastairwyse.taskmanager.models.dtos.NewTaskDto;
import net.alastairwyse.taskmanager.models.dtos.TaskDto;
import net.alastairwyse.taskmanager.models.Task;
import net.alastairwyse.taskmanager.models.TaskDoesntExistException;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link DefaultTaskManager} class.
 */
public class DefaultTaskManagerTests {
        
    private DefaultTaskManager testDefaultTaskManager;

    @Before
    public void setUp() {

        testDefaultTaskManager = new DefaultTaskManager();
    }

    @Test
    public void createTask() {

        var testNewTaskDto = new NewTaskDto();
        testNewTaskDto.setTitle("Do Christmas Shopping");
        testNewTaskDto.setDetail("Turkey, crackers, prawns, presents");
        testNewTaskDto.setDueDate(Optional.of(new DateOnlyDto(2023, 12, 18)));

        Task result = testDefaultTaskManager.createTask(testNewTaskDto);

        
        Task createdTask = null;
        try {
            createdTask = testDefaultTaskManager.getTask(result.getId());
        }
        catch (Exception e) {
            fail("Unexpected exception thrown.");
        }
        assertSame(result, createdTask);
    }
    
    @Test
    public void deleteTask_TaskWithIdDoesntExist() {

        var testTaskDto = new TaskDto();
        testTaskDto.setId(UUID.randomUUID());
        testTaskDto.setTitle("Do Christmas Shopping");
        testTaskDto.setDetail("Turkey, crackers, prawns, presents");
        testTaskDto.setDueDate(Optional.of(new DateOnlyDto(2023, 12, 18)));
        var testTask = new Task(testTaskDto);

        TaskDoesntExistException e = assertThrows(TaskDoesntExistException.class, () -> 
        {
            testDefaultTaskManager.deleteTask(testTask);
        });

        assertTrue(e.getMessage().contains(String.format("A task with id '%s' does not exist in the task manager.", testTask.getId())));
    }

    @Test
    public void deleteTask() {

        var testTaskDto = new TaskDto();
        testTaskDto.setId(UUID.randomUUID());
        testTaskDto.setTitle("Do Christmas Shopping");
        testTaskDto.setDetail("Turkey, crackers, prawns, presents");
        testTaskDto.setDueDate(Optional.of(new DateOnlyDto(2023, 12, 18)));
        Task testTask = testDefaultTaskManager.createTask(testTaskDto);
        
        try {
            testDefaultTaskManager.deleteTask(testTask);
        }
        catch (Exception e) {
            fail("Unexpected exception thrown.");
        }

        assertEquals(0, StreamSupport.stream(testDefaultTaskManager.getAllTasks().spliterator(), false).count());
    }

    @Test
    public void getTask_TaskWithIdDoesntExist() {

        TaskDoesntExistException e = assertThrows(TaskDoesntExistException.class, () -> 
        {
            testDefaultTaskManager.getTask(UUID.fromString("287acea2-21ff-4a42-b379-af6830bd2066"));
        });

        assertTrue(e.getMessage().contains("A task with id '287acea2-21ff-4a42-b379-af6830bd2066' does not exist in the task manager."));
    }

    @Test
    public void getTask() {

        var testTaskDto = new TaskDto();
        testTaskDto.setId(UUID.randomUUID());
        testTaskDto.setTitle("Do Christmas Shopping");
        testTaskDto.setDetail("Turkey, crackers, prawns, presents");
        testTaskDto.setDueDate(Optional.of(new DateOnlyDto(2023, 12, 18)));
        Task testTask = testDefaultTaskManager.createTask(testTaskDto);

        Task result = null;
        try {
            result = testDefaultTaskManager.getTask(testTask.getId());
        }
        catch (Exception e) {
            fail("Unexpected exception thrown.");
        }

        assertEquals(testTask.getId(), result.getId());
        assertEquals(testTask.getTitle(), result.getTitle());
        assertEquals(testTask.getDetail(), result.getDetail());
        assertEquals(testTask.getDueDate(), result.getDueDate());
    }

    @Test
    public void getAllTasks() {

        assertEquals(0, StreamSupport.stream(testDefaultTaskManager.getAllTasks().spliterator(), false).count());
        var testTaskDto1 = new TaskDto();
        testTaskDto1.setId(UUID.randomUUID());
        testTaskDto1.setTitle("Do Christmas Shopping");
        testTaskDto1.setDetail("Turkey, crackers, prawns, presents");
        testTaskDto1.setDueDate(Optional.of(new DateOnlyDto(2023, 12, 18)));
        var testTaskDto2 = new TaskDto();
        testTaskDto2.setId(UUID.randomUUID());
        testTaskDto2.setTitle("Apply for leave");
        testTaskDto2.setDetail("First week of January");
        testTaskDto2.setDueDate(Optional.of(new DateOnlyDto(2023, 12, 02)));
        testDefaultTaskManager.createTask(testTaskDto1);
        testDefaultTaskManager.createTask(testTaskDto2);
        var result = new ArrayList<Task>();

        for (Task currentTask : testDefaultTaskManager.getAllTasks()) {
            result.add(currentTask);
        }

        Comparator<Task> taskComparator = (Task task1, Task task2) -> 
        { 
            return task1.getTitle().compareTo(task2.getTitle());
        };
        Collections.sort(result, taskComparator);
        assertEquals(testTaskDto1.getTitle(), result.get(1).getTitle());
        assertEquals(testTaskDto1.getDetail(), result.get(1).getDetail());
        assertEquals(testTaskDto1.getDueDate().get().getYear(), result.get(1).getDueDate().get().getYear());
        assertEquals(testTaskDto1.getDueDate().get().getMonth(), result.get(1).getDueDate().get().getMonthValue());
        assertEquals(testTaskDto1.getDueDate().get().getDay(), result.get(1).getDueDate().get().getDayOfMonth());
        assertEquals(testTaskDto2.getTitle(), result.get(0).getTitle());
        assertEquals(testTaskDto2.getDetail(), result.get(0).getDetail());
        assertEquals(testTaskDto2.getDueDate().get().getYear(), result.get(0).getDueDate().get().getYear());
        assertEquals(testTaskDto2.getDueDate().get().getMonth(), result.get(0).getDueDate().get().getMonthValue());
        assertEquals(testTaskDto2.getDueDate().get().getDay(), result.get(0).getDueDate().get().getDayOfMonth());
    }
    
    @Test
    public void updateTask_TaskWithIdDoesntExist() {

        var testTaskDto = new TaskDto();
        testTaskDto.setId(UUID.fromString("287acea2-21ff-4a42-b379-af6830bd2066"));
        testTaskDto.setTitle("Do Christmas Shopping");
        testTaskDto.setDetail("Turkey, crackers, prawns, presents");
        testTaskDto.setDueDate(Optional.of(new DateOnlyDto(2023, 12, 18)));
        var testTask = new Task(testTaskDto);

        TaskDoesntExistException e = assertThrows(TaskDoesntExistException.class, () -> 
        {
            testDefaultTaskManager.updateTask(testTask);
        });

        assertTrue(e.getMessage().contains("A task with id '287acea2-21ff-4a42-b379-af6830bd2066' does not exist in the task manager."));
    }

}
