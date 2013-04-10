package com.hoy.utilities;

/*                                                                           
 * Copyright 2001-2004 The Apache Software Foundation                        
 *                                                                           
 * Licensed under the Apache License, Version 2.0 (the "License");           
 * you may not use this file except in compliance with the License.          
 * You may obtain a copy of the License at                                   
 *                                                                           
 * http://www.apache.org/licenses/LICENSE-2.0                                
 *                                                                           
 * Unless required by applicable law or agreed to in writing, software      
 * distributed under the License is distributed on an "AS IS" BASIS,        
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and      
 * limitations under the License.                                           
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Provides utility methods and decorators for {@link java.util.Collection} instances.
 * 
 * @since Commons Collections 1.0
 * @version $Revision: 1.61 $ $Date: 2004/04/27 20:00:18 $
 * 
 * @author Rodney Waldhoff
 * @author Paul Jack
 * @author Stephen Colebourne
 * @author Steve Downey
 * @author Herve Quiroz
 * @author Peter KoBek
 * @author Matthew Hawthorne
 * @author Janek Bogucki
 * @author Phil Steitz
 * @author Steven Melzer
 * @author Jon Schewe
 */

public class MilongaCollectionUtils {

	/**
	 * Filter the collection by applying a Predicate to each element. If the
	 * predicate returns false, remove the element.
	 * <p>
	 * If the input collection or predicate is null, there is no change made.
	 * 
	 * @param collection
	 *            the collection to get the input from, may be null
	 * @param predicate
	 *            the predicate to use as a filter, may be null
	 */
	public static <T> void filter(Collection<T> collection, RestaunoPredicate<T> predicate) {
		if (collection != null && predicate != null) {
			for (Iterator<T> it = collection.iterator(); it.hasNext();) {
				if (predicate.evaluate(it.next()) == false) {
					it.remove();
				}
			}
		}
	}

	/**
	 * Answers true if a predicate is true for at least one element of a
	 * collection.
	 * <p>
	 * A <code>null</code> collection or predicate returns false.
	 * 
	 * @param collection
	 *            the collection to get the input from, may be null
	 * @param restaunoPredicate
	 *            the predicate to use, may be null
	 * @return true if at least one element of the collection matches the
	 *         predicate
	 */
	public static <T> boolean exists(Collection<T> collection, RestaunoPredicate<T> restaunoPredicate) {
		if (collection != null && restaunoPredicate != null) {
			for (Iterator<T> it = collection.iterator(); it.hasNext();) {
				if (restaunoPredicate.evaluate(it.next())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Finds the first element in the given collection which matches the given
	 * predicate.
	 * <p>
	 * If the input collection or predicate is null, or no element of the
	 * collection matches the predicate, null is returned.
	 * 
	 * @param collection
	 *            the collection to search, may be null
	 * @param predicate
	 *            the predicate to use, may be null
	 * @return the first element of the collection which matches the predicate
	 *         or null if none could be found
	 */
	public static <T> T find(Collection<T> collection, RestaunoPredicate<T> predicate) {
		if (collection != null && predicate != null) {
			for (Iterator<T> iter = collection.iterator(); iter.hasNext();) {
				T item = iter.next();
				if (predicate.evaluate(item)) {
					return item;
				}
			}
		}
		return null;
	}

	/**
	 * Selects all elements from input collection which match the given predicate into an output collection.
	 * <p>
	 * A <code>null</code> predicate matches no elements.
	 * 
	 * @param inputCollection
	 *            the collection to get the input from, may not be null
	 * @param predicate
	 *            the predicate to use, may be null
	 * @return the elements matching the predicate (new list)
	 * @throws NullPointerException
	 *             if the input collection is null
	 */
	public static <T> Collection<T> select(Collection<T> inputCollection, RestaunoPredicate<T> predicate) {                       
	    ArrayList<T> answer = new ArrayList<T>(inputCollection.size());                                            
	    select(inputCollection, predicate, answer);                                                                          
	    return answer;                                                                                                       
	}

	/**
	 * Selects all elements from input collection which match the given predicate and adds them to outputCollection.
	 * <p>
	 * If the input collection or predicate is null, there is no change to the output collection.
	 * 
	 * @param inputCollection
	 *            the collection to get the input from, may be null
	 * @param predicate
	 *            the predicate to use, may be null
	 * @param outputCollection
	 *            the collection to output into, may not be null
	 */
	public static <T> void select(Collection<T> inputCollection, RestaunoPredicate<T> predicate, Collection<T> outputCollection) {
		if (inputCollection != null && predicate != null) {
			for (T item : inputCollection) {
				if (predicate.evaluate(item)) {
					outputCollection.add(item);
				}
			}
		}
	}
}
