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

package net.alastairwyse.taskmanager.models.dtos;

import java.time.LocalDate;

/**
 * DTO for the date only portion of a {@link LocalDate}.
 */
public class DateOnlyDto {

    /** The year portion of the date. */
    protected int year;
    /** The month portion of the date (from 1 to 12 inclusive). */
    protected int month;
    /** The day portion of the date. */
    protected int day;

    /**
     * @return The year portion of the date.
     */
    public int getYear() {
        return year;
    }

    /** 
     * @return The month portion of the date (from 1 to 12 inclusive).
     */
    public int getMonth() {
        return month;
    }

    /**
     * @return The day portion of the date.
     */
    public int getDay() {
        return day;
    }

    /**
     * @param year The year portion of the date.
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @param month The month portion of the date (from 1 to 12 inclusive).
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * @param day The day portion of the date.
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Constructs a DateOnlyDto.
     */
    public DateOnlyDto() {
    }

    /**
     * Constructs a DateOnlyDto.
     * @param year The year portion of the date.
     * @param month The month portion of the date (from 1 to 12 inclusive).
     * @param day The day portion of the date.
     */
    public DateOnlyDto(int year, int month, int day) {
        this();
        this.year = year;
        this.month = month;
        this.day = day;
    }
}