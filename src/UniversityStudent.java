import java.util.*;

/**
 * Extends the Student abstract class and implements abstract methods and additional information
 * specific to UniversityStudent
 */
public class UniversityStudent extends Student {

    protected UniversityStudent roommate;
    protected List<UniversityStudent> friends;
    protected List<String> chatHistory;

    public UniversityStudent(String name, int age, String gender, int year, String major, double gpa,
                             List<String> roommatePreferences, List<String> previousInternships) {

        this.name = name;
        this.age = age;
        this.gender = gender;
        this.year = year;
        this.major = major;
        this.gpa = gpa;
        this.roommatePreferences = roommatePreferences;
        this.previousInternships =  previousInternships;
        this.friends = new ArrayList<>();
        this.chatHistory = new ArrayList<>();
        this.roommate = null;
    }

    @Override
    public int calculateConnectionStrength(Student other) {
        UniversityStudent o = (UniversityStudent) other;
        int strength = 0;

        if (this.age == o.age) {
            strength += 1;
        }
        if (this.major != null && this.major.equalsIgnoreCase(o.major)) {
            strength += 2;
        }
        for (String internship : this.previousInternships) {
            if (internship == null || internship.equalsIgnoreCase("None")) continue;

            for (String otherIntern : o.previousInternships) {
                if (internship.equalsIgnoreCase(otherIntern)) {
                    strength += 3;
                }
            }
        }
        if (this.roommate != null && this.roommate.equals(o)) {
            strength += 4;
        }

        return strength;
    }

    public UniversityStudent getRoommate() {
        return this.roommate;
    }

    public List<String> getRoommatePreferences() {
        return roommatePreferences;
    }

    public void setRoommate(UniversityStudent roommate) {
        this.roommate = roommate;
    }

    public List<String> getPreviousInternships() {
        return previousInternships;
    }

    public synchronized void addFriend(UniversityStudent friend) {
        if (friend != null && !friends.contains(friend)) {
            friends.add(friend);
        }
    }

    public synchronized void addToChatHistory(String message) {
        if (message != null) {
            chatHistory.add(message);
        }
    }
    public String getName() {
        return this.name;
    }

}
