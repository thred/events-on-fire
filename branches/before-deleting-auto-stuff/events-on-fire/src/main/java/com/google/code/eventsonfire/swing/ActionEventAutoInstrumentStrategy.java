/*
 * Copyright (c) 2011 Manfred Hantschel
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

import java.awt.Button;
import java.awt.List;
import java.awt.MenuItem;
import java.awt.TextField;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultButtonModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.Timer;

import com.google.code.eventsonfire.ListenerAutoInstrumentStrategy;

public class ActionEventAutoInstrumentStrategy implements ListenerAutoInstrumentStrategy
{

	private static final Collection<Class<?>> PRODUCER_TYPES;

	static
	{
		Collection<Class<?>> types = new HashSet<Class<?>>();

		types.add(MenuItem.class);
		types.add(List.class);
		types.add(TextField.class);
		types.add(Button.class);
		types.add(TrayIcon.class);
		types.add(JFileChooser.class);
		types.add(JTextField.class);
		types.add(JComboBox.class);
		types.add(AbstractButton.class);
		types.add(DefaultButtonModel.class);
		types.add(ComboBoxEditor.class);
		types.add(ButtonModel.class);
		types.add(Timer.class);

		PRODUCER_TYPES = Collections.unmodifiableCollection(types);
	}

	public ActionEventAutoInstrumentStrategy()
	{
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isProducerSupported(Object producer)
	{
		for (Class<?> type : PRODUCER_TYPES)
		{
			if (type.isInstance(producer))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?> getEventType()
	{
		return ActionEvent.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public void instrument(Object producer)
	{
		Method method;

		try
		{
			method = producer.getClass().getMethod("addActionListener", ActionListener.class);
		}
		catch (SecurityException e)
		{
			throw new IllegalArgumentException("Method addActionListener(..) of " + producer.getClass()
			    + " cannot be accessed", e);
		}
		catch (NoSuchMethodException e)
		{
			throw new IllegalArgumentException("Method addActionListener(..) missing in " + producer.getClass(), e);
		}

		try
		{
			method.invoke(producer, new EventsActionListener(producer));
		}
		catch (IllegalArgumentException e)
		{
			throw new IllegalArgumentException("Invocation failed: " + method, e);
		}
		catch (IllegalAccessException e)
		{
			throw new IllegalArgumentException("Invocation failed: " + method, e);
		}
		catch (InvocationTargetException e)
		{
			throw new IllegalArgumentException("Invocation failed: " + method, e);
		}
	}

}
