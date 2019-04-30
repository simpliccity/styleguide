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

package org.simpliccity.styleguide.schema;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * JAXB class representing the &lt;citations&gt; element of the
 * StyleGuide XML.  This element serves as a container for the list
 * of citations for a specific portion of a StyleGuide.
 * 
 * @author Kevin Fox
 * @since 1.1.0
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name="citations", propOrder={"citation"})
public class Citations implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private List<Citation> citation;

	/**
	 * Returns the list of &lt;citation&gt; sub-elements
	 * that represent additional textual information
	 * to be included with a portion of a StyleGuide.
	 * 
	 * @return The &lt;citation&gt; sub-elements.
	 */
	public List<Citation> getCitation() 
	{
		return citation;
	}

	/**
	 * Specifies the &lt;citation&gt; sub-elements.
	 * 
	 * @param citation The value for the &lt;citation&gt; sub-elements.
	 */
	@XmlElement(required=true)
	public void setCitation(List<Citation> citation) 
	{
		this.citation = citation;
	}
}
