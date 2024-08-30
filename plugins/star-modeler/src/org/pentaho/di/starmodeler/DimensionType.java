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

package org.pentaho.di.starmodeler;

import org.pentaho.di.core.util.Utils;

public enum DimensionType {
  SLOWLY_CHANGING_DIMENSION,
  JUNK_DIMENSION,
  DATE,
  TIME,
  OTHER,
  ;


  public static DimensionType getDimensionType(String typeString) {
    if (Utils.isEmpty(typeString)) {
      return DimensionType.OTHER;
    }
    return DimensionType.valueOf(typeString);
  }

}
