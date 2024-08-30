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

package org.pentaho.di.trans.steps.salesforce;

import com.google.common.primitives.Ints;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

import java.util.Calendar;
import java.util.TimeZone;


public abstract class SalesforceStep extends BaseStep implements StepInterface {

  public static Class<?> PKG = SalesforceStep.class;

  public SalesforceStepMeta meta;
  public SalesforceStepData data;

  public SalesforceStep( StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta,
      Trans trans ) {
    super( stepMeta, stepDataInterface, copyNr, transMeta, trans );
  }

  @Override
  public boolean init( StepMetaInterface smi, StepDataInterface sdi ) {
    if ( !super.init( smi, sdi ) ) {
      return false;
    }
    meta = (SalesforceStepMeta) smi;
    data = (SalesforceStepData) sdi;

    String realUrl = environmentSubstitute( meta.getTargetURL() );
    String realUsername = environmentSubstitute( meta.getUsername() );
    String realPassword = environmentSubstitute( meta.getPassword() );
    String realModule = environmentSubstitute( meta.getModule() );

    if ( Utils.isEmpty( realUrl ) ) {
      log.logError( BaseMessages.getString( PKG, "SalesforceStep.TargetURLMissing.Error" ) );
      return false;
    }
    if ( Utils.isEmpty( realUsername ) ) {
      log.logError( BaseMessages.getString( PKG, "SalesforceInput.UsernameMissing.Error" ) );
      return false;
    }
    if ( Utils.isEmpty( realPassword ) ) {
      log.logError( BaseMessages.getString( PKG, "SalesforceInput.PasswordMissing.Error" ) );
      return false;
    }
    if ( Utils.isEmpty( realModule ) ) {
      log.logError( BaseMessages.getString( PKG, "SalesforceInputDialog.ModuleMissing.DialogMessage" ) );
      return false;
    }
    try {
      // The final step should call data.connection.connect(), as other settings may set additional options
      data.connection = new SalesforceConnection( log, realUrl, realUsername, realPassword );
      data.connection.setModule( realModule );
      data.connection.setTimeOut( Const.toInt( environmentSubstitute( meta.getTimeout() ), 0 ) );
      data.connection.setUsingCompression( meta.isCompression() );
    } catch ( KettleException ke ) {
      logError( BaseMessages.getString( PKG, "SalesforceInput.Log.ErrorOccurredDuringStepInitialize" )
        + ke.getMessage() );
      return false;
    }
    return true;
  }

  public void dispose( StepMetaInterface smi, StepDataInterface sdi ) {
    if ( data.connection != null ) {
      try {
        data.connection.close();
      } catch ( KettleException ignored ) {
        /* Ignore */
      }
      data.connection = null;
    }
    super.dispose( smi, sdi );
  }

  /**
   * normalize object for future sent in Salesforce
   *
   * @param valueMeta value meta
   * @param value pentaho internal value object
   * @return object for sending in Salesforce
   * @throws KettleValueException
   */
  public Object normalizeValue( ValueMetaInterface valueMeta, Object value ) throws KettleValueException {
    if ( valueMeta.isDate() ) {
      // Pass date field converted to UTC, see PDI-10836
      Calendar cal = Calendar.getInstance( valueMeta.getDateFormatTimeZone() );
      cal.setTime( valueMeta.getDate( value ) );
      Calendar utc = Calendar.getInstance( TimeZone.getTimeZone( "UTC" ) );
      // Reset time-related fields
      utc.clear();
      utc.set( cal.get( Calendar.YEAR ), cal.get( Calendar.MONTH ), cal.get( Calendar.DATE ),
        cal.get( Calendar.HOUR_OF_DAY ), cal.get( Calendar.MINUTE ), cal.get( Calendar.SECOND ) );
      value = utc;
    } else if ( valueMeta.isStorageBinaryString() ) {
      value = valueMeta.convertToNormalStorageType( value );
    }

    if ( ValueMetaInterface.TYPE_INTEGER == valueMeta.getType() ) {
      // Salesforce integer values can be only http://www.w3.org/2001/XMLSchema:int
      // see org.pentaho.di.ui.trans.steps.salesforceinput.SalesforceInputDialog#addFieldToTable
      // So we need convert Hitachi Vantara integer (real java Long value) to real int.
      // It will be sent correct as http://www.w3.org/2001/XMLSchema:int

      // use checked cast for prevent losing data
      value = Ints.checkedCast( (Long) value );
    }
    return value;
  }
}
