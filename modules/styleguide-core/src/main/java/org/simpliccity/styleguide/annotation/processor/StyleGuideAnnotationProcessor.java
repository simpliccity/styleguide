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

package org.simpliccity.styleguide.annotation.processor;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import org.simpliccity.styleguide.annotation.CodeStyle;
import org.simpliccity.styleguide.annotation.CodeStyles;
import org.simpliccity.styleguide.annotation.StyleCitation;
import org.simpliccity.styleguide.schema.Categories;
import org.simpliccity.styleguide.schema.Category;
import org.simpliccity.styleguide.schema.Citation;
import org.simpliccity.styleguide.schema.Citations;
import org.simpliccity.styleguide.schema.Exemplar;
import org.simpliccity.styleguide.schema.Exemplars;
import org.simpliccity.styleguide.schema.StyleGuide;
import org.simpliccity.styleguide.schema.Topic;
import org.simpliccity.styleguide.schema.Topics;
import org.simpliccity.styleguide.schema.util.CategoryComparator;
import org.simpliccity.styleguide.schema.util.StyleGuideSchemaUtil;
import org.simpliccity.styleguide.schema.util.TopicComparator;

/**
 * An implementation of {@link javax.annotation.processing.Processor} that generates an XML file
 * out of the contents of all {@link org.simpliccity.styleguide.annotation.CodeStyle} and 
 * {@link org.simpliccity.styleguide.annotation.CodeStyles} annotations found in the codebase.
 * The behavior of this class can be configured using system properties as described below.
 * 
 * @see org.simpliccity.styleguide.schema.StyleGuide
 * 
 * @author Kevin Fox
 *
 */
@CodeStyle(categoryId="annotation", topicId="processor")
// --- StyleGuide annotation.processor ---
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("org.simpliccity.styleguide.annotation.*")
public class StyleGuideAnnotationProcessor extends AbstractProcessor 
{
	// --- StyleGuide annotation.processor ---
	
	/**
	 * <p>The system property used to specify the output path of the generated  
	 * XML file.</p>
	 * 
	 * <p><b>Property name:</b> {@value}</p>
	 * <p><b>Default value:</b> {@value #DEFAULT_OUTPUT_PATH}
	 */
	public static final String PROPERTY_OUTPUT_PATH = "org.simpliccity.styleguide.outputPath";

	/**
	 * <p>The system property used to specify the default template for the comment marker 
	 * used to delimit code snippet(s).</p>
	 * 
	 * <p><b>Property name:</b> {@value}</p>
	 * <p><b>Default value:</b> {@value #DEFAULT_MARKER_TEMPLATE}
	 */
	public static final String PROPERTY_MARKER_TEMPLATE = "org.simpliccity.styleguide.markerTemplate";

	/**
	 * <p>The system property used to specify the value used for the ID attribute
	 * of the &lt;styleguide&gt; element in the generated XML file.</p>
	 * 
	 * <p><b>Property name:</b> {@value}</p>
	 * <p><b>Default value:</b> {@value #DEFAULT_STYLEGUIDE_ID}
	 */
	public static final String PROPERTY_STYLEGUIDE_ID = "org.simpliccity.styleguide.id";

	/**
	 * The default value for the {@value #PROPERTY_OUTPUT_PATH} system property.
	 */
	public static final String DEFAULT_OUTPUT_PATH = "./styleGuide.xml";

	/**
	 * The default value for the {@value #PROPERTY_MARKER_TEMPLATE} system property.
	 */
	public static final String DEFAULT_MARKER_TEMPLATE = "--- StyleGuide {0}.{1} ---";
	
	/**
	 * The default value for the {@value #PROPERTY_STYLEGUIDE_ID} system property.
	 */
	public static final String DEFAULT_STYLEGUIDE_ID = "default";
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
		
	private StyleGuide guide;
	
	/**
	 * @see javax.annotation.processing.Processor#process(Set, RoundEnvironment)
	 */
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment round) 
	{
		for (TypeElement element : annotations)
		{
			if (isMatchingAnnotation(CodeStyle.class, element))
			{
				logger.fine("Processing CodeStyle annotation.");
				processAnnotation(element, round, CodeStyle.class);
			}
			else if (isMatchingAnnotation(CodeStyles.class, element))
			{
				logger.fine("Processing CodeStyles annotation.");
				processAnnotation(element, round, CodeStyles.class);				
			}
		}
		
		if (guide != null)
		{
			// Sort categories and topics
			Collections.sort(guide.getCategories().getCategory(), new CategoryComparator());
			for (Category category : guide.getCategories().getCategory())
			{
				Collections.sort(category.getTopics().getTopic(), new TopicComparator());
			}
			
			generatePageTemplate(guide);
		}
		else
		{
			logger.info("No qualifying annotations found.");
		}
		
		return true;
	}
	
	private <T extends Annotation> boolean isMatchingAnnotation(Class<T> annotationType, TypeElement element)
	{
		return annotationType.getName().equals(element.getQualifiedName().toString());
	}
	
	private <T extends Annotation> void processAnnotation(TypeElement element, RoundEnvironment round, Class<T> annotationType)
	{
		if (guide == null)
		{
			initializeStyleGuide();
		}
		
		for (Element enclosed : round.getElementsAnnotatedWith(element))
		{
			T annotation = (T) enclosed.getAnnotation(annotationType);
			
			CodeStyle[] styles = null;
			if (annotation instanceof CodeStyle)
			{
				styles = new CodeStyle[1];
				styles[0] = (CodeStyle) annotation;
			}
			else if (annotation instanceof CodeStyles)
			{
				styles = ((CodeStyles) annotation).value();
			}
			
			if (styles != null)
			{
				for (CodeStyle style: styles)
				{
					processCodeStyle(style, enclosed);
				}
			}
		}						
	}
	
	private void processCodeStyle(CodeStyle style, Element enclosed)
	{
		Category category = getCategory(style);
		Topic topic = getTopic(category, style);
		
		TypeElement type = (TypeElement) ((enclosed.getKind() == ElementKind.METHOD) ?  enclosed.getEnclosingElement() : enclosed);
		String className = type.getQualifiedName().toString();
		Exemplar exemplar = initializeExemplar(style, className);
		topic.getExemplars().getExemplar().add(exemplar);		
	}
	
	private void initializeStyleGuide()
	{
		guide = new StyleGuide();
		guide.setId(System.getProperty(PROPERTY_STYLEGUIDE_ID, DEFAULT_STYLEGUIDE_ID));
		guide.setName(System.getProperty(PROPERTY_STYLEGUIDE_ID, DEFAULT_STYLEGUIDE_ID));
		guide.setCategories(new Categories());
		guide.getCategories().setCategory(new ArrayList<Category>());
	}
	
	private Category getCategory(CodeStyle style)
	{
		String name = (style.categoryName().isEmpty()) ? style.categoryId() : style.categoryName();
		Category result = guide.getCategories().getCategoryById(style.categoryId());
		if (result == null)
		{
			result = new Category();
			result.setId(style.categoryId());
			result.setName(name);
			result.setTopics(new Topics());
			result.getTopics().setTopic(new ArrayList<Topic>());
			guide.getCategories().getCategory().add(result);
		}
		else
		{
			if (!name.isEmpty())
			{
				result.setName(name);
			}
		}
		
		return result;
	}
	
	private Topic getTopic(Category category, CodeStyle style)
	{
		String name = (style.topicName().isEmpty()) ? style.topicId() : style.topicName();
		Topic result = category.getTopics().getTopicById(style.topicId());
		if (result == null)
		{
			result = new Topic();
			result.setId(style.topicId());
			result.setName(name);
			result.setExemplars(new Exemplars());
			result.getExemplars().setExemplar(new ArrayList<Exemplar>());
			category.getTopics().getTopic().add(result);
		}
		else
		{
			if (!name.isEmpty())
			{
				result.setName(name);
			}			
		}
		
		return result;
	}
	
	@SuppressWarnings("deprecation")
	private Exemplar initializeExemplar(CodeStyle style, String className)
	{
		Exemplar result = new Exemplar();
		result.setClassName(className);
		
		String marker = style.marker();
		if (marker.isEmpty())
		{
			marker = MessageFormat.format(System.getProperty(PROPERTY_MARKER_TEMPLATE, DEFAULT_MARKER_TEMPLATE), style.categoryId(), style.topicId());
		}
		result.setMarker(marker);
		
		if (!style.include().isEmpty())
		{
			result.setInclude(style.include());
		}
		
		if (style.citations().length > 0)
		{
			result.setCitations(initializeCitations(style.citations()));
		}
		
		return result;
	}
	
	private Citations initializeCitations(StyleCitation[] styleCitations)
	{
		Citations result = new Citations();
		
		List<Citation> citationList = new ArrayList<Citation>();
		result.setCitation(citationList);
		
		for (StyleCitation styleCitation : styleCitations)
		{
			Citation citation = new Citation();
			citation.setRef(styleCitation.ref());
			citation.setType(styleCitation.type());
			citation.setPlacement(styleCitation.placement());
			citation.setDisplayRef(styleCitation.displayRef());

			// Marker is optional attribute
			String marker = styleCitation.marker();
			if (marker != null && !marker.isEmpty())
			{
				citation.setMarker(marker);
			}
						
			citationList.add(citation);
		}
		
		return result;
	}
	
	private void generatePageTemplate(StyleGuide styleGuide)
	{
		try
		{
			String outputPath = System.getProperty(PROPERTY_OUTPUT_PATH, DEFAULT_OUTPUT_PATH);
			
			StyleGuideSchemaUtil.saveStyleGuide(styleGuide, outputPath);
		}
		catch (Exception e)
		{
			logger.throwing(this.getClass().getName(), "generatePageTemplate", e);
		}
	}
}
