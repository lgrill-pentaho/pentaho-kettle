/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2022 by Hitachi Vantara : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
package org.pentaho.di.trans.steps.avro.input;

import org.pentaho.di.core.RowMetaAndData;

import java.io.Closeable;
import java.util.Collections;
import java.util.List;

public interface IPentahoInputFormat {

  /**
   * Get split parts.
   */
  default List<IPentahoInputSplit> getSplits() {
    return Collections.emptyList();
  }

  /**
   * Read one split part.
   */
  IPentahoRecordReader createRecordReader( IPentahoInputSplit split ) throws Exception;

  public interface IPentahoInputSplit {
  }

  public interface IPentahoRecordReader extends Iterable<RowMetaAndData>, Closeable {
  }
}
