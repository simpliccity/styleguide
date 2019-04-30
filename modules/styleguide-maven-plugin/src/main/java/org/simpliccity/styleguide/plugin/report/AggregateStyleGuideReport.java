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

import java.util.List;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Generates and renders an aggregate StyleGuide for a multi-module project.  The report
 * covers source code from all projects contained in the build and must be run from the
 * top-level project.
 * 
 * @author Kevin Fox
 *
 */
@Mojo(name="aggregate", defaultPhase=LifecyclePhase.SITE, aggregator=true)
public class AggregateStyleGuideReport extends AbstractStyleGuideReport 
{
    /**
     * The projects in the reactor for aggregation report.
     */
    @Parameter(property="reactorProjects", required=true, readonly=true )
    private List<MavenProject> reactorProjects;

    /**
     * @see org.apache.maven.reporting.AbstractMavenReport#canGenerateReport()
     */
    @Override
    public boolean canGenerateReport()
    {
    	return getProject().isExecutionRoot();
    }
    
	@Override
	protected List<String> getSourcePaths() 
	{
		List<String> result = buildSourcePathList(reactorProjects, isIncludeTestSources(), isIncludeResources(), isIncludeBuildOutput());

		if (isValidFile(getIncludesBasePath()))
		{
			result.add(getIncludesBasePath().getAbsolutePath());
		}

		getLog().debug("Source paths: " + result);
		return result;
	}
}
