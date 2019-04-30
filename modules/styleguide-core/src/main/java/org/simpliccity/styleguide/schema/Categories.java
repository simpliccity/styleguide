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

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * JAXB class representing the &lt;categories&gt; element of the 
 * StyleGuide XML.  This element serves as a container for the list
 * of categories that comprise the StyleGuide.
 * 
 * @author Kevin Fox
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name="categories", propOrder={"category"})
public class Categories implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<Category> category;

	/**
	 * Returns the list &lt;category&gt; sub-elements
	 * that represent the individual categories comprising
	 * the StyleGuide.
	 * 
	 * @return The &lt;category&gt; sub-elements.
	 */
	public List<Category> getCategory() 
	{
		return category;
	}

	/**
	 * Specifies the &lt;category&gt; sub-elements.
	 * 
	 * @param category The value for the &lt;category&gt; sub-elements.
	 */
	@XmlElement(required=true)
	public void setCategory(List<Category> category) 
	{
		this.category = category;
	}
	
	/**
	 * A convenience method to find a category by its ID.
	 * 
	 * @param id The id of the desired &lt;category&gt; sub-element.
	 * @return The &lt;category&gt; sub-element, if any, having the specified id; 
	 * <code>null</code> if none is found.
	 */
	public Category getCategoryById(String id)
	{
		Category result = null;
		
		for (Category nextCategory : getCategory())
		{
			if (nextCategory.getId().equals(id))
			{
				result = nextCategory;
				break;
			}
		}
		
		return result;
	}
}
