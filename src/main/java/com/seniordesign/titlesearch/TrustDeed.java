/*
 * Trust Deed Class:
 * Holds the data about an individual Trust Deed
 * Book #, Page #, Grantor, Grantee, Ownership Chain
 */

public class TrustDeed {

    public String bookNumber, pageNumber;
    public String nameOfOwner;
    public String text;
    public Boolean isReleased;
    public byte[] release;
    public byte[] trustDeedPDF;
    public String trustDeedText;
   

    public TrustDeed(String bookNumber, int pageNumber, String nameOfOwner ) {
        this.bookNumber = bookNumber;
        this.pageNumber = pageNumber;
        this.nameOfOwner = nameOfOwner;
    }

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
