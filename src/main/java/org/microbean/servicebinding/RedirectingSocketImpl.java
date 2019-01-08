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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.SocketOption;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import java.util.function.Function;

public class RedirectingSocketImpl extends DelegatingSocketImpl {

  private final Function<? super Entry<?, ? extends Integer>, Entry<?, ? extends Integer>> redirector;

  public RedirectingSocketImpl(final Function<? super Entry<?, ? extends Integer>, Entry<?, ? extends Integer>> redirector) {
    super();
    this.redirector = Objects.requireNonNull(redirector);
  }
  
  public RedirectingSocketImpl(final SocketImpl delegate, final Function<? super Entry<?, ? extends Integer>, Entry<?, ? extends Integer>> redirector) {
    super(Objects.requireNonNull(delegate));
    this.redirector = Objects.requireNonNull(redirector);
  }

  @Override
  protected void connect(final String host, final int port) throws IOException {
    final Entry<?, ? extends Integer> canonicalEntry = this.redirector.apply(new SimpleImmutableEntry<>(host, port));
    if (canonicalEntry == null) {
      super.connect(host, port);
    } else {
      final Object key = canonicalEntry.getKey();
      if (key instanceof String) {
        super.connect((String)key, canonicalEntry.getValue());
      } else if (key instanceof InetAddress) {
        this.connect((InetAddress)key, canonicalEntry.getValue());
      } else if (key instanceof SocketAddress) {
        this.connect((SocketAddress)key, canonicalEntry.getValue());
      } else {
        throw new IllegalStateException("key: " + key);
      }
    }
  }
  
  @Override
  protected void connect(final InetAddress host, final int port) throws IOException {
    final Entry<?, ? extends Integer> canonicalEntry = this.redirector.apply(new SimpleImmutableEntry<>(host, port));
    if (canonicalEntry == null) {
      super.connect(host, port);
    } else {
      final Object key = canonicalEntry.getKey();
      if (key instanceof String) {
        this.connect((String)key, canonicalEntry.getValue());
      } else if (key instanceof InetAddress) {
        super.connect((InetAddress)key, canonicalEntry.getValue());
      } else if (key instanceof SocketAddress) {
        this.connect((SocketAddress)key, canonicalEntry.getValue());
      } else {
        throw new IllegalStateException("key: " + key);
      }
    }
  }

  @Override
  protected void connect(final SocketAddress host, final int timeout) throws IOException {
    final Entry<?, ? extends Integer> canonicalEntry = this.redirector.apply(new SimpleImmutableEntry<>(host, timeout));
    if (canonicalEntry == null) {
      super.connect(host, timeout);
    } else {
      final Object key = canonicalEntry.getKey();
      if (key instanceof String) {
        this.connect((String)key, canonicalEntry.getValue());
      } else if (key instanceof InetAddress) {
        this.connect((InetAddress)key, canonicalEntry.getValue());
      } else if (key instanceof SocketAddress) {
        super.connect((SocketAddress)key, canonicalEntry.getValue());
      } else {
        throw new IllegalStateException("key: " + key);
      }
    }
  }

}
