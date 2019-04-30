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

package org.simpliccity.styleguide.maven.util;

import java.io.File;

import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugin.logging.Log;
import org.simpliccity.styleguide.annotation.CodeStyle;

/**
 * Specifies the operations needed to generate StyleGuide report content within the 
 * context of a Maven plugin.
 * 
 * @author Kevin Fox
 *
 */
@CodeStyle(categoryId="plexus", topicId="defineComponent")
// --- StyleGuide plexus.defineComponent ---
public interface StyleGuideBuilder 
{
	/**
	 * The component <code>role</code> hint for the Plexus container.
	 */
	String ROLE = StyleGuideBuilder.class.getName();
	// --- StyleGuide plexus.defineComponent ---
	
	/**
	 * Generate StyleGuide content as defined by the {@link StyleGuideDescriptor} within the
	 * context of a Maven plugin.
	 * 
	 * @param log The standard Maven log used by the calling plugin. 
	 * @param styleGuideDescriptor The configuration used to generate the StyleGuide content.
	 * @param pluginDescriptor The Maven plugin environment in which the StyleGuide content is generated.
	 * @return The {@link java.io.File} to which the StyleGuide content was written.
	 * @throws StyleGuideException If the StyleGuide content cannot be generated.
	 */
	File collateStyleGuideReport(Log log, StyleGuideDescriptor styleGuideDescriptor, PluginDescriptor pluginDescriptor) throws StyleGuideException;
}
