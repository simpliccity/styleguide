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

/**
 * JAXB class representing the abstract parent type of all
 * sections within the StyleGuide XML.  Section types include
 * the style guide, categories and topics.
 * 
 * @author Kevin Fox
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType()
public class Section extends CitationContainer
{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private String include;
	
	/**
	 * Returns the unique identifier of the section, as specified by the <code>id</code> attribute.
	 * 
	 * @return The value of the <code>id</code> attribute.
	 */
	public String getId() 
	{
		return id;
	}

	/**
	 * Specifies the value of the <code>id</code> attribute.
	 * 
	 * @param id The value for the <code>id</code> attribute.
	 */
	@XmlAttribute(required=true)
	public void setId(String id) 
	{
		this.id = id;
	}

	/**
	 * Returns the name of the section, as specified by the <code>name</code> attribute.
	 * 
	 * @return The value of the <code>name</code> attribute.
	 */
	public String getName() 
	{
		return name;
	}
	
	/**
	 * Specifies the value of the <code>name</code> attribute.
	 * 
	 * @param name The value for the <code>name</code> attribute.
	 */
	@XmlAttribute(required=true)
	public void setName(String name) 
	{
		this.name = name;
	}

	/**
	 * Returns the relative path of an HTML fragment to display with the section in
	 * the StyleGuide, as specified by the <code>include</code> attribute.
	 * 
	 * @return The value of the <code>include</code> attribute.
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
	@Deprecated
	@XmlAttribute()
	public void setInclude(String include) 
	{
		this.include = include;
	}
}
