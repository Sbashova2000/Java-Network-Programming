package org.example;

public class Student {

    private String name;
    private int facultyNumber;
    private double averageGrade;

    public Student(String name, int facultyNumber, double averageGrade) {
        this.name = name;
        this.facultyNumber = facultyNumber;
        this.averageGrade = averageGrade;
    }

    public Student() {
        this.name = "";
        this.facultyNumber = 0;
        this.averageGrade = 0.0;
    }

    public String getName() {
        return name;
    }

    public int getFacultyNumber() {
        return facultyNumber;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFacultyNumber(int facultyNumber) {
        this.facultyNumber = facultyNumber;
    }

    public void setAverageGrade(double averageGrade) {
        this.averageGrade = averageGrade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", facultyNumber=" + facultyNumber +
                ", averageGrade=" + averageGrade +
                '}';
    }
}
