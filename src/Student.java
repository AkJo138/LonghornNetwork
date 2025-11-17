import java.util.*;

/**
 * Abstract class that ontains all information for a singular Student
 * UniversityStudent extends this class
 */
public abstract class Student {

    /**student's name. */
    protected String name;

    /**student's age */
    protected int age;

    /** student's gender*/
    protected String gender;

    /** Academic year of student*/
    protected int year;

    /** Student's intended major*/
    protected String major;

    /** Student's GPA*/
    protected double gpa;

    /** What roommates the student prefers*/
    protected List<String> roommatePreferences;

    /** STring list of previous company that student has worked for */
    protected List<String> previousInternships;

    /**
     * Returns connection strength between this instance of student and another student
     *
     * @param other other student to conduct comparison
     * @return integer that shows how strong the connection is
     */
    public abstract int calculateConnectionStrength(Student other);
}
