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

package org.pentaho.di.trans.step;

import java.util.List;

import org.pentaho.di.core.KettleAttributeInterface;

public interface StepAttributesInterface {
  public KettleAttributeInterface findParent( List<KettleAttributeInterface> attributes, String parentId );

  public KettleAttributeInterface findAttribute( String key );

  public String getXmlCode( String attributeKey );

  public String getRepCode( String attributeKey );

  public String getDescription( String attributeKey );

  public String getTooltip( String attributeKey );
}
