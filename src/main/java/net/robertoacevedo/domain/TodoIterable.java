package net.robertoacevedo.domain;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created by roberto on June 07, 2014.
 */
public class TodoIterable implements Iterable<Todo> {

    private final Enumeration<Todo> enumeration;

    public TodoIterable(final Enumeration<Todo> enumeration) {
        this.enumeration = enumeration;
    }

    private class TodoIterator implements Iterator<Todo> {

        @Override
        public boolean hasNext() {
            return enumeration.hasMoreElements();
        }

        @Override
        public Todo next() {
            return enumeration.nextElement();
        }

        @Override
        public void remove() {
            // No
        }

        @Override
        public void forEachRemaining(Consumer<? super Todo> action) {
            while (enumeration.hasMoreElements())
                action.accept(enumeration.nextElement());
        }
    }

    @Override
    public Iterator<Todo> iterator() {
        return new TodoIterator();
    }
}
