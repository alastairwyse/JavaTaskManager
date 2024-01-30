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

import java.util.Optional;

/**
 * The result of a TryParse() method.
 * 
 * @param <T> The type of object returned from the TryParse() method when successful.
 */
public class TryParseResult<T> {

    /** Whether parsing was successful. */
    protected boolean success;
    /** The parsed object or null if parsing failed. */
    protected Optional<T> result;

    /**
     * @return Whether parsing was successful.
     */
    public boolean getSuccess() {
        return success;
    }
    
    /**
     * @return The parsed object or null if parsing failed.
     */
    public Optional<T> getResult() {
        return result;
    }

    /**
     * Constructs a TryParseResult.
     */
    public TryParseResult(boolean success, Optional<T> result) {
        this.success = success;
        this.result = result;
    }
}
