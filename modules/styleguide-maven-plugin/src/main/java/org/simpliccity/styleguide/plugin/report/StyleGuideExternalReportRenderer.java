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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.reporting.AbstractMavenReportRenderer;
import org.simpliccity.styleguide.plugin.PluginConstants;

/**
 * Helper class used to render a StyleGuide report from generated content.
 * 
 * @author Kevin Fox
 *
 */
public class StyleGuideExternalReportRenderer extends AbstractMavenReportRenderer 
{
	private static final int BUFFER_SIZE = 1024;
	
	private ResourceBundle bundle;
	private String reportLabel;
	private String reportFragment;
	
	/**
	 * Creates a new instance of the renderer.
	 * 
	 * @param bundle The resource bundle used to retrieve localized content.
	 * @param sink The target for the rendered content.
	 * @param reportLabel The project label to use in the report header.
	 * @param fragmentFile The file containing the generated StyleGuide content.
	 * @throws IOException If the fragment file cannot be read.
	 */
	public StyleGuideExternalReportRenderer(ResourceBundle bundle, Sink sink, String reportLabel, File fragmentFile) throws IOException
	{
		super(sink);
		this.bundle = bundle;
		this.reportLabel = reportLabel;
		this.reportFragment = getFileContents(fragmentFile);
	}

	/**
	 * @see org.apache.maven.reporting.AbstractMavenReportRenderer#getTitle()
	 */
	@Override
	public String getTitle() 
	{
		return bundle.getString(PluginConstants.KEY_REPORT_TITLE);
	}

	/**
	 * @see org.apache.maven.reporting.AbstractMavenReportRenderer#renderBody
	 */
	@Override
	protected void renderBody() 
	{
		String header = MessageFormat.format(bundle.getString(PluginConstants.KEY_REPORT_HEADER_TEMPLATE), reportLabel);
		
		sink.rawText(header + "\n" + reportFragment);
	}

	private String getFileContents(File file) throws IOException
	{
		StringBuilder stringBuffer = new StringBuilder(BUFFER_SIZE);
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file)))
		{
			char[] readBuffer = new char[BUFFER_SIZE];
			int count;
			while ((count = reader.read(readBuffer)) >= 0)
			{
				stringBuffer.append(readBuffer, 0, count);
			}
		}
		
		return stringBuffer.toString();
	}
}
