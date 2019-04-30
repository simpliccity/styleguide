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

import java.io.Serializable;
import java.util.Comparator;

import org.simpliccity.styleguide.schema.Category;

/**
 * A {@link java.util.Comparator} implementation used to sort a collection
 * of {@link Category} instances by ID.
 * 
 * @author Kevin Fox
 *
 */
public class CategoryComparator implements Comparator<Category>, Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Compares two categories by their ID.
	 */
	@Override
	public int compare(Category cat1, Category cat2) 
	{
		return cat1.getId().compareTo(cat2.getId());
	}
}
