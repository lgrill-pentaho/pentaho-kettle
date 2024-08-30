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

package org.pentaho.di.www;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.owasp.encoder.Encode;
import org.pentaho.di.core.gui.Point;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LogChannelInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static junit.framework.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class StartTransServletTest {
  private TransformationMap mockTransformationMap;

  private StartTransServlet startTransServlet;

  @Before
  public void setup() {
    mockTransformationMap = mock( TransformationMap.class );
    startTransServlet = new StartTransServlet( mockTransformationMap );
  }

  @Test
  public void testStartTransServletEscapesHtmlWhenTransNotFound() throws ServletException, IOException {
    try ( MockedStatic<Encode> encodeMockedStatic = mockStatic( Encode.class ) ) {
      HttpServletRequest mockHttpServletRequest = mock( HttpServletRequest.class );
      HttpServletResponse mockHttpServletResponse = mock( HttpServletResponse.class );

      StringWriter out = new StringWriter();
      PrintWriter printWriter = new PrintWriter( out );

      spy( Encode.class );
      when( mockHttpServletRequest.getContextPath() ).thenReturn( StartTransServlet.CONTEXT_PATH );
      when( mockHttpServletRequest.getParameter( anyString() ) ).thenReturn( ServletTestUtils.BAD_STRING_TO_TEST );
      when( mockHttpServletResponse.getWriter() ).thenReturn( printWriter );

      startTransServlet.doGet( mockHttpServletRequest, mockHttpServletResponse );
      assertFalse( ServletTestUtils.hasBadText( ServletTestUtils.getInsideOfTag( "H1", out.toString() ) ) );
      encodeMockedStatic.verify( () -> Encode.forHtml( anyString() ) );
    }
  }

  @Test
  public void testStartTransServletEscapesHtmlWhenTransFound() throws ServletException, IOException {
    try ( MockedStatic<Encode> encodeMockedStatic = mockStatic( Encode.class ) ) {
      KettleLogStore.init();
      HttpServletRequest mockHttpServletRequest = mock( HttpServletRequest.class );
      HttpServletResponse mockHttpServletResponse = mock( HttpServletResponse.class );
      Trans mockTrans = mock( Trans.class );
      TransMeta mockTransMeta = mock( TransMeta.class );
      LogChannelInterface mockChannelInterface = mock( LogChannelInterface.class );
      StringWriter out = new StringWriter();
      PrintWriter printWriter = new PrintWriter( out );

      spy( Encode.class );
      when( mockHttpServletRequest.getContextPath() ).thenReturn( StartTransServlet.CONTEXT_PATH );
      when( mockHttpServletRequest.getParameter( anyString() ) ).thenReturn( ServletTestUtils.BAD_STRING_TO_TEST );
      when( mockHttpServletResponse.getWriter() ).thenReturn( printWriter );
      when( mockTransformationMap.getTransformation( any( CarteObjectEntry.class ) ) ).thenReturn( mockTrans );
      when( mockTrans.getLogChannel() ).thenReturn( mockChannelInterface );
      when( mockTrans.getLogChannelId() ).thenReturn( "test" );
      when( mockTrans.getTransMeta() ).thenReturn( mockTransMeta );
      when( mockTransMeta.getMaximum() ).thenReturn( new Point( 10, 10 ) );

      startTransServlet.doGet( mockHttpServletRequest, mockHttpServletResponse );
      assertFalse( ServletTestUtils.hasBadText( ServletTestUtils.getInsideOfTag( "H1", out.toString() ) ) );
      encodeMockedStatic.verify( () -> Encode.forHtml( anyString() ) );
    }
  }
}
