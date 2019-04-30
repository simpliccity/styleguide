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
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.simpliccity.styleguide.maven.util.jcite.JCiteRunner;

/**
 * Performs JCite processing on a set of files to replace JCite tags with the corresponding code snippets.
 * For information on JCite, see <a href="http://www.arrenbrecht.ch/jcite/index.htm">the JCite web site</a>.
 * 
 * @author Kevin Fox
 *
 */
@Mojo(name="jcite", defaultPhase=LifecyclePhase.POST_SITE)
public class JCiteMojo extends AbstractMojo 
{
	/**
	 * The path to which processed files will be written.
	 */
    @Parameter(defaultValue=PluginConstants.DEFAULT_OUTPUT_PATH, required=true)
    private File outputPath;
    
    /**
     * The full path of the JCite executable, including file name and extension.
     */
    @Parameter(required=true, property=PluginConstants.PROPERTY_JCITE_COMMAND_PATH)
    private String jciteCommandPath;
    
    /**
     * The filename pattern specifying the set of files to process.
     */
    @Parameter(defaultValue=PluginConstants.DEFAULT_OUTPUT_PATH + "/*.html", required=true)
    private String filenamePattern;
    
    /**
     * Flag indicating whether to recursively process subfolders of the path specified in the
     * {@link #filenamePattern}.
     */
    @Parameter(defaultValue=PluginConstants.TRUE)
    private boolean recursive;

    /**
     * Flag indicating whether the build should fail if JCite cannot be successfully run.
     * Defaults to <code>false</code> to allow for effective inheritance across projects that
     * may not all have the specified filename pattern.
     */
    @Parameter (defaultValue=PluginConstants.FALSE, property=PluginConstants.PROPERTY_PLUGIN_FAILONERROR)
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
     * A list of paths to optional additional folders, beyond the list of Maven project source paths,
     * in which file references for code snippets can be found.  This can be used to provide
     * content specifically intended for JCite processing.  Only valid paths will be used.
     * 
     * @since 1.0.0
     */
    @Parameter()
    private String[] extraSourcePaths;

    /**
     * The projects in the reactor for the current project.
     */
    @Parameter(property="reactorProjects", required=true, readonly=true )
    private List<MavenProject> reactorProjects;

    /**
     * @see org.apache.maven.plugin.AbstractMojo#execute()
     */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException 
	{
		JCiteRunner jcite = new JCiteRunner(getLog(), jciteCommandPath);
		
        try
        {
        	jcite.processPattern(filenamePattern, outputPath, getSourcePaths(), recursive);
        }
        catch (CommandLineException e)
        {
			getLog().error("Unable to run JCite processing.", e);
			if (failOnError)
			{
				throw new MojoExecutionException("Unable to run JCite processing.", e);
			}
        }
	}
	
	private List<String> getSourcePaths() 
	{
		List<String> result = buildSourcePathList(reactorProjects, includeTestSources, includeResources, includeBuildOutput);
		
		if (extraSourcePaths != null)
		{
			for (String path : extraSourcePaths)
			{
				if (isValidFile(new File(path)))
				{
					result.add(path);
				}
			}
		}
		
		return result;
	}
}
