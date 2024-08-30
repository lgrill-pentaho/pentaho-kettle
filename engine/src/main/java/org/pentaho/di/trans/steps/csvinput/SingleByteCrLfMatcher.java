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

package org.pentaho.di.trans.steps.csvinput;

public class SingleByteCrLfMatcher implements CrLfMatcherInterface {

  @Override
  public boolean isReturn( byte[] source, int location ) {
    return source[location] == '\n';
  }

  @Override
  public boolean isLineFeed( byte[] source, int location ) {
    return source[location] == '\r';
  }

}
