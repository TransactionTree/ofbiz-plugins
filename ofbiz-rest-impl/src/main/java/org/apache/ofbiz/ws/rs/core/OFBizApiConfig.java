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
package org.apache.ofbiz.ws.rs.core;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import org.apache.ofbiz.base.component.ComponentConfig;
import org.apache.ofbiz.base.component.ComponentException;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.ws.rs.model.ModelApi;
import org.apache.ofbiz.ws.rs.model.ModelApiReader;
import org.apache.ofbiz.ws.rs.model.ModelOperation;
import org.apache.ofbiz.ws.rs.model.ModelResource;
import org.apache.ofbiz.ws.rs.process.ServiceRequestHandler;
import org.apache.ofbiz.ws.rs.security.Secured;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceMethod;

public final class OFBizApiConfig extends ResourceConfig {
    private static final String MODULE = OFBizApiConfig.class.getName();
    private static final Map<String, ModelApi> apis = new HashMap<>();

    public OFBizApiConfig() {
        packages("org.apache.ofbiz.ws.rs.resources");
        packages("org.apache.ofbiz.ws.rs.security.auth");
        packages("org.apache.ofbiz.ws.rs.spi.impl");
        // packages("io.swagger.v3.jaxrs2.integration.resources"); //commenting it out
        // to generate customized OpenApi Spec
        register(JacksonFeature.class);
        register(MultiPartFeature.class);
        if (Debug.verboseOn()) {
            register(new LoggingFeature(Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME), Level.INFO,
                    LoggingFeature.Verbosity.PAYLOAD_ANY, 10000));
        }
        registerResources();
    }

    private void registerResources() {
        loadApiDefinitions();
        traverseApiDefinitions();
    }

    public static Map<String, ModelApi> getModelApis() {
        return apis;
    }

    private void loadApiDefinitions() {
        Collection<ComponentConfig> components = ComponentConfig.getAllComponents();
        components.forEach(component -> {
            String cName = component.getComponentName();
            try {
                String loc = ComponentConfig.getRootLocation(cName) + "/api/";
                File folder = new File(loc);
                if (folder.isDirectory() && folder.exists()) {
                    File[] schemaFiles = folder.listFiles((dir, fileName) -> fileName.endsWith(".rest.xml"));
                    for (File schemaFile : schemaFiles) {
                        Debug.logInfo("REST API file " + schemaFile.getName() + " was found in component " + cName, MODULE);
                        Debug.logInfo("Processing REST API " + schemaFile.getName() + " from component " + cName, MODULE);
                        ModelApi api = ModelApiReader.getModelApi(schemaFile);
                        apis.put(cName, api);
                    }
                }
            } catch (ComponentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    private void traverseApiDefinitions() {
        if (UtilValidate.isEmpty(apis)) {
            Debug.logWarning("No API defintion to process", MODULE);
            return;
        }
        apis.forEach((k, v) -> {
            Debug.logInfo("Registring Resource Definitions from API - " + k, MODULE);
            List<ModelResource> resources = v.getResources();
            resources.forEach(modelResource -> {
                Resource.Builder resourceBuilder = Resource.builder(modelResource.getPath()).name(modelResource.getName());
                for (ModelOperation op : modelResource.getOperations()) {
                    if (UtilValidate.isEmpty(op.getPath())) { // Add the method to the parent resource
                        ResourceMethod.Builder methodBuilder = resourceBuilder.addMethod(op.getVerb().toUpperCase());
                        methodBuilder.produces(MediaType.APPLICATION_JSON).nameBindings(Secured.class);
                        String serviceName = op.getService();
                        methodBuilder.handledBy(new ServiceRequestHandler(serviceName));
                    } else {
                        Resource.Builder childResourceBuilder = resourceBuilder.addChildResource(op.getPath());
                        ResourceMethod.Builder childResourceMethodBuilder = childResourceBuilder.addMethod(op.getVerb().toUpperCase());
                        childResourceMethodBuilder.produces(MediaType.APPLICATION_JSON).nameBindings(Secured.class);
                        String serviceName = op.getService();
                        childResourceMethodBuilder.handledBy(new ServiceRequestHandler(serviceName));
                    }
                }
                registerResources(resourceBuilder.build());
            });
        });
    }
}
