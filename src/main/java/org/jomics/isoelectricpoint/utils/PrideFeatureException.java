package org.jomics.isoelectricpoint.utils;

/**
 * Created with IntelliJ IDEA.
 * User: yperez
 * Date: 8/29/13
 * Time: 1:48 PM
 * To change this template use File | Settings | File Templates.
 */

public class PrideFeatureException extends RuntimeException {

    public PrideFeatureException(String message) {
        super(message);
    }

    public PrideFeatureException(String message, Throwable cause) {
        super(message, cause);
    }
}