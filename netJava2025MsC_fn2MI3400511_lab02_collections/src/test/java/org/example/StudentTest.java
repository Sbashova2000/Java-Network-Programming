package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentTest {

    @Test
    void testStudent() {
        final Student student = new Student("Nikolay", 533, 5.5);

        assertEquals("Nikolay", student.getName());
        assertEquals(533, student.getFacultyNumber());
        assertEquals(5.5, student.getAverageGrade());
    }

    @Test
    void testStudentDefaultConstructor() {
        final Student student = new Student();

        assertEquals("", student.getName());
        assertEquals(0, student.getFacultyNumber());
        assertEquals(0.0, student.getAverageGrade());
    }

    @Test
    void testStudentSetters() {
        final Student student = new Student();
        student.setName("Nikolay");
        student.setFacultyNumber(533);
        student.setAverageGrade(5.5);

        assertEquals("Nikolay", student.getName());
        assertEquals(533, student.getFacultyNumber());
        assertEquals(5.5, student.getAverageGrade());
    }

    @Test
    void testStudentToString() {
        final Student student = new Student("Nikolay", 533, 5.5);

        assertEquals("Student{name='Nikolay', facultyNumber=533, averageGrade=5.5}", student.toString());
    }

}