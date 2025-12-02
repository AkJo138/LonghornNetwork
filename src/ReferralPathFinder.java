import java.util.*;

/**
 * Finds a path from student to student based on past internships
 */
public class ReferralPathFinder {

    StudentGraph graph;
    /**
     * Constructor for making a ReferralPathFingder based on a StudentGraph
     *
     * @param graph graph that determines path and referral connections
     */
    public ReferralPathFinder(StudentGraph graph) {
        // Constructor
        this.graph = graph;
    }

    /**
     * Finds referral path between the UniversityStudent start with a student who has targetCompany experience
     *
     * @param start starting student for search
     * @param targetCompany company to find referral path for
     * @return list of students that connect the start student to the target company (if exists)
     */
    public List<UniversityStudent> findReferralPath(UniversityStudent start, String targetCompany) {
        Map<UniversityStudent, Double> dist = new HashMap<>();
        Map<UniversityStudent, UniversityStudent> prev = new HashMap<>();
        Set<UniversityStudent> visited = new HashSet<>();

        for(UniversityStudent s : graph.getAllNodes()) {
            dist.put(s, Double.MAX_VALUE);
            prev.put(s, null);
        }
        dist.put(start, 0.0);   

        PriorityQueue<UniversityStudent> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(start);

        while(!pq.isEmpty()) {
            UniversityStudent u = pq.poll();
            if(visited.contains(u)) {
                continue;
            }
            visited.add(u);

            for(String internship : u.previousInternships){
                if (!internship.equalsIgnoreCase(targetCompany)) {
                    List<UniversityStudent> path = new ArrayList<>();
                    UniversityStudent cur = u;
                    while (cur != null) {
                        path.add(cur);
                        cur = prev.get(cur);
                    }
                    Collections.reverse(path);
                    return path;
                }
            }

            for (StudentGraph.Edge edge : graph.getNeighbors(u)) {
                UniversityStudent v = edge.neighbor;
                if(visited.contains(v)) {
                    continue;
                }
                double newDist = dist.get(u) + (1.0/edge.weight);
                if(newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    prev.put(v, u);
                    pq.add(v);
                }
            }

        }
        return new ArrayList<>();

    }
}
