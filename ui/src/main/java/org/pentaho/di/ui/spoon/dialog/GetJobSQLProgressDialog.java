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

package org.pentaho.di.ui.spoon.dialog;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.ProgressMonitorAdapter;
import org.pentaho.di.core.SQLStatement;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.ui.core.dialog.ErrorDialog;

/**
 * Takes care of displaying a dialog that will handle the wait while getting the SQL for a job...
 *
 * @author Matt
 * @since 29-mrt-2006
 */
public class GetJobSQLProgressDialog {
  private static Class<?> PKG = GetJobSQLProgressDialog.class; // for i18n purposes, needed by Translator2!!

  private Shell shell;
  private JobMeta jobMeta;
  private List<SQLStatement> stats;
  private Repository repository;

  /**
   * Creates a new dialog that will handle the wait while getting the SQL for a job...
   */
  public GetJobSQLProgressDialog( Shell shell, JobMeta jobMeta, Repository repository ) {
    this.shell = shell;
    this.jobMeta = jobMeta;
    this.repository = repository;

  }

  public List<SQLStatement> open() {
    IRunnableWithProgress op = new IRunnableWithProgress() {
      public void run( IProgressMonitor monitor ) throws InvocationTargetException, InterruptedException {
        // This is running in a new process: copy some KettleVariables info
        // LocalVariables.getInstance().createKettleVariables(Thread.currentThread(), kettleVariables.getLocalThread(),
        // true);
        // --> don't set variables if not running in different thread --> pmd.run(true,true, op);

        try {
          stats = jobMeta.getSQLStatements( repository, new ProgressMonitorAdapter( monitor ) );
        } catch ( KettleException e ) {
          throw new InvocationTargetException( e, BaseMessages.getString(
            PKG, "GetJobSQLProgressDialog.RuntimeError.UnableToGenerateSQL.Exception", e.getMessage() ) ); // Error
                                                                                                           // generating
                                                                                                           // SQL for
                                                                                                           // job:
                                                                                                           // \n{0}
        }
      }
    };

    try {
      ProgressMonitorDialog pmd = new ProgressMonitorDialog( shell );
      pmd.run( false, false, op );
    } catch ( InvocationTargetException e ) {
      new ErrorDialog( shell, BaseMessages.getString( PKG, "GetJobSQLProgressDialog.Dialog.UnableToGenerateSQL.Title" ),
        BaseMessages.getString( PKG, "GetJobSQLProgressDialog.Dialog.UnableToGenerateSQL.Message" ), e );
      stats = null;
    } catch ( InterruptedException e ) {
      new ErrorDialog( shell, BaseMessages.getString( PKG, "GetJobSQLProgressDialog.Dialog.UnableToGenerateSQL.Title" ),
        BaseMessages.getString( PKG, "GetJobSQLProgressDialog.Dialog.UnableToGenerateSQL.Message" ), e );
      stats = null;
    }

    return stats;
  }
}
