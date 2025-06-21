package michal_liora;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Department {
    private String name;
    private int studentCount;
    private ArrayList<Lecturer> lecturers;

    public Department(String name, int studentCount) {
        setName(name);
        setStudentCount(studentCount);
        setLecturers(new ArrayList<>());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStudentCount(int studentCount) {
        if (studentCount >= 0) {
            this.studentCount = studentCount;
        }
    }

    public void setLecturers(ArrayList<Lecturer> lecturers) {
        this.lecturers = lecturers;
    }

    public String getName() {
        return name;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public ArrayList<Lecturer> getLecturers() {
        return lecturers;
    }

    public void addLecturer(Lecturer lecturer){
        lecturers.add(lecturer);
    }

    @Override
    public String toString() {
        return "{" +
                "name=" + name +
                ", studentCount=" + studentCount +
                ", lecturers=" + College.lecturerNamesToString(lecturers) +
                "}";
    }

    @Override
    public boolean equals(Object toCompare) {
        if (toCompare == null || getClass() != toCompare.getClass()) return false;
        Department otherDepartment = (Department) toCompare;
        return studentCount == otherDepartment.studentCount &&
                name.equals(otherDepartment.name) &&
                College.LecturerArrEqualsByName(lecturers, otherDepartment.lecturers);
    }
}