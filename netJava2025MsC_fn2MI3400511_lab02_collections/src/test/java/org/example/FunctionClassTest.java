package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FunctionClassTest {

    @Test
    void testSortStudentsByGradeAndName() {
        final Student student1= new Student("Nikolay", 533, 5.5);
        final Student student2= new Student("Niya", 534, 6.0);
        final Student student3= new Student("Dobri", 535, 4.5);
        final Student student4= new Student("Luiza", 536, 5.0);
        final Student student5= new Student("Sibel", 537, 5.5);

        final FunctionClass functionClass = new FunctionClass(new ArrayList<>(List.of(student1, student2, student3, student4, student5)));

        assertEquals("[Student{name='Niya', facultyNumber=534, averageGrade=6.0}, " +
                "Student{name='Nikolay', facultyNumber=533, averageGrade=5.5}, " +
                "Student{name='Sibel', facultyNumber=537, averageGrade=5.5}, " +
                "Student{name='Luiza', facultyNumber=536, averageGrade=5.0}, " +
                "Student{name='Dobri', facultyNumber=535, averageGrade=4.5}]", functionClass.sortStudentsByGradeAndName().toString());
    }

    @Test
    void testAddStudent() {
        final Student student1= new Student("Nikolay", 533, 5.5);
        final Student student2= new Student("Niya", 534, 6.0);

        final FunctionClass functionClass = new FunctionClass(new ArrayList<>(List.of(student1, student2)));

        final Student student6 = new Student("Nikolay", 528, 5.5);
        functionClass.addStudent(student6);

        assertEquals("[Student{name='Nikolay', facultyNumber=533, averageGrade=5.5}, " +
                "Student{name='Niya', facultyNumber=534, averageGrade=6.0}, " +
                "Student{name='Nikolay', facultyNumber=528, averageGrade=5.5}]", functionClass.toString());
    }

    @Test
    void testEmptyList() {
        final FunctionClass functionClass = new FunctionClass();

        assertEquals("[]", functionClass.toString());
    }
}
