import edu.course.gradebook.Gradebook;
import edu.course.gradebook.UndoAction;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Tests {
    private final Map<String, List<Integer>> gradesByStudent = new HashMap<>();
    private final Deque<UndoAction> undoStack = new ArrayDeque<>();
    private final LinkedList<String> activityLog = new LinkedList<>();
    Gradebook gb = new Gradebook();

    @Test
    public void addStudentTest() {

        gb.addStudent("Chani");
        assertEquals("Chani", new ArrayList<>(), "Chani shoudld be added as a stduent");
    }

    @Test
    public void addGradeTest() {
        gb.addGrade("Chani", 97);
        assertEquals("Chani", 97, "Chani shoudld be added with grade 97");
    }

    @Test
    public void removeStudentTest() {
        gb.removeStudent("Chani");
        assertEquals("Chani", "Chani should have been removed");
    }
    @Test
    public void averageForTest() {
        gb.averageFor("Chani");
        assertEquals("97", "avergae should be 97");
    }
}
