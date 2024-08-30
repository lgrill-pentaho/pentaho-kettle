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

package org.pentaho.di.ui.spoon.tree;

import org.eclipse.swt.graphics.Image;
import org.pentaho.di.base.AbstractMeta;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.ui.core.gui.GUIResource;
import org.pentaho.di.ui.core.widget.tree.TreeNode;

/**
 * Created by bmorrise on 6/26/18.
 */
public abstract class TreeFolderProvider {

  protected TreeManager treeManager;

  public abstract void refresh( AbstractMeta meta, TreeNode treeNode, String filter );

  public Class getType() {
    return Object.class;
  }

  public void checkUpdate( AbstractMeta meta, TreeNode treeNode, String filter ) {
    if ( treeManager.shouldUpdate( meta, getTitle() ) ) {
      treeNode.removeAll();
      refresh( meta, treeNode, filter );
    }
  }

  public abstract String getTitle();

  protected boolean filterMatch( String string, String filter ) {
    return Utils.isEmpty( string ) || Utils.isEmpty( filter ) || string.toUpperCase().contains( filter.toUpperCase() );
  }

  public void create( AbstractMeta meta, TreeNode parent ) {
    refresh( meta, createTreeNode( parent, getTitle(), getTreeImage() ), null );
  }

  protected Image getTreeImage() {
    return GUIResource.getInstance().getImageFolder();
  }

  public TreeNode createTreeNode( TreeNode parent, String text, Image image ) {
    TreeNode childTreeNode = new TreeNode();
    childTreeNode.setLabel( text );
    childTreeNode.setImage( image );

    parent.addChild( childTreeNode );
    return childTreeNode;
  }

  public void setTreeManager( TreeManager treeManager ) {
    this.treeManager = treeManager;
  }
}
