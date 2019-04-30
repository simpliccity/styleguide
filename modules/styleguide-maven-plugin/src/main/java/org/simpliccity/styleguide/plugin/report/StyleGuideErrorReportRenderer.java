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

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.reporting.AbstractMavenReportRenderer;
import org.simpliccity.styleguide.plugin.PluginConstants;

/**
 * Helper class used to render an error message for the StyleGuide report when
 * the report content could not be generated.
 * 
 * @author Kevin Fox
 *
 */
public class StyleGuideErrorReportRenderer extends AbstractMavenReportRenderer 
{
	private ResourceBundle bundle;
	private String reportLabel;
	
	/**
	 * Creates a new instance of the renderer.
	 * 
	 * @param bundle The resource bundle used to retrieve localized content.
	 * @param sink The target for the rendered content.
	 * @param reportLabel The project label to use in the report header.
	 */
	public StyleGuideErrorReportRenderer(ResourceBundle bundle, Sink sink, String reportLabel)
	{
		super(sink);
		this.bundle = bundle;
		this.reportLabel = reportLabel;
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
	 * @see org.apache.maven.reporting.AbstractMavenReportRenderer#renderBody()
	 */
	@Override
	protected void renderBody() 
	{
		String header = MessageFormat.format(bundle.getString(PluginConstants.KEY_REPORT_HEADER_TEMPLATE), reportLabel);
		
		sink.rawText(header + "\n" + "<p><h2>" + bundle.getString(PluginConstants.KEY_EMPTY_REPORT_TEXT) + "</h2></p>");
	}
}
