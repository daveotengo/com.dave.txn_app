package com.dave.txn_app.server;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.dave.txn_app.endpoints.TxnEndPoint;




public class TnxApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new LinkedHashSet<Class<?>>();

        resources.add(TxnEndPoint.class);

        return resources;
    }

    /*
     * Exception mapper handler here
     */
    @Override
    public Set<Object> getSingletons() {
        Set<Object> singleton = new LinkedHashSet<Object>();
//        singleton.add(new AuthenticationExceptionMapper());
//        singleton.add(new CallbackExceptionMapper());
//        singleton.add(new GeneralExceptionMapper());
        //singleton.add(new MissingRequiredParamsExceptionMapper());
        return singleton;
    }

}
