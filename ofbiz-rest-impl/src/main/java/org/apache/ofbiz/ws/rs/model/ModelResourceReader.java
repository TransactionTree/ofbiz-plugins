/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.apache.ofbiz.ws.rs.model;

import java.net.URL;
import java.util.Map;

import org.apache.ofbiz.base.config.ResourceHandler;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.service.ModelService;

public class ModelResourceReader {
    private static final String MODULE = ModelResourceReader.class.getName();

    private boolean isFromURL;
    private URL readerURL = null;
    private ResourceHandler handler = null;
    private Delegator delegator = null;

    private ModelResourceReader(boolean isFromURL, URL readerURL, ResourceHandler handler, Delegator delegator) {
        this.isFromURL = isFromURL;
        this.readerURL = readerURL;
        this.handler = handler;
    }

    public static Map<String, ModelService> getModelResourceMap(URL readerURL, Delegator delegator) {
        if (readerURL == null) {
            Debug.logError("Cannot add reader with a null reader URL", MODULE);
            return null;
        }

        ModelResourceReader reader = new ModelResourceReader(true, readerURL, null, delegator);
        return reader.getModelResources();
    }

    private Map<String, ModelService> getModelResources() {
        return null;
    }
}
