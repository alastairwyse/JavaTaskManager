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

import java.util.Optional;
import java.util.UUID;

import net.alastairwyse.taskmanager.models.Task;

/**
 * DTO equivalent of {@link Task} objects.
 */
public class TaskDto extends NewTaskDto {
    
    /** A unique id for the task. */
    protected UUID id;

    /**
     * @return A unique id for the task.
     */
    public UUID getId() {
        return id;
    }

    /**
     * @param id A unique id for the task.
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Constructs a TaskDto.
     */
    public TaskDto() {
        super();
        id = UUID.randomUUID();
    }

    /**
     * Constructs a TaskDto from and instance of {@link Task}.
     * 
     * @param task The {@link Task} to create the TaskDto from.
     */
    public TaskDto(Task task) {
        id = task.getId();
        title = task.getTitle();
        detail = task.getDetail();
        if (task.getDueDate().isPresent() == true) {
            var dateOnlyDto = new DateOnlyDto(
                task.getDueDate().get().getYear(), 
                task.getDueDate().get().getMonthValue(), 
                task.getDueDate().get().getDayOfMonth()
            );
            dueDate = Optional.of(dateOnlyDto);
        }
        else {
            dueDate = Optional.empty();
        }
    }
}
