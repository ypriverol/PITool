package uk.ac.ebi.pride.utils;

import uk.ac.ebi.pride.utilities.mol.AminoAcid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yperez
 * Date: 8/20/13
 * Time: 3:52 PM
 */
public class SequenceUtils {

   /* This method returns a List of AminoAcid from a given String AminoAcid Sequence. The
    * AminoAcid class contains the Tree and One Letter Code and the Mass of each AminoAcids
    */
    public static List<AminoAcid> getSequence(String sequence){
        List<AminoAcid> sequenceList = new ArrayList<AminoAcid>();
        for(Character character: sequence.toCharArray()) sequenceList.add(AminoAcid.getAminoAcid(character));
        return sequenceList;
    }

   /*
    * This method returns the Common Amino Acids between two Sequences.
    */
    public static List<AminoAcid> getCommonAminoAcids(List<AminoAcid> v1, List<AminoAcid> v2){
        List<AminoAcid> resultList = new ArrayList<AminoAcid>();
        for(AminoAcid aminoAcid: v1)
            if(v2.contains(v1)) resultList.add(aminoAcid);
        return resultList;
    }

   /*
    * Calculate the number of a certain type of aa. The set gives the type of these amino acids
    * (for example, the type could be the list "A", "I", "L", "M", "F", "W", "Y", "V", "C" for
    * hydrophobic aa)
    */
    public static Integer numberTypeAA(List<AminoAcid> sequence, List<AminoAcid> aaTypes) {
        Integer occurences = 0;
        for(AminoAcid aminoAcid: sequence)
            if(aaTypes.contains(aminoAcid)) occurences++;
        return occurences;
    }

   /*
    *  This method returns the number of occurrences of one AminoAcid in a Sequence List
    */
    public static Integer numberAAOccurrences(List<AminoAcid> sequence, AminoAcid aminoAcid){
        Integer occurrences = 0;
        for(AminoAcid aminoAcidType: sequence)
            if(aminoAcidType == aminoAcid) occurrences++;
        return occurrences;
    }

   /*
    * This method returns the Unique Amino Acids present in a List of sequences.The function
    * loop through each sequence and retrieve the new amino acids.
    */
    public static List<AminoAcid> getPresentAAList(Collection<List<AminoAcid>> sequences) {
        List<AminoAcid> resultAAList = new ArrayList<AminoAcid>();
        for(List<AminoAcid> sequence: sequences)
            for(AminoAcid aminoAcid: sequence)
                if(!resultAAList.contains(aminoAcid)) resultAAList.add(aminoAcid);
        return resultAAList;
    }

    public static boolean containPTMs(Collection<List<AminoAcid>> lists) {
        return false;
    }
}
