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

package org.simpliccity.styleguide.schema.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.simpliccity.styleguide.annotation.CodeStyle;
import org.simpliccity.styleguide.annotation.CodeStyles;
import org.simpliccity.styleguide.schema.Categories;
import org.simpliccity.styleguide.schema.Category;
import org.simpliccity.styleguide.schema.StyleGuide;
import org.simpliccity.styleguide.schema.Topic;

/**
 * Provides functions to help manipulate the JAXB representation of a 
 * StyleGuide XML document.
 * 
 * @author Kevin Fox
 * @see org.simpliccity.styleguide.schema.StyleGuide
 *
 */
// --- StyleGuide styleGuide.multiple ---
@CodeStyles
({
	@CodeStyle(categoryId="JAXB", topicId="marshal"),
	@CodeStyle(categoryId="JAXB", topicId="unmarshal"),
	@CodeStyle(categoryId="styleGuide", topicId="multiple"),
	@CodeStyle(categoryId="styleGuide", topicId="marker")
})
public final class StyleGuideSchemaUtil 
{
	// --- StyleGuide styleGuide.multiple ---
	
	/**
	 * The default character encoding used for XML output ({@value}).
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";
	
	private static Logger logger = Logger.getLogger(StyleGuideSchemaUtil.class.getName());
	
	private StyleGuideSchemaUtil()
	{
		super();
	}
	
	/**
	 * Unmarshals a StyleGuide XML file to its JAXB representation.
	 * 
	 * @param inputPath The path to the StyleGuide XML file.
	 * @return The JAXB representation of the content.
	 * @throws JAXBException If the file cannot be unmarshalled.
	 */
	// --- StyleGuide JAXB.unmarshal ---
	public static StyleGuide loadStyleGuide(String inputPath) throws JAXBException
	{
		JAXBContext context = JAXBContext.newInstance(StyleGuide.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		StyleGuide result = (StyleGuide) unmarshaller.unmarshal(new File(inputPath));
		
		return result;
	}
	// --- StyleGuide JAXB.unmarshal ---
	
	/**
	 * Marshals the JAXB representation of StyleGuide content to a physical
	 * XML file.
	 * 
	 * @param styleGuide The JAXB representation of StyleGuide content.
	 * @param outputPath The path of the XML output file.
	 * @throws IOException If the file cannot written.
	 * @throws JAXBException If the JAXB content cannot be marshalled.
	 */
	public static void saveStyleGuide(StyleGuide styleGuide, String outputPath) throws IOException, JAXBException
	{
		try (OutputStream out = new FileOutputStream(outputPath))
		{
			marshallStyleGuide(styleGuide, out);
		}
	}
	
	/**
	 * Marshals the JAXB representation of StyleGuide content to a an ouput
	 * stream.
	 * 
	 * @param styleGuide The JAXB representation of StyleGuide content.
	 * @param out The output stream to which the XML output will be written.
	 * @throws IOException If the file cannot written.
	 * @throws JAXBException If the JAXB content cannot be marshalled.
	 */	
	public static void marshallStyleGuide(StyleGuide styleGuide, OutputStream out) throws IOException, JAXBException
	{
			// --- StyleGuide JAXB.marshal ---
			JAXBContext context = JAXBContext.newInstance(StyleGuide.class);
			
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			marshaller.marshal(styleGuide, out);
			// --- StyleGuide JAXB.marshal ---
	}
	
	/**
	 * Performs an XSL transformation on the JAXB representation of StyleGuide content and writes
	 * the result to an output file.
	 * 
	 * @param styleGuide The JAXB representation of StyleGuide content.
	 * @param transformResource The XSL transform, loaded as a resource from the classpath.
	 * @param outputPath The path of the output file.
	 * @throws IOException If the file cannot be written.
	 * @throws JAXBException If the JAXB content cannot be processed.
	 * @throws TransformerException If the XSL transformation cannot be performed.
	 */
	// --- StyleGuide styleGuide.marker ---
	@CodeStyle(categoryId="JAXB", topicId="transform")
	public static void transformStyleGuide(StyleGuide styleGuide, String transformResource, String outputPath) throws IOException, JAXBException, TransformerException
	{
		// --- StyleGuide JAXB.transform ---
		try(InputStream xsl = StyleGuideSchemaUtil.class.getResourceAsStream(transformResource); OutputStream out = new FileOutputStream(outputPath))
		{
			JAXBContext context = JAXBContext.newInstance(StyleGuide.class);
			JAXBSource source = new JAXBSource(context, styleGuide);
			
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer xform = factory.newTransformer(new StreamSource(xsl));
			
			xform.transform(source, new StreamResult(out));
			// --- StyleGuide JAXB.transform ---
			// --- StyleGuide styleGuide.marker ---
		}
	}
	
	/**
	 * Merges StyleGuide content with a skeleton template.  The template controls the structure
	 * of the result by specifying details of the categories and topics.  The individual code
	 * styles within the topics are defined by the StyleGuide content.
	 * 
	 * @param template The JAXB representation of the StyleGuide template.
	 * @param content The JAXB representation of the StyleGuide content.
	 * @return The JAXB representation of the merged StyleGuide content.
	 */
	@SuppressWarnings("deprecation")
	public static StyleGuide populateTemplate(StyleGuide template, StyleGuide content)
	{
		// Initialize the StyleGuide - template values take precedence
		StyleGuide result = new StyleGuide();
		result.setId((template.getId().isEmpty()) ? content.getId() : template.getId());
		result.setName((template.getName().isEmpty()) ? result.getId() : template.getName());
		result.setInclude(template.getInclude());
		result.setCitations(template.getCitations());
		
		// Merge content into categories that already exist in the template
		mergeExistingCategories(result, template, content);
		
		// Add on any categories from content that don't exist in the template
		for (Category category : content.getCategories().getCategory())
		{
			if (template.getCategories().getCategoryById(category.getId()) == null)
			{
				logger.fine("Adding category not included in template: " + category.getName());
				result.getCategories().getCategory().add(category);
			}
		}
		
		return result;
	}
	
	private static void mergeExistingCategories(StyleGuide result, StyleGuide template, StyleGuide content)
	{
		List<Category> categoryList = new ArrayList<Category>();
		
		// Loop through all categories in the template
		for (Category templateCategory : template.getCategories().getCategory())
		{
			logger.fine("Processing template category: " + templateCategory.getName());
			Category contentCategory = content.getCategories().getCategoryById(templateCategory.getId());
			
			// If there is a corresponding content category ...
			if (contentCategory != null)
			{
				// ... update the template category with information from it ...
				if (templateCategory.getName().isEmpty())
				{
					logger.fine("Setting category name from content category: " + contentCategory.getName());
					templateCategory.setName(contentCategory.getName());
				}
				
				// ... including any topics
				int topicCount = mergeTopics(templateCategory, contentCategory);
				
				// If there are any contained topics ...
				if (topicCount > 0)
				{
					// ... add this category to the list
					logger.fine("Adding category from template: " + templateCategory.getName());
					categoryList.add(templateCategory);
				}
			}

		}
		
		// Associate resulting categories with result style guide
		Categories categories = new Categories();
		categories.setCategory(categoryList);
		result.setCategories(categories);
	}
	
	private static int mergeTopics(Category template, Category content)
	{
		List<Topic> topicList = new ArrayList<Topic>();
		
		// Merge content into existing topics in template
		for (Topic templateTopic : template.getTopics().getTopic())
		{
			logger.fine("Processing template topic: " + templateTopic.getId());
			
			Topic contentTopic = content.getTopics().getTopicById(templateTopic.getId());
			
			if (contentTopic != null)
			{
				if (templateTopic.getName().isEmpty())
				{
					templateTopic.setName(contentTopic.getName());
				}
				
				templateTopic.setExemplars(contentTopic.getExemplars());
				
				logger.fine("Adding topic from template: " + templateTopic.getName());
				topicList.add(templateTopic);
			}
			else
			{
				logger.fine("No corresponding topic found in content: " + templateTopic.getId());
			}
		}
		
		// Add missing topics
		for (Topic contentTopic : content.getTopics().getTopic())
		{
			if (template.getTopics().getTopicById(contentTopic.getId()) == null)
			{
				logger.fine("Adding topic from conent; " + contentTopic.getName());
				topicList.add(contentTopic);
			}
		}
		
		// Update template category with topics
		template.getTopics().setTopic(topicList);
		
		return topicList.size();
	}
}
