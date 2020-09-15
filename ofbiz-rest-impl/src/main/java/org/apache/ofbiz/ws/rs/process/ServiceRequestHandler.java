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
package org.apache.ofbiz.ws.rs.process;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ModelParam;
import org.apache.ofbiz.service.ModelService;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.ws.rs.util.RestApiUtil;
import org.glassfish.jersey.server.ExtendedUriInfo;

public final class ServiceRequestHandler extends RestRequestHandler {

    private String service;

    public ServiceRequestHandler(String service) {
        this.service = service;
    }

    @Inject
    HttpHeaders httpHeaders;

    @Inject
    UriInfo uriInfo;

    @Inject
    ExtendedUriInfo extendedUriInfo;

    @Inject
    ResourceInfo resourceInfo;

    @Inject
    ServletContext servletContext;

    @Inject
    HttpServletRequest httpRequest;

    /**
     * @param data
     * @return
     * @throws GenericServiceException
     */
    @Override
    protected Response execute(ContainerRequestContext data, Map<String, Object> arguments) {
        LocalDispatcher dispatcher = (LocalDispatcher) servletContext.getAttribute("dispatcher");
        System.out.println("arguments: "+arguments);
        Map<String, Object> serviceContext = null;
        try {
            serviceContext = dispatcher.getDispatchContext().makeValidContext(service, ModelService.IN_PARAM, arguments);
        } catch (GenericServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ModelService svc = getModelService(dispatcher.getDispatchContext());
        GenericValue userLogin = (GenericValue) httpRequest.getAttribute("userLogin");
        serviceContext.put("userLogin", userLogin);
        Map<String, Object> result = null;
        try {
            result = dispatcher.runSync(service, serviceContext);
        } catch (GenericServiceException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        Map<String, Object> responseData = new LinkedHashMap<>();
        if (ServiceUtil.isSuccess(result)) {
            Set<String> outParams = svc.getOutParamNames();
            for (String outParamName : outParams) {
                ModelParam outParam = svc.getParam(outParamName);
                if (!outParam.isInternal()) {
                    Object value = result.get(outParamName);
                    if (UtilValidate.isNotEmpty(value)) {
                        responseData.put(outParamName, value);
                    }
                }
            }
            return RestApiUtil.success((String) result.get(ModelService.SUCCESS_MESSAGE), responseData);
        } else {
            return RestApiUtil.error(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    (String) result.get(ModelService.ERROR_MESSAGE));
        }
    }

    private ModelService getModelService(DispatchContext dispatchContext) {
        ModelService svc = null;
        try {
            svc = dispatchContext.getModelService(service);
        } catch (GenericServiceException gse) {
            throw new NotFoundException(gse.getMessage());
        }
        return svc;
    }
}
