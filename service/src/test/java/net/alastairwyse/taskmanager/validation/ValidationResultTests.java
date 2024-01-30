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

import net.alastairwyse.taskmanager.validation.ValidationResult;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link ValidationResult} class.
 */
public class ValidationResultTests {
    
    @Before
    public void setUp() {

    }

    @Test
    public void constructor_IsValidParameterOnlyAndValueSetFalse() {

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> 
        {
            new ValidationResult(false);
        });

        assertTrue(e.getMessage().contains("Parameter 'isValid' must be set 'true' when the 'validationError' parameter is omitted."));
    }

    @Test
    public void constructor_IsValidParameterSetTrueAndValidationErrorParameterProvided() {

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> 
        {
            new ValidationResult(true, "Fake validation error");
        });

        assertTrue(e.getMessage().contains("Parameter 'isValid' must be set 'false' when the 'validationError' parameter is specified."));
    }

    @Test
    public void constructor_ValidationErrorParameterNull() {

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> 
        {
            new ValidationResult(false, null);
        });

        assertTrue(e.getMessage().contains("Parameter 'validationError' cannot be null or blank."));
    }

    @Test
    public void constructor_ValidationErrorParameterBlank() {
        
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> 
        {
            new ValidationResult(false, " ");
        });

        assertTrue(e.getMessage().contains("Parameter 'validationError' cannot be null or blank."));
    }
}
