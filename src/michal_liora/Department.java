package michal_liora;

import java.util.Arrays;
import java.util.Objects;

public class Department {
    private String name;
    private int studentCount;
    private Lecturer[] lecturers;
    private int lecturerCount;

    public Department(String name, int studentCount) {
        setName(name);
        setStudentCount(studentCount);
        setLecturers(new Lecturer[1]);
        setLecturerCount(0);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStudentCount(int studentCount) {
        if (studentCount >= 0) {
            this.studentCount = studentCount;
        }
    }

    public void setLecturers(Lecturer[] lecturers) {
        this.lecturers = lecturers;
    }

    public void setLecturerCount(int lecturerCount) {
        this.lecturerCount = lecturerCount;
    }

    public String getName() {
        return name;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public Lecturer[] getLecturers() {
        return lecturers;
    }

    public int getLecturerCount() {
        return lecturerCount;
    }

    @Override
    public String toString() {
        return "{" +
                "name=" + name +
                ", studentCount=" + studentCount +
                ", lecturers=" + College.lecturerNamesToString(lecturers,lecturerCount) +
                "}";
    }

    @Override
    public boolean equals(Object toCompare) {
        if (toCompare == null || getClass() != toCompare.getClass()) return false;
        Department otherDepartment = (Department) toCompare;
        return studentCount == otherDepartment.studentCount &&
                lecturerCount == otherDepartment.lecturerCount &&
                name.equals(otherDepartment.name) &&
                College.LecturerArrEqualsByName(lecturers, otherDepartment.lecturers, lecturerCount);
    }
}