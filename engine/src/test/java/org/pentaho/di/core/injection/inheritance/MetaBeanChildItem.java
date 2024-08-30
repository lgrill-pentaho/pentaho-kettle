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
package org.pentaho.di.core.injection.inheritance;

import org.pentaho.di.core.injection.Injection;

public class MetaBeanChildItem extends MetaBeanParentItem {

  @Injection( name = "ITEM_CHILD_NAME" )
  public String nameChild;
}
