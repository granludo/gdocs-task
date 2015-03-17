package edu.upc.essi.sushitos.imsglc.lis.rolemanager;

/**
 * RoleManager class
 * 
 * @author ngalanis
 * @author jpiguillem
 * 
 */
public class RoleManager {
    private static String LearnerRoles[] = {
            "Student", "urn:lti:insrole:ims/lis/Student",
            "Learner", "urn:lti:insrole:ims/lis/Learner",
            "Alumni", "urn:lti:insrole:ims/lis/Alumni",
            "ProspectiveStudent", "urn:lti:insrole:ims/lis/ProspectiveStudent"
    };

    private static String TeacherRoles[] = {
            "Instructor", "urn:lti:insrole:ims/lis/Instructor",
            "Mentor", "urn:lti:insrole:ims/lis/Mentor",
            "Faculty", "urn:lti:insrole:ims/lis/Faculty",
            "Administrator", "urn:lti:insrole:ims/lis/Administrator"
    };

    public static boolean isLearner(String role) {
        for (int i = 0; i < LearnerRoles.length; i++) {
            if (LearnerRoles[i].equals(role)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTeacher(String role) {
        for (int i = 0; i < TeacherRoles.length; i++) {
            if (TeacherRoles[i].equals(role)) {
                return true;
            }
        }
        return false;
    }
}
