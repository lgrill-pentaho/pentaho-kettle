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

package org.pentaho.di.engine.configuration.impl;

import org.pentaho.metastore.api.exceptions.MetaStoreException;
import org.pentaho.metastore.api.IMetaStore;



@FunctionalInterface
public interface CheckedMetaStoreSupplier {

  IMetaStore get() throws MetaStoreException;

}
