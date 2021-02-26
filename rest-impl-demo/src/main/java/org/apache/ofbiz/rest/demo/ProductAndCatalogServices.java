package org.apache.ofbiz.rest.demo;

import java.util.List;
import java.util.Map;

import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ModelService;
import org.apache.ofbiz.service.ServiceUtil;

/**
 * @author grv
 *
 */
public final class ProductAndCatalogServices {

    private ProductAndCatalogServices() {

    }

    public static Map<String, Object> getAllCategories(DispatchContext dctx, Map<String, ? extends Object> context)
            throws GenericEntityException {
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Delegator delegator = dctx.getDelegator();
        List<GenericValue> categories = EntityQuery.use(delegator).from("ProductCategory").queryList();
        result.put("categories", categories);
        return result;
    }

    public static Map<String, Object> createProductAndAddCategories(DispatchContext dctx,
            Map<String, ? extends Object> context) throws GenericServiceException {
        System.out.println("context: " + context);
        LocalDispatcher dispatcher = dctx.getDispatcher();
        List<String> categoriesToAddTo = (List<String>) context.get("categories");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        System.out.println("categoriesToAddTo " + categoriesToAddTo);
        Map<String, Object> result = null;
        Map<String, Object> createProductCtx;

        createProductCtx = dctx.makeValidContext("createProduct", ModelService.IN_PARAM, context);
        result = dispatcher.runSync("createProduct", createProductCtx);

        if (ServiceUtil.isError(result)) {
            String errorMessage = ServiceUtil.getErrorMessage(result);
            System.out.println("errorMessage " + errorMessage);
            return result;
        }

        String productId = (String) result.get("productId");
        result.clear();
        System.out.println("ProductId generated: " + productId);
        Map<String, Object> addProductToCategoriesCtx;

        addProductToCategoriesCtx = dctx.makeValidContext("addProductToCategories", ModelService.IN_PARAM,
                UtilMisc.toMap("productId", productId, "categories", categoriesToAddTo, "userLogin", userLogin));
        addProductToCategoriesCtx.put("userLogin", userLogin);
        result = dispatcher.runSync("addProductToCategories", addProductToCategoriesCtx);

        if (ServiceUtil.isError(result)) {
            String errorMessage = ServiceUtil.getErrorMessage(result);
            return result;
        }
        result.put("productId", productId);
        return result;
    }

    public static Map<String, Object> getProductFeatures(DispatchContext dctx, Map<String, ? extends Object> context)
            throws GenericEntityException {
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Delegator delegator = dctx.getDelegator();
        String productId = (String) context.get("productId");
        List<GenericValue> existingProductFeatureList = EntityQuery.use(delegator).from("ProductFeatureAndAppl")
                .where("productId", productId).filterByDate().queryList();
        result.put("productFeatures", existingProductFeatureList);
        return result;
    }

    public static Map<String, Object> getProductFeature(DispatchContext dctx, Map<String, ? extends Object> context)
            throws GenericEntityException {
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Delegator delegator = dctx.getDelegator();
        String productId = (String) context.get("productId");
        String featureId = (String) context.get("featureId");
        GenericValue existingProductFeatureList = EntityQuery.use(delegator).from("ProductFeatureAndAppl")
                .where("productId", productId, "productFeatureId", featureId).filterByDate().queryFirst();
        result.put("productFeature", existingProductFeatureList);
        ;
        return result;
    }
}
