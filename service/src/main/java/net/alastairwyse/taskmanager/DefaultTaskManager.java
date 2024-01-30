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

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.alastairwyse.taskmanager.models.Task;
import net.alastairwyse.taskmanager.models.TaskDoesntExistException;
import net.alastairwyse.taskmanager.models.dtos.NewTaskDto;

/**
 * Default implementation of {@link TaskManager}.
 */
public class DefaultTaskManager implements TaskManager {

    /** Stores all tasks indexed by the id of each task. */
    protected HashMap<UUID, Task> idToTaskMap;
    /** A mutliple reader, single writer lock object for the 'idToTaskMap' field. */
    protected ReentrantReadWriteLock idToTaskMapLock;

    public DefaultTaskManager() {
        idToTaskMap = new HashMap<UUID, Task>();
        idToTaskMapLock = new ReentrantReadWriteLock();
    }

    @Override
    public Task createTask(NewTaskDto newTaskDto) {
        
        var task = new Task(newTaskDto);
        idToTaskMapLock.writeLock().lock();
        try {
            idToTaskMap.put(task.getId(), task);
        }
        finally {
            idToTaskMapLock.writeLock().unlock();
        }

        return task;
    }

    @Override
    public void deleteTask(Task task) throws TaskDoesntExistException {
        
        idToTaskMapLock.writeLock().lock();
        try {
            ThrowExceptionIfTaskWithIdDoesntExist(task.getId());

            idToTaskMap.remove(task.getId());
        }
        finally {
            idToTaskMapLock.writeLock().unlock();
        }
    }

    @Override
    public Iterable<Task> getAllTasks() {

        idToTaskMapLock.readLock().lock();
        try {
            return idToTaskMap.values();
        }
        finally {
            idToTaskMapLock.readLock().unlock();
        }
    }

    @Override
    public Task getTask(UUID id) throws TaskDoesntExistException {
        
        idToTaskMapLock.readLock().lock();
        try {
            ThrowExceptionIfTaskWithIdDoesntExist(id);

            return idToTaskMap.get(id);
        }
        finally {
            idToTaskMapLock.readLock().unlock();
        }
    }
    
    @Override
    public void updateTask(Task task) throws TaskDoesntExistException {

        idToTaskMapLock.writeLock().lock();
        try {
            ThrowExceptionIfTaskWithIdDoesntExist(task.getId());

            idToTaskMap.remove(task.getId());
            idToTaskMap.put(task.getId(), task);
        }
        finally {
            idToTaskMapLock.writeLock().unlock();
        }
    }

    protected void ThrowExceptionIfTaskWithIdDoesntExist(UUID id) throws TaskDoesntExistException {
        
        if (idToTaskMap.containsKey(id) == false)
            throw new TaskDoesntExistException(String.format("A task with id '%s' does not exist in the task manager.", id));
    }
}
