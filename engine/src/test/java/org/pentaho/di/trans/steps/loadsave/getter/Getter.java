/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/

package org.pentaho.di.trans.steps.loadsave.getter;

import java.lang.reflect.Type;

public interface Getter<T> {
  public T get( Object obj );

  public Class<T> getType();

  public Type getGenericType();
}
