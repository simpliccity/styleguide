/*
 *    Copyright 2014 Information Control Corporation
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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.simpliccity.styleguide.schema.CitationPlacement;
import org.simpliccity.styleguide.schema.CitationReferenceType;

/**
 * Defines an individual source file citation as part of a {@link CodeStyle}
 * annotation.  Citations provide a mechanism to include associated code 
 * snippets or explanatory text with a code style.
 * 
 * @author Kevin Fox
 * @since 1.1.0
 *
 * @see org.simpliccity.styleguide.annotation.CodeStyle#citations()
 */
@Documented
@Target({TYPE, METHOD})
@Retention(SOURCE)
public @interface StyleCitation 
{
	/**
	 * Specifies the relative path to a resource containing the snippet to be used for the citation.
	 * The resource specified must exist in any one of the base paths configured for the active goal
	 * for the StyleGuide plugin.
	 * 
	 * @return The relative path to a resource from which a snippet can be extracted for inclusion with 
	 * the corresponding style in the StyleGuide report.
	 */
	String ref();
	
	/**
	 * Specifies the type of resource containing the citation.  The StyleGuide plugin may process
	 * snippets from various resource types differently.
	 * 
	 * @return An option of the {@link CitationReferenceType} enumeration identifying the type
	 * of resource referenced by the citation.
	 */
	CitationReferenceType type();
	
	/**
	 * Defines a comment marker used to delimit one or more snippets in the resource being cited.  If no
	 * marker is defined, the entire content is included.  Only the content of the comment need be
	 * specified.  The actual comment in the referenced resource must, of course, follow the syntax of 
	 * the specified resource type.
	 * 
	 * @return The content of the comment marker used to define a snippet in the resource referenced
	 * by the citation.
	 * @see org.simpliccity.styleguide.annotation.CodeStyle#marker()
	 */
	String marker() default "";
	
	/**
	 * Specifies where the citation should appear in the overall code style presentation.  Options
	 * are to include the citation at the top or the bottom of the style listing.
	 * 
	 * @return An option of the {@link CitationPlacement} enumeration specifying the location
	 * where the citation should be displayed.
	 */
	CitationPlacement placement();
	
	/**
	 * Indicates whether or not a label showing the citation reference should be displayed.
	 * 
	 * @return A boolean indicating whether or not to display the reference label.
	 */
	boolean displayRef() default false;
}
