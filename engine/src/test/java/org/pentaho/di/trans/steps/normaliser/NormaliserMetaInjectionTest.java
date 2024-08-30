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

package org.pentaho.di.trans.steps.normaliser;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.pentaho.di.core.injection.BaseMetadataInjectionTest;
import org.pentaho.di.junit.rules.RestorePDIEngineEnvironment;

public class NormaliserMetaInjectionTest extends BaseMetadataInjectionTest<NormaliserMeta> {
  @ClassRule public static RestorePDIEngineEnvironment env = new RestorePDIEngineEnvironment();

  @Before
  public void setup() {
    setup( new NormaliserMeta() );
  }

  @Test
  public void test() throws Exception {
    check( "NAME", new StringGetter() {
      public String get() {
        return meta.getNormaliserFields()[0].getName();
      }
    } );
    check( "VALUE", new StringGetter() {
      public String get() {
        return meta.getNormaliserFields()[0].getValue();
      }
    } );
    check( "NORMALISED", new StringGetter() {
      public String get() {
        return meta.getNormaliserFields()[0].getNorm();
      }
    } );
  }

}
