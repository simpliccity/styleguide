<?xml version="1.0" encoding="UTF-8"?>
<!--

    Copyright 2013 Information Control Corporation

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 
-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sg="http://www.simpliccity.org/schema/styleguide">
	<xsl:output method="html" omit-xml-declaration="yes" encoding="utf-8" />
	
	<xsl:template match="/sg:styleguide">
		<xsl:if test="string-length(@include) > 0">
		<p>[inc:<xsl:value-of select="@include" />]</p>
		</xsl:if>
		<xsl:if test="sg:citations">
			<xsl:apply-templates select="sg:citations/sg:citation[@placement='TOP']" />
		</xsl:if>
		
		<xsl:apply-templates mode="body"/>

		<xsl:if test="sg:citations">
			<xsl:apply-templates select="sg:citations/sg:citation[@placement='BOTTOM']" />
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="sg:category" mode="body">
		<p><a><xsl:attribute name='id'><xsl:call-template name="formatCategoryAnchor" /></xsl:attribute><h2><xsl:value-of select="@name" /></h2></a></p>
		<xsl:if test="string-length(@include) > 0">
		<p>[inc:<xsl:value-of select="@include" />]</p>
		</xsl:if>
		<xsl:if test="sg:citations">
			<xsl:apply-templates select="sg:citations/sg:citation[@placement='TOP']" />
		</xsl:if>
		
		<xsl:apply-templates mode="body"/>
		
		<xsl:if test="sg:citations">
			<xsl:apply-templates select="sg:citations/sg:citation[@placement='BOTTOM']" />
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="sg:topic" mode="body">
		<p><a><xsl:attribute name='id'><xsl:call-template name="formatTopicAnchor" /></xsl:attribute><h3><xsl:value-of select="@name" /></h3></a></p>
		<xsl:if test="string-length(@include) > 0">
		<p>[inc:<xsl:value-of select="@include" />]</p>
		</xsl:if>
		<xsl:if test="sg:citations">
			<xsl:apply-templates select="sg:citations/sg:citation[@placement='TOP']" />
		</xsl:if>
		
		<xsl:apply-templates mode="body"/>
		
		<xsl:if test="sg:citations">
			<xsl:apply-templates select="sg:citations/sg:citation[@placement='BOTTOM']" />
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="sg:exemplar" mode="body">
		<xsl:if test="string-length(@include) > 0">
		<p>[inc:<xsl:value-of select="@include" />]</p>
		</xsl:if>
		<xsl:if test="sg:citations">
			<xsl:apply-templates select="sg:citations/sg:citation[@placement='TOP']" />
		</xsl:if>
		
		<p>[jc:<xsl:value-of select="@className" />:<xsl:value-of select="@marker" />]</p>
		<p><b><xsl:value-of select="@className" /></b></p>
		
		<xsl:if test="sg:citations">
			<xsl:apply-templates select="sg:citations/sg:citation[@placement='BOTTOM']" />
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="sg:citation">
		<xsl:choose>
			<xsl:when test="@type='JAVA'">
				<p>[jc:<xsl:value-of select="@ref" /><xsl:if test="string-length(@marker) > 0">:<xsl:value-of select="@marker" /></xsl:if>]</p>
			</xsl:when>
			<xsl:when test="@type='XML'">
				<p>[text:<xsl:value-of select="@ref" /><xsl:if test="string-length(@marker) > 0">:xml! <xsl:value-of select="@marker" /></xsl:if> ]</p>
			</xsl:when>
			<xsl:when test="@type='HTML'">
				<p>[inc:<xsl:value-of select="@ref" />]</p>
			</xsl:when>
			<xsl:when test="@type='TEXT'">
				<p>[text:<xsl:value-of select="@ref" /><xsl:if test="string-length(@marker) > 0">: <xsl:value-of select="@marker" /></xsl:if>]</p>
			</xsl:when>		
		</xsl:choose>
		<xsl:if test="@displayRef = 'true'">
			<p><b><xsl:value-of select="@ref" /></b></p>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="formatCategoryAnchor" >sg_cat_<xsl:value-of select="@id" /></xsl:template>
	
	<xsl:template name="formatTopicAnchor">sg_top_<xsl:value-of select="ancestor::sg:category/@id" />_<xsl:value-of select="@id" /></xsl:template>
</xsl:stylesheet>