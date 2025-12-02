import java.io.*;
import java.util.*;

/**
 * Parses data files from filename into UniversityStudent objects
* Entire class exists for one function
 */
public class DataParser {
    /**
     * Parses students contained in input file at file location filename.
     *
     * @param filename the path to input file
     * @return list of UniversityStudent instances parsed from file
     * @throws IOException if I/O error occurs while reading the file
     */
    public static List<UniversityStudent> parseStudents(String filename) throws IOException {
        ArrayList<UniversityStudent> students = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        String name = null;
        int age = 0;
        String gender = null;
        int year = 0;
        String major = null;
        double gpa = 0.0;
        List<String> preferences = new ArrayList<>();
        List<String> internships = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.equals("Student:")) continue;
            String[] parts = line.split(":");
            if (parts.length < 2) continue;
            String key = parts[0].trim();
            String val = parts[1].trim();
            switch (key) {
                case "Name": name = val; break;
                case "Age": age = Integer.parseInt(val); break;
                case "Gender": gender = val; break;
                case "Year": year = Integer.parseInt(val); break;
                case "Major": major = val; break;
                case "GPA": gpa = Double.parseDouble(val); break;
                case "RoommatePreferences":
                    preferences = Arrays.asList(val.split(","));
                    break;
                case "PreviousInternships":
                    internships = Arrays.asList(val.split(","));
                    students.add(new UniversityStudent(name, age, gender, year, major, gpa, preferences, internships));
                    break;
            }
        }
        br.close();
        return students;
    }

}

