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
 * Stores the results of validating a specific property of a model/container class.
 */
public class PropertyValidationResult extends ValidationResult {
    
    /** The name of the property which failed validation.  Null if validation was successful. */
    protected String propertyName;

    /**
     * @return The name of the property which failed validation.  Null if validation was successful.
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Constructs a PropertyValidationResult.
     * 
     * @param isValid Whether the validation was successful.
     */
    public PropertyValidationResult(boolean isValid) {
        super(isValid);

        propertyName = null;
    }

    /**
     * Constructs a PropertyValidationResult.
     * 
     * @param isValid Whether the validation was successful.
     * @param validationError The reason for failure if the validation was not successful.
     * @param propertyName The name of the property which failed validation.
     */
    public PropertyValidationResult(boolean isValid, String validationError, String propertyName) {
        super(isValid, validationError);

        if (propertyName == null || propertyName.isBlank())
            throw new IllegalArgumentException("Parameter 'propertyName' cannot be null or blank.");

        this.propertyName = propertyName;
    }
}
