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

import net.alastairwyse.taskmanager.models.Task;

/**
 * DTO for new {@link Task} objects.
 */
public class NewTaskDto {
    
    /** The title or heading of the task. */
    protected String title;
    /** The detail of the task. */
    protected String detail;
    /** The optional due date of the task. */
    protected Optional<DateOnlyDto> dueDate;

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
    public Optional<DateOnlyDto> getDueDate() {
        return dueDate;
    }

    /**
     * @param title The title or heading of the task.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @param detail The detail of the task.
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * @param dueDate The optional due date of the task.
     */
    public void setDueDate(Optional<DateOnlyDto> dueDate) {

        if (dueDate == null) 
            throw new IllegalArgumentException("Parameter 'dueDate' cannot be null.");

        this.dueDate = dueDate;
    }

    /**
     * Constructs a NewTaskDto.
     */
    public NewTaskDto() {
        title = "";
        detail = "";
        dueDate = Optional.empty();
    }
}
