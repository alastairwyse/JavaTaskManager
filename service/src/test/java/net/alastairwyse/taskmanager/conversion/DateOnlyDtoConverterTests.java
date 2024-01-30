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

package net.alastairwyse.taskmanager.conversion;

import java.time.LocalDate;

import net.alastairwyse.taskmanager.models.dtos.DateOnlyDto;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link DateOnlyDtoConverter} class.
 */
public class DateOnlyDtoConverterTests {
        
    private DateOnlyDtoConverter testDateOnlyDtoConverter;

    @Before
    public void setUp() {

        testDateOnlyDtoConverter = new DateOnlyDtoConverter();
    }

    @Test
    public void convert_ConversionFails() {
        
        var testDateOnlyDto = new DateOnlyDto();
        testDateOnlyDto.setYear(2023);
        testDateOnlyDto.setMonth(0);
        testDateOnlyDto.setDay(28);

        Exception e = assertThrows(Exception.class, () -> 
        {
            testDateOnlyDtoConverter.convert(testDateOnlyDto);
        });

        assertTrue(e.getMessage().contains("Year 2023, month 0, and day of month 28 could not be converted to a valid date."));
    }

    @Test
    public void convert() {
        
        var testDateOnlyDto = new DateOnlyDto();
        testDateOnlyDto.setYear(2023);
        testDateOnlyDto.setMonth(11);
        testDateOnlyDto.setDay(28);

        LocalDate result = null;
        try {
            result = testDateOnlyDtoConverter.convert(testDateOnlyDto);
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
        
        assertEquals(testDateOnlyDto.getYear(), result.getYear());
        assertEquals(testDateOnlyDto.getMonth(), result.getMonthValue());
        assertEquals(testDateOnlyDto.getDay(), result.getDayOfMonth());
    }
}
