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

package org.pentaho.di.trans.steps.mergejoin;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.pentaho.di.core.injection.BaseMetadataInjectionTest;
import org.pentaho.di.junit.rules.RestorePDIEngineEnvironment;

public class MergeJoinMetaInjectionStep extends BaseMetadataInjectionTest<MergeJoinMeta> {
  @ClassRule public static RestorePDIEngineEnvironment env = new RestorePDIEngineEnvironment();
  @Before
  public void setup() {
    setup( new MergeJoinMeta() );
  }

  @Test
  public void test() throws Exception {
    check( "JOIN_TYPE", new StringGetter() {
      public String get() {
        return meta.getJoinType();
      }
    } );
    check( "KEY_FIELD1", new StringGetter() {
      public String get() {
        return meta.getKeyFields1()[0];
      }
    } );
    check( "KEY_FIELD2", new StringGetter() {
      public String get() {
        return meta.getKeyFields2()[0];
      }
    } );
  }
}
