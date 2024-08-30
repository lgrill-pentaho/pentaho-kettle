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

package org.pentaho.di.core.injection;

public class MetaBeanLevel2Base {
  @Injection( name = "BASE" )
  private String baseField;

  private String first;

  public String first() {
    return first;
  }

  @Injection( name = "FIRST" )
  public void firstset( int a ) {
    first = "" + a;
  }
}
