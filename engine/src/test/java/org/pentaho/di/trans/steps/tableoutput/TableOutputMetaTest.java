/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2019 by Hitachi Vantara : http://www.pentaho.com
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

package org.pentaho.di.trans.steps.tableoutput;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.core.database.DatabaseInterface;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.repository.StringObjectId;
import org.pentaho.di.trans.steps.loadsave.MemoryRepository;
import org.pentaho.di.utils.TestUtils;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

public class TableOutputMetaTest {

  private List<DatabaseMeta> databases;
  private IMetaStore metaStore;

  @SuppressWarnings( "unchecked" )
  @Before
  public void setUp() {
    databases = mock( List.class );
    metaStore = mock( IMetaStore.class );
  }

  /**
   * @see 
   *     <a href=http://jira.pentaho.com/browse/BACKLOG-377>http://jira.pentaho.com/browse/BACKLOG-377</a>
   * @throws KettleException
   */
  @Test
  public void testReadRep() throws KettleException {

    //check variable
    String commitSize = "${test}";

    Repository rep = new MemoryRepository();
    rep.saveStepAttribute( null, null, "commit", commitSize );

    TableOutputMeta tableOutputMeta = new TableOutputMeta();
    tableOutputMeta.readRep( rep, metaStore, null, databases );

    assertEquals( commitSize, tableOutputMeta.getCommitSize() );

    //check integer size
    int commitSizeInt = 1;
    Repository rep2 = new MemoryRepository();
    rep2.saveStepAttribute( null, null, "commit", commitSizeInt );

    TableOutputMeta tableOutputMeta2 = new TableOutputMeta();
    tableOutputMeta2.readRep( rep2, metaStore, null, databases );

    assertEquals( String.valueOf( commitSizeInt ), tableOutputMeta2.getCommitSize() );
  }

  @Test
  public void testIsReturningGeneratedKeys() throws Exception {
    TableOutputMeta tableOutputMeta = new TableOutputMeta(),
        tableOutputMetaSpy = spy( tableOutputMeta );

    DatabaseMeta databaseMeta = mock( DatabaseMeta.class );
    doReturn( true ).when( databaseMeta ).supportsAutoGeneratedKeys();
    doReturn( databaseMeta ).when( tableOutputMetaSpy ).getDatabaseMeta();

    tableOutputMetaSpy.setReturningGeneratedKeys( true );
    assertTrue( tableOutputMetaSpy.isReturningGeneratedKeys() );

    doReturn( false ).when( databaseMeta ).supportsAutoGeneratedKeys();
    assertFalse( tableOutputMetaSpy.isReturningGeneratedKeys() );

    tableOutputMetaSpy.setReturningGeneratedKeys( true );
    assertFalse( tableOutputMetaSpy.isReturningGeneratedKeys() );

    tableOutputMetaSpy.setReturningGeneratedKeys( false );
    assertFalse( tableOutputMetaSpy.isReturningGeneratedKeys() );
  }

  @Test
  public void testProvidesModeler() throws Exception {
    TableOutputMeta tableOutputMeta = new TableOutputMeta();
    tableOutputMeta.setFieldDatabase( new String[] {"f1", "f2", "f3"} );
    tableOutputMeta.setFieldStream( new String[] {"s4", "s5", "s6"} );

    TableOutputData tableOutputData = new TableOutputData();
    tableOutputData.insertRowMeta = mock( RowMeta.class );
    assertEquals( tableOutputData.insertRowMeta, tableOutputMeta.getRowMeta( tableOutputData ) );

    tableOutputMeta.setSpecifyFields( false );
    assertEquals( 0, tableOutputMeta.getDatabaseFields().size() );
    assertEquals( 0, tableOutputMeta.getStreamFields().size() );

    tableOutputMeta.setSpecifyFields( true );
    assertEquals( 3, tableOutputMeta.getDatabaseFields().size() );
    assertEquals( "f1", tableOutputMeta.getDatabaseFields().get( 0 ) );
    assertEquals( "f2", tableOutputMeta.getDatabaseFields().get( 1 ) );
    assertEquals( "f3", tableOutputMeta.getDatabaseFields().get( 2 ) );
    assertEquals( 3, tableOutputMeta.getStreamFields().size() );
    assertEquals( "s4", tableOutputMeta.getStreamFields().get( 0 ) );
    assertEquals( "s5", tableOutputMeta.getStreamFields().get( 1 ) );
    assertEquals( "s6", tableOutputMeta.getStreamFields().get( 2 ) );
  }

  @Test
  public void testLoadXml() throws Exception {

    TableOutputMeta tableOutputMeta = new TableOutputMeta();
    tableOutputMeta.loadXML( getTestNode(), databases, metaStore );
    assertEquals( "1000", tableOutputMeta.getCommitSize() );
    assertEquals( null, tableOutputMeta.getGeneratedKeyField() );
    assertEquals( "public", tableOutputMeta.getSchemaName() );
    assertEquals( "sales_csv", tableOutputMeta.getTableName() );
    assertEquals( null, tableOutputMeta.getPartitioningField() );
    assertTrue( tableOutputMeta.truncateTable() );
    assertTrue( tableOutputMeta.specifyFields() );
    assertFalse( tableOutputMeta.ignoreErrors() );
    assertFalse( tableOutputMeta.isPartitioningEnabled() );
    assertTrue( tableOutputMeta.useBatchUpdate() );
    assertFalse( tableOutputMeta.isTableNameInField() );
    assertTrue( tableOutputMeta.isTableNameInTable() );
    assertFalse( tableOutputMeta.isReturningGeneratedKeys() );
    String expectedXml = ""
      + "    <connection/>\n"
      + "    <schema>public</schema>\n"
      + "    <table>sales_csv</table>\n"
      + "    <commit>1000</commit>\n"
      + "    <truncate>Y</truncate>\n"
      + "    <ignore_errors>N</ignore_errors>\n"
      + "    <use_batch>Y</use_batch>\n"
      + "    <specify_fields>Y</specify_fields>\n"
      + "    <partitioning_enabled>N</partitioning_enabled>\n"
      + "    <partitioning_field/>\n"
      + "    <partitioning_daily>N</partitioning_daily>\n"
      + "    <partitioning_monthly>Y</partitioning_monthly>\n"
      + "    <tablename_in_field>N</tablename_in_field>\n"
      + "    <tablename_field/>\n"
      + "    <tablename_in_table>Y</tablename_in_table>\n"
      + "    <return_keys>N</return_keys>\n"
      + "    <return_field/>\n"
      + "    <fields>\n"
      + "        <field>\n"
      + "          <column_name>ORDERNUMBER</column_name>\n"
      + "          <stream_name>ORDERNUMBER</stream_name>\n"
      + "        </field>\n"
      + "        <field>\n"
      + "          <column_name>QUANTITYORDERED</column_name>\n"
      + "          <stream_name>QUANTITYORDERED</stream_name>\n"
      + "        </field>\n"
      + "        <field>\n"
      + "          <column_name>PRICEEACH</column_name>\n"
      + "          <stream_name>PRICEEACH</stream_name>\n"
      + "        </field>\n"
      + "    </fields>\n";
    String actualXml = TestUtils.toUnixLineSeparators( tableOutputMeta.getXML() );
    assertEquals( expectedXml, actualXml );
  }

  @Test
  public void testSaveRep() throws Exception {
    TableOutputMeta tableOutputMeta = new TableOutputMeta();
    tableOutputMeta.loadXML( getTestNode(), databases, metaStore );
    StringObjectId id_step = new StringObjectId( "stepid" );
    StringObjectId id_transformation = new StringObjectId( "transid" );
    Repository rep = mock( Repository.class );
    tableOutputMeta.saveRep( rep, metaStore, id_transformation, id_step );
    verify( rep ).saveDatabaseMetaStepAttribute( id_transformation, id_step, "id_connection", null );
    verify( rep ).saveStepAttribute( id_transformation, id_step, "schema", "public" );
    verify( rep ).saveStepAttribute( id_transformation, id_step, "table", "sales_csv" );
    verify( rep ).saveStepAttribute( id_transformation, id_step, "commit", "1000" );
    verify( rep ).saveStepAttribute( id_transformation, id_step, "truncate", true );
    verify( rep ).saveStepAttribute( id_transformation, id_step, "ignore_errors", false );
    verify( rep ).saveStepAttribute( id_transformation, id_step, "use_batch", true );
    verify( rep ).saveStepAttribute( id_transformation, id_step, "specify_fields", true );

    verify( rep ).saveStepAttribute( id_transformation, id_step, "partitioning_enabled", false );
    verify( rep ).saveStepAttribute( id_transformation, id_step, "partitioning_field", null );
    verify( rep ).saveStepAttribute( id_transformation, id_step, "partitioning_daily", false );
    verify( rep ).saveStepAttribute( id_transformation, id_step, "partitioning_monthly", true );

    verify( rep ).saveStepAttribute( id_transformation, id_step, "tablename_in_field", false );
    verify( rep ).saveStepAttribute( id_transformation, id_step, "tablename_field", null );
    verify( rep ).saveStepAttribute( id_transformation, id_step, "tablename_in_table", true );

    verify( rep ).saveStepAttribute( id_transformation, id_step, "return_keys", false );
    verify( rep ).saveStepAttribute( id_transformation, id_step, "return_field", null );

    verify( rep ).saveStepAttribute( id_transformation, id_step, 0, "column_name", "ORDERNUMBER" );
    verify( rep ).saveStepAttribute( id_transformation, id_step, 0, "stream_name", "ORDERNUMBER" );

    verify( rep ).saveStepAttribute( id_transformation, id_step, 1, "column_name", "QUANTITYORDERED" );
    verify( rep ).saveStepAttribute( id_transformation, id_step, 1, "stream_name", "QUANTITYORDERED" );

    verify( rep ).saveStepAttribute( id_transformation, id_step, 2, "column_name", "PRICEEACH" );
    verify( rep ).saveStepAttribute( id_transformation, id_step, 2, "stream_name", "PRICEEACH" );

    verifyNoMoreInteractions( rep );
  }

  @Test
  public void testSetupDefault() throws Exception {
    TableOutputMeta tableOutputMeta = new TableOutputMeta();
    tableOutputMeta.setDefault();
    assertEquals( "", tableOutputMeta.getTableName() );
    assertEquals( "1000", tableOutputMeta.getCommitSize() );
    assertFalse( tableOutputMeta.isPartitioningEnabled() );
    assertTrue( tableOutputMeta.isPartitioningMonthly() );
    assertEquals( "", tableOutputMeta.getPartitioningField() );
    assertTrue( tableOutputMeta.isTableNameInTable() );
    assertEquals( "", tableOutputMeta.getTableNameField() );
    assertFalse( tableOutputMeta.specifyFields() );
  }

  @Test
  public void testClone() throws Exception {
    TableOutputMeta tableOutputMeta = new TableOutputMeta();
    tableOutputMeta.setDefault();
    tableOutputMeta.setFieldStream( new String[] {"1", "2", "3"} );
    tableOutputMeta.setFieldDatabase( new String[] {"d1", "d2", "d3"} );
    TableOutputMeta clone = (TableOutputMeta) tableOutputMeta.clone();
    assertNotSame( clone, tableOutputMeta );
    assertEquals( clone.getXML(), tableOutputMeta.getXML() );
  }

  @Test
  public void testSupportsErrorHandling() throws Exception {
    TableOutputMeta tableOutputMeta = new TableOutputMeta();
    DatabaseMeta dbMeta = mock( DatabaseMeta.class );
    tableOutputMeta.setDatabaseMeta( dbMeta );
    DatabaseInterface databaseInterface = mock( DatabaseInterface.class );
    when( dbMeta.getDatabaseInterface() ).thenReturn( databaseInterface );
    when( databaseInterface.supportsErrorHandling() ).thenReturn( true, false );
    assertTrue( tableOutputMeta.supportsErrorHandling() );
    assertFalse( tableOutputMeta.supportsErrorHandling() );
    tableOutputMeta.setDatabaseMeta( null );
    assertTrue( tableOutputMeta.supportsErrorHandling() );
  }

  private Node getTestNode() throws KettleXMLException {
    String xml =
      "  <step>\n"
        + "    <name>Table output</name>\n"
        + "    <type>TableOutput</type>\n"
        + "    <description/>\n"
        + "    <distribute>Y</distribute>\n"
        + "    <custom_distribution/>\n"
        + "    <copies>1</copies>\n"
        + "         <partitioning>\n"
        + "           <method>none</method>\n"
        + "           <schema_name/>\n"
        + "           </partitioning>\n"
        + "    <connection>local postgres</connection>\n"
        + "    <schema>public</schema>\n"
        + "    <table>sales_csv</table>\n"
        + "    <commit>1000</commit>\n"
        + "    <truncate>Y</truncate>\n"
        + "    <ignore_errors>N</ignore_errors>\n"
        + "    <use_batch>Y</use_batch>\n"
        + "    <specify_fields>Y</specify_fields>\n"
        + "    <partitioning_enabled>N</partitioning_enabled>\n"
        + "    <partitioning_field/>\n"
        + "    <partitioning_daily>N</partitioning_daily>\n"
        + "    <partitioning_monthly>Y</partitioning_monthly>\n"
        + "    <tablename_in_field>N</tablename_in_field>\n"
        + "    <tablename_field/>\n"
        + "    <tablename_in_table>Y</tablename_in_table>\n"
        + "    <return_keys>N</return_keys>\n"
        + "    <return_field/>\n"
        + "    <fields>\n"
        + "        <field>\n"
        + "          <column_name>ORDERNUMBER</column_name>\n"
        + "          <stream_name>ORDERNUMBER</stream_name>\n"
        + "        </field>\n"
        + "        <field>\n"
        + "          <column_name>QUANTITYORDERED</column_name>\n"
        + "          <stream_name>QUANTITYORDERED</stream_name>\n"
        + "        </field>\n"
        + "        <field>\n"
        + "          <column_name>PRICEEACH</column_name>\n"
        + "          <stream_name>PRICEEACH</stream_name>\n"
        + "        </field>\n"
        + "    </fields>\n"
        + "     <cluster_schema/>\n"
        + " <remotesteps>   <input>   </input>   <output>   </output> </remotesteps>    <GUI>\n"
        + "      <xloc>368</xloc>\n"
        + "      <yloc>64</yloc>\n"
        + "      <draw>Y</draw>\n"
        + "      </GUI>\n"
        + "    </step>\n";
    return XMLHandler.loadXMLString( xml, "step" );
  }

  @Test
  public void metaSetTruncateTable() {
    TableOutputMeta tableOutputMeta = new TableOutputMeta();
    tableOutputMeta.metaSetTableNameDefinedInField( "Y" );
    assertTrue( tableOutputMeta.isTableNameInField() );
    tableOutputMeta.metaSetTableNameDefinedInField( "N" );
    assertFalse( tableOutputMeta.isTableNameInField() );
    tableOutputMeta.metaSetTableNameDefinedInField( "Ynot" );
    assertFalse( tableOutputMeta.isTableNameInField() );
  }

  @Test
  public void metaSetSpecifyDatabaseFields() {
    TableOutputMeta tableOutputMeta = new TableOutputMeta();
    tableOutputMeta.metaSetSpecifyDatabaseFields( "Y" );
    assertTrue( tableOutputMeta.specifyFields() );
    tableOutputMeta.metaSetSpecifyDatabaseFields( "N" );
    assertFalse( tableOutputMeta.specifyFields() );
    tableOutputMeta.metaSetSpecifyDatabaseFields( "Ynot" );
    assertFalse( tableOutputMeta.specifyFields() );
  }

  @Test
  public void metaSetIgnoreInsertErrors() {
    TableOutputMeta tableOutputMeta = new TableOutputMeta();
    tableOutputMeta.metaSetIgnoreInsertErrors( "Y" );
    assertTrue( tableOutputMeta.ignoreErrors() );
    tableOutputMeta.metaSetIgnoreInsertErrors( "N" );
    assertFalse( tableOutputMeta.ignoreErrors() );
    tableOutputMeta.metaSetIgnoreInsertErrors( "Ynot" );
    assertFalse( tableOutputMeta.ignoreErrors() );
  }

  @Test
  public void metaSetUseBatchUpdate() {
    TableOutputMeta tableOutputMeta = new TableOutputMeta();
    tableOutputMeta.metaSetUseBatchUpdate( "Y" );
    assertTrue( tableOutputMeta.useBatchUpdate() );
    tableOutputMeta.metaSetUseBatchUpdate( "N" );
    assertFalse( tableOutputMeta.useBatchUpdate() );
    tableOutputMeta.metaSetUseBatchUpdate( "Ynot" );
    assertFalse( tableOutputMeta.useBatchUpdate() );
  }

  @Test
  public void metaSetPartitionOverTables() {
    TableOutputMeta tableOutputMeta = new TableOutputMeta();
    tableOutputMeta.metaSetPartitionOverTables( "Y" );
    assertTrue( tableOutputMeta.isPartitioningEnabled() );
    tableOutputMeta.metaSetPartitionOverTables( "N" );
    assertFalse( tableOutputMeta.isPartitioningEnabled() );
    tableOutputMeta.metaSetPartitionOverTables( "Ynot" );
    assertFalse( tableOutputMeta.isPartitioningEnabled() );
  }

  @Test
  public void metaSetTableNameDefinedInField() {
    TableOutputMeta tableOutputMeta = new TableOutputMeta();
    tableOutputMeta.metaSetTableNameDefinedInField( "Y" );
    assertTrue( tableOutputMeta.isTableNameInField() );
    tableOutputMeta.metaSetTableNameDefinedInField( "N" );
    assertFalse( tableOutputMeta.isTableNameInField() );
    tableOutputMeta.metaSetTableNameDefinedInField( "Ynot" );
    assertFalse( tableOutputMeta.isTableNameInField() );
  }

  @Test
  public void metaSetReturningGeneratedKeys() {
    TableOutputMeta tableOutputMeta = new TableOutputMeta();
    DatabaseMeta databaseMeta = mock( DatabaseMeta.class );
    // isReturningGeneratedKeys uses database meta as part of its logic.
    doReturn( true ).when( databaseMeta ).supportsAutoGeneratedKeys();
    tableOutputMeta.setDatabaseMeta( databaseMeta );

    tableOutputMeta.metaSetReturningGeneratedKeys( "Y" );
    assertTrue( tableOutputMeta.isReturningGeneratedKeys() );
    tableOutputMeta.metaSetReturningGeneratedKeys( "N" );
    assertFalse( tableOutputMeta.isReturningGeneratedKeys() );
    tableOutputMeta.metaSetReturningGeneratedKeys( "Ynot" );
    assertFalse( tableOutputMeta.isReturningGeneratedKeys() );
  }

}
