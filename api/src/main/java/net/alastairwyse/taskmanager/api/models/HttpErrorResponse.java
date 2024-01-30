package net.alastairwyse.taskmanager.api.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Container class holding the data returned from a REST API when an error occurs.  Based on the Microsoft REST API Guidelines... https://github.com/Microsoft/api-guidelines/blob/master/Guidelines.md#7102-error-condition-responses.
 */
public class HttpErrorResponse {

        /** An internal code representing the error.  Typically this should contain the class name of the exception which caused the server error. */
        protected String code;
        /** A description of the error. */
        protected String message;
        /** A collection of key/value pairs which give additional details of the error. */
        protected List<Map.Entry<String, String>> attributes;
        /** The error which caused this error. */
        protected HttpErrorResponse innerError;

        /**
         * @return An internal code representing the error.  Typically this should contain the class name of the exception which caused the server error.
         */
        public String getCode() {
            return code;
        }

        /**
         * @return A description of the error.
         */
        public String getMessage() {
            return message;
        }

        /**
         * @return A collection of key/value pairs which give additional details of the error. 
         */
        public List<Map.Entry<String, String>> getAttributes() {
            return attributes;
        }

        /**
         * @return The error which caused this error.
         */
        public HttpErrorResponse getInnerError() {
            return innerError;
        }
        
        /**
         * @param innerError The error which caused this error.
         */
        public void setInnerError(HttpErrorResponse innerError) {
            this.innerError = innerError;
        }

        /**
         * Constructs an HttpErrorResponse. 
         * 
         * @param code An internal code representing the error.  Typically this should contain the class name of the exception which caused the server error.
         * @param message A description of the error.
         */
        public HttpErrorResponse(String code, String message) {
            this.code = code;
            this.message = message;
            attributes = new ArrayList<Map.Entry<String, String>>();
            innerError = null;
        }

        /**
         * Constructs an HttpErrorResponse. 
         * 
         * @param code An internal code representing the error.  Typically this should contain the class name of the exception which caused the server error.
         * @param message A description of the error.
         * @param attributes A collection of key/value pairs which give additional details of the error. 
         */
        public HttpErrorResponse(String code, String message, List<Map.Entry<String, String>> attributes) {
            this(code, message);
            this.attributes = attributes;
        }
        
        /**
         * Constructs an HttpErrorResponse. 
         * 
         * @param code An internal code representing the error.  Typically this should contain the class name of the exception which caused the server error.
         * @param message A description of the error.
         * @param innerError The error which caused this error.
         */
        public HttpErrorResponse(String code, String message, HttpErrorResponse innerError) {
            this(code, message);
            this.innerError = innerError;
        }
                
        /**
         * Constructs an HttpErrorResponse. 
         * 
         * @param code An internal code representing the error.  Typically this should contain the class name of the exception which caused the server error.
         * @param message A description of the error.
         * @param attributes A collection of key/value pairs which give additional details of the error. 
         * @param innerError The error which caused this error.
         */
        public HttpErrorResponse(String code, String message, List<Map.Entry<String, String>> attributes, HttpErrorResponse innerError) {
            this(code, message, attributes);
            this.innerError = innerError;
        }
    }
