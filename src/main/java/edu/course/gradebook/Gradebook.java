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

                    gradesByStudent.get(name).remove(Integer.valueOf(grade));
                }
            });


            activityLog.push("Added grade " + grade + " for " + name);
            return true;
        }
        return false;
    }

    public boolean removeStudent(String name) {
        if (gradesByStudent.containsKey(name)) {
            List<Integer> removedGrades = new ArrayList<>(gradesByStudent.get(name));
            gradesByStudent.remove(name);
            undoStack.push(new UndoAction() {
                @Override
                public void undo(Gradebook gradebook) {
                    gradesByStudent.put(name,removedGrades);
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
        if (grades == null || grades.isEmpty()) return Optional.empty();
        if (gradesByStudent.containsKey(name)) {
            for (Integer student : grades) {
                grade += student;
            }

           double average= grade/grades.size();
            return Optional.of(average);
        }
        return Optional.empty();
    }



    public Optional<String> letterGradeFor(String name) {

            Optional<Double> avg = averageFor(name);
        if (avg.isEmpty()) return Optional.empty();
            int x = avg.get().intValue();

            String grade = switch (x/10) {
                case 10,9 -> "A";
                case 8-> "B";
                case 7 -> "C";
                case 6-> "D";
                default -> "F";
            };
        return Optional.of(grade);

    }







    public Optional<Double> classAverage() {
        double grade = 0;
        double cl = 0;
        for (List<Integer> s : gradesByStudent.values()) {

            for (Integer student : s) {
                grade += student;
                cl++;
            }
        }
        if (cl ==0 ) return Optional.empty();
            double average = grade / cl;
            return Optional.of(average);

    }




        public boolean undo () {
            if(undoStack.isEmpty()) {
                return false;
            }
        UndoAction action =undoStack.pop();
            try {
                action.undo(this); // make sure undo() safely handles empty lists or missing elements
                activityLog.push("undo");
                return true;
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Undo failed: " + e.getMessage());
                return false;
            }
        }






        public List<String> recentLog ( int maxItems)
        {
            List<String> result = new ArrayList<>();

            int count = 0;
            ListIterator<String> it = activityLog.listIterator(activityLog.size());

            while (it.hasPrevious() && count < maxItems) {
                result.add(it.previous());
                count++;
            }

            return result;
        }
    }
