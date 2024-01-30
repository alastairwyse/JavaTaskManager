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

import net.alastairwyse.taskmanager.validation.DateOnlyDtoValidator;
import net.alastairwyse.taskmanager.validation.ValidationResult;
import net.alastairwyse.taskmanager.models.dtos.DateOnlyDto;

/**
 * Converter for instances of {@link DateOnlyDto} to other object types.
 */
public class DateOnlyDtoConverter {
    
    protected DateOnlyDtoValidator validator;

    /**
     * Constructs a DateOnlyDtoConverter.
     */
    public DateOnlyDtoConverter() {

        validator = new DateOnlyDtoValidator();
    }

    /**
     * Attempts to convert the specified {@link DateOnlyDto} to a {@link LocalDate}.
     * 
     * @param dateOnlyDto The {@link DateOnlyDto} to convert.
     * @return The converted {@link DateOnlyDto}.
     * @throws Exception If the conversion fails.
     */
    public LocalDate convert(DateOnlyDto dateOnlyDto) throws Exception {

        ValidationResult result = validator.validate(dateOnlyDto);
        if (result.getIsValid() == true) {
            return LocalDate.of(dateOnlyDto.getYear(), dateOnlyDto.getMonth(), dateOnlyDto.getDay());
        }
        else {
            throw new Exception(result.getValidationError());
        }
    }
}
