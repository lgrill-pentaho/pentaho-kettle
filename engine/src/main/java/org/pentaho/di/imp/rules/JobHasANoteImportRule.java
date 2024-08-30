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

package org.pentaho.di.imp.rules;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.imp.rule.ImportRuleInterface;
import org.pentaho.di.imp.rule.ImportValidationFeedback;
import org.pentaho.di.imp.rule.ImportValidationResultType;
import org.pentaho.di.job.JobMeta;
import org.w3c.dom.Node;

public class JobHasANoteImportRule extends BaseImportRule implements ImportRuleInterface {

  public JobHasANoteImportRule() {
    super();
  }

  @Override
  public List<ImportValidationFeedback> verifyRule( Object subject ) {

    List<ImportValidationFeedback> feedback = new ArrayList<ImportValidationFeedback>();

    if ( !isEnabled() ) {
      return feedback;
    }
    if ( !( subject instanceof JobMeta ) ) {
      return feedback;
    }

    JobMeta jobMeta = (JobMeta) subject;

    if ( jobMeta.nrNotes() == 0 ) {
      feedback.add( new ImportValidationFeedback(
        this, ImportValidationResultType.ERROR, "There is not even a single note in the job." ) );
    } else {
      feedback.add( new ImportValidationFeedback(
        this, ImportValidationResultType.APPROVAL, "At least one not is present in the job." ) );
    }

    return feedback;
  }

  @Override
  public String getXML() {

    StringBuilder xml = new StringBuilder();
    xml.append( XMLHandler.openTag( XML_TAG ) );

    xml.append( super.getXML() ); // id, enabled

    xml.append( XMLHandler.closeTag( XML_TAG ) );
    return xml.toString();
  }

  @Override
  public void loadXML( Node ruleNode ) throws KettleException {
    super.loadXML( ruleNode );
  }

}
