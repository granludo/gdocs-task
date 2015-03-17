package edu.upc.essi.sushitos.ltigdocstool.document;

import java.net.URL;

/**
 * Document Class It represent a Document inside GDocs4Learning Activity
 * 
 * @author ngalanis
 * @author jpiguillem
 * 
 */
public class Document {

    private Integer id;
    private String docId;
    private String org;
    private String contextLabel;
    private String resourceLabel;
    private String resourceId;
    private String userId;
    private String sourcedId;
    private boolean submitted;
    private boolean graded;
    private String pdfId;
    
    private String title;
    private URL link;
    private String timeCreated;
    private String timeModified;
    
    public Document(Integer id, String docId, String org, String contextLabel,
            String resourceLabel, String resourceId, String userId, String sourcedId,
            boolean submitted, boolean graded, String pdfId) {

        // TODO implement this method
        this.id = id;
        this.docId = docId;
        this.org = org;
        this.contextLabel = contextLabel;
        this.resourceLabel = resourceLabel;
        this.resourceId = resourceId;
        this.userId = userId;
        this.setSourcedId(sourcedId);
        this.submitted = submitted;
        this.graded = graded;
        this.pdfId = pdfId;
    }

    public Integer getId() {

        return id;
    }

    public String getDocId() {

        return docId;
    }

    public String getOrg() {

        return org;
    }

    public String getContextLabel() {

        return contextLabel;
    }

    public String getResourceLabel() {

        return resourceLabel;
    }

    public String getResourceId() {

        return resourceId;
    }

    public String getUserId() {

        return userId;
    }

    public boolean getSubmitted() {

        return submitted;
    }

    public void setSubmitted() {

        this.submitted = true;
    }

    public boolean getGraded() {

        return graded;
    }

    public void setGraded() {

        this.graded = true;
    }

    public void unsetGraded() {

        this.graded = false;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getTitle() {

        return title;
    }

    public void setLink(URL link) {

        this.link = link;
    }

    public URL getLink() {

        return link;
    }

    public void setSourcedId(String sourcedId) {

        this.sourcedId = sourcedId;
    }

    public String getSourcedId() {

        return sourcedId;
    }

    public void setTimeCreated(String timeCreated) {

        this.timeCreated = timeCreated;
    }

    public String getTimeCreated() {

        return timeCreated;
    }

    public void setTimeModified(String timeModified) {

        this.timeModified = timeModified;
    }

    public String getTimeModified() {

        return timeModified;
    }

    public String getPdfId() {

        return pdfId;
    }

}
