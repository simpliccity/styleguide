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

package org.simpliccity.styleguide.maven.util.annotation;

import static org.simpliccity.styleguide.maven.util.PathUtils.filterArtifactsByGroup;
import static org.simpliccity.styleguide.maven.util.PathUtils.getArtifactPath;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.codehaus.plexus.compiler.Compiler;
import org.codehaus.plexus.compiler.CompilerConfiguration;
import org.codehaus.plexus.compiler.CompilerException;
import org.codehaus.plexus.compiler.CompilerMessage;
import org.codehaus.plexus.compiler.CompilerResult;
import org.codehaus.plexus.compiler.manager.CompilerManager;
import org.codehaus.plexus.compiler.manager.NoSuchCompilerException;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.simpliccity.styleguide.annotation.CodeStyle;

/**
 * An implementation of {@link AnnotationProcessor} using the Plexus
 * {@link org.codehaus.plexus.compiler.manager.CompilerManager}.
 * 
 * @author Kevin Fox
 *
 */
@CodeStyle(categoryId="plexus", topicId="useComponent")
@Component(role=AnnotationProcessor.class)
public class PlexusCompilerAnnotationProcessor implements AnnotationProcessor 
{
	private static final String ID_COMPILER = "javac";
	private static final String DEFAULT_COMPILER_VERSION = "1.7";
	private static final String DEFAULT_ANNOTATION_PROCESSING = "only";
	
	// --- StyleGuide plexus.useComponent ---
	@Requirement(role=CompilerManager.class)
	private CompilerManager compilerManager;
	// --- StyleGuide plexus.useComponent ---
	
	/**
	 * Returns the {@link org.codehaus.plexus.compiler.manager.CompilerManager} used
	 * to perform annotation processing.
	 * 
	 * @return The configured {@link org.codehaus.plexus.compiler.manager.CompilerManager}.
	 */
	public CompilerManager getCompilerManager() 
	{
		return compilerManager;
	}

	/**
	 * Specifies the {@link org.codehaus.plexus.compiler.manager.CompilerManager} used
	 * to perform annotation processing.  This component is injected by the Plexus container.
	 * 
	 * @param compilerManager The configured {@link org.codehaus.plexus.compiler.manager.CompilerManager}.
	 */
	public void setCompilerManager(CompilerManager compilerManager) 
	{
		this.compilerManager = compilerManager;
	}

	@Override
	public List<String> processAnnotations(List<String> sourceLocations, File buildDirectory, String[] annotationProcessors, List<String> classpathEntries) throws AnnotationProcessorException
	{
		CompilerConfiguration config = new CompilerConfiguration();
		
		config.setSourceLocations(sourceLocations);
		config.setAnnotationProcessors(annotationProcessors);
		config.setClasspathEntries(classpathEntries);
		config.setOutputLocation(buildDirectory.getAbsolutePath());
		config.setWorkingDirectory(buildDirectory);
		config.setSourceVersion(DEFAULT_COMPILER_VERSION);
		config.setTargetVersion(DEFAULT_COMPILER_VERSION);
		config.setProc(DEFAULT_ANNOTATION_PROCESSING);
		
		List<String> messages = null;
		
		try
		{
			Compiler compiler = getCompilerManager().getCompiler(ID_COMPILER);
			
			CompilerResult result = compiler.performCompile(config);
			
			messages = compileResultMessages(result);
			
		}
		catch (NoSuchCompilerException e)
		{
			throw new AnnotationProcessorException("Annotation processing was unable to obtain a compiler instance.", e);
		}
		catch (CompilerException e)
		{
			throw new AnnotationProcessorException("Annotation processing experienced an error.", e);
		}
		
		return messages;
	}

	@Override
	public List<String> processAnnotations(List<String> sourceLocations, File buildDirectory, String[] annotationProcessors, PluginDescriptor descriptor) throws AnnotationProcessorException 
	{
		return processAnnotations(sourceLocations, buildDirectory, annotationProcessors, generateProcessorClasspath(descriptor));
	}
	
	private List<String> generateProcessorClasspath(PluginDescriptor descriptor)
	{
		List<String> results = new ArrayList<String>();
		
		List<Artifact> dependencies = filterArtifactsByGroup(descriptor.getArtifacts(), descriptor.getGroupId());
		
		for(Artifact artifact : dependencies)
		{
			results.add(getArtifactPath(artifact));
		}
		
		return results;
	}
	
	private List<String> compileResultMessages(CompilerResult result)
	{
		List<String> messages = new ArrayList<String>();
		
		List<CompilerMessage> resultMessages = result.getCompilerMessages();
		for (CompilerMessage message : resultMessages)
		{
			messages.add(message.getMessage());
		}
		
		return messages;
	}
}
