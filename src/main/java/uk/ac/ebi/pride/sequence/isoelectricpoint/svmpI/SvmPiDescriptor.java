package uk.ac.ebi.pride.sequence.isoelectricpoint.svmpI;

/**
 * Created with IntelliJ IDEA.
 * User: yperez
 * Date: 8/15/13
 * Time: 9:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class SvmPiDescriptor {

    private double bjellpI;

    private double zimmermanpI;

    private double exppI;

    public SvmPiDescriptor(double bjellpI, double zimmermanpI, double exppI) {
        this.bjellpI = bjellpI;
        this.zimmermanpI = zimmermanpI;
        this.exppI = exppI;
    }

    public double getBjellpI() {
        return bjellpI;
    }

    public double getZimmermanpI() {
        return zimmermanpI;
    }

    public double getExppI() {
        return exppI;
    }
}