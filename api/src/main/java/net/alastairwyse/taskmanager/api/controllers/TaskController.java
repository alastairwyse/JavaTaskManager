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

package net.alastairwyse.taskmanager.api.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import net.alastairwyse.taskmanager.*;
import net.alastairwyse.taskmanager.api.models.HttpErrorResponse;
import net.alastairwyse.taskmanager.models.*;
import net.alastairwyse.taskmanager.models.dtos.*;

/**
 * Controller which exposes CRUD methods for managing a collection of {@link Task} objects.
 */
@RestController
@RequestMapping("/api/task")
@Tag(name = "Task")
public class TaskController {
    
    protected final TaskManager taskManager;

    /**
     * Constructs a TaskController.
     */
    public TaskController(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    /**
     * Creates a task from the specified {@link NewTaskDto}.
     * 
     * @param newTaskDto The {@link NewTaskDto} to create the task from.
     * @return The new {@link Task} created from the {@link NewTaskDto}.
     */
    @Operation(summary = "Creates a new task")
    @PostMapping("")
    @ApiResponse(responseCode = "201", description = "Task created successfully")
    public ResponseEntity<TaskDto> createTask(@RequestBody NewTaskDto newTaskDto) throws Exception, IllegalArgumentException {

        Task newTask = taskManager.createTask(newTaskDto);
        
        return new ResponseEntity<TaskDto>(new TaskDto(newTask), HttpStatus.CREATED); 
    }

    /**
     * Updates the specified task in the manager.
     * 
     * @param taskDto The updated {@link TaskDto}.
     */
    @Operation(summary = "Updates a task")
    @PutMapping("")
    @ApiResponse(responseCode = "200", description = "Task updated successfully")
    @ApiResponse(responseCode = "404", description = "A task with the specified id does not exist", content = @Content)
    public ResponseEntity<?> updateTask(@RequestBody TaskDto taskDto) throws TaskDoesntExistException {

        var task = new Task(taskDto);
        taskManager.updateTask(task);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * Returns all tasks in the manager.
     * 
     * @return All tasks
     */
    @Operation(summary = "Returns all tasks")
    @GetMapping("")
    @ApiResponse(responseCode = "200")
	public Iterable<TaskDto> getTasks() {

		var allTasks = new ArrayList<Task>();
        for (Task currentTask : taskManager.getAllTasks()) {
            allTasks.add(currentTask);
        }
        Function<Task, TaskDto> conversionFunction = (Task inputTask) -> {
            return new TaskDto(inputTask);
        };

	   	return new ArrayListIteratorConverter<Task, TaskDto>(allTasks, conversionFunction);
	}

    /**
     * Deletes the specified task from the manager.
     * 
     * @param taskDto The {@link TaskDto} to delete.
     */
    @Operation(summary = "Deletes a task")
    @DeleteMapping("")
    @ApiResponse(responseCode = "200", description = "Task deleted successfully")
    @ApiResponse(responseCode = "404", description = "A task with the specified id does not exist", content = @Content)
    public ResponseEntity<?> deleteTask(@RequestBody TaskDto taskDto) throws TaskDoesntExistException {

        var task = new Task(taskDto);
        taskManager.deleteTask(task);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Operation(summary = "Retrieves the task with the specified id")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Task retrieved successfully")
    @ApiResponse(responseCode = "404", description = "A task with the specified id does not exist", content = @Content)
    public TaskDto getTask(
        @Parameter(description = "The id of the task to retrieve")
        @PathVariable(value="id") UUID id
    ) throws TaskDoesntExistException {

        return new TaskDto(taskManager.getTask(id));
    }

    //#region Exception to HTTP Status Mapping

    @ExceptionHandler({TaskDoesntExistException.class})
    public ResponseEntity<?> processException(TaskDoesntExistException taskDoesntExistException) {
        var errorResponse = new HttpErrorResponse(TaskDoesntExistException.class.getSimpleName(), taskDoesntExistException.getMessage());
        return new ResponseEntity<HttpErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
    }

    //#endregion

    /**
     * Implements {@link Iterable} and {@link Iterator}, applying a specified conversion function to each element in an {@link ArrayList}.
     * 
     * @param <TIn> The type of the elements in the {@link ArrayList}.
     * @param <TOut> The type the elements of the {@link ArrayList} are converted to.
     */
    protected class ArrayListIteratorConverter<TIn, TOut> implements Iterable<TOut>, Iterator<TOut> {

        protected ArrayList<TIn> inputArray;
        protected Function<TIn, TOut> conversionFunction;
        protected int currentIndex;

        /**
         * Constructs an ArrayListIteratorConverter.
         * 
         * @param inputArray The {@link ArrayList} to apply the conversion function to.
         * @param conversionFunction A function which converts elements of the {@link ArrayList} to type {@link TOut}
         */
        public ArrayListIteratorConverter(ArrayList<TIn> inputArray, Function<TIn, TOut> conversionFunction) {
            this.inputArray = inputArray;
            this.conversionFunction = conversionFunction;
            currentIndex = 0;
        }

        @Override
        public Iterator<TOut> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            return (currentIndex < inputArray.size());
        }

        @Override
        public TOut next() {
            currentIndex++;
            return conversionFunction.apply(inputArray.get(currentIndex - 1));
        }
    }
}
