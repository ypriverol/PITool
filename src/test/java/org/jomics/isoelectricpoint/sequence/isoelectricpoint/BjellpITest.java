package org.jomics.isoelectricpoint.sequence.isoelectricpoint;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.pride.utilities.mol.AminoAcid;
import org.jomics.isoelectricpoint.sequence.isoelectricpoint.bjellpI.BjellpI;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: yperez
 * Date: 8/15/13
 * Time: 12:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class BjellpITest {

    BjellpI calculator = null;

    @Test
    public void testComputePIGroup() throws Exception {

        List<AminoAcid> sequence = new ArrayList<AminoAcid>();
        String temp = "EYQLNDSASYYLNDLDR";
        for(Character character: temp.toCharArray()) sequence.add(AminoAcid.getAminoAcid(character));

        List<List<AminoAcid>> sequences = new ArrayList<List<AminoAcid>>();
        sequences.add(sequence);

        Double sequencesPI = calculator.computePI(sequence);

        assertTrue("Isoelectric Point equal to ", sequencesPI == 3.69);


    }

    @Before
    public void setUp() throws Exception {
        calculator = BjellpI.getInstance(BjellpI.BJELL_PKMETHOD, 4.8);
    }

    @After
    public void tearDown() throws Exception {
    }
}
