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

package org.pentaho.di.engine.ui;

import org.pentaho.di.core.annotations.LifecyclePlugin;
import org.pentaho.di.core.lifecycle.LifeEventHandler;
import org.pentaho.di.core.lifecycle.LifecycleException;
import org.pentaho.di.core.lifecycle.LifecycleListener;
import org.pentaho.di.ui.spoon.Spoon;

import java.util.function.Supplier;

/**
 * Created by bmorrise on 7/6/18.
 */
@LifecyclePlugin( id = "RunConfigurationLifecycleListener" )
public class RunConfigurationLifecycleListener implements LifecycleListener {

  private RunConfigurationDelegate runConfigurationDelegate;
  private Supplier<Spoon> spoonSupplier = Spoon::getInstance;

  public RunConfigurationLifecycleListener() {
    this.runConfigurationDelegate = RunConfigurationDelegate.getInstance();
  }

  @Override
  public void onStart( LifeEventHandler handler ) throws LifecycleException {
    Spoon spoon = spoonSupplier.get();
    if ( spoon != null ) {
      spoon.getTreeManager().addTreeProvider( Spoon.STRING_TRANSFORMATIONS, new RunConfigurationFolderProvider(
              runConfigurationDelegate ) );
      spoon.getTreeManager().addTreeProvider( Spoon.STRING_JOBS, new RunConfigurationFolderProvider(
              runConfigurationDelegate ) );
    }
  }

  @Override
  public void onExit( LifeEventHandler handler ) throws LifecycleException {

  }
}
