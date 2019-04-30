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
import java.io.Serializable;
import java.util.List;

/**
 * A wrapper class used to configure the process of generating StyleGuide content
 * within the context of a Maven plugin.
 * 
 * @author Kevin Fox
 *
 */
public class StyleGuideDescriptor implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String jciteCommandPath;
    private File buildDirectory;
    private File styleGuideTemplate;
    private File includesBasePath;
    private List<String> sourcePaths;
    private File reportFile;
    private String xslTransformResource;
    
    /**
     * Returns the full path, including file name and extension, to the JCite executable
     * used to extract code snippets for the StyleGuide content.
     * 
     * @return The path to the JCite executable.
     */
	public String getJciteCommandPath() 
	{
		return jciteCommandPath;
	}
	
	/**
	 * Specifies the full path, including file name and extension, to the JCite executable
     * used to extract code snippets for the StyleGuide content.
     * 
	 * @param jciteCommandPath The path to the JCite executable.
	 */
	public void setJciteCommandPath(String jciteCommandPath) 
	{
		this.jciteCommandPath = jciteCommandPath;
	}

    /**
     * Returns the directory in which intermediate files will be written while generating the 
     * StyleGuide content.
     * 
     * @return The path of the build directory.
     */
	public File getBuildDirectory() 
	{
		return buildDirectory;
	}

	/**
	 * Specifies the directory in which intermediate files will be written while generating the 
     * StyleGuide content.
     * 
	 * @param buildDirectory The path of the build directory.
	 */
	public void setBuildDirectory(File buildDirectory) 
	{
		this.buildDirectory = buildDirectory;
	}

	/**
	 * Returns the full path of a template for this StyleGuide, if any.
	 * 
	 * @return The path of the StyleGuide template.
	 */
	public File getStyleGuideTemplate() 
	{
		return styleGuideTemplate;
	}

	/**
	 * Specifies the full path of a template for this StyleGuide.
	 * 
	 * @param styleGuideTemplate The path of the StyleGuide template to use.
	 */
	public void setStyleGuideTemplate(File styleGuideTemplate) 
	{
		this.styleGuideTemplate = styleGuideTemplate;
	}

	/**
     * Returns the path to a folder containing HTML snippets referenced as includes by the StyleGuide annotations
     * or the project StyleGuide template.  The use of includes is optional; however, this parameter must be
     * set appropriately if any includes are referenced through annotations or the project template.  Missing
     * included files will cause the report to fail.
     * 
	 * @return The base path for included files.
	 * @see org.simpliccity.styleguide.annotation.CodeStyle#include()
	 * @see org.simpliccity.styleguide.schema.Category#getInclude()
	 * @see org.simpliccity.styleguide.schema.Topic#getInclude()
	 */
	public File getIncludesBasePath() 
	{
		return includesBasePath;
	}

	/**
	 * Specifies the path to a folder containing HTML snippets referenced as includes by the StyleGuide annotations
     * or the project StyleGuide template.  The use of includes is optional; however, this parameter must be
     * set appropriately if any includes are referenced through annotations or the project template.  Missing
     * included files will cause the report to fail.
     * 
	 * @param includesBasePath The base path for included files.
	 */
	public void setIncludesBasePath(File includesBasePath) 
	{
		this.includesBasePath = includesBasePath;
	}

	/**
	 * Returns the list of source paths to include in annotation scanning and JCite processing.
	 * 
	 * @return The list of source paths to use.
	 */
	public List<String> getSourcePaths() 
	{
		return sourcePaths;
	}
	
	/**
	 * Specifies the list of source paths to include in annotation scanning and JCite processing.
	 * 
	 * @param sourcePaths The list of source paths to use.
	 */
	public void setSourcePaths(List<String> sourcePaths) 
	{
		this.sourcePaths = sourcePaths;
	}

	/**
	 * Returns the full path of the StyleGuide content file to create.
	 * 
	 * @return The path of the generated StyleGuide content file.
	 */
	public File getReportFile() 
	{
		return reportFile;
	}

	/**
	 * Specifies the full path of the StyleGuide content file to create.
	 * 
	 * @param reportFile The path of the generated StyleGuide content file.
	 */
	public void setReportFile(File reportFile) 
	{
		this.reportFile = reportFile;
	}

	/**
	 * Returns the name of the resource to use to perform an XSL transformation on the
	 * XML output produced by the StyleGuide annotation processing.  The specified resource
	 * must appear on the classpath.
	 * 
	 * @return The name of the XSL transformation resource.
	 */
	public String getXslTransformResource() 
	{
		return xslTransformResource;
	}

	/**
	 * Specifies the name of the resource to use to perform an XSL transformation on the
	 * XML output produced by the StyleGuide annotation processing.  The specified resource
	 * must appear on the classpath.
	 * 
	 * @param xslTransformResource The name of the XSL transformation resource.
	 */
	public void setXslTransformResource(String xslTransformResource) 
	{
		this.xslTransformResource = xslTransformResource;
	}
}
