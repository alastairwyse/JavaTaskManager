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
import java.util.Optional;
import java.time.DateTimeException;

import net.alastairwyse.taskmanager.models.dtos.DateOnlyDto;

/**
 * Base for validators for instances of {@link DateOnlyDto}.
 */
public abstract class DateOnlyDtoValidatorBase {
    
    /**
     * Attempts to convert the specified {@link DateOnlyDto} to its {@link LocalDate} equivalent.
     * 
     * @param dateOnlyDto The {@link DateOnlyDto} to convert.
     * @return The result of the attempt to convert.
     */
    protected TryParseResult<LocalDate> tryParse(DateOnlyDto dateOnlyDto) {

        LocalDate returnLocateDate = null;
        try {
            returnLocateDate = LocalDate.of(dateOnlyDto.getYear(), dateOnlyDto.getMonth(), dateOnlyDto.getDay());
        }
        catch (DateTimeException e) {
            return new TryParseResult<LocalDate>(false, Optional.empty());
        }

        return new TryParseResult<>(true, Optional.of(returnLocateDate));
    }
}
