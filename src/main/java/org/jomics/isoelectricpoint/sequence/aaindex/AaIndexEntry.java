package org.jomics.isoelectricpoint.sequence.aaindex;

import uk.ac.ebi.pride.utilities.mol.AminoAcid;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: yperez
 * Date: 8/22/13
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class AaIndexEntry {

    private String accession;
    /* (H) Accession number */

    private String description;
    /* (D) Data description */

    private String literatureId;
    /* (R) LITDB entry number */

    private String authors;
    /* (A) Author(s) */

    private String manuscriptTitle;
    /* (T) Title of the article */

    private String journalReference;
    /* (J) Journal reference */

    private Map<String, Double> correlationMatrix;
    /* (C) Accession numbers of similar entries with the correlation
     * coefficients of 0.8 (-0.8) or more (less).
     */

    private Map<AminoAcid,Double> aminoAcidIndexes;
    /* (I) Amino acid index data in the following order
     *   Ala    Arg    Asn    Asp    Cys    Gln    Glu    Gly    His    Ile
     *   Leu    Lys    Met    Phe    Pro    Ser    Thr    Trp    Tyr    Val
     */

    public AaIndexEntry(String accession, String description, String literatureId, String authors, String manuscriptTitle, String journalReference, Map<String, Double> correlationMatrix, Map<AminoAcid, Double> aminoAcidIndexes) {
        this.accession = accession;
        this.description = description;
        this.literatureId = literatureId;
        this.authors = authors;
        this.manuscriptTitle = manuscriptTitle;
        this.journalReference = journalReference;
        this.correlationMatrix = correlationMatrix;
        this.aminoAcidIndexes = aminoAcidIndexes;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLiteratureId() {
        return literatureId;
    }

    public void setLiteratureId(String literatureId) {
        this.literatureId = literatureId;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getManuscriptTitle() {
        return manuscriptTitle;
    }

    public void setManuscriptTitle(String manuscriptTitle) {
        this.manuscriptTitle = manuscriptTitle;
    }

    public String getJournalReference() {
        return journalReference;
    }

    public void setJournalReference(String journalReference) {
        this.journalReference = journalReference;
    }

    public Map<String, Double> getCorrelationMatrix() {
        return correlationMatrix;
    }

    public void setCorrelationMatrix(Map<String, Double> correlationMatrix) {
        this.correlationMatrix = correlationMatrix;
    }

    public Map<AminoAcid, Double> getAminoAcidIndexes() {
        return aminoAcidIndexes;
    }

    public void setAminoAcidIndexes(Map<AminoAcid, Double> aminoAcidIndexes) {
        this.aminoAcidIndexes = aminoAcidIndexes;
    }
}
