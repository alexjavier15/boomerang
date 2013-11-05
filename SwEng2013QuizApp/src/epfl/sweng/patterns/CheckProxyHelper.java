package epfl.sweng.patterns;

import epfl.sweng.servercomm.HttpCommsProxy;

public class CheckProxyHelper implements ICheckProxyHelper {

    @Override
    public Class<?> getServerCommunicationClass() {
        return HttpCommsProxy.getInstance().getServerCommsClass();
    }

    @Override
    public Class<?> getProxyClass() {
        return HttpCommsProxy.getInstance().getClass();

    }

}
