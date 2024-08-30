/*! ******************************************************************************
 *
 * Pentaho Community Edition
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/

package org.pentaho.di.ui.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.pentaho.di.ui.util.DialogUtils.getPathOf;
import static org.pentaho.di.ui.util.DialogUtils.objectWithTheSameNameExists;

import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.RepositoryElementMetaInterface;
import org.pentaho.di.shared.SharedObjectInterface;

/**
 * @author Andrey Khayrutdinov
 */
public class DialogUtilsTest {

  @Test
  public void nullObject() {
    assertNull( getPathOf( null ) );
  }

  @Test
  public void deletedObject() {
    RepositoryElementMetaInterface object = mock( RepositoryElementMetaInterface.class );
    when( object.isDeleted() ).thenReturn( true );
    assertNull( getPathOf( object ) );
  }

  @Test
  public void nullDirectory() {
    RepositoryElementMetaInterface object = mock( RepositoryElementMetaInterface.class );
    when( object.getRepositoryDirectory() ).thenReturn( null );
    assertNull( getPathOf( object ) );
  }

  @Test
  public void nullDirectoryPath() {
    RepositoryElementMetaInterface object = mock( RepositoryElementMetaInterface.class );

    RepositoryDirectoryInterface directory = mock( RepositoryDirectoryInterface.class );
    when( object.getRepositoryDirectory() ).thenReturn( directory );

    assertNull( getPathOf( object ) );
  }

  @Test
  public void pathWithSlash() {
    testPathAndName( "/path/", "name", "/path/name" );
  }

  @Test
  public void pathWithOutSlash() {
    testPathAndName( "/path", "name", "/path/name" );
  }

  private void testPathAndName( String path, String name, String expected ) {
    RepositoryElementMetaInterface object = mock( RepositoryElementMetaInterface.class );

    RepositoryDirectoryInterface directory = mock( RepositoryDirectoryInterface.class );
    when( directory.getPath() ).thenReturn( path );
    when( object.getRepositoryDirectory() ).thenReturn( directory );

    when( object.getName() ).thenReturn( name );

    assertEquals( expected, getPathOf( object ) );
  }

  @Test
  public void objectWithTheSameNameExists_true_if_exists() {
    SharedObjectInterface sharedObject = mock( SharedObjectInterface.class );
    when( sharedObject.getName() ).thenReturn( "TEST_OBJECT" );

    assertTrue( objectWithTheSameNameExists( sharedObject, createTestScope( "TEST_OBJECT" ) ) );
  }

  @Test
  public void objectWithTheSameNameExists_false_if_not_exist() {
    SharedObjectInterface sharedObject = mock( SharedObjectInterface.class );
    when( sharedObject.getName() ).thenReturn( "NEW_TEST_OBJECT" );

    assertFalse( objectWithTheSameNameExists( sharedObject, createTestScope( "TEST_OBJECT" ) ) );
  }

  @Test
  public void objectWithTheSameNameExists_false_if_same_object() {
    SharedObjectInterface sharedObject = mock( SharedObjectInterface.class );
    when( sharedObject.getName() ).thenReturn( "TEST_OBJECT" );

    assertFalse( objectWithTheSameNameExists( sharedObject, Collections.singleton( sharedObject ) ) );
  }

  private static Collection<SharedObjectInterface> createTestScope( String objectName ) {
    SharedObjectInterface sharedObject = mock( SharedObjectInterface.class );
    when( sharedObject.getName() ).thenReturn( objectName );
    return Collections.singleton( sharedObject );
  }

  @Test
  public void testGetPath() {
    String path = "/this/is/the/path/to/file";
    String parentPath = "/this/is/the";

    String newPath = DialogUtils.getPath( parentPath, path );
    assertEquals( "${Internal.Entry.Current.Directory}/path/to/file", newPath );
  }

}
