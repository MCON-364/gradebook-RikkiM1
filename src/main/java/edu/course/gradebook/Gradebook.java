package edu.course.gradebook;

import java.util.*;

public class Gradebook {

    private final Map<String, List<Integer>> gradesByStudent = new HashMap<>();
    private final Deque<UndoAction> undoStack = new ArrayDeque<>();
    private final LinkedList<String> activityLog = new LinkedList<>();
    Gradebook gb;

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

                    gradesByStudent.get(name).remove(grade);
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
                    gradesByStudent.put(name, new ArrayList<>());
                }
            });
            activityLog.push("removed student " + name);
            return true;
        }
        return false;
    }

    public Optional<Double> averageFor(String name) {
        double grade = 0;

        List<Integer> grades = gradesByStudent.get(name);
        if (gradesByStudent.containsKey(name)) {
            for (Integer student : grades) {
                grade += student;
            }

           double average= grade/grades.size();
            return Optional.of(average);
        }
        return Optional.empty();
    }

    public Optional<Integer> letterGradeFor(String name) {
        if (gradesByStudent.containsKey(name)) {
            Optional<Double> avg = gb.averageFor(name);
            int x = avg.get().intValue();

            String grade = switch (x) {
                case 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100 -> "A";
                case 80, 81, 82, 83, 84, 85, 86, 87, 88, 89 -> "B";
                case 70, 71, 72, 73, 74, 75, 76, 77, 78, 79 -> "C";
                case 60, 61, 62, 63, 64, 65, 66, 67, 68, 69 -> "D";
                default -> {
                    System.out.println("you failed!");
                    yield "F";
                }
            };
            return Optional.of(grade.length());
        }
        return Optional.empty();
    }

    public Optional<Double> classAverage() {
        double grade = 0;
        double cl = 0;
        for (String s : gradesByStudent.keySet()) {
            List<Integer> sg = gradesByStudent.get(s);
            for (Integer student : sg) {
                grade += student;
                cl++;
            }
            double average = grade / cl;
            return Optional.of(average);
        }
        return Optional.empty();
    }

        public boolean undo () {
            if(undoStack.isEmpty()) {
                return false;
            }
        UndoAction action =undoStack.pop();
            action.undo(this);
            activityLog.push("undo");
            return true;
        }

        public List<String> recentLog ( int maxItems){
            throw new UnsupportedOperationException();
        }
    }
