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
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;

/**
 * Provides functions to work with dependencies and file paths in a Maven plugin context.
 * 
 * @author Kevin Fox
 *
 */
public final class PathUtils
{
	private PathUtils()
	{
		super();
	}
	
	/**
	 * Selects the artifacts in a collection that belong to a specified group.
	 * 
	 * @param artifacts A collection of Maven artifacts.
	 * @param groupId The <code>groupId</code> by which to search.
	 * @return The list of Maven artifacts from the original collection that belong to the specified group.
	 */
	public static List<Artifact> filterArtifactsByGroup(Collection<Artifact> artifacts, String groupId)
	{
		List<Artifact> filtered = new ArrayList<Artifact>();
		
		for(Artifact artifact : artifacts)
		{
			if (groupId.equals(artifact.getGroupId()))
			{
				filtered.add(artifact);
			}
		}
		
		return filtered;
	}
	
	/**
	 * Determines the absolute file path of a Maven artifact.
	 * 
	 * @param artifact The Maven artifact.
	 * @return The absolute path of the corresponding file or an empty string if none.
	 */
	public static String getArtifactPath(Artifact artifact)
	{
		File artifactFile = artifact.getFile();
		
		String result = (artifactFile == null) ? "" : artifactFile.getAbsolutePath();
				
		return result;
	}

	/**
	 * Filters a list of file paths to return only those that represent actual
	 * file system objects.
	 * 
	 * @param candidatePaths The list of paths to filter.
	 * @return A list, possibly empty, of those paths that correspond to file system objects.
	 */
	public static List<String> filterValidPaths(List<String> candidatePaths)
	{
		return filterPaths(candidatePaths, new ValidFileFilter());
	}
	
	/**
	 * Filters a list of file paths to exclude those matching the specified
	 * regular expression patterns.
	 * 
	 * @param candidatePaths The list of paths to filter.
	 * @param exclusions The list of regular expression patterns to exclude.
	 * @return A list, possibly empty, of those paths that correspond to file system objects.
	 */
	public static List<String> filterExcludedPaths(List<String> candidatePaths, Pattern[] exclusions)
	{
		return filterPaths(candidatePaths, new ExclusionMatchingFilter(exclusions));
	}
	
	/**
	 * Filters a list of file paths to return only those that meet the criteria
	 * of the supplied filter.
	 * 
	 * @param candidatePaths The list of paths to filter.
	 * @param filter The filter criteria to apply.
	 * @return A list, possibly empty, of those paths that match the filter criteria.
	 */
	public static List<String> filterPaths(List<String> candidatePaths, FileFilter filter)
	{
		List<String> result = new ArrayList<String>();
		
		for (String path : candidatePaths)
		{
			if (path != null)
			{
				File tempFile = new File(path);
				if (filter.accept(tempFile))
				{
					result.add(path);
				}
			}
		}
		
		return result;		
	}
	
	/**
	 * Determines whether a {@link java.io.File} represents an actual
	 * file system object.
	 * 
	 * @param candidate The {@link java.io.File} to check.
	 * @return <code>true</code> if a corresponding file system object exists; <code>false</code> otherwise.
	 */
	public static boolean isValidFile(File candidate)
	{
		return new ValidFileFilter().accept(candidate);
	}
	
	/**
	 * Determines whether a {@link java.io.File} pathname matches
	 * any of the specified regular expression patterns.
	 * 
	 * @param candidate The {@link java.io.File} to check.
	 * @param patterns The list of regular expression patterns to match.
	 * @return <code>true</code> if a corresponding file system object exists; <code>false</code> otherwise.
	 */
	public static boolean isMatchingFile(File candidate, Pattern[] patterns)
	{
		return new PatternMatchingFilter(patterns).accept(candidate);
	}
	
	/**
	 * Builds a list of file paths representing the source locations of a collection of Maven projects.
	 * Only valid paths are included in the result
	 * 
	 * @param projects The Maven projects whose source locations are processed.
	 * @param includeTestSources A flag indicating whether to include the source paths for test classes.
	 * @param includeResources A flag indicating whether to include the source path for project resources.
	 * @return The list of valid file paths represented by the source locations of the specified projects.
	 * @see #filterValidPaths(List)
	 */
	public static List<String> buildSourcePathList(List<MavenProject> projects, boolean includeTestSources, boolean includeResources, boolean includeBuildOutput)
	{
		List<String> result = new ArrayList<String>();
		
		for (MavenProject subproject : projects)
		{
			if (! "pom".equalsIgnoreCase(subproject.getPackaging()))
			{
				result.addAll(filterValidPaths(subproject.getCompileSourceRoots()));
				if (includeResources)
				{
					result.addAll(buildResourcePathList(subproject.getResources()));
				}
				if (includeBuildOutput)
				{
					List<String> buildPaths = Collections.singletonList(subproject.getBuild().getOutputDirectory());
					result.addAll(filterValidPaths(buildPaths));
				}
		
				if (includeTestSources)
				{
					result.addAll(filterValidPaths(subproject.getTestCompileSourceRoots()));
					if (includeResources)
					{
						result.addAll(buildResourcePathList(subproject.getTestResources()));
					}
					if (includeBuildOutput)
					{
						List<String> buildPaths = Collections.singletonList(subproject.getBuild().getTestOutputDirectory());
						result.addAll(filterValidPaths(buildPaths));
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Builds a list of file paths representing processed project resources.  Only valid paths are included in the result.
	 * 
	 * @param resources The Maven project resources to process.
	 * @return The list of valid file paths represented by the target locations of the specified resources.
	 * @see #filterValidPaths(List)
	 */
	public static List<String> buildResourcePathList(List<Resource> resources)
	{
		List<String> result = new ArrayList<String>();
		
		for (Resource resource : resources)
		{
			result.add(resource.getDirectory());
		}
		
		return filterValidPaths(result);
	}
}

class ValidFileFilter implements FileFilter
{
	@Override
	public boolean accept(File pathname) 
	{
		return (pathname != null && pathname.exists());
	}
}

class PatternMatchingFilter implements FileFilter
{
	private Pattern[] patterns;
	
	PatternMatchingFilter(Pattern[] patterns)
	{
		this.patterns = (patterns == null) ? null : Arrays.copyOf(patterns, patterns.length);
	}

	@Override
	public boolean accept(File pathname) 
	{
		boolean result = false;
		
		for (Pattern pattern : patterns)
		{
			Matcher matcher = pattern.matcher(pathname.getAbsolutePath());
			result = matcher.matches();
			
			if (result)
			{
				break;
			}
		}
		
		return result;
	}
}

class ExclusionMatchingFilter extends PatternMatchingFilter
{
	ExclusionMatchingFilter(Pattern[] exclusions)
	{
		super(exclusions);
	}
	
	@Override
	public boolean accept(File pathname)
	{
		return !super.accept(pathname);
	}
}
