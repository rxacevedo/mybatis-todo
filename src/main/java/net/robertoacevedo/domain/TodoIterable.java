package net.robertoacevedo.domain;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created by roberto on June 07, 2014.
 */
public class TodoIterable implements Iterable<Todo> {

    Enumeration<Todo> todoEnumeration;

    public TodoIterable(Enumeration<Todo> todoEnumeration) {
        this.todoEnumeration = todoEnumeration;
    }

    private class TodoIterator implements Iterator<Todo> {

        @Override
        public boolean hasNext() {
            return todoEnumeration.hasMoreElements();
        }

        @Override
        public Todo next() {
            return todoEnumeration.nextElement();
        }

        @Override
        public void remove() {
            // No
        }

        @Override
        public void forEachRemaining(Consumer<? super Todo> action) {
            // Need to read about this
        }
    }

    @Override
    public Iterator<Todo> iterator() {
        return new TodoIterator();
    }
}
