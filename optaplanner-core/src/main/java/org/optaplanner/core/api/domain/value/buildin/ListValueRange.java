/*
 * Copyright 2013 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.core.api.domain.value.buildin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.optaplanner.core.api.domain.value.AbstractValueRange;
import org.optaplanner.core.impl.heuristic.selector.common.iterator.CachedListRandomIterator;

public class ListValueRange<T> extends AbstractValueRange<T> {

    private final List<T> list;

    public ListValueRange(List<T> list) {
        this.list = list;
    }

    public ListValueRange(Collection<?> collection) {
        // TODO The user might not be aware of these performance pitfalls with Set and LinkedList:
        // - If only createOriginalIterator() is used, cloning a Set to a List is a waste of time.
        // - If the List is a LinkedList, createRandomIterator(Random) and get(int) are not efficient.
        this(collection instanceof List ? (List) collection : new ArrayList<Object>(collection));
    }

    @Override
    public boolean isCountable() {
        return true;
    }

    @Override
    public long getSize() {
        return list.size();
    }

    @Override
    public T get(long index) {
        if (index > Integer.MAX_VALUE) {
            throw new IndexOutOfBoundsException("The index (" + index + ") must fit in an int.");
        }
        return list.get((int) index);
    }

    @Override
    public Iterator<T> createOriginalIterator() {
        return list.iterator();
    }

    @Override
    public Iterator<T> createRandomIterator(Random workingRandom) {
        return new CachedListRandomIterator<T>(list, workingRandom);
    }

}
