package com.seniordesign.titlesearch;

/*
 * Trust Deed Class:
 * Holds the data about an individual Trust Deed
 * Book #, Page #, Grantor, Grantee, Ownership Chain
 */

public class TrustDeed {

    private String bookNumber, pageNumber;
    private String nameOfOwner;
    private String text;
    private Boolean isReleased;
    private byte[] release;
    private byte[] trustDeedPDF;
    private String trustDeedText;

    // Gets Trust deed book #
    private String getBookNumber() {
        return this.bookNumber;
    }

    // Assigns Trust deed book #
    private void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }

    // Gets Trust deed page #
    private String getPageNumber() {
        return this.pageNumber;
    }

    // Assigns Trust deed book #
    private void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    // Gets name of owner
    private String getNameOfOwner() {
        return this.nameOfOwner;
    }

    // Assigns name of owner
    private void setNameOfOwner(String nameOfOwner) {
        this.nameOfOwner = nameOfOwner;
    }

    private Boolean setReleased() { 
        return this.isReleased;
    }

    private byte[] getRelease() {
        return this.release;
    }

    // Gets Trust deed PDF
    private byte[] getPDF() {
        return this.trustDeedPDF;
    }

    // Assigns next Trust deed PDF
    // TODO does this need to be a filename input or a byte[]
    private void setPDF(byte[] trustDeedPDF) {
        this.trustDeedPDF = trustDeedPDF;
    }

    private void setTrustDeedText(String trustDeedText) {
        this.trustDeedText = trustDeedText;
    }

    // Gets Trust deed plaintext
    private String getTrustDeedText() {
        return this.trustDeedText;
    }

}
