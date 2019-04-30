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

package org.simpliccity.styleguide.plugin;

import static org.simpliccity.styleguide.maven.util.PathUtils.buildSourcePathList;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.simpliccity.styleguide.annotation.processor.StyleGuideAnnotationProcessor;
import org.simpliccity.styleguide.maven.util.annotation.AnnotationProcessor;
import org.simpliccity.styleguide.maven.util.annotation.AnnotationProcessorException;
import org.simpliccity.styleguide.schema.Category;
import org.simpliccity.styleguide.schema.StyleGuide;
import org.simpliccity.styleguide.schema.Topic;
import org.simpliccity.styleguide.schema.util.StyleGuideSchemaUtil;

/**
 * Generates an XML file, based on the <code>@CodeStyle</code> annotations in all source code
 * covered by the build, which can be used as a template for a StyleGuide report.
 * 
 * @author Kevin Fox
 *
 */
@Mojo(name="generate-template", aggregator=true)
public class StyleGuideTemplateMojo extends AbstractMojo 
{
    /**
     * The directory in which the plugin will write intermediate files generated while creating the template,
     * including the template itself.
     */
    @Parameter (defaultValue=PluginConstants.DEFAULT_BUILD_PATH, property=PluginConstants.PROPERTY_TEMPLATE_BUILD_PATH, required=true)
    private File buildDirectory;
    
    /**
     * The name of the generated template file.
     */
    @Parameter (defaultValue=PluginConstants.DEFAULT_TEMPLATE_FILENAME, property=PluginConstants.PROPERTY_TEMPLATE_FILENAME, required=true)
    private String templateFileName;
    
    /**
     * Flag indicating whether to merge the entries generated from the annotation scan
     * with an existing StyleGuide template.
     */
    @Parameter (defaultValue=PluginConstants.TRUE, property=PluginConstants.PROPERTY_TEMPLATE_MERGE)
    private boolean mergeTemplates;

    /**
     * The path of the existing template file to use if {@link #mergeTemplates}
     * is true.
     */
    @Parameter(defaultValue=PluginConstants.DEFAULT_TEMPLATE_PATH, property=PluginConstants.PROPERTY_TEMPLATE_EXISTING_PATH)
    private File existingTemplatePath;
    
    /**
     * Flag indicating whether to scan test code for code snippets.
     * 
     * @since 1.0.0
     */
    @Parameter(defaultValue=PluginConstants.TRUE, property=PluginConstants.PROPERTY_TEMPLATE_INCLUDE_TEST)
    private boolean includeTestSources;
    
    /**
     * The projects in the reactor for aggregation report.
     */
    @Parameter(property="reactorProjects", required=true, readonly=true )
    private List<MavenProject> reactorProjects;
        
	/**
	 * The descriptor for this plugin.
	 */
    @Parameter(property="plugin", required=true, readonly=true )
	private PluginDescriptor descriptor;

    /**
     * Helper class used to perform annotation scanning.
     */
	@Component
	private AnnotationProcessor processor;

    /**
     * @see org.apache.maven.plugin.AbstractMojo#execute()
     */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException 
	{
		File template = new File(buildDirectory, templateFileName);
		String templateFilePath = template.getAbsolutePath();
		
		getLog().info("Scanning source paths for StyleGuide annotations.");
		executeAnnotationProcessing(templateFilePath);
		
		StyleGuide styleGuide;
		try 
		{
			getLog().info("Loading template file produced by annotation processing: " + templateFilePath);
			styleGuide = StyleGuideSchemaUtil.loadStyleGuide(templateFilePath);
		} 
		catch (JAXBException e) 
		{
			getLog().error("Unable to load template file for additional processing.", e);
			throw new MojoFailureException("Unable to load template file for additional processing.", e);
		}
		
		getLog().info("Processing generated template file: removing exemplars.");
		styleGuide = stripExemplars(styleGuide);
		
		getLog().info("Processing generated template file: merging with existing template.");
		styleGuide = mergeWithExistingTemplate(styleGuide);
		
		try 
		{
			getLog().info("Saving final template file: " + templateFilePath);
			StyleGuideSchemaUtil.saveStyleGuide(styleGuide, templateFilePath);
		} 
		catch (IOException e) 
		{
			getLog().error("Unable to save final template file.", e);
			throw new MojoExecutionException("Unable to save final template file.", e);
		} 
		catch (JAXBException e) 
		{
			getLog().error("Unable to save final template file.", e);
			throw new MojoExecutionException("Unable to save final template file.", e);
		}
	}

	private void executeAnnotationProcessing(String outputPath) throws MojoFailureException
	{
		
		String[] annotationProcessors = {"org.simpliccity.styleguide.annotation.processor.StyleGuideAnnotationProcessor"};
		
		System.setProperty(StyleGuideAnnotationProcessor.PROPERTY_OUTPUT_PATH, outputPath);
		
		try
		{
			List<String> messages = processor.processAnnotations(getSourcePaths(), buildDirectory, annotationProcessors, descriptor);
			
			getLog().info("Completed annotation processing....");
			for (String message : messages)
			{
				getLog().info(message);
			}
		}
		catch (AnnotationProcessorException e)
		{
			getLog().error("Unable to process Style Guide annotations in source.", e);
			throw new MojoFailureException("Unable to process Style Guide annotations in source.", e);
		}
		
	}
	
	private StyleGuide stripExemplars(StyleGuide styleGuide)
	{
		for (Category category : styleGuide.getCategories().getCategory())
		{
			for (Topic topic : category.getTopics().getTopic())
			{
				topic.setExemplars(null);
			}
		}
		
		return styleGuide;
	}
	
	private StyleGuide mergeWithExistingTemplate(StyleGuide styleGuide) throws MojoFailureException
	{
		StyleGuide result = styleGuide;
		
		if (existingTemplatePath.exists() && mergeTemplates)
		{
			StyleGuide existingTemplate;
			try 
			{
				existingTemplate = StyleGuideSchemaUtil.loadStyleGuide(existingTemplatePath.getAbsolutePath());
			} 
			catch (JAXBException e) 
			{
				getLog().error("Unable to load existing Style Guide template.", e);
				throw new MojoFailureException("Unable to load existing Style Guide template.", e);
			}
			
			result = StyleGuideSchemaUtil.populateTemplate(existingTemplate, styleGuide);
		}
		else
		{
			getLog().info("No existing template to merge.");
		}
		
		return result;
	}

	private List<String> getSourcePaths() 
	{
		List<String> result = buildSourcePathList(reactorProjects, includeTestSources, false, false);
		
		getLog().debug("Source paths to process: " + result);
		
		return result;
	}
}
