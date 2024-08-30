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

package org.pentaho.di.engine.configuration.impl.pentaho.scheduler;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( propOrder = { "name", "type", "stringValue" } )
public class JobScheduleParam implements Serializable {

  private static final long serialVersionUID = -4214459740606299083L;

  private String name;
  private String type;
  private List<String> stringValue = new ArrayList<>();


  public String getName() {
    return name;
  }

  public void setName( String name ) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType( String type ) {
    this.type = type;
  }

  public List<String> getStringValue() {
    return stringValue;
  }

  public void setStringValue( List<String> value ) {
    this.stringValue = value;
  }

}
