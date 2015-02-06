package org.jomics.isoelectricpoint.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * User: yperez
 * Date: 4/19/13
 * Time: 10:34 AM
 *
 */
public class ModificationContribution {

    private static Map<String, Double> modificationMap = new HashMap<String, Double>();

    private static String              PHOSPHOSET_SCANSITE = "SCANSITE";

    private static String              PHOSPHOSET_PROMOST  = "PROMOST";

    private static String              PHOSPHOSET_DEFAULT  = "DEFAULT";

    private static String              defaultPhosphoSet   =  PHOSPHOSET_DEFAULT;



    public static enum ModificationsNoContribution{
        OXI("Oxidation", "15", 15.994915);

        ModificationsNoContribution(String oxidation, String prideID, Double delta) {
            //To change body of created methods use File | Settings | File Templates.
        }
    }

    public static enum ModificationCleanContribution{

    }


}
