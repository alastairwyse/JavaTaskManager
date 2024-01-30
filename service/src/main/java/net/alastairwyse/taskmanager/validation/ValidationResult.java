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

package net.alastairwyse.taskmanager.validation;

/**
 * Stores the results of validating a model/container class.
 */
public class ValidationResult {
    
    /** Whether the validation was successful. */
    protected boolean isValid;
    /** The reason for failure if the validation was not successful.  Null of validation was successful. */
    protected String validationError;

    /**
     * @return Whether the validation was successful.
     */
    public boolean getIsValid() {
        return isValid;
    }
    
    /**
     * @return The reason for failure if the validation was not successful.  Null of validation was successful. 
     */
    public String getValidationError() {
        return validationError;
    }
    
    /**
     * Constructs a ValidationResult.
     * 
     * @param isValid Whether the validation was successful.
     */
    public ValidationResult(boolean isValid) {

        if (isValid == false) 
            throw new IllegalArgumentException("Parameter 'isValid' must be set 'true' when the 'validationError' parameter is omitted.");

        this.isValid = isValid;
        validationError = null;
    }
    
    /**
     * Constructs a ValidationResult.
     * 
     * @param isValid Whether the validation was successful.
     * @param validationError The reason for failure if the validation was not successful.
     */
    public ValidationResult(boolean isValid, String validationError) {

        if (isValid == true) 
            throw new IllegalArgumentException("Parameter 'isValid' must be set 'false' when the 'validationError' parameter is specified.");
        if (validationError == null || validationError.isBlank())
            throw new IllegalArgumentException("Parameter 'validationError' cannot be null or blank.");

        this.isValid = isValid;
        this.validationError = validationError;
    }
}
