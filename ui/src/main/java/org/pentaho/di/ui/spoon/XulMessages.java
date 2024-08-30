/*! ******************************************************************************
 *
 * Pentaho Community Edition
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/

package org.pentaho.di.ui.spoon;

import org.pentaho.di.i18n.BaseMessages;

public class XulMessages implements org.pentaho.xul.Messages {
  private static Class<?> PKG = XulMessages.class; // for i18n purposes, needed by Translator2!!

  public String getString( String key, String... parameters ) {
    return BaseMessages.getString( PKG, key, parameters );
  }
}
