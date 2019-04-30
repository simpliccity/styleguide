/*
 *    Copyright 2013 Information Control Corporation
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

package org.simpliccity.styleguide.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A container annotation that makes it possible to apply multiple {@link CodeStyle}
 * annotations to a single class or method.
 * 
 * @author Kevin Fox
 *
 */
@Target({TYPE, METHOD})
@Retention(SOURCE)
public @interface CodeStyles 
{
	/**
	 * The set of individual {@link CodeStyle} annotations to apply.
	 * 
	 * @return An array of applicable {@link CodeStyle} annotations.
	 * @see CodeStyle
	 */
	CodeStyle[] value();
}
