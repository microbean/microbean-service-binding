/* -*- mode: Java; c-basic-offset: 2; indent-tabs-mode: nil; coding: utf-8-unix -*-
 *
 * Copyright © 2019 microBean.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.microbean.servicebinding;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.SocketOption;

import java.util.Objects;

public abstract class DelegatingSocketImpl extends SocketImpl {

  private final SocketImpl delegate;

  private static final Field addressField;
  
  private static final Field fdField;
  
  private static final Field localportField;

  private static final Field portField;

  private static final Field serverSocketField;

  private static final Field socketField;

  private static final Method acceptMethod;

  private static final Method availableMethod;

  private static final Method bindMethod;

  private static final Method closeMethod;

  private static final Method connectMethodStringInt;

  private static final Method connectMethodInetAddressInt;

  private static final Method connectMethodSocketAddressInt;

  private static final Method createMethod;

  private static final Method getFileDescriptorMethod;

  private static final Method getInetAddressMethod;

  private static final Method getInputStreamMethod;

  private static final Method getLocalPortMethod;

  private static final Method getOutputStreamMethod;

  private static final Method getPortMethod;

  private static final Method listenMethod;

  private static final Method sendUrgentDataMethod;

  private static final Method setPerformancePreferencesMethod;

  private static final Method shutdownInputMethod;

  private static final Method shutdownOutputMethod;

  private static final Method supportsUrgentDataMethod;
  
  static {
    Field tempField = null;
    Method tempMethod = null;

    try {
      tempField = SocketImpl.class.getDeclaredField("address");
      tempField.setAccessible(true);
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      addressField = tempField;
    }
    
    try {
      tempField = SocketImpl.class.getDeclaredField("fd");
      tempField.setAccessible(true);
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      fdField = tempField;
    }
    
    try {
      tempField = SocketImpl.class.getDeclaredField("localport");
      tempField.setAccessible(true);
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      localportField = tempField;
    }

    try {
      tempField = SocketImpl.class.getDeclaredField("port");
      tempField.setAccessible(true);
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      portField = tempField;
    }

    try {
      tempField = SocketImpl.class.getDeclaredField("serverSocket");
      tempField.setAccessible(true);
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      serverSocketField = tempField;
    }

    try {
      tempField = SocketImpl.class.getDeclaredField("socket");
      tempField.setAccessible(true);
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      socketField = tempField;
    }
    
    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("accept", SocketImpl.class);
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      acceptMethod = tempMethod;
    }

    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("available");
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      availableMethod = tempMethod;
    }

    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("bind", InetAddress.class, int.class);
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      bindMethod = tempMethod;
    }

    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("close");
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      closeMethod = tempMethod;
    }

    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("connect", String.class, int.class);
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      connectMethodStringInt = tempMethod;
    }

    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("connect", InetAddress.class, int.class);
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      connectMethodInetAddressInt = tempMethod;
    }

    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("connect", SocketAddress.class, int.class);
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      connectMethodSocketAddressInt = tempMethod;
    }

    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("create", boolean.class);
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      createMethod = tempMethod;
    }

    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("getFileDescriptor");
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      getFileDescriptorMethod = tempMethod;
    }

    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("getInetAddress");
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      getInetAddressMethod = tempMethod;
    }

    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("getInputStream");
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      getInputStreamMethod = tempMethod;
    }

    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("getLocalPort");
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      getLocalPortMethod = tempMethod;
    }

    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("getOutputStream");
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      getOutputStreamMethod = tempMethod;
    }

    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("getPort");
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      getPortMethod = tempMethod;
    }

    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("listen", int.class);
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      listenMethod = tempMethod;
    }

    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("sendUrgentData", int.class);
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      sendUrgentDataMethod = tempMethod;
    }

    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("setPerformancePreferences", int.class, int.class, int.class);
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      setPerformancePreferencesMethod = tempMethod;
    }

    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("shutdownInput");
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      shutdownInputMethod = tempMethod;
    }

    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("shutdownOutput");
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      shutdownOutputMethod = tempMethod;
    }

    try {
      tempMethod = SocketImpl.class.getDeclaredMethod("supportsUrgentData");
      tempMethod.setAccessible(true);      
    } catch (final ReflectiveOperationException kaboom) {
      throw new ExceptionInInitializerError(kaboom);
    } finally {
      supportsUrgentDataMethod = tempMethod;
    }
    
  }

  protected DelegatingSocketImpl() {
    this(null);
  }
  
  protected DelegatingSocketImpl(SocketImpl delegate) {
    super();
    if (delegate == null) {
      try {
        final Constructor<?> constructor = Class.forName("java.net.SocksSocketImpl").getDeclaredConstructor();
        constructor.setAccessible(true);
        delegate = (SocketImpl)constructor.newInstance();
      } catch (final Exception willNotHappen) {
        throw new InternalError(willNotHappen);
      }
    }
    assert delegate != null;
    this.delegate = delegate;
  }

  @Override
  protected void accept(final SocketImpl socketImpl) throws IOException {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      acceptMethod.invoke(this.delegate, socketImpl);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final ReflectiveOperationException kaboom) {
      throwable = kaboom;
      throw new IOException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  protected int available() throws IOException {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      return (int)availableMethod.invoke(this.delegate);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final ReflectiveOperationException kaboom) {
      throwable = kaboom;
      throw new IOException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  protected void bind(final InetAddress host, final int port) throws IOException {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      bindMethod.invoke(this.delegate, host, port);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final ReflectiveOperationException kaboom) {
      throwable = kaboom;
      throw new IOException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  protected void close() throws IOException {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      closeMethod.invoke(this.delegate);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final ReflectiveOperationException kaboom) {
      throwable = kaboom;
      throw new IOException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  protected void connect(final String host, final int port) throws IOException {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      connectMethodStringInt.invoke(this.delegate, host, port);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final ReflectiveOperationException kaboom) {
      throwable = kaboom;
      throw new IOException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }
  
  @Override
  protected void connect(final InetAddress host, final int port) throws IOException {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      connectMethodInetAddressInt.invoke(this.delegate, host, port);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final ReflectiveOperationException kaboom) {
      throwable = kaboom;
      throw new IOException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  protected void connect(final SocketAddress host, final int timeout) throws IOException {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      connectMethodSocketAddressInt.invoke(this.delegate, host, timeout);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final Exception kaboom) {
      throwable = kaboom;
      throw new IOException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  protected void create(final boolean stream) throws IOException {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      createMethod.invoke(this.delegate, stream);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final ReflectiveOperationException kaboom) {
      throwable = kaboom;
      throw new IOException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  protected FileDescriptor getFileDescriptor() {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      return (FileDescriptor)getFileDescriptorMethod.invoke(this.delegate);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final ReflectiveOperationException kaboom) {
      throwable = kaboom;
      throw new RuntimeException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  protected InetAddress getInetAddress() {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      return (InetAddress)getInetAddressMethod.invoke(this.delegate);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final ReflectiveOperationException kaboom) {
      throwable = kaboom;
      throw new RuntimeException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }
  
  @Override
  protected InputStream getInputStream() throws IOException {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      return (InputStream)getInputStreamMethod.invoke(this.delegate);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final ReflectiveOperationException kaboom) {
      throwable = kaboom;
      throw new IOException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  protected int getLocalPort() {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      return (int)getLocalPortMethod.invoke(this.delegate);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final ReflectiveOperationException kaboom) {
      throwable = kaboom;
      throw new RuntimeException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  public Object getOption(final int optID) throws SocketException {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      return this.delegate.getOption(optID);
    } catch (final RuntimeException | SocketException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  protected OutputStream getOutputStream() throws IOException {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      return (OutputStream)getOutputStreamMethod.invoke(this.delegate);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final Exception kaboom) {
      throwable = kaboom;
      throw new IOException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  protected int getPort() {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      return (int)getPortMethod.invoke(this.delegate);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final ReflectiveOperationException kaboom) {
      throwable = kaboom;
      throw new RuntimeException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  protected void listen(final int backlog) throws IOException {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      listenMethod.invoke(this.delegate, backlog);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final Exception kaboom) {
      throwable = kaboom;
      throw new IOException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  protected void sendUrgentData(final int data) throws IOException {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      sendUrgentDataMethod.invoke(this.delegate, data);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final Exception kaboom) {
      throwable = kaboom;
      throw new IOException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  public void setOption(final int optID, final Object value) throws SocketException {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      this.delegate.setOption(optID, value);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  protected void setPerformancePreferences(final int connectionTime, final int latency, final int bandwidth) {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      setPerformancePreferencesMethod.invoke(this.delegate, connectionTime, latency, bandwidth);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final ReflectiveOperationException kaboom) {
      throwable = kaboom;
      throw new RuntimeException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  protected void shutdownInput() throws IOException {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      shutdownInputMethod.invoke(this.delegate);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final ReflectiveOperationException kaboom) {
      throwable = kaboom;
      throw new IOException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  protected void shutdownOutput() throws IOException {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      shutdownOutputMethod.invoke(this.delegate);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final ReflectiveOperationException kaboom) {
      throwable = kaboom;
      throw new IOException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  protected boolean supportsUrgentData() {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      return (boolean)supportsUrgentDataMethod.invoke(this.delegate);
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } catch (final Exception kaboom) {
      throwable = kaboom;
      throw new RuntimeException(kaboom.getMessage(), kaboom);
    } finally {
      copyState(throwable, this.delegate, this);
    }
  }

  @Override
  public String toString() {
    Throwable throwable = null;
    try {
      copyState(this, this.delegate);
      return this.delegate.toString();
    } catch (final RuntimeException kaboom) {
      throwable = kaboom;
      throw kaboom;
    } finally {      
      copyState(throwable, this.delegate, this);
    }
  }

  protected static final void copyState(final SocketImpl source, final SocketImpl target) {
    copyState(null, source, target);
  }

  protected static final void copyState(final Throwable throwable, final SocketImpl source, final SocketImpl target) {
    try {
      addressField.set(target, addressField.get(source));
      fdField.set(target, fdField.get(source));
      localportField.set(target, localportField.get(source));
      portField.set(target, portField.get(source));
      serverSocketField.set(target, serverSocketField.get(source));
      socketField.set(target, socketField.get(source));
    } catch (final ReflectiveOperationException rfe) {
      if (throwable == null) {
        throw new RuntimeException(rfe.getMessage(), rfe);
      } else {
        throwable.addSuppressed(rfe);
      }
    }
  }
  
}
