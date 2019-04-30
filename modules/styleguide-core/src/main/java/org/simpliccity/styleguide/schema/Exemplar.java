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

package org.simpliccity.styleguide.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.simpliccity.styleguide.annotation.CodeStyle;
import org.simpliccity.styleguide.annotation.CodeStyles;

/**
 * JAXB class representing the &lt;exemplar&gt; element of the
 * StyleGuide XML.  This represents an individual code example for
 * a StyleGuide topic.
 * 
 * @author Kevin Fox
 *
 */
@CodeStyles
({
	@CodeStyle(categoryId="JAXB", topicId="typeNoElements"),
	@CodeStyle(categoryId="JAXB", topicId="attribute")
})
// --- StyleGuide JAXB.typeNoElements ---
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType()
public class Exemplar extends CitationContainer 
{
	// --- StyleGuide JAXB.typeNoElements ---
	
	private static final long serialVersionUID = 1L;

	private String className;
	private String marker;
	private String include;
	
	/**
	 * Returns the name of the class from which the code example is taken, as 
	 * specified by the <code>className</code>.  This corresponds to the name of a
	 * class marked with the @CodeStyle annotation.
	 * 
	 * @return The name of the example class.
	 * @see org.simpliccity.styleguide.annotation.CodeStyle
	 */
	public String getClassName() 
	{
		return className;
	}
	
	/**
	 * Specifies the value of the <code>className</code> attribute.
	 * 
	 * @param className The value for the <code>className</code> attribute.
	 */
	// --- StyleGuide JAXB.attribute ---
	@XmlAttribute(required=true)
	public void setClassName(String className) 
	{
		this.className = className;
	}
	// --- StyleGuide JAXB.attribute ---
	
	/**
	 * Returns the comment marker used to delimit the code snippet(s) to be displayed for this
	 * example, as specified by the <code>marker</code> attribute.  This corresponds to the
	 * <code>marker</code> element of the @CodeStyle annotation.
	 * 
	 * @return The comment marker for code snippets.
	 * @see org.simpliccity.styleguide.annotation.CodeStyle#marker()
	 */
	public String getMarker() 
	{
		return marker;
	}

	/**
	 * Specifies the value of the <code>marker</code> attribute.
	 * 
	 * @param marker The value for the <code>marker</code> attribute.
	 */
	@XmlAttribute(required=true)
	public void setMarker(String marker) 
	{
		this.marker = marker;
	}

	/**
	 * Returns the relative path of an HTML fragment to display with the code example in
	 * the StyleGuide, as specified by the <code>include</code> attribute.  This corresponds
	 * to the <code>include</code> element of the @CodeStyle annotation.
	 * 
	 * @return The value of the <code>include</code> attribute.
	 * @see org.simpliccity.styleguide.annotation.CodeStyle#include()
	 */
	@Deprecated
	public String getInclude() 
	{
		return include;
	}

	/**
	 * Specifies the value of the <code>include</code> attribute.
	 * 
	 * @param include The value for the <code>include</code> attribute.
	 */
	// --- StyleGuide JAXB.attribute ---
	@Deprecated
	@XmlAttribute()
	public void setInclude(String include) 
	{
		this.include = include;
	}
	// --- StyleGuide JAXB.attribute ---
}
