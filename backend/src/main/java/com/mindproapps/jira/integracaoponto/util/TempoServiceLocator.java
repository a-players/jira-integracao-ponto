/*
package com.mindproapps.jira.integracaoponto.util;

import com.tempoplugin.platform.api.user.UserManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import javax.inject.Named;

@Named
public class TempoServiceLocator {

    public UserManager getUserManager() {
        BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        ServiceReference<UserManager> serviceReference = bundleContext.getServiceReference(UserManager.class);
        if (serviceReference != null) {
            return bundleContext.getService(serviceReference);
        }
        return null;
    }

}
*/