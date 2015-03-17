package edu.upc.essi.sushitos.ltigdocstool.web.forms;

/**
 * SubmitDocumentForm class
 * Spring form for document submission
 * 
 * @author ngalanis
 * @author jpiguillem
 *
 */
public class SubmitDocumentForm {
    
    private String docId;
    
    public SubmitDocumentForm(){
        
    }
    
    public SubmitDocumentForm(String newDocId){
        docId = newDocId;
    }

    public void setDocId(String docId) {

        this.docId = docId;
    }

    public String getDocId() {

        return docId;
    }

}
