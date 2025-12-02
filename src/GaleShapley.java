import java.util.*;


/**
 * Based on preference, class to apply the Gale-Shipley algorithm to assign roommates
 */
public class GaleShapley {
    /**
     * Assigns roommates for the provided list of students
     *
     * @param students the list of students needing pairs
     */
    public static void assignRoommates(List<UniversityStudent> students) {
        Map<UniversityStudent, UniversityStudent> roommatePairs = new HashMap<>();
        Map<UniversityStudent, Integer> nextProposalIndex = new HashMap<>();
        Map<String, UniversityStudent> nameToStudent = new HashMap<>();

        for(UniversityStudent s : students) {
            nameToStudent.put(s.name, s);
            nextProposalIndex.put(s, 0);
        }

        Queue<UniversityStudent> freeStudents = new LinkedList<>(students);
        for (UniversityStudent s: students) {
            if (!s.roommatePreferences.isEmpty()) {
                freeStudents.offer(s);
            }
        }

        while(!freeStudents.isEmpty()){
            UniversityStudent s = freeStudents.poll();
            if(s.getRoommate() != null){
                continue;
            }

            int index = nextProposalIndex.get(s);
            if(index >= s.roommatePreferences.size()){
                continue;
            }

            String preferredName = s.roommatePreferences.get(index);
            nextProposalIndex.put(s, index + 1);
            UniversityStudent t = nameToStudent.get(preferredName);

            if (t == null){
                if (nextProposalIndex.get(s) < s.roommatePreferences.size()) {
                    freeStudents.offer(s);
                }
                continue; 
            }

            if(t.getRoommate() == null){
                roommatePairs.put(s, t);
                roommatePairs.put(t, s);
                s.setRoommate(t);
                t.setRoommate(s);
            } else{
                UniversityStudent currentPartner = t.getRoommate();
                int currentIndex = t.roommatePreferences.indexOf(currentPartner.name);
                int newIndex = t.roommatePreferences.indexOf(s.name);
                if (newIndex < currentIndex){
                    roommatePairs.put(t, s);
                    roommatePairs.put(s, t);
                    roommatePairs.remove(currentPartner);
                    freeStudents.offer(currentPartner);
                    currentPartner.setRoommate(null);
                    s.setRoommate(t);
                    t.setRoommate(s);
                } else {
                    if (nextProposalIndex.get(s) < s.roommatePreferences.size()) {
                        freeStudents.offer(s);
                    }
                }
            }
        }

        System.out.println("\nRoommate Pairings (Gale-Shapley):");
        Set<UniversityStudent> printed = new HashSet<>();
        for (UniversityStudent s : roommatePairs.keySet()){
            UniversityStudent partner = roommatePairs.get(s);
            if (!printed.contains(s) && !printed.contains(partner)){
                System.out.println(s.name + " paired with " + partner.name);
                printed.add(s);
                printed.add(partner);
            }
        }
    }
}
