package uk.ac.ebi.pride.sequence.isoelectricpoint;


import uk.ac.ebi.pride.utilities.mol.AminoAcid;

import java.util.List;
import java.util.Map;

/**
 * This is a simple interface that represent all the methods that an isoelectric method
 * need to compute the isoelectric point.
 * @author yperez
 */

public interface IsoelectricPointMethod{


    /**
     * This method compute the isoelectric point for a set of sequences. For each class this method is
     * computed by the especific algorithm.    
     * @param sequence
     * @return
     */
    
    public Double computePI(List<AminoAcid> sequence) throws Exception;


    /**
     * This method compute the charge of a set of sequences at a pH value. The method return the last
     * value of the pH for the method and all the charges for sequences.
     * by the
     * @param sequence
     * @param pH
     * @return
     */

    public Double computeChargeAtpH(List<AminoAcid> sequence, Double pH);

    /**
     * This method compute the isoelectric point for a set of sequences and return the steps of
     * the computation. This methods compute the uk.ac.ebi.pride.isoelectricpoint moved with and step of 0.5 the algorithm.
     *
     * @param sequence
     * @return
     */

    public Map<Double,Double> computeStepMethodPI(List<AminoAcid> sequence);



}
