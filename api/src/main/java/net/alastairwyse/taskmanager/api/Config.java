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

package net.alastairwyse.taskmanager.api;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import net.alastairwyse.taskmanager.*;
import net.alastairwyse.taskmanager.models.dtos.DateOnlyDto;
import net.alastairwyse.taskmanager.models.dtos.TaskDto;

// References
//  https://www.tabnine.com/code/java/classes/io.swagger.v3.oas.annotations.info.Info

/**
 * Common configuration for the task manager REST API.
 */
@Configuration
    @OpenAPIDefinition(
        info = @Info(
            title = "Task Manager",
            version = "1.0.0",
            description = "Simple task manager API"
        )
)
public class Config {
    
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public TaskManager TaskManagerBean() {
        var returnTaskManager = new DefaultTaskManager();
        // TODO: Remove test tasks
        var testTaskDto1 = new TaskDto();
        testTaskDto1.setId(UUID.randomUUID());
        testTaskDto1.setTitle("Do Christmas Shopping");
        testTaskDto1.setDetail("Turkey, crackers, prawns, presents");
        testTaskDto1.setDueDate(Optional.of(new DateOnlyDto(2023, 12, 18)));
        var testTaskDto2 = new TaskDto();
        testTaskDto2.setId(UUID.randomUUID());
        testTaskDto2.setTitle("Apply for leave");
        testTaskDto2.setDetail("First week of January");
        testTaskDto2.setDueDate(Optional.empty());
        returnTaskManager.createTask(testTaskDto1);
        returnTaskManager.createTask(testTaskDto2);

        return returnTaskManager;
    }
}
