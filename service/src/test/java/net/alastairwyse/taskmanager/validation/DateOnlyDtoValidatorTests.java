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

import net.alastairwyse.taskmanager.models.dtos.DateOnlyDto;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link DateOnlyDtoValidator} class.
 */
public class DateOnlyDtoValidatorTests {
    
    private DateOnlyDtoValidator testDateOnlyDtoValidator;

    @Before
    public void setUp() {
    
        testDateOnlyDtoValidator = new DateOnlyDtoValidator();
    }

    @Test
    public void validate_DateOnlyDtoParameterValid() {
        
        var testDateOnlyDto = new DateOnlyDto();
        testDateOnlyDto.setYear(0000);
        testDateOnlyDto.setMonth(1);
        testDateOnlyDto.setDay(1);

        ValidationResult result = testDateOnlyDtoValidator.validate(testDateOnlyDto);

        assertEquals(true, result.getIsValid());

        
        testDateOnlyDto.setYear(9999);
        testDateOnlyDto.setMonth(12);
        testDateOnlyDto.setDay(31);

        result = testDateOnlyDtoValidator.validate(testDateOnlyDto);

        assertEquals(true, result.getIsValid());
    }

    @Test
    public void validate_DateOnlyDtoParameterInvalid() {
        
        var testDateOnlyDto = new DateOnlyDto();
        testDateOnlyDto.setYear(2023);
        testDateOnlyDto.setMonth(0);
        testDateOnlyDto.setDay(28);

        ValidationResult result = testDateOnlyDtoValidator.validate(testDateOnlyDto);

        assertEquals(false, result.getIsValid());
        assertEquals("Year 2023, month 0, and day of month 28 could not be converted to a valid date.", result.getValidationError());
        
        
        testDateOnlyDto.setYear(2023);
        testDateOnlyDto.setMonth(13);
        testDateOnlyDto.setDay(28);

        result = testDateOnlyDtoValidator.validate(testDateOnlyDto);

        assertEquals(false, result.getIsValid());
        assertEquals("Year 2023, month 13, and day of month 28 could not be converted to a valid date.", result.getValidationError());
        
        
        testDateOnlyDto.setYear(2023);
        testDateOnlyDto.setMonth(11);
        testDateOnlyDto.setDay(0);

        result = testDateOnlyDtoValidator.validate(testDateOnlyDto);

        assertEquals(false, result.getIsValid());
        assertEquals("Year 2023, month 11, and day of month 0 could not be converted to a valid date.", result.getValidationError());
        
        
        testDateOnlyDto.setYear(2023);
        testDateOnlyDto.setMonth(11);
        testDateOnlyDto.setDay(31);

        result = testDateOnlyDtoValidator.validate(testDateOnlyDto);

        assertEquals(false, result.getIsValid());
        assertEquals("Year 2023, month 11, and day of month 31 could not be converted to a valid date.", result.getValidationError());
    }
}
