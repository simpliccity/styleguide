/*
 *    Copyright 2014 Information Control Corporation
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

package org.simpliccity.styleguide.schema;

import javax.xml.bind.annotation.XmlEnum;

/**
 * The available options for where a citation, as represented by a {@link Citation}, 
 * can be displayed within the presentation of a {@link CitationContainer}.
 * 
 * @author Kevin Fox
 * @since 1.1.0
 *
 */
@XmlEnum(String.class)
public enum CitationPlacement 
{
	TOP, BOTTOM;
}