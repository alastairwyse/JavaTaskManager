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

import net.alastairwyse.taskmanager.models.dtos.NewTaskDto;

import java.util.UUID;

import net.alastairwyse.taskmanager.models.Task;
import net.alastairwyse.taskmanager.models.TaskDoesntExistException;

/**
 * Defines methods for a class which manages a set of tasks.
 */
public interface TaskManager {
    
    /**
     * Returns all tasks in the manager.
     * 
     * @return All tasks.
     */
    Iterable<Task> getAllTasks();

    /**
     * Creates a task from the specified {@link NewTaskDto}.
     * 
     * @param newTaskDto The {@link NewTaskDto} to create the task from.
     * @return The new {@link Task} created from the {@link NewTaskDto}.
     */
    Task createTask(NewTaskDto newTaskDto);

    /**
     * Updates the specified task in the manager.
     * 
     * @param task The {@link Task} to update.
     * @throws TaskDoesntExistException If the specified task doesn't exist in the manager.
     */
    void updateTask(Task task) throws TaskDoesntExistException;

    /**
     * Deletes the specified task from the manager.
     * 
     * @param task The {@link Task} to delete.
     * @throws TaskDoesntExistException If the specified task doesn't exist in the manager.
     */
    void deleteTask(Task task) throws TaskDoesntExistException;

    /**
     * Retrieves the task with the specified id from the manager.
     * 
     * @param id The id of the task to retrieve.
     * @throws TaskDoesntExistException If the specified task doesn't exist in the manager.
     * @return The task.
     */
    Task getTask(UUID id) throws TaskDoesntExistException;
}
