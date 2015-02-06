package org.jomics.isoelectricpoint.sequence.aaindex;

import uk.ac.ebi.pride.utilities.mol.AminoAcid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yperez
 * Date: 8/21/13
 * Time: 10:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class AaIndex {

    private static volatile AaIndex instance = null;

    private Map<String, AaIndexEntry> index = null;

    private static Character START_LINE_CHAR      = 'H';

    private static Character DESCRIPTION_LINE_CHAR = 'D';

    private static Character LITERATURE_LINE_CHAR  = 'R';

    private static Character AUTHORS_LINE_CHAR     = 'A';

    private static Character MANUSCRIPT_TITLE_LINE_CHAR = 'T';

    private static Character JOURNAL_LINE_CHAR          = 'J';

    private static Character CORRELATION_MATRIX_LINE_CHAR = 'C';

    private static Character AMINO_ACID_INDEX_LINE_CHAR = 'I';

    private static String DOUBLE_NA                     = "NA";


    public static AaIndex getInstance() throws Exception {
        if (instance == null) {
            synchronized (AaIndex .class){
                if (instance == null) {
                    instance = new AaIndex();
                }
            }
        }
        return instance;
    }

    public Map<String, Map<AminoAcid, Double>> getAllAminoAcidIndexes(){
        Map<String, Map<AminoAcid, Double>> aminoAcidIndexes = new HashMap<String, Map<AminoAcid,Double>>();
        for(String accession: index.keySet())
            aminoAcidIndexes.put(accession,index.get(accession).getAminoAcidIndexes());
        return aminoAcidIndexes;
    }

    public Map<AminoAcid,Double> getAminoAcidIndexByAccession(String accession){
        return index.get(accession.toUpperCase()).getAminoAcidIndexes();
    }

    public AaIndexEntry getAaIndexEntryByAccession(String accession){
        return index.get(accession.toUpperCase());
    }

    public List<AaIndexEntry> getAaIndexEntriesByDescriptionKeyWord(String keyWord){
        List<AaIndexEntry> keyWordEntries = new ArrayList<AaIndexEntry>();
        for(String accession: index.keySet())
            if(index.get(accession).getDescription().toUpperCase().contains(keyWord.toUpperCase()))
                keyWordEntries.add(index.get(accession));
        return keyWordEntries;
    }

    public List<AaIndexEntry> getAaIndexEntriesByAuthorsKeyWord(String keyWord){
        List<AaIndexEntry> keyWordEntries = new ArrayList<AaIndexEntry>();
        for(String accession: index.keySet())
            if(index.get(accession).getAuthors().toUpperCase().contains(keyWord.toUpperCase()))
                keyWordEntries.add(index.get(accession));
        return keyWordEntries;
    }

    public List<AaIndexEntry> getAaIndexEntriesByManuscriptKeyWord(String keyWord){
        List<AaIndexEntry> keyWordEntries = new ArrayList<AaIndexEntry>();
        for(String accession: index.keySet()){
            String manuscript = index.get(accession).getManuscriptTitle().toUpperCase();
            manuscript += " " + index.get(accession).getJournalReference().toUpperCase();
            manuscript += " " + index.get(accession).getLiteratureId().toUpperCase();
            if(manuscript.contains(keyWord.toUpperCase()))
                keyWordEntries.add(index.get(accession));
        }
        return keyWordEntries;
    }

    public static Double getAvgAaIndexEntry(AaIndexEntry aaIndexEntry){
        int count = 0;
        double sum = 0.0;
        for(Double aaIndexValue: aaIndexEntry.getAminoAcidIndexes().values()){
            sum += (aaIndexValue != 0)? aaIndexValue: 0.0;
            count += (aaIndexValue != 0)? 1:0;
        }
        return sum / (double) count;
    }

    public static Double getSumAaIndexEntry(AaIndexEntry aaIndexEntry){
        int count = 0;
        double sum = 0.0;
        for(Double aaIndexValue: aaIndexEntry.getAminoAcidIndexes().values()){
            sum += (aaIndexValue != 0)? aaIndexValue: 0.0;
        }
        return sum;
    }

    private AaIndex() throws Exception {
        URL url = AaIndex.class.getClassLoader().getResource("aaindex.csv");
        if (url == null) {
            throw new IllegalStateException("The AAIndex database was not found!!");
        }
        File inputFile = new File(url.toURI());

        indexFile(inputFile);
    }

    private void indexFile(File inputFile) throws Exception {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
        String line = null;
        index = new HashMap<String, AaIndexEntry>();
        String accession = null,description = null, literatureId = null, authors = null, manuscriptTitle = null, journalReference = null;
        Map<String, Double> correlationMatrix = null;
        Map<AminoAcid,Double> aminoAcidIndexes = null;
        Character previousStart = null;
        while ((line = bufferedReader.readLine()) != null) {
            //Process the data, here we just print it out
            Character startline = line.charAt(0);
            if( startline == START_LINE_CHAR){
                if(accession != null){
                    AaIndexEntry aaIndexEntry = new AaIndexEntry(accession,description,literatureId,authors,manuscriptTitle,journalReference,correlationMatrix,aminoAcidIndexes);
                    index.put(accession, aaIndexEntry);
                }
                accession = removeStartLine(line);

            }else if(startline == DESCRIPTION_LINE_CHAR){
                description = removeStartLine(line);

            }else if(startline == LITERATURE_LINE_CHAR){
                literatureId = removeStartLine(line);

            }else if(startline == AUTHORS_LINE_CHAR){
                authors = removeStartLine(line);

            }else if(startline == MANUSCRIPT_TITLE_LINE_CHAR){
                manuscriptTitle = removeStartLine(line);

            }else if(startline == JOURNAL_LINE_CHAR){
                journalReference = removeStartLine(line);

            }else if(startline == CORRELATION_MATRIX_LINE_CHAR || (startline == ' ' && previousStart == CORRELATION_MATRIX_LINE_CHAR)){
                line = removeStartLine(line);
                correlationMatrix = new HashMap<String, Double>();
                correlationMatrix = getCorrelationFromLine(line,correlationMatrix);

            }else if(startline == AMINO_ACID_INDEX_LINE_CHAR){
                aminoAcidIndexes = new HashMap<AminoAcid, Double>();
                line = removeStartLine(line);
                String lineIndexUper = bufferedReader.readLine();
                lineIndexUper = removeStartLine(lineIndexUper);
                String lineIndexDown = bufferedReader.readLine();
                lineIndexDown = removeStartLine(lineIndexDown);
                aminoAcidIndexes = aminoAcidIndexes(line,lineIndexUper,lineIndexDown,aminoAcidIndexes);
            }
        }
    }

    private Map<String, Double> getCorrelationFromLine(String line, Map<String, Double> correlationMatrix){
        correlationMatrix = (correlationMatrix == null)? new HashMap<String, Double>():correlationMatrix;
        String[] array = line.split(" +");
        for(int i = 0; i < array.length-1; i = i+2){
            correlationMatrix.put(array[i],Double.valueOf(array[i+1]));
        }
        return correlationMatrix;
    }

    private Map<AminoAcid, Double> aminoAcidIndexes(String label, String lineUp, String lineDown, Map<AminoAcid,Double> aminoAcidIdexes){
        aminoAcidIdexes = (aminoAcidIdexes == null)? new HashMap<AminoAcid, Double>():aminoAcidIdexes;

        List<String> labelArray = new ArrayList<String>();
        String[] lineArray = label.split(" +");
        for(int i = 0; i < lineArray.length; i++)
            if(!lineArray[i].equalsIgnoreCase(" ") && !lineArray[i].equalsIgnoreCase("\t") &&
               !lineArray[i].equalsIgnoreCase("") && lineArray[i].length() != 0)
                labelArray.add(lineArray[i]);

        List<String> lineUps     = new ArrayList<String>();
        lineArray = lineUp.split(" +");
        for(int i = 0; i < lineArray.length; i++)
            if(!lineArray[i].equalsIgnoreCase(" ") && !lineArray[i].equalsIgnoreCase("\t") &&
               !lineArray[i].equalsIgnoreCase("")  && lineArray[i].length() != 0)
                lineUps.add(lineArray[i]);

        List<String> lineDowns   = new ArrayList<String>();
        lineArray = lineDown.split(" +");
        for(int i = 0; i < lineArray.length; i++)
            if(!lineArray[i].equalsIgnoreCase(" ") && !lineArray[i].equalsIgnoreCase("\t") &&
               !lineArray[i].equalsIgnoreCase("")  && lineArray[i].length() != 0)
                lineDowns.add(lineArray[i]);

        for(int i=0; i < labelArray.size(); i++){
            String[] aaString = labelArray.get(i).split("\\/");
            Double indexUp   = (lineUps.get(i).equalsIgnoreCase(DOUBLE_NA))?null:Double.parseDouble(lineUps.get(i));
            Double indexDown = (lineDowns.get(i).equalsIgnoreCase(DOUBLE_NA))?null:Double.parseDouble(lineDowns.get(i));
            aminoAcidIdexes.put(AminoAcid.getAminoAcid(aaString[0].charAt(0)),indexUp);
            aminoAcidIdexes.put(AminoAcid.getAminoAcid(aaString[1].charAt(0)),indexDown);
        }
        return aminoAcidIdexes;
    }

    private String removeStartLine(String line){
        line = line.substring(2, line.length());
        line.trim();
        return line;
    }


}
