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
import static org.simpliccity.styleguide.maven.util.PathUtils.isValidFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.simpliccity.styleguide.maven.util.StyleGuideBuilder;
import org.simpliccity.styleguide.maven.util.StyleGuideDescriptor;
import org.simpliccity.styleguide.maven.util.StyleGuideException;

/**
 * Generates the core content of a StyleGuide report to a file for further processing.
 * Combined with the <code>report</code> goal, this allows a StyleGuide generated from
 * the top-level project to be output as part of the site for a subproject.
 * 
 * @author Kevin Fox
 *
 */
@Mojo(name="collate", defaultPhase=LifecyclePhase.PRE_SITE, aggregator=true)
public class StyleGuideCollateMojo extends AbstractMojo 
{
	/**
	 * Collate scope setting to process all projects in the build (<b>{@value}</b>)
	 */
	public static final String SCOPE_BUILD = "build";

	/**
	 * Collate scope setting to process only the current project (<b>{@value}</b>)
	 */
	public static final String SCOPE_PROJECT = "project";
	
    /**
     * The directory in which the plugin will write intermediate files generated while creating the report.
     */
    @Parameter (defaultValue=PluginConstants.DEFAULT_BUILD_PATH, required=true)
    private File buildDirectory;
    
    /**
     * The full path of the JCite executable, including file name and extension.
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
	 * Path, including filename, of the report file to be created.
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
     * The scope of projects to process when collating the StyleGuide content.  A setting
     * of <b>build</b> causes all projects in the build to be scanned, while <b>project</b>
     * limits the scan to the current project.
     * 
     * @since 1.0.0
     * @see #SCOPE_BUILD
     * @see #SCOPE_PROJECT
     */
    @Parameter(defaultValue=SCOPE_BUILD)
    private String scope;
    
    /**
     * The projects in the reactor for aggregation report.
     */
    @Parameter(property="reactorProjects", required=true, readonly=true )
    private List<MavenProject> reactorProjects;
    
    /**
     * The Maven Project.
     */
    @Parameter(property="project", required=true, readonly=true )
    private MavenProject project;

	/**
	 * The descriptor for the plugin.
	 */
    @Parameter(property="plugin", required=true, readonly=true )
	private PluginDescriptor pluginDescriptor;
	
	/**
	 * Helper class used to build Style Guide content.
	 */
	@Component
	private StyleGuideBuilder styleGuideBuilder;
	
    /**
     * @see org.apache.maven.plugin.AbstractMojo#execute()
     */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException 
	{
		StyleGuideDescriptor styleGuideDescriptor = new StyleGuideDescriptor();
		styleGuideDescriptor.setBuildDirectory(buildDirectory);
		styleGuideDescriptor.setJciteCommandPath(jciteCommandPath);
		styleGuideDescriptor.setStyleGuideTemplate(styleGuideTemplate);
		styleGuideDescriptor.setIncludesBasePath(includesBasePath);
		styleGuideDescriptor.setSourcePaths(getSourcePaths());
		styleGuideDescriptor.setReportFile(reportFile);
		styleGuideDescriptor.setXslTransformResource(xslTransformResource);
		
		try 
		{
			styleGuideBuilder.collateStyleGuideReport(getLog(), styleGuideDescriptor, pluginDescriptor);
		}
		catch (StyleGuideException e) 
		{
			getLog().error("Unable to collate StyleGuide report content.", e);
			throw new MojoExecutionException("Unable to collate StyleGuide report content.", e);
		}		
	}

	private List<String> getSourcePaths() 
	{
		// Set Maven projects to include based on scope setting
		List<MavenProject> scannedProjects;
		if (SCOPE_BUILD.equalsIgnoreCase(scope))
		{
			// Include all projects in the build
			scannedProjects = reactorProjects ;
		}
		else
		{
			// Limit to current project
			scannedProjects = new ArrayList<MavenProject>();
			scannedProjects.add(project);
		}
		
		List<String> result = buildSourcePathList(scannedProjects, includeTestSources, includeResources, includeBuildOutput);

		if (isValidFile(includesBasePath))
		{
			result.add(includesBasePath.getAbsolutePath());
		}

		getLog().debug("Source paths: " + result);
		return result;
	}
}
