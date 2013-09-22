/*
 * Copyright (c) 2011-2013 events-on-fire Team
 * 
 * This file is part of Events-On-Fire (http://code.google.com/p/events-on-fire), licensed under the terms of the MIT
 * License (MIT).
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.google.code.eventsonfire.swing;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.google.code.eventsonfire.Events;
import com.google.code.eventsonfire.awt.AWTEvents;

/**
 * Utilities for Swing
 * 
 * @author Manfred HANTSCHEL
 */
public class SwingEvents extends AWTEvents
{

    /**
     * A custom implementation of the change listener interface using either the specified producer or the source of the
     * event.
     * 
     * @author ham
     */
    public static class EventsChangeListener extends AWTEvents.AbstractListener implements ChangeListener
    {

        /**
         * Creates a change listener using the specified producer and tags. If the producer is null, the source of the
         * event will be used as producer.
         * 
         * @param producer the producer, may be null
         * @param tags the tags, may be null
         */
        public EventsChangeListener(Object producer, String... tags)
        {
            super(producer, tags);
        }

        /**
         * {@inheritDoc}
         */
        public void stateChanged(ChangeEvent event)
        {
            fire(event.getSource(), event);
        }
    }

    /**
     * A custom implementation of the list selection listener interface using either the specified producer or the
     * source of the event.
     * 
     * @author ham
     */
    public static class EventsListSelectionListener extends AWTEvents.AbstractListener implements ListSelectionListener
    {

        /**
         * Creates a list selection listener using the specified producer and tags. If the producer is null, the source
         * of the event will be used as producer.
         * 
         * @param producer the producer, may be null
         * @param tags the tags, may be null
         */
        public EventsListSelectionListener(Object producer, String... tags)
        {
            super(producer, tags);
        }

        /**
         * {@inheritDoc}
         */
        public void valueChanged(ListSelectionEvent event)
        {
            if (event.getValueIsAdjusting())
            {
                return;
            }

            fire(event.getSource(), event);
        }
    }

    /**
     * A custom implementation of the tree selection listener interface using either the specified producer or the
     * source of the event.
     * 
     * @author ham
     */
    public static class EventsTreeSelectionListener extends AWTEvents.AbstractListener implements TreeSelectionListener
    {

        /**
         * Creates a tree selection listener using the specified producer and tags. If the producer is null, the source
         * of the event will be used as producer.
         * 
         * @param producer the producer, may be null
         * @param tags the tags, may be null
         */
        public EventsTreeSelectionListener(Object producer, String... tags)
        {
            super(producer, tags);
        }

        /**
         * {@inheritDoc}
         */
        public void valueChanged(TreeSelectionEvent event)
        {
            fire(event.getSource(), event);
        }
    }

    private static final ChangeListener DEFAULT_CHANGE_LISTENER = new EventsChangeListener(null);
    private static final ListSelectionListener DEFAULT_LIST_SELECTION_LISTENER = new EventsListSelectionListener(null);
    private static final TreeSelectionListener DEFAULT_TREE_SELECTION_LISTENER = new EventsTreeSelectionListener(null);

    private static long uniqueKey = 0;

    static
    {
        Events.registerStrategy(new SwingEventHandlerAnnotationStrategy());
    }

    /**
     * Registers a keystroke and an action to the component. Fires an action event, if the component that receives the
     * keystroke is member of a focused window.
     * 
     * @param producer the component
     * @param keyStroke the keystroke
     * @param tags some tags
     * @return the component
     */
    public static <TYPE extends JComponent> TYPE fireOnWindowsKeyStroke(final TYPE producer, String keyStroke,
        final String... tags)
    {
        return fireOnWindowsKeyStroke(producer, KeyStroke.getKeyStroke(keyStroke), tags);
    }

    /**
     * Registers a keystroke and an action to the component. Fires an action event, if the component that receives the
     * keystroke is member of a focused window.
     * 
     * @param producer the component
     * @param keyStroke the keystroke
     * @param tags some tags
     * @return the component
     */
    public static <TYPE extends JComponent> TYPE fireOnWindowsKeyStroke(final TYPE producer, KeyStroke keyStroke,
        final String... tags)
    {
        return fireOnKeyStroke(producer, JComponent.WHEN_IN_FOCUSED_WINDOW, keyStroke, tags);
    }

    /**
     * Registers a keystroke and an action to the component. Fires an action event, if an ancestor of the specified
     * component receives the keystroke.
     * 
     * @param producer the component
     * @param keyStroke the keystroke
     * @param tags some tags
     * @return the component
     */
    public static <TYPE extends JComponent> TYPE fireOnAncestorsKeyStroke(final TYPE producer, String keyStroke,
        final String... tags)
    {
        return fireOnAncestorsKeyStroke(producer, KeyStroke.getKeyStroke(keyStroke), tags);
    }

    /**
     * Registers a keystroke and an action to the component. Fires an action event, if an ancestor of the specified
     * component receives the keystroke.
     * 
     * @param producer the component
     * @param keyStroke the keystroke
     * @param tags some tags
     * @return the component
     */
    public static <TYPE extends JComponent> TYPE fireOnAncestorsKeyStroke(final TYPE producer, KeyStroke keyStroke,
        final String... tags)
    {
        return fireOnKeyStroke(producer, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, keyStroke, tags);
    }

    /**
     * Registers a keystroke and an action to the component. Fires an action event, if the component is focused and
     * receives the keystroke.
     * 
     * @param producer the component
     * @param keyStroke the keystroke
     * @param tags some tags
     * @return the component
     */
    public static <TYPE extends JComponent> TYPE fireOnKeyStroke(final TYPE producer, String keyStroke,
        final String... tags)
    {
        return fireOnKeyStroke(producer, KeyStroke.getKeyStroke(keyStroke), tags);
    }

    /**
     * Registers a keystroke and an action to the component. Fires an action event, if the component is focused and
     * receives the keystroke.
     * 
     * @param producer the component
     * @param keyStroke the keystroke
     * @param tags some tags
     * @return the component
     */
    public static <TYPE extends JComponent> TYPE fireOnKeyStroke(final TYPE producer, KeyStroke keyStroke,
        final String... tags)
    {
        return fireOnKeyStroke(producer, JComponent.WHEN_FOCUSED, keyStroke, tags);
    }

    private static <TYPE extends JComponent> TYPE fireOnKeyStroke(final TYPE producer, int condition,
        KeyStroke keyStroke, final String... tags)
    {
        String actionMapKey = "Events Key #" + uniqueKey++;

        producer.getInputMap(condition).put(keyStroke, actionMapKey);
        producer.getActionMap().put(actionMapKey, new AbstractAction()
        {
            private static final long serialVersionUID = 5541145536773299681L;

            public void actionPerformed(ActionEvent e)
            {
                Events.fire(producer, e,
                    ((tags != null) && (tags.length > 0)) ? tags : new String[]{e.getActionCommand()});
            }
        });

        return producer;
    }

    /**
     * Returns a change listener, that fires an event using the source of the event as producer.
     * 
     * @return the change listener
     */
    public static ChangeListener fireOnChange()
    {
        return DEFAULT_CHANGE_LISTENER;
    }

    /**
     * Creates a change listener, that fires an event using the source of the event as producer and the specified tags.
     * If the producer is null, the source of the event will be used as producer.
     * 
     * @param tags the tags
     * @return the change listener
     */
    public static ChangeListener fireOnChange(String... tags)
    {
        return new EventsChangeListener(null, tags);
    }

    /**
     * Creates a change listener, that fires an event using the specified producer and the specified tags. If the
     * producer is null, the source of the event will be used as producer.
     * 
     * @param producer the producer
     * @param tags the tags
     * @return the change listener
     */
    public static ChangeListener fireOnChange(Object producer, String... tags)
    {
        return new EventsChangeListener(producer, tags);
    }

    /**
     * Returns a list selection listener, that fires an event using the source of the event as producer.
     * 
     * @return the list selection listener
     */
    public static ListSelectionListener fireOnListSelection()
    {
        return DEFAULT_LIST_SELECTION_LISTENER;
    }

    /**
     * Creates a list selection listener, that fires an event using the specified producer and the specified tags. If
     * the producer is null, the source of the event will be used as producer.
     * 
     * @param producer the producer
     * @param tags the tags
     * @return the list selection listener
     */
    public static ListSelectionListener fireOnListSelection(Object producer, String... tags)
    {
        return new EventsListSelectionListener(producer, tags);
    }

    /**
     * Returns a tree selection listener, that fires an event using the source of the event as producer.
     * 
     * @return the tree selection listener
     */
    public static TreeSelectionListener fireOnTreeSelection()
    {
        return DEFAULT_TREE_SELECTION_LISTENER;
    }

    /**
     * Creates a tree selection listener, that fires an event using the specified producer and the specified tags. If
     * the producer is null, the source of the event will be used as producer.
     * 
     * @param producer the producer
     * @param tags the tags
     * @return the tree selection listener
     */
    public static TreeSelectionListener fireOnTreeSelection(Object producer, String... tags)
    {
        return new EventsTreeSelectionListener(producer, tags);
    }

}
