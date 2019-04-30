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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * JAXB class representing the &lt;citation&gt; element of the
 * StyleGuide XML.  This represents additional textual information
 * to be included with a portion of a StyleGuide.
 * 
 * @author Kevin Fox
 * @since 1.1.0
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType()
public class Citation implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private String ref;
	private CitationReferenceType type;
	private String marker;
	private CitationPlacement placement;
	private boolean displayRef;
	
	/**
	 * Returns the name of a resource referenced by the citation.  Handling
	 * of this resource will be determined by its <code>type</code> and the 
	 * comment <code>marker</code>, if any, specified by the corresponding 
	 * attributes.
	 * 
	 * @return The name of the cited resource.
	 * @see org.simpliccity.styleguide.annotation.StyleCitation#ref()
	 */
	public String getRef() 
	{
		return ref;
	}
	
	/**
	 * Specifies the value of the <code>ref</code> attribute.
	 * 
	 * @param reference The value for the <code>ref</code> attribute.
	 */
	@XmlAttribute(required=true)
	public void setRef(String reference) 
	{
		this.ref = reference;
	}

	/**
	 * Returns the type of the cited resource.  Handling of the resource
	 * may be determined by its type.
	 * 
	 * @return The type of resource referenced by the citation.
	 * @see org.simpliccity.styleguide.annotation.StyleCitation#type()
	 * @see org.simpliccity.styleguide.schema.CitationReferenceType
	 */
	public CitationReferenceType getType() 
	{
		return type;
	}

	/**
	 * Specifies the value of the <code>type</code> attribute.
	 * 
	 * @param type The value for the <code>type</code> attribute.
	 * @see org.simpliccity.styleguide.schema.CitationReferenceType
	 */
	@XmlAttribute(required=true)
	public void setType(CitationReferenceType type) 
	{
		this.type = type;
	}

	/**
	 * Returns the comment marker used to delimit the portion or portions of the 
	 * cited resource to be displayed.  The actual comment in the referenced resource 
	 * must, of course, follow the syntax of the specified resource type. 
	 * 
	 * @return The content of the comment marker used to define a snippet in the 
	 * resource referenced by the citation.
	 * @see org.simpliccity.styleguide.annotation.StyleCitation#marker()
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
	@XmlAttribute(required=false)
	public void setMarker(String marker) 
	{
		this.marker = marker;
	}

	/**
	 * Returns the location within the code style presentation where the citation
	 * should be displayed.
	 * 
	 * @return The display location for the citation.
	 * @see org.simpliccity.styleguide.annotation.StyleCitation#placement()
	 * @see org.simpliccity.styleguide.schema.CitationPlacement
	 */
	public CitationPlacement getPlacement() 
	{
		return placement;
	}

	/**
	 * Specifies the value of the <code>placement</code> attribute.
	 * 
	 * @param placement The value for the <code>placement</code> attribute.
	 * @see org.simpliccity.styleguide.schema.CitationPlacement
	 */
	@XmlAttribute(required=true)
	public void setPlacement(CitationPlacement placement) 
	{
		this.placement = placement;
	}

	/**
	 * Returns a flag indicating whether or not a label showing the citation reference 
	 * should be displayed.
	 * 
	 * @return <code>true</code> if the label should be displayed; <code>false</code> otherwise.
	 * @see org.simpliccity.styleguide.annotation.StyleCitation#displayRef()
	 */
	public boolean isDisplayRef() 
	{
		return displayRef;
	}

	/**
	 * Specifies the value of the <code>displayRef</code> attribute.
	 * 
	 * @param displayRef The value for the <code>displayRef</code> attribute.
	 */
	@XmlAttribute(required=true)
	public void setDisplayRef(boolean displayRef) 
	{
		this.displayRef = displayRef;
	}
}
