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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller which exposes the current deployment environment of the application (e.g. 'Development, 'Staging', 'UAT', 'Production', etc...).
 */
@RestController
@RequestMapping("/api/v1/environment")
@Tag(name = "Environment")
public class EnvironmentController {
    
    /** 
     * Gets the current deployment environment.
     * 
     * @return The current environment
     */
    @Operation(summary = "Returns the current deployment environment")
    @GetMapping("")
    @ApiResponse(responseCode = "200")
    public String getEnvironment() {

        return "local-development";
    }
}
