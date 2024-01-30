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

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link PropertyValidationResult} class.
 */
public class PropertyValidationResultTests {
       
    @Before
    public void setUp() {

    }

    @Test
    public void constructor_PropertyNameParameterNull() {

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> 
        {
            new PropertyValidationResult(false, "Fake validation error", null);
        });

        assertTrue(e.getMessage().contains("Parameter 'propertyName' cannot be null or blank."));
    }

    @Test
    public void constructor_PropertyNameParameterBlank() {
        
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> 
        {
            new PropertyValidationResult(false, "Fake validation error", " ");
        });

        assertTrue(e.getMessage().contains("Parameter 'propertyName' cannot be null or blank."));
    }

    @Test
    public void constructor() {
    
        PropertyValidationResult testPropertyValidationResult = new PropertyValidationResult(true);
        
        assertEquals(true, testPropertyValidationResult.getIsValid());
        assertNull(testPropertyValidationResult.getValidationError());
        assertNull(testPropertyValidationResult.getPropertyName());


        testPropertyValidationResult = new PropertyValidationResult(false, "Test validation error", "TestPropertyName");
        
        assertEquals(false, testPropertyValidationResult.getIsValid());
        assertEquals("Test validation error", testPropertyValidationResult.getValidationError());
        assertEquals("TestPropertyName", testPropertyValidationResult.getPropertyName());
    }
}
