package org.example;

import java.util.ArrayList;
import java.util.List;

public class FunctionClass {
    private final List<Student> studentList;

    public FunctionClass() {
        this.studentList = new ArrayList<>();
    }

    public FunctionClass(List<Student> studentList) {
        this.studentList = studentList;
    }

    public void addStudent(Student student) {
        studentList.add(student);
    }

    public void printStudents() {
        for (Student student : studentList) {
            System.out.println(student);
        }
    }

    public List<Student> sortStudentsByGradeAndName() {
        studentList.sort((s1, s2) -> {
            int gradeComparison = Double.compare(s2.getAverageGrade(), s1.getAverageGrade());
            if (gradeComparison != 0) {
                return gradeComparison;
            }
            // Ако средният успех е еднакъв, сортиране по име в азбучен ред
            return s1.getName().compareTo(s2.getName());
        });
        // Извеждане на сортирания списък
        printStudents();

        return studentList;
    }

    @Override
    public String toString() {
        return studentList.toString();
    }

    public static void main(String[] args) {

        final Student student1= new Student("Nikolay", 533, 5.5);
        final Student student2= new Student("Niya", 534, 6.0);
        final Student student3= new Student("Dobri", 535, 4.5);
        final Student student4= new Student("Luiza", 536, 5.0);
        final Student student5= new Student("Sibel", 537, 5.5);

        List<Student> studentList = new ArrayList<>(List.of(student1, student2, student3, student4,student5));

        final FunctionClass functionClass = new FunctionClass(studentList);

        System.out.println("Списък на студентите:");
        functionClass.printStudents();

        System.out.println();
        System.out.println("Списък на студентите, сортирани по име и среден успех:");
        functionClass.sortStudentsByGradeAndName();

    }
}
