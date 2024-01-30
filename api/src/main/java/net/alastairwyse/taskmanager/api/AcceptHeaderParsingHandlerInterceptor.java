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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashSet;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import net.alastairwyse.taskmanager.api.models.HttpErrorResponse;

// References
//   https://stackoverflow.com/questions/48785878/spring-boot-intercept-all-exception-handlers
//   https://www.baeldung.com/spring-mvc-handlerinterceptor

/**
 * Implementation of {@link HandlerInterceptor} which checks the 'accept' header of incoming HTTP requests and returns a 406 status and appropriate JSON content if it doesn't match one of a specified set of values.
 */
public class AcceptHeaderParsingHandlerInterceptor implements HandlerInterceptor {
    
    /** The accepted/allowed values in the 'accept' header. */
    protected HashSet<String> validAcceptHeaderValues;
    /** The contents of field 'validAcceptHeaderValues' represented as a comma-delimited string. */
    protected String validAcceptHeaderValuesAsString;

    /**
     * Contructs an AcceptHeaderParsingHandlerInterceptor.
     * 
     * @param validAcceptHeaderValues The accepted/allowed values in the 'accept' header.
     */
    public AcceptHeaderParsingHandlerInterceptor(Iterable<String> validAcceptHeaderValues) {
        this.validAcceptHeaderValues = new HashSet<String>();
        var validAcceptHeaderValuesStringBuilder  = new StringBuilder();
        for (String currentAcceptHeaderValues : validAcceptHeaderValues) {
            if (this.validAcceptHeaderValues.contains(currentAcceptHeaderValues))
                throw new IllegalArgumentException(String.format("Parameter '%s' contains duplicate values '%s'.", "validAcceptHeaderValues", currentAcceptHeaderValues));

            this.validAcceptHeaderValues.add(currentAcceptHeaderValues);
            validAcceptHeaderValuesStringBuilder.append(currentAcceptHeaderValues);
            validAcceptHeaderValuesStringBuilder.append(", ");
        }
        if (this.validAcceptHeaderValues.size() == 0) 
            throw new IllegalArgumentException(String.format("Parameter '%s' cannot be empty.", "validAcceptHeaderValues"));

        validAcceptHeaderValuesAsString = validAcceptHeaderValuesStringBuilder.toString().substring(0, validAcceptHeaderValuesStringBuilder.length() - 2);
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response, 
        Object handler
    ) throws Exception {

        var wrapper = new Object() { Boolean validContentTypeFoundInAcceptHeader = false; };
        request.getHeaders("accept").asIterator().forEachRemaining((String currContentTypeList) -> { 
            String[] contentTypes = currContentTypeList.split(",");
            for (String currContentType : contentTypes) {
                String currContentTypeClean = currContentType.trim();
                // Remove the quality value if it exists
                if (currContentTypeClean.contains(";")) {
                    int semicolonIndex = currContentTypeClean.indexOf(";");
                    currContentTypeClean = currContentTypeClean.substring(0, semicolonIndex);
                }
                if (validAcceptHeaderValues.contains(currContentTypeClean) == true) {
                    wrapper.validContentTypeFoundInAcceptHeader = true;
                }
            }
        });
        if (wrapper.validContentTypeFoundInAcceptHeader == false) {
            var errorResponse = new HttpErrorResponse(
                "ContentTypeNotAcceptable", 
                String.format("'Accept' header did not contain an acceptable content type.  Acceptable values are '%s'.", validAcceptHeaderValuesAsString)
            );
            ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String stringResponse = "";
            try {
                stringResponse = objectWriter.writeValueAsString(errorResponse);
            }
            catch (JsonProcessingException e) {
                stringResponse = "An unspecified error occurred.";
            }
            PrintWriter responseWriter = response.getWriter();
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            response.setContentType("application/json");
            responseWriter.write(stringResponse);
            responseWriter.flush();

            return false;
        }

        return true;
    }

    @Override
    public void postHandle(
        HttpServletRequest request, 
        HttpServletResponse response,
        Object handler, 
        ModelAndView modelAndView
    ) throws Exception {
        
    }
}