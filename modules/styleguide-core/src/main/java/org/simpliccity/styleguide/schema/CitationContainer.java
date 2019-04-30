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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * JAXB class representing the abstract parent type of all
 * types within the StyleGuide XML that can contain citations.  
 * 
 * @author Kevin Fox
 * @since 1.1.0
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder={"citations"})
public class CitationContainer implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private Citations citations;

	/**
	 * Returns the &lt;citations&gt; element, if any, that contains
	 * a list of &lt;citation&gt; sub-elements.
	 * 
	 * @return The &lt;citations&gt; element.
	 */
	public Citations getCitations() 
	{
		return citations;
	}

	/**
	 * Specifies the optional &lt;citations&gt; element.
	 * 
	 * @param citations The value for the &lt;citations&gt; element.
	 */
	@XmlElement()
	public void setCitations(Citations citations) 
	{
		this.citations = citations;
	}
}
