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
package org.pentaho.di.plugins.fileopensave.service;

import org.pentaho.di.plugins.fileopensave.cache.FileCache;

public enum FileCacheService {

    INSTANCE;

    private FileCache fileCache;

    private FileCacheService() {
        fileCache = new FileCache();
    }

    public FileCache get() {
        return fileCache;
    }

}
