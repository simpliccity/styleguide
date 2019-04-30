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
 * JAXB class representing the &lt;topics&gt; element of the 
 * StyleGuide XML.  This element serves as a container for the list
 * of topics that comprise a category.
 * 
 * @author Kevin Fox
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name="topics", propOrder={"topic"})
public class Topics implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private List<Topic> topic;

	/**
	 * Returns the list &lt;topic&gt; sub-elements
	 * that represent the individual topics comprising
	 * a category.
	 * 
	 * @return The &lt;topic&gt; sub-elements.
	 */
	public List<Topic> getTopic() 
	{
		return topic;
	}

	/**
	 * Specifies the &lt;topic&gt; sub-elements.
	 * 
	 * @param topic The value for the &lt;topic&gt; sub-elements.
	 */
	@XmlElement(required=true)
	public void setTopic(List<Topic> topic) 
	{
		this.topic = topic;
	}
	
	/**
	 * A convenience method to find a topic by its ID.
	 * 
	 * @param id The id of the desired &lt;topic&gt; sub-element.
	 * @return The &lt;topic&gt; sub-element, if any, having the specified id; 
	 * <code>null</code> if none is found.
	 */
	public Topic getTopicById(String id)
	{
		Topic result = null;
		
		for (Topic nextTopic : getTopic())
		{
			if (nextTopic.getId().equals(id))
			{
				result = nextTopic;
				break;
			}
		}
		
		return result;
	}
}
