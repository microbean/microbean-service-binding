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

import java.io.IOException;

import java.lang.reflect.InvocationTargetException;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketImpl;
import java.net.UnknownHostException;
import java.net.URL;
import java.net.URLConnection;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import java.util.Objects;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestCaseRedirectingSocketImpl {

  public TestCaseRedirectingSocketImpl() {
    super();
  }

  @BeforeClass
  public static void setFactory() throws IOException {
    Socket.setSocketImplFactory(() -> new RedirectingSocketImpl(TestCaseRedirectingSocketImpl::redirector));
  }
  
  @Test
  public void testAll() throws IOException {
    try {
      new Socket("www.google.com", 7);
    } catch (final IOException e) {
      Object cause = e.getCause();
      assertTrue(cause instanceof InvocationTargetException);
      cause = ((InvocationTargetException)cause).getCause();
      assertTrue(cause instanceof UnknownHostException);
      final String message = ((Throwable)cause).getMessage();
      assertNotNull(message);
      assertTrue(message.startsWith("invalid."));
    }    
  }

  @Test
  public void testHttp() throws IOException {
    try {
      new URL("https://www.google.com/").openConnection().connect();
    } catch (final IOException e) {
      Object cause = e.getCause();
      assertTrue(cause instanceof InvocationTargetException);
      cause = ((InvocationTargetException)cause).getCause();
      assertTrue(cause instanceof UnknownHostException);
      final String message = ((Throwable)cause).getMessage();
      assertNotNull(message);
      assertTrue(message.startsWith("invalid."));
    }
  }

  private static final Entry<?, ? extends Integer> redirector(final Entry<?, ? extends Integer> entry) {
    Objects.requireNonNull(entry);
    final int port;
    final Object key = entry.getKey();
    if (key instanceof InetSocketAddress) {
      port = ((InetSocketAddress)key).getPort();
    } else {
      port = entry.getValue();
    }
    // "invalid" is a top-level domain (a child of the root domain,
    // "", hence the trailing ".") that is guaranteed by RFC never to
    // exist.
    return new SimpleImmutableEntry<>("invalid.", port);
  }
  
}
