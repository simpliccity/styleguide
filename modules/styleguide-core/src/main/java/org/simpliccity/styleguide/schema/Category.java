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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.simpliccity.styleguide.annotation.CodeStyle;

/**
 * JAXB class representing the &lt;category&gt; element of the
 * StyleGuide XML.  This represents an individual category within
 * the StyleGuide.
 * 
 * @author Kevin Fox
 *
 */
@CodeStyle(categoryId="JAXB", topicId="type")
// --- StyleGuide JAXB.type ---
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder={"topics"})
public class Category extends Section
{
	// --- StyleGuide JAXB.type ---
	
	private static final long serialVersionUID = 1L;

	private Topics topics;
	
	/**
	 * Returns the &lt;topics&gt; sub-element, which contains the list of 
	 * topics that comprise this category.
	 * 
	 * @return The &lt;topics&gt; sub-element.
	 */
	public Topics getTopics() 
	{
		return topics;
	}

	/**
	 * Specifies the &lt;topics&gt; sub-element.
	 * 
	 * @param topics The value for the &lt;topics&gt; sub-element.
	 */
	@XmlElement(required=true)
	public void setTopics(Topics topics) 
	{
		this.topics = topics;
	}
}
