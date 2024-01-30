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

import java.time.LocalDate;

import net.alastairwyse.taskmanager.models.dtos.DateOnlyDto;

/**
 * Validator for instances of {@link DateOnlyDto}
 */
public class DateOnlyDtoValidator extends DateOnlyDtoValidatorBase implements Validator<DateOnlyDto, ValidationResult> {

    @Override
    public ValidationResult validate(DateOnlyDto dateOnlyDto) {
        
        TryParseResult<LocalDate> result = tryParse(dateOnlyDto);
        if (result.getSuccess() == true) {
            return new ValidationResult(true);
        }
        else {
            return new ValidationResult(false, String.format("Year %d, month %d, and day of month %d could not be converted to a valid date.", dateOnlyDto.getYear(), dateOnlyDto.getMonth(), dateOnlyDto.getDay()));
        }
    }
}
