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

package org.simpliccity.styleguide.maven.util.annotation;

/**
 * An exception indicating that annotation processing has failed.
 * 
 * @author Kevin Fox
 *
 */
public class AnnotationProcessorException extends Exception 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new AnnotationProcessorException.
	 * 
	 * @param message The message describing the exception.
	 */
	public AnnotationProcessorException(String message)
	{
		super(message);
	}

	/**
	 * Creates a new AnnotationProcessorException with an underlying cause.
	 * 
	 * @param message The message describing the exception.
	 * @param cause The exception that caused the AnnotationProcessorException.
	 */
	public AnnotationProcessorException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
