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

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import net.alastairwyse.taskmanager.api.models.HttpErrorResponse;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.boot.web.servlet.error.ErrorController;

/**
 * Controller which overrides the Spring Boot standard '/error' route/page with a JSON error response.
 * Specific errors stemming from exceptions are handled/mapped by @ExceptionHandler annotations within the controllers.  This basically handles unmapped URLs.
 * 
 * TODO: Actually this is no longer required, as any unhandled exceptions (including NoResourceFoundException which be default routes to /error) are now caught 
 *   by the @ExceptionHandler for Exception defined in GlobalControllerExceptionHandler.  Will leave this class in the short term for reference (i.e. to show
 *   how to override the spring boot 'whitelabel' exception page if you're not using @ExceptionHandler annotations).
 */
@RestController
@Hidden
public class CustomErrorController implements ErrorController {
	
	/**
	 * @returns An {@link HttpErrorResponse} stating that no resource exists at the specified URL.
	 */
	@RequestMapping(value = "/error")
	@ApiResponse(responseCode = "400")
	public HttpErrorResponse returnDefaultError() {
		return new HttpErrorResponse("BadRequest", "No resource exists at the specified URL.");
	}
}

