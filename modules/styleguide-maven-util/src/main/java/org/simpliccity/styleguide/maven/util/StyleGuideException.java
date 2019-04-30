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

package org.simpliccity.styleguide.maven.util;

/**
 * An exception indicating that processing of StyleGuide content has failed.
 * 
 * @author Kevin Fox
 *
 */
public class StyleGuideException extends Exception 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new StyleGuideException.
	 * 
	 * @param message The message describing the exception.
	 */
	public StyleGuideException(String message)
	{
		super(message);
	}
	
	/**
	 * Creates a new StyleGuideException with an underlying cause.
	 * 
	 * @param message The message describing the exception.
	 * @param cause The exception that caused the StyleGuideException.
	 */
	public StyleGuideException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
