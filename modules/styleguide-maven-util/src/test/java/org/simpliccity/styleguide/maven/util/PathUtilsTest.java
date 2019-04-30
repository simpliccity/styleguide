package org.simpliccity.styleguide.maven.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.simpliccity.styleguide.maven.util.PathUtils.isMatchingFile;

import java.io.File;
import java.util.regex.Pattern;

import org.junit.Test;

public class PathUtilsTest 
{
	private static final String PATTERN_FILE_SEP = Pattern.quote(File.separator);
	private static final String PATTERN_SHARED_RESOURCES = ".*" + PATTERN_FILE_SEP + "maven-shared-archive-resources";
	private static final String PATTERN_GENERATED_SOURCES = ".*" + PATTERN_FILE_SEP + "generated-sources" + PATTERN_FILE_SEP + ".*";
	private static final String PATTERN_GENERATED_TEST_SOURCES = ".*" + PATTERN_FILE_SEP + "generated-test-sources" + PATTERN_FILE_SEP + ".*";
	
	private static final Pattern[] PATH_PATTERNS = {Pattern.compile(PATTERN_SHARED_RESOURCES), Pattern.compile(PATTERN_GENERATED_SOURCES), Pattern.compile(PATTERN_GENERATED_TEST_SOURCES)};
	
	@Test
	public void testValidPath()
	{
		assertFalse("Validate inclusion of path not matching any pattern", isMatchingFile(new File(File.separator + "abc"), PATH_PATTERNS));
	}
	
	@Test
	public void testExcludeGeneratedSourcesPath()
	{
		assertTrue("Validate exclusion of generated-sources path", isMatchingFile(new File(File.separator + "junk" + File.separator + "generated-sources" + File.separator + "test"), PATH_PATTERNS));		
	}
	
	@Test
	public void testExcludeGeneratedTestSourcesPath()
	{
		assertTrue("Validate exclusion of generated-test-sources path", isMatchingFile(new File(File.separator + "junk" + File.separator + "generated-test-sources" + File.separator + "test"), PATH_PATTERNS));		
	}
	
	@Test
	public void testExcludeMavenSharedResources()
	{
		assertTrue("Validate exclusion of maven-shared-archive-resources path", isMatchingFile(new File(File.separator + "junk" + File.separator + "maven-shared-archive-resources"), PATH_PATTERNS));		
	}
}
