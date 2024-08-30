/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2020 by Hitachi Vantara : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.core.compress.zip;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import org.pentaho.di.core.compress.CompressionInputStream;
import org.pentaho.di.core.compress.CompressionProvider;

public class ZIPCompressionInputStream extends CompressionInputStream {
  private static final String INVALID_INPUT_MSG = "Not a valid input stream!";

  public ZIPCompressionInputStream( InputStream in, CompressionProvider provider ) {
    super( getDelegate( in ), provider );
  }

  protected static ZipInputStream getDelegate( InputStream in ) {
    ZipInputStream delegate;
    if ( in instanceof ZipInputStream ) {
      delegate = (ZipInputStream) in;
    } else {
      delegate = new ZipInputStream( in );
    }
    return delegate;
  }

  @Override
  public void close() throws IOException {
    ZipInputStream zis = (ZipInputStream) delegate;
    if ( zis == null ) {
      throw new IOException( INVALID_INPUT_MSG );
    }
    zis.close();
  }

  @Override
  public int read() throws IOException {
    ZipInputStream zis = (ZipInputStream) delegate;
    if ( zis == null ) {
      throw new IOException( INVALID_INPUT_MSG );
    }
    return zis.read();
  }

  @Override
  public Object nextEntry() throws IOException {
    ZipInputStream zis = (ZipInputStream) delegate;
    if ( zis == null ) {
      throw new IOException( INVALID_INPUT_MSG );
    }
    return zis.getNextEntry();
  }

}
