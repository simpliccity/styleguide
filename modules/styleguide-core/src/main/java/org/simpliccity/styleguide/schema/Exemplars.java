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
 * JAXB class representing the &lt;exemplars&gt; element of the 
 * StyleGuide XML.  This element serves as a container for the list
 * of code examples for a specific topic.
 * 
 * @author Kevin Fox
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name="exemplars", propOrder={"exemplar"})
public class Exemplars implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private List<Exemplar> exemplar;

	/**
	 * Returns the list of &lt;exemplar&gt; sub-elements
	 * that represent the individual code examples for
	 * a topic.
	 * 
	 * @return The &lt;exemplar&gt; sub-elements.
	 */
	public List<Exemplar> getExemplar() 
	{
		return exemplar;
	}

	/**
	 * Specifies the &lt;exemplar&gt; sub-elements.
	 * 
	 * @param exemplar The value for the &lt;exemplar&gt; sub-elements.
	 */
	@XmlElement(required=true)
	public void setExemplar(List<Exemplar> exemplar) 
	{
		this.exemplar = exemplar;
	}
}
