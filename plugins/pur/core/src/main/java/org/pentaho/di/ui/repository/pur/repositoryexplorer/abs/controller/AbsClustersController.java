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
package org.pentaho.di.ui.repository.pur.repositoryexplorer.abs.controller;

import java.util.List;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.ui.repository.pur.services.IAbsSecurityProvider;
import org.pentaho.di.ui.repository.repositoryexplorer.controllers.ClustersController;
import org.pentaho.di.ui.repository.repositoryexplorer.model.UICluster;

public class AbsClustersController extends ClustersController implements java.io.Serializable {

  private static final long serialVersionUID = -8073486897745053639L; /* EESOURCE: UPDATE SERIALVERUID */
  IAbsSecurityProvider service;
  boolean isAllowed = false;

  @Override
  protected boolean doLazyInit() {
    boolean superSucceeded = super.doLazyInit();
    if ( !superSucceeded ) {
      return false;
    }
    try {
      if ( repository.hasService( IAbsSecurityProvider.class ) ) {
        service = (IAbsSecurityProvider) repository.getService( IAbsSecurityProvider.class );
        setAllowed( allowedActionsContains( service, IAbsSecurityProvider.CREATE_CONTENT_ACTION ) );
      }
    } catch ( KettleException e ) {
      throw new RuntimeException( e );
    }
    return true;
  }

  public boolean isAllowed() {
    return isAllowed;
  }

  public void setAllowed( boolean isAllowed ) {
    this.isAllowed = isAllowed;
    this.firePropertyChange( "allowed", null, isAllowed );
  }

  public void setEnableButtons( List<UICluster> clusters ) {
    if ( isAllowed ) {
      super.setEnableButtons( clusters );
    } else {
      enableButtons( false, false, false );
    }
  }

  private boolean allowedActionsContains( IAbsSecurityProvider service, String action ) throws KettleException {
    List<String> allowedActions = service.getAllowedActions( IAbsSecurityProvider.NAMESPACE );
    for ( String actionName : allowedActions ) {
      if ( action != null && action.equals( actionName ) ) {
        return true;
      }
    }
    return false;
  }

}
