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

import static org.simpliccity.styleguide.maven.util.PathUtils.buildSourcePathList;
import static org.simpliccity.styleguide.maven.util.PathUtils.isValidFile;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

/**
 * Generates and renders a StyleGuide for a single project. The report
 * covers only the source code contained in that project.  This goal can
 * be inherited from a parent POM.
 *  
 * @author Kevin Fox
 *
 */
@Mojo(name="styleguide", defaultPhase=LifecyclePhase.SITE)
public class StyleGuideReport extends AbstractStyleGuideReport 
{
	@Override
	protected List<String> getSourcePaths() 
	{
		// Include only paths from this project
		List<MavenProject> projectList = new ArrayList<MavenProject>();
		projectList.add(getProject());
		
		List<String> result = buildSourcePathList(projectList, isIncludeTestSources(), isIncludeResources(), isIncludeBuildOutput());
		
		if (isValidFile(getIncludesBasePath()))
		{
			result.add(getIncludesBasePath().getAbsolutePath());
		}
		
		getLog().debug("Source paths: " + result);
		return result;
	}
}
