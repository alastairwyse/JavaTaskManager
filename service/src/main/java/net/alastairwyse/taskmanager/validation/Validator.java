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
 * Defines methods to validate objects of a specified type.
 * 
 * @param <TObject> The type of objects to validate.
 * @param <TValidationResult> The instance or subclass of {@link ValidationResult} to return as the result of validation.
 */
public interface Validator<TObject, TValidationResult extends ValidationResult> {
    
    /**
     * Validates an object.
     * 
     * @param objectToValidate The object to validate.
     * @return The result of the validation.
     */
    TValidationResult validate(TObject objectToValidate);
}
