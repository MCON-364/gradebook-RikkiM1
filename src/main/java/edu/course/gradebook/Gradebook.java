package edu.course.gradebook;

import java.util.*;

public class Gradebook {

    private final Map<String, List<Integer>> gradesByStudent = new HashMap<>();
    private final Deque<UndoAction> undoStack = new ArrayDeque<>();
    private final LinkedList<String> activityLog = new LinkedList<>();

    public Optional<List<Integer>> findStudentGrades(String name) {
        return Optional.ofNullable(gradesByStudent.get(name));
    }

    public boolean addStudent(String name) {
        if (gradesByStudent.containsKey(name)) {
            return false;
        }
        gradesByStudent.put(name, new ArrayList<>());
        activityLog.push("added student " + name);
        return true;

    }

    public boolean addGrade(String name, int grade) {
        if (gradesByStudent.containsKey(name)) {
            gradesByStudent.get(name).add(grade);

            undoStack.push(new UndoAction() {
                @Override
                public void undo(Gradebook gradebook) {

                }
            });


            activityLog.push("Added grade " + grade + " for " + name);
            return true;
        }
        return false;
    }

    public boolean removeStudent(String name) {
        if (gradesByStudent.containsKey(name)) {
            gradesByStudent.get(name).remove(name);
            undoStack.push(new UndoAction() {
                @Override
                public void undo(Gradebook gradebook) {
                }
            });
            activityLog.push("removed student " + name);
            return true;
        }
        return false;
    }

    public Optional<Double> averageFor(String name) {
        double grade = 0;
        if (gradesByStudent.containsKey(name)) {
            for (String student : gradesByStudent.keySet()) {
                grade += gradesByStudent.get(student).get((int) grade);
            }

            grade /= gradesByStudent.size();
            return Optional.ofNullable(grade);
        }
        return Optional.empty();
    }

    public Optional<List<Integer>> letterGradeFor(String name) {
        if (gradesByStudent.containsKey(name)) {
            var x = gradesByStudent.get(averageFor(name));
            switch (x) {
                case 90, 100 -> System.out.print("A");
                case 80, 89 -> System.out.print("B");
                case 70, 79 -> System.out.print("C");
                case 60, 69 -> System.out.print("D");
                default -> System.out.print("F");

            }
            System.out.println(": " + x);
            return Optional.ofNullable(x);
        }


        return Optional.empty();
    }

    public Optional<Double> classAverage()
    {

    }
    public boolean undo() {
        throw new UnsupportedOperationException();
    }

    public List<String> recentLog(int maxItems) {
        throw new UnsupportedOperationException();
    }
}
