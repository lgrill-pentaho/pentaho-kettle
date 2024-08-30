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
package org.pentaho.di.ui.core.events.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectionAdapterOptions {
  private SelectionOperation selectionOperation;
  private String[] filters;
  private String defaultFilter;
  private String[] providerFilters;
  private List<ConnectionFilterType> connectionFilters = new ArrayList<>();
  private boolean useSchemaPath;

  public SelectionAdapterOptions( SelectionOperation selectionOperation, String[] filters, String defaultFilter,
                                  String[] providerFilters, boolean useSchemaPath  ) {
    this.selectionOperation = selectionOperation;
    this.filters = filters;
    this.defaultFilter = defaultFilter;
    this.providerFilters = providerFilters;
    this.useSchemaPath = useSchemaPath;
  }

  public SelectionAdapterOptions( SelectionOperation selectionOperation, FilterType[] filters, FilterType defaultFilter,
                                  String[] providerFilters, boolean useSchemaPath  ) {
    this( selectionOperation, toStringArray( filters ), toString( defaultFilter ), providerFilters, useSchemaPath );
  }

  public SelectionAdapterOptions( SelectionOperation selectionOperation, String[] filters, String defaultFilter ) {
    this( selectionOperation, filters, defaultFilter, null, false );
  }

  public SelectionAdapterOptions( SelectionOperation selectionOperation, FilterType[] filters, FilterType defaultFilter ) {
    this( selectionOperation, toStringArray( filters ), toString( defaultFilter ), null, false );
  }

  public SelectionAdapterOptions( SelectionOperation selectionOperation, String[] filters, String defaultFilter,
                                  String[] providerFilters ) {
    this( selectionOperation, filters, defaultFilter, providerFilters, false );
  }

  public SelectionAdapterOptions( SelectionOperation selectionOperation, FilterType[] filters, FilterType defaultFilter,
                                  ProviderFilterType[] providerFilters ) {
    this( selectionOperation, toStringArray( filters ), toString( defaultFilter ), toStringArray( providerFilters ), false );
  }

  public SelectionAdapterOptions( SelectionOperation selectionOperation ) {
    this( selectionOperation, (String[]) null, null, null, false );
  }

  public SelectionOperation getSelectionOperation() {
    return selectionOperation;
  }

  public SelectionAdapterOptions setSelectionOperation( SelectionOperation selectionOperation ) {
    this.selectionOperation = selectionOperation;
    return this;
  }

  protected static String[] toStringArray( FilterType[] filterTypes ) {
    return Arrays.stream( filterTypes ).map( Enum::toString ).toArray( String[]::new );
  }

  protected static String[] toStringArray( ProviderFilterType[] providerFilterTypes ) {
    return Arrays.stream( providerFilterTypes ).map( Enum::toString ).toArray( String[]::new );
  }

  protected static String toString( FilterType filterType ) {
    return filterType == null ? null : filterType.toString();
  }

  public String[] getFilters() {
    return filters;
  }

  public SelectionAdapterOptions setFilters( String[] filters ) {
    this.filters = filters;
    return this;
  }

  public SelectionAdapterOptions setFilters( FilterType[] filters ) {
    return setFilters( toStringArray( filters ) );
  }

  public String getDefaultFilter() {
    return defaultFilter;
  }

  public SelectionAdapterOptions setDefaultFilter( String defaultFilter ) {
    this.defaultFilter = defaultFilter;
    return this;
  }

  public String[] getProviderFilters() {
    return providerFilters;
  }

  public SelectionAdapterOptions setProviderFilters( String[] providerFilters ) {
    this.providerFilters = providerFilters;
    return this;
  }

  public SelectionAdapterOptions setProviderFilters( ProviderFilterType[] providerFilters ) {
    return setProviderFilters( toStringArray( providerFilters ) );
  }

  public boolean getUseSchemaPath() {
    return useSchemaPath;
  }

  public SelectionAdapterOptions setUseSchemaPath( boolean useSchemaPath ) {
    this.useSchemaPath = useSchemaPath;
    return this;
  }

  public List<ConnectionFilterType> getConnectionFilters() {
    return connectionFilters;
  }

  public void setConnectionFilters( List<ConnectionFilterType> connectionFilters ) {
    this.connectionFilters = connectionFilters;
  }

}
