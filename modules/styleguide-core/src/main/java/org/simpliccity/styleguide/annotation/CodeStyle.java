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
 * <p>Identifies a code sample to be included as a style in the StyleGuide report.  A code style
 * is a representative example of how a particular coding problem should be consistently
 * solved within the containing codebase.  This style becomes an idiom that can be used to
 * communicate effective solutions between members of the development team.</p>
 * 
 * <p>The presence of this annotation on a class or method defines a style based on code within the
 * class.  The elements of the annotation define the content to be generated for this style within
 * the StyleGuide report.</p>
 * 
 * @author Kevin Fox
 *
 */
@Target({TYPE, METHOD})
@Retention(SOURCE)
public @interface CodeStyle 
{
	/**
	 * Defines the category to which this code style belongs.  The StyleGuide report is structured into
	 * a set of categories, each of which can contain a set of topics.  All code styles sharing the same
	 * category ID will be combined into the same category in the StyleGuide report
	 * 
	 * @return The ID of the category under which this style will appear in the StyleGuide report.
	 */
	String categoryId();
	
	/**
	 * <p>Defines the descriptive name to be displayed for the category containing this style on the StyleGuide
	 * report.  If no name is provided, the ID of the category will be used.  Note that, if multiple CodeStyle
	 * annotations occur within the codebase under the same category (i.e. with the same category ID), the name
	 * used for the category will depend on the order in which those annotations are processed and is, therefore, 
	 * not deterministic.</p>
	 * 
	 * <p>Alternately, category names can be centrally controlled by using a template for the StyleGuide report.  
	 * A name provided in the template will override the value specified in this annotation.</p>
	 * 
	 * @return The descriptive name of the category under which this style will appear in the StyleGuide report.
	 */
	String categoryName() default "";

	/**
	 * Defines the topic to which this code style belongs.  The StyleGuide report is structured into
	 * a set of categories, each of which can contain a set of topics.  All code styles sharing the same
	 * topic ID will be combined into the same topic in the StyleGuide report
	 * 
	 * @return The ID of the topic under which this style will appear in the StyleGuide report.
	 */
	String topicId();
	
	/**
	 * <p>Defines the descriptive name to be displayed for the topic containing this style on the StyleGuide
	 * report.  If no name is provided, the ID of the topic will be used.  Note that, if multiple CodeStyle
	 * annotations occur within the codebase under the same topic (i.e. with the same category and topic ID's), 
	 * the name used for the topic will depend on the order in which those annotations are processed and is, 
	 * therefore, not deterministic.</p>
	 * 
	 * <p>Alternately, topic names can be centrally controlled by using a template for the StyleGuide report.  
	 * A name provided in the template will override the value specified in this annotation.</p>
	 * 
	 * @return The descriptive name of the topic under which this style will appear in the StyleGuide report.
	 */	
	String topicName() default "";
	
	/**
	 * <p>Defines the comment marker used to delimit the code snippet(s) to be displayed for this style.  A
	 * double-slash comment containing the marker text should appear immediately before and after a section
	 * of code that demonstrates the use of this code style.  That code will be extracted and displayed
	 * in the StyleGuide report.</p>
	 * 
	 * <p>If no value is provided for this element, the default marker content will be used.</p>
	 * 
	 * <p>When setting a marker other than the default, several considerations should be kept in mind:</p>
	 * <ul>
	 * <li>The comment should be descriptive so that it does not interfere with the readability of the code.</li>
	 * <li>The text should be unique so that other code snippets are not accidentally associated with this style.</li>
	 * <li>The text should avoid the use of special characters, as this could cause problems processing the
	 * StyleGuide report.</li>
	 * <li>The tool used to extract code snippets for the StyleGuide report may provide specific guidelines
	 * for the marker text.</li>
	 * </ul>
	 *  
	 * @return The text used in a comment delimiting the code snippet(s) associated with this style.
	 * @see org.simpliccity.styleguide.annotation.processor.StyleGuideAnnotationProcessor#DEFAULT_MARKER_TEMPLATE
	 */
	String marker() default "";
	
	/**
	 * <p>Specifies a file containing an HTML fragment that will be included with the other content for this style
	 * in the StyleGuide report.  The use of an include is optional.  It provides a way to incorporate descriptive
	 * text along with the code snippet for this style.</p>
	 * 
	 * <p>The path for the include file will be resolved relative to the include base path specified on the StyleGuide
	 * plugin when generating the report.</p> 
	 * 
	 * @return The relative path to an HTML fragment to be included with this style in the StyleGuide report.
	 */
	@Deprecated
	String include() default "";
	
	/**
	 * <p>Lists additional citations to be displayed with the code snippet identified by this style.
	 * Citations can be used to include other snippets or explanatory text that amplify the basic snippet.
	 * Individual citations are specified using the {@link StyleCitation} annotation.</p>
	 * 
	 * <p>A citation is displayed either before or after the main code snippet, based on its {@link StyleCitation#placement()}
	 * setting.  If multiple citations are specified with the same placement value, they are displayed in the
	 * order in which they listed here.</p>
	 * 
	 * <p>Citations serve the same purpose as an include but are a more robust approach.</p>
	 * 
	 * @since 1.1.0
	 * 
	 * @return An array of {@link StyleCitation} annotations defining citations to include with the code snippet.
	 * @see #include()
	 */
	StyleCitation[] citations() default {};
}
