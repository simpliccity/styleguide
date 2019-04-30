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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.simpliccity.styleguide.annotation.CodeStyle;

/**
 * JAXB class representing the top-level (&lt;styleguide&gt;) element
 * of the StyleGuide XML.
 * 
 * @author Kevin Fox
 *
 */
@CodeStyle(categoryId="JAXB", topicId="rootElement")
// --- StyleGuide JAXB.rootElement ---
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder={"categories"})
@XmlRootElement(name="styleguide")
public class StyleGuide extends Section
{
	// --- StyleGuide JAXB.rootElement ---
	
	private static final long serialVersionUID = 1L;
	
	private Categories categories;
	
	/**
	 * Returns the &lt;categories&gt; sub-element, which contains
	 * the list of categories comprising the StyleGuide.
	 * 
	 * @return The &lt;categories&gt; sub-element.
	 */
	public Categories getCategories() 
	{
		return categories;
	}

	/**
	 * Specifies the &lt;categories&gt; sub-element.
	 * 
	 * @param categories The value for the &lt;categories&gt; sub-element.
	 */
	@XmlElement(required=true)
	public void setCategories(Categories categories) 
	{
		this.categories = categories;
	}
}
