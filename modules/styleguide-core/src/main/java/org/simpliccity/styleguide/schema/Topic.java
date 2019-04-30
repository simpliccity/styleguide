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

/**
 * JAXB class representing the &lt;topic&gt; element of the
 * StyleGuide XML.  This represents an individual topic within
 * a StyleGuide category.
 * 
 * @author Kevin Fox
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder={"exemplars"})
public class Topic extends Section
{
	private static final long serialVersionUID = 1L;

	private Exemplars exemplars;
	
	/**
	 * Returns the &lt;exemplars&gt; sub-element, which contains the list of 
	 * code examples for this topic.
	 * 
	 * @return The &lt;exemplars&gt; sub-element.
	 */
	public Exemplars getExemplars() 
	{
		return exemplars;
	}

	/**
	 * Specifies the &lt;exemplars&gt; sub-element.
	 * 
	 * @param exemplars The value for the &lt;exemplars&gt; sub-element.
	 */
	@XmlElement()
	public void setExemplars(Exemplars exemplars) 
	{
		this.exemplars = exemplars;
	}
}
