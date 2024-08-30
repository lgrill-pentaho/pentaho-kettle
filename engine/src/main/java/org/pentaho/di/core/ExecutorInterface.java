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

package org.pentaho.di.core;

public interface ExecutorInterface {

  public String getExecutingServer();

  public void setExecutingServer( String executingServer );

  public String getExecutingUser();

  public void setExecutingUser( String executingUser );

}
