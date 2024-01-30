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

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import net.alastairwyse.taskmanager.api.models.HttpErrorResponse;

/**
 * Defines common exception handling rules/mappings for all controllers.
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    /**
     * Maps a {@link NoResourceFoundException} to a 400 status error.
     * 
     * @param noResourceFoundException The exception.
     * @return The exception mapped to a {@link HttpErrorResponse}.
     */
    @ExceptionHandler({NoResourceFoundException.class})
    public ResponseEntity<HttpErrorResponse> processException(NoResourceFoundException noResourceFoundException) {
        String errorMessage = String.format("No resource exists at URL '%s'.", noResourceFoundException.getResourcePath());
        var attributes = new ArrayList<Map.Entry<String, String>>();
        attributes.add(new AbstractMap.SimpleEntry<String,String>("ResourcePath", noResourceFoundException.getResourcePath()));
        attributes.add(new AbstractMap.SimpleEntry<String,String>("HttpMethod", noResourceFoundException.getHttpMethod().toString()));
        var errorResponse = new HttpErrorResponse(noResourceFoundException.getClass().getSimpleName(), errorMessage, attributes);
        return new ResponseEntity<HttpErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maps a {@link HttpRequestMethodNotSupportedException} to a 405 status error.
     * 
     * @param httpRequestMethodNotSupportedException The exception.
     * @return The exception mapped to a {@link HttpErrorResponse}.
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<HttpErrorResponse> processException(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
        var attributes = new ArrayList<Map.Entry<String, String>>();
        attributes.add(new AbstractMap.SimpleEntry<String,String>("HttpMethod", httpRequestMethodNotSupportedException.getMethod()));
        var errorResponse = new HttpErrorResponse(httpRequestMethodNotSupportedException.getClass().getSimpleName(), httpRequestMethodNotSupportedException.getMessage(), attributes);
        return new ResponseEntity<HttpErrorResponse>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Maps a {@link MethodArgumentTypeMismatchException} to a 400 status error.
     * 
     * @param methodArgumentTypeMismatchException The exception.
     * @return The exception mapped to a {@link HttpErrorResponse}.
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<HttpErrorResponse> processException(MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
        HttpErrorResponse innerError = null;
        if (methodArgumentTypeMismatchException.getCause() != null) {
            innerError = new HttpErrorResponse(methodArgumentTypeMismatchException.getCause().getClass().getSimpleName(), methodArgumentTypeMismatchException.getMessage());
        }
        var attributes = new ArrayList<Map.Entry<String, String>>();
        attributes.add(new AbstractMap.SimpleEntry<String,String>("ArgumentName", methodArgumentTypeMismatchException.getPropertyName()));
        var errorResponse = new HttpErrorResponse(methodArgumentTypeMismatchException.getClass().getSimpleName(), methodArgumentTypeMismatchException.getMessage(), attributes, innerError);
        
        return new ResponseEntity<HttpErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maps a {@link HttpMessageNotReadableException} to a 400 status error.
     * 
     * @param httpMessageNotReadableException The exception.
     * @return The exception mapped to a {@link HttpErrorResponse}.
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<HttpErrorResponse> processException(HttpMessageNotReadableException httpMessageNotReadableException) {
        var errorResponse = new HttpErrorResponse(httpMessageNotReadableException.getClass().getSimpleName(), httpMessageNotReadableException.getMessage());
        return new ResponseEntity<HttpErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maps any un-caught exceptions to JSON-serialized instances of {@link HttpErrorResponse}.
     * 
     * @param exception The un-caught exception.
     * @return The exception mapped to a {@link HttpErrorResponse}.
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<HttpErrorResponse> processException(Exception exception) {
        var errorResponse = new HttpErrorResponse(exception.getClass().getSimpleName(), exception.getMessage());
        return new ResponseEntity<HttpErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
