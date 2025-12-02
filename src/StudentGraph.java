import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing a graph for UniversityStudents and their connections
 */
public class StudentGraph {

    public static class Edge{
        UniversityStudent neighbor;
        int weight;

        public Edge(UniversityStudent neighbor, int weight){
            this.neighbor = neighbor;
            this.weight = weight;
        }

        @Override
        public String toString(){
            return "(" + neighbor.name + " ," + weight + ")";
        }
    }

    private Map<UniversityStudent, List<Edge>> graph;

    public StudentGraph(List<UniversityStudent> students) {
        graph = new HashMap<>();
        for (UniversityStudent s : students) {
            graph.put(s, new ArrayList<>());
        }
        for (int i = 0; i < students.size(); i++) {
            for (int j = i + 1; j < students.size(); j++) {
                UniversityStudent s1 = students.get(i);
                UniversityStudent s2 = students.get(j);
                int weight = s1.calculateConnectionStrength(s2);
                if (weight > 0) {
                    addEdge(s1, s2, weight);
                }
            }
        }
    }

    public void addEdge(UniversityStudent s1, UniversityStudent s2, int weight){
        graph.get(s1).add(new Edge(s2, weight));
        graph.get(s2).add(new Edge(s1, weight));
    }

    public List<Edge> getNeighbors(UniversityStudent student) {
        return graph.get(student);
    }

    public List<UniversityStudent> getAllNodes() {
        return new ArrayList<>(graph.keySet());
    }   

    public void displayGraph() {
        System.out.println("\nStudent Graph:");
        for(UniversityStudent s : graph.keySet()) {
            System.out.println(s.name + " -> " + graph.get(s));
        }
    }



}
