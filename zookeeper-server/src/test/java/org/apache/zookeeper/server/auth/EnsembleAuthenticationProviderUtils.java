package org.apache.zookeeper.server.auth;

public class EnsembleAuthenticationProviderUtils {
    public enum RetType {
        NullPointerException,
        ENSEMBLE_AUTH_SKIP,
        ENSEMBLE_AUTH_FAIL,
        ENSEMBLE_AUTH_SUCCESS
    }
}
