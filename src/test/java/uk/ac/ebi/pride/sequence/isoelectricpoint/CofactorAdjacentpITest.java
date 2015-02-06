package uk.ac.ebi.pride.sequence.isoelectricpoint;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.pride.utilities.mol.AminoAcid;
import uk.ac.ebi.pride.sequence.isoelectricpoint.cofactorAdjacentpI.CofactorAdjacentpI;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 *
 * User: yperez
 * Date: 4/19/13
 * Time: 1:55 AM
 * */
public class CofactorAdjacentpITest {

    CofactorAdjacentpI calculator = null;

    @Test
    public void testComputePIGroup() throws Exception {

        List<AminoAcid> sequence = new ArrayList<AminoAcid>();
        String temp = "EYQLNDSASYYLNDLDR";
        for(Character character: temp.toCharArray()) sequence.add(AminoAcid.getAminoAcid(character));

        List<List<AminoAcid>> sequences = new ArrayList<List<AminoAcid>>();
        sequences.add(sequence);

        Double sequencesPI = calculator.computePI(sequence);

        System.out.println(sequencesPI);

        assertTrue("Isoelectric Point equal to ", sequencesPI == 3.7196044921875);


    }

    @Before
    public void setUp() throws Exception {
        calculator = CofactorAdjacentpI.getInstance();
    }

    @After
    public void tearDown() throws Exception {
    }


}
