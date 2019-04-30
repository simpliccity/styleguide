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

package org.simpliccity.styleguide.plugin.report;

import static org.simpliccity.styleguide.maven.util.PathUtils.isValidFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.apache.maven.reporting.MavenReportRenderer;
import org.simpliccity.styleguide.annotation.CodeStyle;
import org.simpliccity.styleguide.maven.util.StyleGuideBuilder;
import org.simpliccity.styleguide.maven.util.StyleGuideDescriptor;
import org.simpliccity.styleguide.maven.util.StyleGuideException;
import org.simpliccity.styleguide.plugin.PluginConstants;

/**
 * The abstract base class for all StyleGuide reports.  This class performs the core processing used
 * to generate and render the report.
 * 
 * @author Kevin Fox
 *
 */
@CodeStyle(categoryId="plugin", topicId="usePlexus")
public abstract class AbstractStyleGuideReport extends AbstractMavenReport 
{
    /**
     * Report output directory. Note that this parameter is only relevant if the goal is run from the command line or
     * from the default build lifecycle. If the goal is run indirectly as part of a site generation, the output
     * directory configured in the Maven Site Plugin is used instead.
     */
    @Parameter(defaultValue=PluginConstants.DEFAULT_OUTPUT_PATH, required=true)
    private File outputDirectory;
    
    /**
     * The directory in which the plugin will write intermediate files generated while creating the report.
     */
    @Parameter (defaultValue=PluginConstants.DEFAULT_BUILD_PATH, required=true)
    private File buildDirectory;
    
    /**
     * The full path, including file name and extension, of the JCite executable used to extract 
     * code snippets for the StyleGuide content.
     */
    @Parameter(required=true, property=PluginConstants.PROPERTY_JCITE_COMMAND_PATH)
    private String jciteCommandPath;
    
    /**
     * The full path of a template for this report, if any.  A sample template can be generated from StyleGuide
     * annotations in the project code using the <code>generate-template</code> goal.
     */
    @Parameter(defaultValue=PluginConstants.DEFAULT_TEMPLATE_PATH)
    private File styleGuideTemplate;
    
    /**
     * The path to a folder containing HTML snippets referenced as includes by the StyleGuide annotations
     * or the project StyleGuide template.  The use of includes is optional; however, this parameter must be
     * set appropriately if any includes are referenced through annotations or the project template.  Missing
     * included files will cause the report to fail.
     */
    @Parameter(defaultValue=PluginConstants.DEFAULT_INCLUDES_PATH)
    private File includesBasePath;
    
	/**
	 * Path to the report file created by the JCite processing.
	 * 
	 * @since 0.0.3
	 */
	@Parameter(defaultValue=PluginConstants.DEFAULT_GENERATED_REPORT, required=true)
	private File reportFile;

	/**
	 * The name of the resource to use to perform an XSL transformation on the
	 * XML output produced by the StyleGuide annotation processing.  The specified resource
	 * must appear on the classpath.
	 * 
	 * @since 0.0.4
	 */
	@Parameter(defaultValue=PluginConstants.DEFAULT_TRANSFORM_RESOURCE)
	private String xslTransformResource;
	
    /**
     * Flag indicating whether the build should fail if the report cannot be successfully generated.
     * 
     * @since 0.0.2
     */
    @Parameter (defaultValue=PluginConstants.TRUE, property=PluginConstants.PROPERTY_PLUGIN_FAILONERROR)
    private boolean failOnError;
    
    /**
     * Flag indicating whether to scan test code for code snippets.
     * 
     * @since 1.0.0
     */
    @Parameter(defaultValue=PluginConstants.TRUE)
    private boolean includeTestSources;
    
    /**
     * Flag indicating whether to scan project resources for code snippets.
     * 
     * @since 1.0.0
     */
    @Parameter(defaultValue=PluginConstants.TRUE)
    private boolean includeResources;

    /**
     * Flag indicating whether to scan project build output for code snippets.
     * 
     * @since 1.1.2
     */
    @Parameter(defaultValue=PluginConstants.TRUE)
    private boolean includeBuildOutput;

    /**
     * Project label to use in the report header.
     * 
     * @since 1.0.0
     */
    @Parameter(defaultValue=PluginConstants.DEFAULT_REPORT_LABEL)
    private String reportLabel;
    
    /**
     * The Maven Project.
     */
    @Parameter(property="project", required=true, readonly=true )
    private MavenProject project;

	/**
	 * The descriptor for the plugin.
	 */
    @Parameter(property="plugin", required=true, readonly=true )
	private PluginDescriptor descriptor;
	
    /**
     * Doxia Site Renderer.
     */
    @Component
    private Renderer siteRenderer;

	/**
	 * Helper class used to build Style Guide content.
	 */
	// --- StyleGuide plugin.usePlexus ---
	@Component
	private StyleGuideBuilder styleGuideBuilder;
	// --- StyleGuide plugin.usePlexus ---
	
	/**
	 * @see org.apache.maven.reporting.AbstractMavenReport#getDescription(Locale)
	 */
	@Override
	public String getDescription(Locale locale) 
	{
		return getBundle(locale).getString(PluginConstants.KEY_REPORT_TITLE);
	}

	/**
	 * @see org.apache.maven.reporting.AbstractMavenReport#getName(Locale)
	 */
	@Override
	public String getName(Locale locale) 
	{
		return getBundle(locale).getString(PluginConstants.KEY_PLUGIN_NAME);
	}

	/**
	 * @see org.apache.maven.reporting.AbstractMavenReport#getOutputName()
	 */
	@Override
	public String getOutputName() 
	{
		return "styleGuide";
	}

	/**
	 * @see org.apache.maven.reporting.AbstractMavenReport#getOutputDirectory()
	 */
	@Override
	protected String getOutputDirectory() 
	{
		return outputDirectory.getAbsolutePath();
	}

	/**
	 * Returns the configured build directory.
	 * 
	 * @return File representing the build directory.
	 * @see #buildDirectory
	 */
	protected File getBuildDirectory() 
	{
		return buildDirectory;
	}

	/**
	 * Returns the configured path of the JCite executable used to extract 
     * code snippets for the StyleGuide content.
	 * 
	 * @return The full path of the JCite executable.
	 * @see #jciteCommandPath
	 */
	protected String getJciteCommandPath() 
	{
		return jciteCommandPath;
	}

	/**
	 * Returns the configured full path of the StyleGuide template.
	 * 
	 * @return File representing the StyleGuide template.
	 * @see #styleGuideTemplate
	 */
	protected File getStyleGuideTemplate() 
	{
		return styleGuideTemplate;
	}

	/**
	 * Returns the configured base path for included files.
	 * 
	 * @return File representing the base directory for included files.
	 * @see #includesBasePath
	 */
	protected File getIncludesBasePath() 
	{
		return includesBasePath;
	}

	/**
	 * Returns the configured full path of the generated report file.
	 * 
	 * @return File representing the generated report.
	 * @see #reportFile
	 */
	protected File getReportFile() 
	{
		return reportFile;
	}

	/**
	 * Returns the configured name of the XSL transformation resource.
	 * 
	 * @return The name of the XSL transformation resource.
	 * @see #xslTransformResource
	 */
	protected String getXslTransformResource() 
	{
		return xslTransformResource;
	}

	/**
	 * Indicates the configuration of whether the report should fail when an error
	 * is encountered.
	 * 
	 * @return <code>true</code> if an error should cause the report to fail; <code>false</code> otherwise.
	 * @see #failOnError
	 */
	protected boolean isFailOnError() 
	{
		return failOnError;
	}

	/**
	 * Indicates the configuration of whether the test sources should be included when scanning
	 * for code snippets.
	 * 
	 * @return <code>true</code> if test sources should be included in the scan; <code>false</code> otherwise.
	 * @see #includeTestSources
	 */
	protected boolean isIncludeTestSources() 
	{
		return includeTestSources;
	}

	/**
	 * Indicates the configuration of whether the resources should be included when scanning
	 * for code snippets.
	 * 
	 * @return <code>true</code> if resources should be included in the scan; <code>false</code> otherwise.
	 * @see #includeResources
	 */
	protected boolean isIncludeResources() 
	{
		return includeResources;
	}

	/**
	 * Indicates the configuration of whether the build output should be included when scanning
	 * for code snippets.
	 * 
	 * @return <code>true</code> if the build output location should be included in the scan; <code>false</code> otherwise.
	 * @see #includeBuildOutput
	 */
	protected boolean isIncludeBuildOutput() 
	{
		return includeBuildOutput;
	}
	
	/**
	 * Returns the configured project label to include in the report header.
	 * 
	 * @return The configured project label.
	 * @see #reportLabel
	 */
	protected String getReportLabel() 
	{
		return reportLabel;
	}

	/**
	 * @see org.apache.maven.reporting.AbstractMavenReport#getProject()
	 */
	@Override
	protected MavenProject getProject() 
	{
		return project;
	}

	/**
	 * @see org.apache.maven.reporting.AbstractMavenReport#getSiteRenderer()
	 */
	@Override
	protected Renderer getSiteRenderer() 
	{
		return siteRenderer;
	}
	
	/**
	 * Returns the descriptor for the active plugin.
	 * 
	 * @return The descriptor for the plugin.
	 */
	protected PluginDescriptor getPluginDescriptor()
	{
		return descriptor;
	}
	
	/**
	 * Returns the helper class used to build a StyleGuide.
	 * 
	 * @return The StyleGuide helper class.
	 */
	protected StyleGuideBuilder getStyleGuideBuilder()
	{
		return styleGuideBuilder;
	}

	/**
	 * @see org.apache.maven.reporting.AbstractMavenReport#executeReport(Locale)
	 * @see StyleGuideExternalReportRenderer
	 * @see StyleGuideErrorReportRenderer
	 */
	@Override
	public void executeReport(Locale defaultLocale) throws MavenReportException 
	{
		ResourceBundle bundle = getBundle(defaultLocale);
		
		File jciteFragment = null;
		try
		{
			jciteFragment = generateContent();
		}
		catch (StyleGuideException e)
		{
			getLog().error("Unable to generate StyleGuide report content.", e);
			if (isFailOnError())
			{
				throw new MavenReportException("Unable to generate StyleGuide report content.", e);
			}
		}
		
		if (isValidFile(jciteFragment))
		{
			try
			{
				getLog().info("Rendering StyleGuide report.");
				getLog().debug("Processing file as JCite input: " + jciteFragment.getAbsolutePath());
				MavenReportRenderer renderer = new StyleGuideExternalReportRenderer(bundle, getSink(), reportLabel, jciteFragment);
				renderer.render();
			}
			catch (IOException e)
			{
				getLog().error("Unable to render StyleGuide report from JCite fragement.", e);
				if (isFailOnError())
				{
					throw new MavenReportException("Unable to render StyleGuide report from JCite fragement.", e);
				}
			}
		}
		else
		{
			getLog().info("No content was generated for the StyleGuide report.  The report file could not be found: " + getReportFile());
			if (isFailOnError())
			{
				throw new MavenReportException("No content was generated for the StyleGuide report.  The report file could not be found: " + getReportFile());
			}
			else
			{
				MavenReportRenderer renderer = new StyleGuideErrorReportRenderer(bundle, getSink(), reportLabel);
				renderer.render();					
			}
		}
	}

	/**
	 * Returns the list of source paths to include in annotation scanning and JCite processing.
	 * Subclasses must implement this method to specify the appropriate set of paths.
	 * 
	 * @return A list of source code paths to use in the plugin processing.
	 */
	protected abstract List<String> getSourcePaths();
	
	/**
	 * Generates the StyleGuide report content to be rendered.  The base implementation uses
	 * {@link StyleGuideBuilder#collateStyleGuideReport(org.apache.maven.plugin.logging.Log, StyleGuideDescriptor, PluginDescriptor)}.  
	 * Subclasses can override this implementation as needed.
	 * 
	 * @return A File containing the content of the StyleGuide report.
	 * @throws StyleGuideException If the report content cannot be generated.
	 */
	protected File generateContent() throws StyleGuideException
	{
		StyleGuideDescriptor styleGuideDescriptor = new StyleGuideDescriptor();
		styleGuideDescriptor.setBuildDirectory(getBuildDirectory());
		styleGuideDescriptor.setJciteCommandPath(getJciteCommandPath());
		styleGuideDescriptor.setStyleGuideTemplate(getStyleGuideTemplate());
		styleGuideDescriptor.setIncludesBasePath(getIncludesBasePath());
		styleGuideDescriptor.setSourcePaths(getSourcePaths());
		styleGuideDescriptor.setReportFile(getReportFile());
		styleGuideDescriptor.setXslTransformResource(getXslTransformResource());
		
		File jciteFragment = getStyleGuideBuilder().collateStyleGuideReport(getLog(), styleGuideDescriptor, getPluginDescriptor());
		
		return jciteFragment;
	}
	
	/**
	 * Returns the resource bundle used to resolve localized messages.
	 * 
	 * @param locale The locale for which the report is being rendered.
	 * @return The resource bundle to use for localized messages.
	 */
	protected ResourceBundle getBundle(Locale locale)
	{
		return ResourceBundle.getBundle("styleGuide", locale, this.getClass().getClassLoader());
	}
}
