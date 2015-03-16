package edu.upc.essi.sushitos.ltigdocstool.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import edu.upc.essi.sushitos.configuration.ToolConfiguration;
import edu.upc.essi.sushitos.imsglc.lis.rolemanager.DuplicatedProfessorException;
import edu.upc.essi.sushitos.imsglc.lis.rolemanager.NoProfessorException;
import edu.upc.essi.sushitos.imsglc.lis.rolemanager.Professor;
import edu.upc.essi.sushitos.ltigdocstool.activity.Activity;
import edu.upc.essi.sushitos.ltigdocstool.activity.DuplicatedActivityException;
import edu.upc.essi.sushitos.ltigdocstool.activity.UnexistantActivityException;
import edu.upc.essi.sushitos.ltigdocstool.document.Document;
import edu.upc.essi.sushitos.ltigdocstool.document.DuplicatedDocumentException;
import edu.upc.essi.sushitos.ltigdocstool.document.UnexistantDocument;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.DuplicatedOrganizationException;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.Organization;
import edu.upc.essi.sushitos.ltigdocstool.imsglc.basiclti.organization.UnexistantOrganizationException;
import edu.upc.essi.sushitos.ltigdocstool.session.UnexistantUserException;

/**
 * DatabaseManager class
 * 
 * It implements all database related functions
 * 
 * @author ngalanis
 * @author jpiguillem
 * 
 */
public class DatabaseManager {

    private Connection conn;

    private String driver;
    private String protocol;
    private String host;
    private String port;
    private String db;
    private String user;
    private String pass;
    private int version;

    public DatabaseManager() throws ClassNotFoundException, SQLException,
            NeedCreateException, IOException {

        ToolConfiguration tc = ToolConfiguration.getToolConfiguration();
        driver = tc.getProperty("database", "driver");
        protocol = tc.getProperty("database", "protocol");
        host = tc.getProperty("database", "host");
        port = tc.getProperty("database", "port");
        db = tc.getProperty("database", "db");
        user = tc.getProperty("database", "user");
        pass = tc.getProperty("database", "password");
        version = Integer.parseInt(tc.getProperty("database", "version"));

        Class.forName(driver);
        try {
            conn = DriverManager.getConnection(protocol + "://" + host + ":"
                    + port + "/" + db, user, pass);
        } catch (Exception e) {
            throw new NeedCreateException();
        }
    }

    public void createDatabase() {

        String sql;
        try {

            Statement stmt = conn.createStatement();

            sql = "CREATE TABLE `system` ("
                    + " `key` CHAR( 20 ) NOT NULL PRIMARY KEY, "
                    + " `value` CHAR( 20 ) NOT NULL" + ") ENGINE = innodb;";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO `system` VALUES ('version'," + version + ");";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE `organizations` ("
                    + " `orgId` CHAR( 100 ) NOT NULL PRIMARY KEY,"
                    + " `secret` CHAR( 100 ) NOT NULL" + ") ENGINE = innodb;";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE `users` ("
                    + " `userId` CHAR( 100 ) NOT NULL PRIMARY KEY,"
                    + " `token` CHAR( 100 ) NOT NULL" + ") ENGINE = innodb;";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE `documents` ("
                    + " `id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + " `docId` CHAR( 100 ) NOT NULL,"
                    + " `activityId` INT( 10 ) NOT NULL,"
                    + " `userId` CHAR( 100 ) NOT NULL,"
                    + " `sourcedId` CHAR( 100 ) NOT NULL,"
                    + " `submitted` INT( 1 ) NOT NULL,"
                    + " `graded` INT(1) NOT NULL,"
                    + " `pdfId` CHAR( 100 ) NOT NULL default '',"
                    + " UNIQUE KEY (`docId`, `userId`)" + ") ENGINE = innodb;";
            stmt.execute(sql);

            sql = "CREATE TABLE `activities` ("
                    + " `id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + " `orgId` CHAR( 100 ) NOT NULL,"
                    + " `contextLabel` CHAR( 100 ) NOT NULL,"
                    + " `resourceLabel` CHAR( 100 ) NOT NULL,"
                    + " `resourceId` CHAR( 100 ) NOT NULL,"
                    + " `activityType` CHAR( 100 ) NOT NULL,"
                    + " UNIQUE KEY (`orgId`, `contextLabel`, `resourceLabel`,`resourceId`)"
                    + ") ENGINE = innodb;";
            stmt.execute(sql);

            sql = "CREATE TABLE `professors` ("
                    + " `id` INT( 10 ) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                    + " `activityId` CHAR( 100 ) NOT NULL,"
                    + " `email` CHAR( 100 ) NOT NULL,"
                    + " UNIQUE KEY (`activityId`, `email`)"
                    + ") ENGINE = innodb;";
            stmt.execute(sql);

            stmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void upgradeDatabase() {

        // TODO implement this method
    }

    public boolean isDatabaseCreated() throws SQLException,
            NeedUpgradeException {

        Statement stmt = conn.createStatement();

        String sql = "SELECT * " + "FROM `system` "
                + "WHERE `key` = \"version\";";

        try {
            ResultSet res = stmt.executeQuery(sql);
            res.next();
            String ver = res.getString(2);

            if (ver.compareTo(String.valueOf(version)) < 0) {
                stmt.close();
                throw new NeedUpgradeException();
            }
        } catch (SQLException e) {
            stmt.close();
            return false;
        }
        stmt.close();
        return true;
    }

    public void addOrganization(Organization org)
            throws DuplicatedOrganizationException {

        try {
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO `organizations` (`orgId`, `secret`) VALUES ('"
                    + org.getOrgId() + "','" + org.getSecret() + "');";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new DuplicatedOrganizationException();
        }

    }

    public Organization getOrganization(String orgId) throws SQLException,
            UnexistantOrganizationException {

        String sql = "SELECT *" + " FROM `organizations`" + " WHERE orgId = '"
                + orgId + "';";

        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(sql);
        Organization org = null;

        if (!res.next()) {
            throw new UnexistantOrganizationException();
        }
        org = new Organization(res.getString("orgId"), res.getString("secret"));

        stmt.close();
        return org;

    }

    public List<Document> getDocuments(String org, String contextLabel,
            String resourceLabel, String resourceId) throws SQLException {

        List<Document> l = new LinkedList<Document>();

        String sql = "SELECT *" + " FROM `documents` d"
                + " LEFT JOIN `activities` a ON d.activityId=a.id"
                + " WHERE a.orgId = '" + org + "' AND" + " a.contextLabel = '"
                + contextLabel + "' AND" + " a.resourceLabel = '"
                + resourceLabel + "' AND" + " a.resourceId = '" + resourceId
                + "';";

        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(sql);

        while (res.next()) {
            Integer id = res.getInt("id");
            String newDocId = res.getString("docId");
            String newOrgId = res.getString("orgId");
            String newContextLabel = res.getString("contextLabel");
            String newResourceLabel = res.getString("resourceLabel");
            String newResourceId = res.getString("resourceId");
            String newUserId = res.getString("userId");
            String newSourcedId = res.getString("sourcedId");
            boolean newSubmitted = (res.getInt("submitted") == 1 ? true : false);
            boolean newGraded = (res.getInt("graded") == 1 ? true : false);
            String newPdfId = res.getString("pdfId");

            Document doc = new Document(id, newDocId, newOrgId,
                    newContextLabel, newResourceLabel, newResourceId,
                    newUserId, newSourcedId, newSubmitted, newGraded, newPdfId);

            l.add(doc);
        }
        stmt.close();
        return l;
    }

    public Document getDocumentById(Integer id) throws SQLException {
        String sql = "SELECT *" + " FROM `documents` d"
                + " LEFT JOIN `activities` a ON d.activityId=a.id"
                + " WHERE d.id = '" + id + "';";
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(sql);
        Document doc = null;

        while (res.next()) {

            String newDocId = res.getString("docId");
            String newOrgId = res.getString("orgId");
            String newContextLabel = res.getString("contextLabel");
            String newResourceLabel = res.getString("resourceLabel");
            String newResourceId = res.getString("resourceId");
            String newUserId = res.getString("userId");
            String newSourcedId = res.getString("sourcedId");
            boolean newSubmitted = (res.getInt("submitted") == 1 ? true : false);
            boolean newGraded = (res.getInt("graded") == 1 ? true : false);
            String newPdfId = res.getString("pdfId");

            doc = new Document(id, newDocId, newOrgId, newContextLabel,
                    newResourceLabel, newResourceId, newUserId, newSourcedId, newSubmitted,
                    newGraded, newPdfId);
        }
        stmt.close();
        return doc;
    }

    public List<Document> getActivityDocuments(String docId) throws SQLException {
        String sql = "SELECT *" + " FROM `documents` d"
                + " LEFT JOIN `activities` a ON d.activityId=a.id"
                + " WHERE d.docId = '" + docId + "';";
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(sql);
        List<Document> l = new LinkedList<Document>();
        while (res.next()) {

            Integer newid = res.getInt("id");
            String newOrgId = res.getString("orgId");
            String newContextLabel = res.getString("contextLabel");
            String newResourceLabel = res.getString("resourceLabel");
            String newResourceId = res.getString("resourceId");
            String newUserId = res.getString("userId");
            String newSourcedId = res.getString("sourcedId");
            boolean newSubmitted = (res.getInt("submitted") == 1 ? true : false);
            boolean newGraded = (res.getInt("graded") == 1 ? true : false);
            String newPdfId = res.getString("pdfId");

            Document doc = new Document(newid, docId, newOrgId, newContextLabel,
                    newResourceLabel, newResourceId, newUserId, newSourcedId, newSubmitted,
                    newGraded, newPdfId);

            l.add(doc);
        }
        stmt.close();
        return l;
    }

    public List<Document> getSubmittedDocuments(String org,
            String contextLabel, String resourceLabel, String resourceId)
            throws SQLException {

        List<Document> l = new LinkedList<Document>();

        String sql = "SELECT *" + " FROM `documents` d"
                + " LEFT JOIN `activities` a ON d.resourceId=a.resourceId"
                + " WHERE a.orgId = '" + org + "' AND" + " a.contextLabel = '"
                + contextLabel + "' AND" + " a.resourceLabel = '"
                + resourceLabel + "' AND" + " d.resourceId = '" + resourceId
                + "' AND" + " submitted = 1;";

        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(sql);

        while (res.next()) {
            Integer newId = res.getInt("id");
            String newDocId = res.getString("docId");
            String newOrgId = res.getString("orgId");
            String newContextLabel = res.getString("contextLabel");
            String newResourceLabel = res.getString("resourceLabel");
            String newResourceId = res.getString("resourceId");
            String newUserId = res.getString("userId");
            String newSourcedId = res.getString("sourcedId");
            boolean newGraded = (res.getInt("graded") == 1 ? true : false);
            String newPdfId = res.getString("pdfId");

            Document doc = new Document(newId, newDocId, newOrgId,
                    newContextLabel, newResourceLabel, newResourceId,
                    newUserId, newSourcedId, true, newGraded, newPdfId);

            l.add(doc);
        }
        stmt.close();
        return l;
    }

    public Document submitDocument(Integer id, String pdfId) throws SQLException {

        Statement stmt = conn.createStatement();
        String sql = "UPDATE" + " `documents`" + " SET submitted = 1, pdfId ='" + pdfId + "'"
                + " WHERE id = '" + id + "';";
        stmt.executeUpdate(sql);
        stmt.close();
        return getDocumentById(id);
    }

    public void gradeDocument(Integer id) throws SQLException {

        Statement stmt = conn.createStatement();
        String sql = "UPDATE" + " `documents`" + " SET graded = 1"
                + " WHERE id = '" + id + "';";
        stmt.executeUpdate(sql);
        stmt.close();
    }

    public void deleteDocumentGrade(Integer id) throws SQLException {

        Statement stmt = conn.createStatement();
        String sql = "UPDATE" + " `documents`" + " SET graded = 0"
                + " WHERE id = '" + id + "';";
        stmt.executeUpdate(sql);
        stmt.close();
    }

    public Document getDocument(String org, String contextLabel,
            String resourceLabel, String resourceId, String userId)
            throws SQLException, UnexistantDocument {

        Statement stmt = conn.createStatement();

        String sql = "SELECT *" + " FROM `documents` d"
                + " LEFT JOIN `activities` a ON d.activityId=a.id"
                + " WHERE a.orgId = '" + org + "' AND" + " a.contextLabel = '"
                + contextLabel + "' AND" + " a.resourceLabel = '"
                + resourceLabel + "' AND" + " a.resourceId = '" + resourceId
                + "' AND" + " d.userId = '" + userId + "';";
        System.out.println("GET DOCUMENT SQL: " + sql);
        ResultSet resDoc = stmt.executeQuery(sql);

        if (!resDoc.next()) {
            throw new UnexistantDocument();
        }

        Integer newId = resDoc.getInt("id");
        String newDocId = resDoc.getString("docId");
        String newUserId = resDoc.getString("userId");
        boolean newSubmitted = (resDoc.getInt("submitted") == 1 ? true : false);
        String newOrgId = resDoc.getString("orgId");
        String newContextLabel = resDoc.getString("contextLabel");
        String newResourceLabel = resDoc.getString("resourceLabel");
        String newResourceId = resDoc.getString("resourceId");
        String newSourcedId = resDoc.getString("sourcedId");
        boolean newGraded = (resDoc.getInt("graded") == 1 ? true : false);
        String newPdfId = resDoc.getString("pdfId");

        Document doc = new Document(newId, newDocId, newOrgId, newContextLabel,
                newResourceLabel, newResourceId, newUserId, newSourcedId, newSubmitted,
                newGraded, newPdfId);
        stmt.close();
        return doc;
    }

    public Document addDocument(String docId, String org, String contextLabel,
            String resourceLabel, String resourceId, String userId, String sourcedId,
            boolean submitted, boolean graded)
            throws DuplicatedDocumentException, UnexistantActivityException {

        String author = org + ((userId != null) ? userId : "");
        try {

            Activity act = getActivity(org, contextLabel, resourceLabel,
                    resourceId);
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO `documents` (`docId`, `activityId`, `userId`, `sourcedId`, `submitted`, `graded`) VALUES ('"
                    + docId
                    + "',"
                    + act.getId()
                    + ",'"
                    + author
                    + "','"
                    + sourcedId
                    + "',"
                    + (submitted ? "1" : "0")
                    + ","
                    + (graded ? "1" : "0")
                    + ");";
            System.out.println("ADDING DOCUMENT SQL: " + sql);
            stmt.executeUpdate(sql);

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DuplicatedDocumentException();
        }

        Document doc;
        try {
            doc = getDocument(org, contextLabel, resourceLabel, resourceId, author);
            return doc;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnexistantDocument e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public Document getDocumentByActivityId(Integer activityId) throws SQLException, UnexistantDocument {
        Statement stmt = conn.createStatement();

        String sql = "SELECT *" + " FROM `documents` d"
                    + " LEFT JOIN `activities` a ON d.activityId=a.id"
                    + " WHERE d.activityId = " + activityId + " LIMIT 1;";

        System.out.println("getDocumentByActivityId SQL Query: " + sql);
        ResultSet res = stmt.executeQuery(sql);

        if (!res.next()) {
            throw new UnexistantDocument();
        }

        Integer newId = res.getInt("id");
        String newDocId = res.getString("docId");
        String newUserId = res.getString("userId");
        boolean newSubmitted = (res.getInt("submitted") == 1 ? true : false);
        String newOrgId = res.getString("orgId");
        String newContextLabel = res.getString("contextLabel");
        String newResourceLabel = res.getString("resourceLabel");
        String newResourceId = res.getString("resourceId");
        boolean newGraded = (res.getInt("graded") == 1 ? true : false);
        String newSourcedId = res.getString("sourcedId");
        String newPdfId = res.getString("pdfId");

        Document doc = new Document(newId, newDocId, newOrgId, newContextLabel,
                newResourceLabel, newResourceId, newUserId, newSourcedId, newSubmitted,
                newGraded, newPdfId);
        stmt.close();
        return doc;
    }

    public String[] getProfessorMails(String resourceId)
            throws NoProfessorException, SQLException {

        Statement stmt = conn.createStatement();
        String sql = "SELECT email FROM `professors`"
                + " WHERE activityId = '" + resourceId + "';";
        ResultSet res = stmt.executeQuery(sql);

        String emails = "";
        while (res.next()) {
            emails += res.getString("email") + " ";
        }

        if (emails.equals("")) {
            throw new NoProfessorException();
        }

        String[] professorMails = emails.split(" ");

        stmt.close();

        return professorMails;

    }

    public void storeToken(String userId, String token)
            throws DuplicatedDocumentException {
        try {
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO `users` (`userId`, `token`) "
                    + "VALUES ('" + userId + "','" + token + "');";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DuplicatedDocumentException();
        }
    }

    public void updateToken(String userId, String token) throws SQLException {

        Statement stmt = conn.createStatement();
        String sql = "UPDATE `users` SET token = '" + token + "' "
                + "WHERE userId = '" + userId + "';";
        stmt.executeUpdate(sql);
        stmt.close();

    }

    public String getUserToken(String userId) throws SQLException,
            UnexistantUserException {

        String sql = "SELECT *" + " FROM `users`" + " WHERE userId = '"
                + userId + "';";
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(sql);

        if (!res.next())
            throw new UnexistantUserException();
        return res.getString("token");
    }

    public Activity addActivity(String orgId, String contextLabel,
            String resourceLabel, String resourceId, String activityType)
            throws DuplicatedActivityException, UnexistantActivityException,
            SQLException {
        try {
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO `activities` (`orgId`, `contextLabel`, `resourceLabel`, `resourceId`, `activityType`) "
                    + "VALUES ('"
                    + orgId
                    + "','"
                    + contextLabel
                    + "','"
                    + resourceLabel
                    + "','"
                    + resourceId
                    + "','"
                    + activityType
                    + "');";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DuplicatedActivityException();
        }
        return getActivity(orgId, contextLabel, resourceLabel, resourceId);
    }

    public Activity getActivity(String orgId, String contextLabel,
            String resourceLabel, String resourceId)
            throws UnexistantActivityException, SQLException {

        Statement stmt = conn.createStatement();

        String sqlDoc = "SELECT *" + " FROM `activities`" + " WHERE orgId = '"
                + orgId + "' AND" + " contextLabel = '" + contextLabel
                + "' AND" + " resourceLabel = '" + resourceLabel + "' AND"
                + " resourceId = '" + resourceId + "';";

        ResultSet res = stmt.executeQuery(sqlDoc);

        if (!res.next()) {
            throw new UnexistantActivityException();
        }

        Integer newId = res.getInt("id");
        String newOrgId = res.getString("orgId");
        String newContextLabel = res.getString("contextLabel");
        String newResourceLabel = res.getString("resourceLabel");
        String newResourceId = res.getString("resourceId");
        String newActivityType = res.getString("activityType");

        Activity activity = new Activity(newId, newOrgId, newContextLabel,
                newResourceLabel, newResourceId, newActivityType);
        stmt.close();
        return activity;
    }

    public List<Professor> getActivityProfessors(String resourceId) throws SQLException {
        List<Professor> l = new LinkedList<Professor>();

        Statement stmt = conn.createStatement();

        String sqlDoc = "SELECT *" + " FROM `professors`"
                + " WHERE activityId = '" + resourceId + "';";

        ResultSet res = stmt.executeQuery(sqlDoc);

        while (res.next()) {
            Integer newId = res.getInt("id");
            String newActivityId = res.getString("activityId");
            String newEmail = res.getString("email");

            Professor professor = new Professor(newId, newActivityId, newEmail);

            l.add(professor);
        }
        stmt.close();

        return l;
    }

    public Professor setActivityProfessor(String activityId, String email) throws DuplicatedProfessorException {
        try {
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO `professors` (`activityId`, `email`) "
                    + "VALUES ('"
                    + activityId
                    + "','"
                    + email
                    + "');";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DuplicatedProfessorException();
        }
        return null;
    }

    public Boolean removeActivityProfessor(String activityId, String email) {
        Boolean success = false;
        try {
            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM `professors`"
                    + "WHERE activityId = '" + activityId + "' "
                    + "AND email = '" + email + "';";
            stmt.executeUpdate(sql);
            stmt.close();
            success = true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return success;
    }
}
