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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.net.InetAddress;
import java.net.InetSocketAddress; // for javadoc only
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

  private final Function<? super Entry<?, ? extends Integer>, ? extends Entry<?, ? extends Integer>> redirector;

  /**
   * Creates a new {@link RedirectingSocketImpl}.
   *
   * @param redirector a {@link Function} that accepts an {@link
   * Entry} whose {@linkplain Entry#getKey() key} is either a {@link
   * String} hostname, an {@link InetAddress} or an {@link
   * InetSocketAddress}, and whose {@linkplain Entry#getValue() value}
   * is a non-{@code null} {@link Integer} representing a port in the
   * first two cases and a timeout in seconds in the third case; may
   * be {@code null}
   *
   * @see #RedirectingSocketImpl(SocketImpl, Function)
   */
  public RedirectingSocketImpl(final Function<? super Entry<?, ? extends Integer>, ? extends Entry<?, ? extends Integer>> redirector) {
    this(null, redirector);
  }

  /**
   * Creates a new {@link RedirectingSocketImpl}.
   *
   * @param delegate the {@link SocketImpl} to delegate operations to;
   * may be {@code null} in which case an instance of {@code
   * java.net.SocksSocketImpl} will be used instead
   *
   * @param redirector a {@link Function} that accepts an {@link
   * Entry} whose {@linkplain Entry#getKey() key} is either a {@link
   * String} hostname, an {@link InetAddress} or an {@link
   * InetSocketAddress}, and whose {@linkplain Entry#getValue() value}
   * is a non-{@code null} {@link Integer} representing a port in the
   * first two cases and a timeout in seconds in the third case; may
   * be {@code null}
   */
  public RedirectingSocketImpl(final SocketImpl delegate, final Function<? super Entry<?, ? extends Integer>, ? extends Entry<?, ? extends Integer>> redirector) {
    super(delegate);
    this.redirector = redirector;
  }

  @Override
  protected void connect(final String host, final int port) throws IOException {
    if (this.redirector == null) {
      super.connect(host, port);
    } else {
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
  }
  
  @Override
  protected void connect(final InetAddress host, final int port) throws IOException {
    if (this.redirector == null) {
      super.connect(host, port);
    } else {
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
  }

  @Override
  protected void connect(final SocketAddress host, final int timeout) throws IOException {
    if (this.redirector == null) {
      super.connect(host, timeout);
    } else {
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

}
