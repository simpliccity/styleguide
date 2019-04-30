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

package org.simpliccity.styleguide.maven.util.jcite;

import static org.simpliccity.styleguide.maven.util.PathUtils.isValidFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.simpliccity.styleguide.annotation.CodeStyle;
import org.simpliccity.styleguide.annotation.processor.StyleGuideAnnotationProcessor;
import org.simpliccity.styleguide.maven.util.StyleGuideBuilder;
import org.simpliccity.styleguide.maven.util.StyleGuideDescriptor;
import org.simpliccity.styleguide.maven.util.StyleGuideException;
import org.simpliccity.styleguide.maven.util.annotation.AnnotationProcessor;
import org.simpliccity.styleguide.maven.util.annotation.AnnotationProcessorException;
import org.simpliccity.styleguide.schema.StyleGuide;
import org.simpliccity.styleguide.schema.util.StyleGuideSchemaUtil;

/**
 * An implementation of {@link org.simpliccity.styleguide.maven.util.StyleGuideBuilder}
 * that relies on JCite to extract code snippets for the StyleGuide.
 * 
 * @author Kevin Fox
 * @see <a href="http://www.arrenbrecht.ch/jcite/index.htm">JCite documentation</a>
 *
 */
@CodeStyle(categoryId="plexus", topicId="defineComponent")
// --- StyleGuide plexus.defineComponent ---
@Component(role=StyleGuideBuilder.class)
public class JCiteStyleGuideBuilder implements StyleGuideBuilder
{
	// --- StyleGuide plexus.defineComponent ---
	/**
	 * The default name of the file to which annotation processing results are written as XML ({@value}).
	 */
	public static final String DEFAULT_ANNOTATION_RESULT = "styleGuide.xml";
	
	/**
	 * The default name of the file generated as a result of the XML transformation ({@value}).
	 */
	public static final String DEFAULT_TRANSFORM_RESULT = "jciteFragment.html";
	
	@Requirement(role=AnnotationProcessor.class)
	private AnnotationProcessor processor;
	
	/**
	 * Returns the {@link org.simpliccity.styleguide.maven.util.annotation.AnnotationProcessor}
	 * component used to perform the scan for {@link org.simpliccity.styleguide.annotation.CodeStyle}
	 * annotations.
	 * 
	 * @return The configured {@link org.simpliccity.styleguide.maven.util.annotation.AnnotationProcessor}.
	 */
	public AnnotationProcessor getProcessor() 
	{
		return processor;
	}

	/**
	 * Specifies the {@link org.simpliccity.styleguide.maven.util.annotation.AnnotationProcessor}
	 * component used to perform the scan for {@link org.simpliccity.styleguide.annotation.CodeStyle}
	 * annotations.  This component is injected by the Plexus container.
	 * 
	 * @param processor The configured {@link org.simpliccity.styleguide.maven.util.annotation.AnnotationProcessor}.
	 */
	public void setProcessor(AnnotationProcessor processor) 
	{
		this.processor = processor;
	}

	@Override
	public File collateStyleGuideReport(Log log, StyleGuideDescriptor styleGuideDescriptor, PluginDescriptor pluginDescriptor) throws StyleGuideException
	{
		File jciteFragment = null;
		
		log.info("Scanning source paths for StyleGuide annotations.");
		File styleGuideXml = executeAnnotationProcessing(log, styleGuideDescriptor, pluginDescriptor);
		
		// Check for existence of output from annotation processing.  Processing may succeed without producing any output.
		if (isValidFile(styleGuideXml))
		{
			log.info("Merging annotation scanning results with project StyleGuide template.");
			StyleGuide styleGuide = mergeWithTemplate(log, styleGuideDescriptor, styleGuideXml);
			
			File jciteTemplate = null;
			if (styleGuide != null)
			{
				log.info("Transforming annotation scan results to JCite input fragment");
				jciteTemplate = transformStyleGuide(log, styleGuideDescriptor, styleGuide);
			}
			
			if (isValidFile(jciteTemplate))
			{
				log.info("Running JCite to generate report content.");
				jciteFragment = executeJcite(log, styleGuideDescriptor, jciteTemplate);
			}
		}
		else
		{
			log.info("No style annotations located.  StyleGuide report not generated.");
		}
		
		return jciteFragment;
	}
	
	private File executeAnnotationProcessing(Log log, StyleGuideDescriptor styleGuideDescriptor, PluginDescriptor pluginDescriptor) throws StyleGuideException
	{
		File scanResults = new File(styleGuideDescriptor.getBuildDirectory(), DEFAULT_ANNOTATION_RESULT);
		
		String[] annotationProcessors = {"org.simpliccity.styleguide.annotation.processor.StyleGuideAnnotationProcessor"};
		
		System.setProperty(StyleGuideAnnotationProcessor.PROPERTY_OUTPUT_PATH, scanResults.getAbsolutePath());
		
		try
		{
			List<String> messages = getProcessor().processAnnotations(styleGuideDescriptor.getSourcePaths(), styleGuideDescriptor.getBuildDirectory(), annotationProcessors, pluginDescriptor);
			
			log.debug("Completed annotation processing....");
			for (String message : messages)
			{
				log.debug(message);
			}
			log.debug("Result of annotation scan saved to " + scanResults);
		}
		catch (AnnotationProcessorException e)
		{
			log.error("Unable to process Style Guide annotations in source.", e);
			throw new StyleGuideException("Unable to process Style Guide annotations in source.", e);
		}
		
		return scanResults;
	}

	private StyleGuide mergeWithTemplate(Log log, StyleGuideDescriptor styleGuideDescriptor, File styleGuideFile) throws StyleGuideException
	{
		// Load XML result file from annotation scanning
		
		StyleGuide styleGuide = null;
		try 
		{
			log.debug("Loading results of annotation scanning for processing: " + styleGuideFile.getAbsolutePath());
			styleGuide = StyleGuideSchemaUtil.loadStyleGuide(styleGuideFile.getAbsolutePath());
			log.debug("Style guide content from annotation processing:");
			logStyleGuide(log, styleGuide);
		} 
		catch (JAXBException e) 
		{
			log.error("Unable to load Style Guide XML.", e);
			throw new StyleGuideException("Unable to load Style Guide XML.", e);
		}
		
		// If a template file exists...
		if (styleGuide != null && styleGuideDescriptor.getStyleGuideTemplate().exists())
		{
			StyleGuide template;
			try
			{
				// ... load the template file ...
				log.debug("Loading project StyleGuide template: " + styleGuideDescriptor.getStyleGuideTemplate().getAbsolutePath());
				template = StyleGuideSchemaUtil.loadStyleGuide(styleGuideDescriptor.getStyleGuideTemplate().getAbsolutePath());
				log.debug("Style guide template content:");
				logStyleGuide(log, template);
				
				// ... and populate it from the result file.
				log.debug("Merging results of annotation scan to project StyleGuideTemplate.");
				styleGuide = StyleGuideSchemaUtil.populateTemplate(template, styleGuide);
				log.debug("Merged style guide content:");
				logStyleGuide(log, styleGuide);
			}
			catch (JAXBException e)
			{
				log.error("Unable to process StyleGuide template XML.", e);
				throw new StyleGuideException("Unable to process StyleGuide template XML.", e);
			}
		}

		return styleGuide;
	}
	
	private File transformStyleGuide(Log log, StyleGuideDescriptor styleGuideDescriptor, StyleGuide styleGuide) throws StyleGuideException
	{
		
		// Transform the style guide XML to create an HTML fragment file
		
		File outputPath = new File(styleGuideDescriptor.getBuildDirectory(), DEFAULT_TRANSFORM_RESULT);
		
		try 
		{
			log.debug("Transforming StyleGuide content to HTML for JCite processing.");
			StyleGuideSchemaUtil.transformStyleGuide(styleGuide, styleGuideDescriptor.getXslTransformResource(), outputPath.getAbsolutePath());
		} 
		catch (IOException e) 
		{
			log.error("Unable to transform JCite fragment.", e);
			throw new StyleGuideException("Unable to transform JCite fragment.", e);
		} 
		catch (JAXBException e) 
		{
			log.error("Unable to transform JCite fragment.", e);
			throw new StyleGuideException("Unable to transform JCite fragment.", e);
		} 
		catch (TransformerException e) 
		{
			log.error("Unable to transform JCite fragment.", e);
			throw new StyleGuideException("Unable to transform JCite fragment.", e);
		}
		
		// Return the HTML fragment file
		return outputPath;
	}
	
	private File executeJcite(Log log, StyleGuideDescriptor styleGuideDescriptor, File template) throws StyleGuideException
	{
		File outputFile = styleGuideDescriptor.getReportFile();
		
		JCiteRunner jcite = new JCiteRunner(log, styleGuideDescriptor.getJciteCommandPath());
		
        try
        {
        	log.debug("Processing StyleGuide report content through JCite to include code snippets.");
        	jcite.processTemplate(template, outputFile, styleGuideDescriptor.getSourcePaths());
        }
        catch (CommandLineException e)
        {
			log.error("Unable to run JCite processing.", e);
			throw new StyleGuideException("Unable to run JCite processing." ,e);
        }
        
        return outputFile;
	}
	
	private void logStyleGuide(Log log, StyleGuide styleGuide)
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try 
		{
			// Convert JAXB representation to XML
			StyleGuideSchemaUtil.marshallStyleGuide(styleGuide, out);
			
			// Write debug message to log with XML content
			log.debug(out.toString(StyleGuideSchemaUtil.DEFAULT_ENCODING));
		} 
		catch (IOException e) 
		{
			log.error("Error logging style guide content.", e);
		} 
		catch (JAXBException e) 
		{
			log.error("Error logging style guide content.", e);
		}
	}
}
