package edu.upc.essi.sushitos.imsglc.basiclti.httpserver;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.oauth.OAuthException;
import edu.upc.essi.sushitos.imsglc.basiclti.security.Security;
import edu.upc.essi.sushitos.ltigdocstool.database.NeedCreateException;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.UnexistantOrganizationException;

/**
 * BLTILaunchRequest class
 * It represents the Basic LTI launch request.
 * 
 * @author ngalanis
 * @author jpiguillem
 *
 */
public class BLTILaunchRequest {

    private String ltiMessageType;
    private String ltiVersion;

    private String resourceLinkId;
    private String resourceLinkTitle;
    private String resourceLinkDescription;
    private String resourceLinkContent;

    private String userId;
    private String userImage;
    private String roles;

    private String lisPersonNameGiven;
    private String lisPersonNameFamily;
    private String lisPersonNameFull;
    private String lisPersonContactEmailPrimary;

    private String contextId;
    private String contextType;
    private String contextTitle;
    private String contextLabel;

    private String launchPresentationLocale;
    private String launchPresentationDocumentTarget;
    private String launchPresentationWidth;
    private String launchPresentationHeight;
    private String launchPresentationReturnURL;

    private String toolConsumerInstanceGuid;
    private String toolConsumerInstanceName;
    private String toolConsumerInstanceDescription;
    private String toolConsumerInstanceURL;
    private String toolConsumerInstanceContactEmail;

    private String oauthConsumerKey;
    private String oauthSignature;
    private String oauthSignatureMethod;
    private String oauthNonce;
    private String oauthTimestamp;
    private String oauthVersion;

    private String lisResultSourcedid;
    private String extImsLisBasicOutcomeUrl;
    private String extImsLisMembershipsId;
    private String extImsLisMembershipsUrl;
    private String extImsLtiToolSettingId;
    private String extImsLtiToolSettingUrl;
    private String extImsLtiTool;

    private Map<String, String> custom;

    public BLTILaunchRequest(HttpServletRequest request) throws OAuthException, ClassNotFoundException, SQLException, NeedCreateException, IOException, UnexistantOrganizationException {
        
        Security.validateRequest(request);

        setLtiMessageType(request.getParameter("lti_message_type"));
        setLtiVersion(request.getParameter("lti_version"));

        setResourceLinkId(request.getParameter("resource_link_id"));
        setResourceLinkTitle(request.getParameter("resource_link_title"));
        setResourceLinkDescription(request.getParameter("resource_link_description"));
        setResourceLinkContent(request.getParameter("resource_link_content"));

        setUserId(request.getParameter("user_id"));
        setUserImage(request.getParameter("user_image"));
        setRoles(request.getParameter("roles"));

        setLisPersonNameGiven(request.getParameter("lis_person_name_given"));
        setLisPersonNameFamily(request.getParameter("lis_person_name_family"));
        setLisPersonNameFull(request.getParameter("lis_person_name_full"));
        setLisPersonContactEmailPrimary(request.getParameter("lis_person_contact_email_primary"));

        setContextId(request.getParameter("context_id"));
        setContextType(request.getParameter("context_type"));
        setContextTitle(request.getParameter("context_title"));
        setContextLabel(request.getParameter("context_label"));

        setLaunchPresentationLocale(request.getParameter("launch_presentation_locale"));
        setLaunchPresentationDocumentTarget(request
                .getParameter("launch_presentation_document_target"));
        setLaunchPresentationWidth(request.getParameter("launch_presentation_width"));
        setLaunchPresentationHeight(request.getParameter("launch_presentation_height"));
        setLaunchPresentationReturnURL(request.getParameter("launch_presentation_return_url"));

        setToolConsumerInstanceGuid(request.getParameter("tool_consumer_instance_guid"));
        setToolConsumerInstanceName(request.getParameter("tool_consumer_instance_name"));
        setToolConsumerInstanceDescription(request
                .getParameter("tool_consumer_instance_description"));
        setToolConsumerInstanceURL(request.getParameter("tool_consumer_instance_url"));
        setToolConsumerInstanceContactEmail(request
                .getParameter("tool_consumer_instance_contact_email"));

        setOauthConsumerKey(request.getParameter("oauth_consumer_key"));
        setOauthSignatureMethod(request.getParameter("oauth_signature_method"));
        setOauthTimestamp(request.getParameter("oauth_timestamp"));
        setOauthNonce(request.getParameter("oauth_nonce"));
        setOauthSignature(request.getParameter("oauth_signature"));
        setOauthVersion(request.getParameter("oauth_version"));

        setLisResultSourcedid(request.getParameter("lis_result_sourcedid"));
        setExtImsLisBasicOutcomeUrl(request.getParameter("ext_ims_lis_basic_outcome_url"));
        setExtImsLisMembershipsId(request.getParameter("ext_ims_lis_memberships_id"));
        setExtImsLisMembershipsUrl(request.getParameter("ext_ims_lis_memberships_url"));
        setExtImsLtiToolSettingId(request.getParameter("ext_ims_lti_tool_setting_id"));
        setExtImsLtiToolSettingUrl(request.getParameter("ext_ims_lti_tool_setting_url"));
        setExtImsLtiTool(request.getParameter("ext_ims_lti_tool_setting"));

        setCustom(request);
        
    }

    private void setLtiMessageType(String ltiMessageType) {

        this.ltiMessageType = ltiMessageType;
    }

    public String getLtiMessageType() {

        return ltiMessageType;
    }

    private void setLtiVersion(String ltiVersion) {

        this.ltiVersion = ltiVersion;
    }

    public String getLtiVersion() {

        return ltiVersion;
    }

    private void setResourceLinkId(String resourceLinkId) {

        this.resourceLinkId = resourceLinkId;
    }

    public String getResourceLinkId() {

        return resourceLinkId;
    }

    private void setResourceLinkTitle(String resourceLinkTitle) {

        this.resourceLinkTitle = resourceLinkTitle;
    }

    public String getResourceLinkTitle() {

        return resourceLinkTitle;
    }

    private void setResourceLinkDescription(String resourceLinkDescription) {

        this.resourceLinkDescription = resourceLinkDescription;
    }

    public String getResourceLinkDescription() {

        return resourceLinkDescription;
    }

    private void setResourceLinkContent(String resourceLinkContent) {

        this.resourceLinkContent = resourceLinkContent;
    }

    public String getResourceLinkContent() {

        return resourceLinkContent;
    }

    private void setUserId(String userId) {

        this.userId = userId;
    }

    public String getUserId() {

        return userId;
    }

    private void setRoles(String roles) {

        this.roles = roles;
    }

    public String getRoles() {

        return roles;
    }

    private void setUserImage(String userImage) {

        this.userImage = userImage;
    }

    public String getUserImage() {

        return userImage;
    }

    private void setLisPersonNameGiven(String lisPersonNameGiven) {

        this.lisPersonNameGiven = lisPersonNameGiven;
    }

    public String getLisPersonNameGiven() {

        return lisPersonNameGiven;
    }

    private void setLisPersonNameFamily(String lisPersonNameFamily) {

        this.lisPersonNameFamily = lisPersonNameFamily;
    }

    public String getLisPersonNameFamily() {

        return lisPersonNameFamily;
    }

    private void setLisPersonNameFull(String lisPersonNameFull) {

        this.lisPersonNameFull = lisPersonNameFull;
    }

    public String getLisPersonNameFull() {

        return lisPersonNameFull;
    }

    private void setLisPersonContactEmailPrimary(String lisPersonContactEmailPrimary) {

        this.lisPersonContactEmailPrimary = lisPersonContactEmailPrimary;
    }

    public String getLisPersonContactEmailPrimary() {

        return lisPersonContactEmailPrimary;
    }

    private void setContextId(String contextId) {

        this.contextId = contextId;
    }

    public String getContextId() {

        return contextId;
    }

    private void setContextType(String contextType) {

        this.contextType = contextType;
    }

    public String getContextType() {

        return contextType;
    }

    private void setContextTitle(String contextTitle) {

        this.contextTitle = contextTitle;
    }

    public String getContextTitle() {

        return contextTitle;
    }

    private void setContextLabel(String contextLabel) {

        this.contextLabel = contextLabel;
    }

    public String getContextLabel() {

        return contextLabel;
    }

    private void setLaunchPresentationLocale(String launchPresentationLocale) {

        this.launchPresentationLocale = launchPresentationLocale;
    }

    public String getLaunchPresentationLocale() {

        return launchPresentationLocale;
    }

    private void setLaunchPresentationDocumentTarget(String launchPresentationDocumentTarget) {

        this.launchPresentationDocumentTarget = launchPresentationDocumentTarget;
    }

    public String getLaunchPresentationDocumentTarget() {

        return launchPresentationDocumentTarget;
    }

    private void setLaunchPresentationWidth(String launchPresentationWidth) {

        this.launchPresentationWidth = launchPresentationWidth;
    }

    public String getLaunchPresentationWidth() {

        return launchPresentationWidth;
    }

    private void setLaunchPresentationHeight(String launchPresentationHeight) {

        this.launchPresentationHeight = launchPresentationHeight;
    }

    public String getLaunchPresentationHeight() {

        return launchPresentationHeight;
    }

    private void setLaunchPresentationReturnURL(String launchPresentationReturnURL) {

        this.launchPresentationReturnURL = launchPresentationReturnURL;
    }

    public String getLaunchPresentationReturnURL() {

        return launchPresentationReturnURL;
    }

    private void setToolConsumerInstanceGuid(String toolConsumerInstanceGuid) {

        this.toolConsumerInstanceGuid = toolConsumerInstanceGuid;
    }

    public String getToolConsumerInstanceGuid() {

        return toolConsumerInstanceGuid;
    }

    private void setToolConsumerInstanceName(String toolConsumerInstanceName) {

        this.toolConsumerInstanceName = toolConsumerInstanceName;
    }

    public String getToolConsumerInstanceName() {

        return toolConsumerInstanceName;
    }

    private void setToolConsumerInstanceDescription(String toolConsumerInstanceDescription) {

        this.toolConsumerInstanceDescription = toolConsumerInstanceDescription;
    }

    public String getToolConsumerInstanceDescription() {

        return toolConsumerInstanceDescription;
    }

    private void setToolConsumerInstanceURL(String toolConsumerInstanceURL) {

        this.toolConsumerInstanceURL = toolConsumerInstanceURL;
    }

    public String getToolConsumerInstanceURL() {

        return toolConsumerInstanceURL;
    }

    private void setToolConsumerInstanceContactEmail(String toolConsumerInstanceContactEmail) {

        this.toolConsumerInstanceContactEmail = toolConsumerInstanceContactEmail;
    }

    public String getToolConsumerInstanceContactEmail() {

        return toolConsumerInstanceContactEmail;
    }

    private void setOauthConsumerKey(String oauthConsumerKey) {

        this.oauthConsumerKey = oauthConsumerKey;
    }

    public String getOauthConsumerKey() {

        return oauthConsumerKey;
    }

    private void setOauthSignature(String oauthSignature) {

        this.oauthSignature = oauthSignature;
    }

    public String getOauthSignature() {

        return oauthSignature;
    }

    private void setOauthSignatureMethod(String oauthSignatureMethod) {

        this.oauthSignatureMethod = oauthSignatureMethod;
    }

    public String getOauthSignatureMethod() {

        return oauthSignatureMethod;
    }

    private void setOauthNonce(String oauthNonce) {

        this.oauthNonce = oauthNonce;
    }

    public String getOauthNonce() {

        return oauthNonce;
    }

    private void setOauthTimestamp(String oauthTimestamp) {

        this.oauthTimestamp = oauthTimestamp;
    }

    public String getOauthTimestamp() {

        return oauthTimestamp;
    }

    private void setOauthVersion(String oauthVersion) {

        this.oauthVersion = oauthVersion;
    }

    public String getOauthVersion() {

        return oauthVersion;
    }

    private void setLisResultSourcedid(String lisResultSourcedid) {

        this.lisResultSourcedid = lisResultSourcedid;
    }

    public String getLisResultSourcedid() {

        return lisResultSourcedid;
    }

    private void setExtImsLisBasicOutcomeUrl(String extImsLisBasicOutcomeUrl) {

        this.extImsLisBasicOutcomeUrl = extImsLisBasicOutcomeUrl;
    }

    public String getExtImsLisBasicOutcomeUrl() {

        return extImsLisBasicOutcomeUrl;
    }

    private void setExtImsLisMembershipsId(String extImsLisMembershipsId) {

        this.extImsLisMembershipsId = extImsLisMembershipsId;
    }

    public String getExtImsLisMembershipsId() {

        return extImsLisMembershipsId;
    }

    private void setExtImsLisMembershipsUrl(String extImsLisMembershipsUrl) {

        this.extImsLisMembershipsUrl = extImsLisMembershipsUrl;
    }

    public String getExtImsLisMembershipsUrl() {

        return extImsLisMembershipsUrl;
    }

    private void setExtImsLtiToolSettingId(String extImsLtiToolSettingId) {

        this.extImsLtiToolSettingId = extImsLtiToolSettingId;
    }

    public String getExtImsLtiToolSettingId() {

        return extImsLtiToolSettingId;
    }

    private void setExtImsLtiToolSettingUrl(String extImsLtiToolSettingUrl) {

        this.extImsLtiToolSettingUrl = extImsLtiToolSettingUrl;
    }

    public String getExtImsLtiToolSettingUrl() {

        return extImsLtiToolSettingUrl;
    }

    private void setExtImsLtiTool(String extImsLtiTool) {

        this.extImsLtiTool = extImsLtiTool;
    }

    public String getExtImsLtiTool() {

        return extImsLtiTool;
    }

    private void setCustom(HttpServletRequest request) {

        custom = new HashMap<String, String>();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if (paramName.startsWith("custom_")) {
                String paramValue = request.getParameter(paramName);
                custom.put(paramName, paramValue);
            }
        }
    }

    public Map<String, String> getCustom() {

        return custom;
    }
}
