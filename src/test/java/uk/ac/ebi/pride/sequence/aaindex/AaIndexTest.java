package uk.ac.ebi.pride.sequence.aaindex;

import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.pride.utilities.mol.AminoAcid;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: yperez
 * Date: 8/23/13
 * Time: 9:56 AM
 */
public class AaIndexTest {

    AaIndex calculator;
    @Before
    public void setUp() throws Exception {
        calculator = AaIndex.getInstance();
    }

    @Test
    public void testGetAllAminoAcidIndexes() throws Exception {
        Map<String, Map<AminoAcid, Double>> aminoAcidIdexes = calculator.getAllAminoAcidIndexes();
        assertTrue(aminoAcidIdexes.get("ZIMJ680104").get(AminoAcid.R) == 10.76);
    }

    @Test
    public void testGetAminoAcidIndexByAccession() throws Exception {
        Map<AminoAcid, Double> aminoAcidIdexes = calculator.getAminoAcidIndexByAccession("GUOD860101");
        assertTrue(aminoAcidIdexes.get(AminoAcid.R) == -7);
    }

    @Test
    public void testGetAaIndexEntryByAccession() throws Exception {
        AaIndexEntry aaIndexEntry = calculator.getAaIndexEntryByAccession("WOLR790101");
        assertTrue(aaIndexEntry.getAminoAcidIndexes().get(AminoAcid.V) == 1.13);
    }

    @Test
    public void testGetAaIndexEntriesByDescriptionKeyWord() throws Exception {
        List<AaIndexEntry> aaIndexEntryList = calculator.getAaIndexEntriesByDescriptionKeyWord("retention");
        assertTrue(aaIndexEntryList.size() == 7);
    }

    @Test
    public void testGetAaIndexEntriesByAuthorsKeyWord() throws Exception {
        List<AaIndexEntry> aaIndexEntryList = calculator.getAaIndexEntriesByAuthorsKeyWord("Nakashima");
        assertTrue(aaIndexEntryList.size() == 21);
    }

    @Test
    public void testGetAaIndexEntriesByManuscriptKeyWord() throws Exception {
        List<AaIndexEntry> aaIndexEntryList = calculator.getAaIndexEntriesByManuscriptKeyWord("structure");
        assertTrue(aaIndexEntryList.size() == 122);
    }

    @Test
    public void testGetAvgAaIndexEntry() throws Exception {
        AaIndexEntry aaIndexEntry = calculator.getAaIndexEntryByAccession("GUOD860101");
        DecimalFormat df = new DecimalFormat("#.##");
        assertTrue(Double.parseDouble(df.format(AaIndex.getAvgAaIndexEntry(aaIndexEntry))) == 32.68);
    }

    @Test
    public void testGetSumAaIndexEntry() throws Exception {
        AaIndexEntry aaIndexEntry = calculator.getAaIndexEntryByAccession("GUOD860101");
        DecimalFormat df = new DecimalFormat("#.##");
        assertTrue(Double.parseDouble(df.format(AaIndex.getSumAaIndexEntry(aaIndexEntry))) == 621.0);
    }
}
