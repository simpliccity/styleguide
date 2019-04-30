// --- StyleGuide simpliccity.license ---
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
// --- StyleGuide simpliccity.license ---

package org.simpliccity.styleguide.plugin;

import org.simpliccity.styleguide.annotation.CodeStyle;

/**
 * Constants used by the various plugins included in this project.
 * 
 * @author Kevin Fox
 *
 */
@CodeStyle(categoryId="simpliccity", topicId="license")
public interface PluginConstants 
{
	/**
	 * String representation of true for parameter default value ({@value}).
	 */
	String TRUE = "true";
	
	/**
	 * String representation of false for parameter default value ({@value}).
	 */
	String FALSE = "false";
	
	/**
	 * The default output path for reports ({@value}).
	 */
	String DEFAULT_OUTPUT_PATH = "${project.reporting.outputDirectory}";
	
	/**
	 * The default path for intermediate work products produced by the plugin ({@value}).
	 */
	String DEFAULT_BUILD_PATH = "${project.build.directory}/styleGuide";
	
	/**
	 * The default name of the StyleGuide template file ({@value}).
	 */
	String DEFAULT_TEMPLATE_FILENAME = "styleGuideTemplate.xml";
	
	/**
	 * The default full path, including file name, of the StyleGuide template file ({@value}).
	 */
	String DEFAULT_TEMPLATE_PATH = "${basedir}/src/styleGuide/template/" + DEFAULT_TEMPLATE_FILENAME;
	
	/**
	 * The default folder holding files which can be referenced as includes ({@value}).
	 */
	String DEFAULT_INCLUDES_PATH = "${basedir}/src/styleGuide/includes";
	
	/**
	 * The default full path, including filename, of the generated StyleGuide report file ({@value}).
	 */
	String DEFAULT_GENERATED_REPORT = DEFAULT_BUILD_PATH + "/generatedReport.html";
	
	/**
	 * The default name of the resource to use as an XSL transformation ({@value}).
	 */
	String DEFAULT_TRANSFORM_RESOURCE = "/styleGuideBasic.xsl";

	/**
	 * The default label to use in the report header ({@value}).
	 */
	String DEFAULT_REPORT_LABEL = "${project.name}";

	/**
	 * Resource bundle key for the name of the plugin ({@value}).
	 */
	String KEY_PLUGIN_NAME = "styleGuide.pluginName";
	
	/**
	 * Resource bundle key for the report header template ({@value}).
	 */
	String KEY_REPORT_HEADER_TEMPLATE = "styleGuide.reportHeader.template";
	
	/**
	 * Resource bundle key for the report title ({@value}).
	 */
	String KEY_REPORT_TITLE = "styleGuide.reportTitle";
	
	/**
	 * Resource bundle key for the empty report error message ({@value}).
	 */
	String KEY_EMPTY_REPORT_TEXT="styleGuide.emptyReport.text";
	
	/**
	 * The name of the property used to set the plugin <code>failOnError</code>
	 * flag from the command line ({@value}).
	 */
	String PROPERTY_PLUGIN_FAILONERROR = "maven.styleguide.failOnError";
	
	/**
	 * The name of the property used to set the plugin <code>jciteCommandPath</code>
	 * setting from the command line ({@value}).
	 */
	String PROPERTY_JCITE_COMMAND_PATH = "jciteCommandPath";
	
	/**
	 * The name of the property used to set the plugin <code>buildDirectory</code>
	 * setting from the command line ({@value}).
	 */
	String PROPERTY_TEMPLATE_BUILD_PATH = "styleGuide.template.buildDir";
	
	/**
	 * The name of the property used to set the plugin <code>templateFileName</code>
	 * setting from the command line ({@value}).
	 */
	String PROPERTY_TEMPLATE_FILENAME = "styleGuide.template.fileName";
	
	/**
	 * The name of the property used to set the plugin <code>mergeTemplates</code>
	 * flag from the command line ({@value}).
	 */
	String PROPERTY_TEMPLATE_MERGE = "styleGuide.template.merge";
	
	/**
	 * The name of the property used to set the plugin <code>existingTemplatePath</code>
	 * setting from the command line ({@value}).
	 */
	String PROPERTY_TEMPLATE_EXISTING_PATH = "styleGuide.template.existing";
	
	/**
	 * The name of the property used to set the plugin <code>includeTestSources</code>
	 * setting from the command line ({@value}).
	 */
	String PROPERTY_TEMPLATE_INCLUDE_TEST = "styleGuide.template.includeTest";
}
