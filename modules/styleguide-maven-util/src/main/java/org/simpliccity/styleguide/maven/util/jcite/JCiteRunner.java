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

import static org.simpliccity.styleguide.maven.util.PathUtils.filterExcludedPaths;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.cli.Arg;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * A helper class used to perform JCite processing of files in the context of a Maven plugin.
 * JCite extracts code snippets from Java files and embeds them in HTML documentation.  
 * An instance of JCite must be available to execute on the local system.
 * 
 * @author Kevin Fox
 * @see <a href="http://www.arrenbrecht.ch/jcite/index.htm">JCite documentation</a>
 *
 */
public class JCiteRunner 
{
	private static final String PATTERN_FILE_SEP = Pattern.quote(File.separator);
	private static final String PATTERN_SHARED_RESOURCES = ".*" + PATTERN_FILE_SEP + "maven-shared-archive-resources";
	private static final String PATTERN_GENERATED_SOURCES = ".*" + PATTERN_FILE_SEP + "generated-sources" + PATTERN_FILE_SEP + ".*";
	private static final String PATTERN_GENERATED_TEST_SOURCES = ".*" + PATTERN_FILE_SEP + "generated-test-sources" + PATTERN_FILE_SEP + ".*";
	
	private static final Pattern[] PATH_EXCLUSIONS = {Pattern.compile(PATTERN_SHARED_RESOURCES), Pattern.compile(PATTERN_GENERATED_SOURCES), Pattern.compile(PATTERN_GENERATED_TEST_SOURCES)};
	
	private Log log;
	private String jciteCommandPath;
	
	/**
	 * Constructs a new instance of this class using the standard Maven logging mechanism and
	 * the path to the local JCite executable.
	 * 
	 * @param log The logging mechanism used within the context of a Maven plugin.
	 * @param jciteCommandPath The full path to the JCite executable.
	 */
	public JCiteRunner(Log log, String jciteCommandPath)
	{
		this.log = log;
		this.jciteCommandPath = jciteCommandPath;
	}
	
	/**
	 * Returns the Maven plugin logging mechanism.
	 * 
	 * @return The Maven plugin log.
	 */
	public Log getLog() 
	{
		return log;
	}

	/**
	 * Specifies the Maven plugin logging mechanism.
	 * 
	 * @param log The Maven plugin log.
	 */
	public void setLog(Log log) 
	{
		this.log = log;
	}

    /**
     * Returns the full path, including file name and extension, to the JCite executable
     * used to extract code snippets.
     * 
     * @return The path to the JCite executable.
     */
	public String getJciteCommandPath() 
	{
		return jciteCommandPath;
	}

	/**
	 * Specifies the full path, including file name and extension, to the JCite executable
     * used to extract code snippets.
     * 
	 * @param jciteCommandPath The path to the JCite executable.
	 */
	public void setJciteCommandPath(String jciteCommandPath) 
	{
		this.jciteCommandPath = jciteCommandPath;
	}

	/**
	 * Processes a single JCite template file, writing the results to the specified output path.
	 * The source code to scan for referenced code snippets or HTML includes is specified as a list of root paths.
	 * 
	 * @param template The JCite template file to process.  This file includes JCite tags that reference code snippets.
	 * @param outputPath The path to which to write the result of the JCite processing.
	 * @param sourceRoots The list of paths to scan for source code or HTML includes referenced by JCite tags in the template file.
	 * @throws CommandLineException If the JCite executable cannot be run.
	 */
	public void processTemplate(File template, File outputPath, List<String> sourceRoots) throws CommandLineException
	{
		performProcessing("template", template.getAbsolutePath(), outputPath.getAbsolutePath(), sourceRoots, false);		
	}

	/**
	 * Processes a set of JCite template files defined by a filename pattern, writing the results to the specified output path.
	 * The source code to scan for referenced code snippets or HTML includes is specified as a list of root paths.
	 * 
	 * @param filenamePattern The name pattern specifying the set of files to process.  The pattern should include the full path and may use wildcards.
	 * @param outputPath The path to which to write the results of the JCite processing.
	 * @param sourceRoots The list of paths to scan for source code or HTML includes referenced by JCite tags in the template file.
	 * @param recursive Flag indicating whether JCite should recursively apply the filename pattern to subfolders when referencing templates to process.
	 * @throws CommandLineException If the JCite executable cannot be run.
	 */
	public void processPattern(String filenamePattern, File outputPath, List<String> sourceRoots, boolean recursive) throws CommandLineException
	{
		performProcessing("pattern", filenamePattern, outputPath.getAbsolutePath(), sourceRoots, recursive);
	}

	private void performProcessing(String processingType, String inputArgValue, String ouputArgValue, List<String> sourceRoots, boolean recursive) throws CommandLineException
	{
		// Initialize command line
        Commandline commandLine = initializeCommandLine();        

        // Set command line arguments
        
        addInputArgument(commandLine, inputArgValue);
        
        addSourceArgument(commandLine, sourceRoots);

        addOutputArgument(commandLine,  ouputArgValue);

        addRecursiveArgument(commandLine, recursive);
        
        // Execute command
     	executeCommandLine(processingType, commandLine);
	}
	
	private Commandline initializeCommandLine()
	{
        Commandline commandLine = new Commandline(getJciteCommandPath());
        commandLine.getShell().setQuotedExecutableEnabled(true);
        commandLine.getShell().setQuotedArgumentsEnabled(true);

        return commandLine;
	}
	
	private void executeCommandLine(String processingType, Commandline commandLine) throws CommandLineException
	{
        CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();
        CommandLineUtils.StringStreamConsumer out = new CommandLineUtils.StringStreamConsumer();
        
    	getLog().debug("Executing JCite using command: " + CommandLineUtils.toString(commandLine.getCommandline()));
        getLog().debug("JCite command shell: " + commandLine.getShell());
        
    	int result = CommandLineUtils.executeCommandLine(commandLine, out, err);
    	
    	getLog().debug("JCite result: " + result);
    	
     	// Report results
     	switch (result)
     	{
     		case 0:
     			getLog().info("JCite " + processingType + " processing completed successfully.  Result file successfully generated.");
     	    	getLog().debug("JCite standard out:\n" + out.getOutput());
     	    	getLog().debug("JCite standard err:\n" + err.getOutput());
     			break;
     		case 1:
     			getLog().error("JCite " + processingType + " processing completed with errors.  Run Maven with the -X parameter for details.");
     	    	getLog().debug("JCite standard out:\n" + out.getOutput());
     	    	getLog().error("JCite standard err:\n" + err.getOutput());
     			throw new CommandLineException("JCite folder processing completed with errors.");
     		case 2:
     			getLog().info("JCite " + processingType + " processing completed with warnings.  Result file successfully generated.");
     	    	getLog().info("JCite standard out:\n" + out.getOutput());
     	    	getLog().debug("JCite standard err:\n" + err.getOutput());
     			break;
     		default:
     			getLog().error("JCite " + processingType + " processing completed with an unknown result.  Run Maven with the -X parameter for details.");
     	    	getLog().debug("JCite standard out:\n" + out.getOutput());
     	    	getLog().error("JCite standard err:\n" + err.getOutput());
     			throw new CommandLineException("JCite folder processing completed with an unknown result.");
     	}		
	}
	
	private void addInputArgument(Commandline commandLine, String input)
	{
        Arg inputArg = commandLine.createArg();
        inputArg.setLine("--input '" + input + "'");		
	}
	
	private void addOutputArgument(Commandline commandLine, String output)
	{
        Arg outputArg = commandLine.createArg();
        outputArg.setLine("--output '" + output + "'");		
	}
	
	private void addSourceArgument(Commandline commandLine, List<String> sourceRoots)
	{
		List<String> sources = filterExcludedPaths(sourceRoots, PATH_EXCLUSIONS);
		
        for (String sourcePath : sources)
        {
	        Arg sourceArg = commandLine.createArg();
	        sourceArg.setLine("--source-path '" + sourcePath + "'");
        }		
	}
	
	private void addRecursiveArgument(Commandline commandLine, boolean recursive)
	{
		if (recursive)
		{
			Arg recursiveArg = commandLine.createArg();
			recursiveArg.setLine("--recursive");
		}
	}
}
