package michal_liora;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.io.Serializable;

public class Department implements Serializable{
    private String name;
    private int studentCount;
    private Set<Lecturer> lecturers; // Set doesnt allow duplicates

    public Department(String name, int studentCount) {
        setName(name);
        setStudentCount(studentCount);
        setLecturers(new HashSet<>());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStudentCount(int studentCount) {
        if (studentCount >= 0) {
            this.studentCount = studentCount;
        }
    }

    public void setLecturers(Set<Lecturer> lecturers) {
        this.lecturers = lecturers;
    }

    public String getName() {
        return name;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public Set<Lecturer> getLecturers() {
        return lecturers;
    }

    // Adds a lecturer and throws an exception if they already exist
    public void addLecturer(Lecturer lecturer) throws NoDuplicatesException {
        boolean isAdded = lecturers.add(lecturer);
        if (!isAdded)
            throw new NoDuplicatesException(Enums.errorMessage.LECTURER_EXISTS.getMessage());
    }
    
    // Print names using Iterator only
    private String getLecturerNamesWithIterator() {
       Iterator<Lecturer> it = lecturers.iterator();
       StringBuilder names = new StringBuilder("[");

       while(it.hasNext()) {
           names.append(it.next().getName());
           if (it.hasNext())
               names.append(", ");
       }
       names.append("]");
       return names.toString();
    }

    @Override
    public String toString() {
        return "{" +
                "name=" + name +
                ", studentCount=" + studentCount +
                ", lecturers=" + getLecturerNamesWithIterator() + // Print with Iterator
                "}";
    }

    @Override
    public boolean equals(Object toCompare) {
        if (toCompare == null || getClass() != toCompare.getClass()) return false;
        Department otherDepartment = (Department) toCompare;
        return studentCount == otherDepartment.studentCount &&
                name.equals(otherDepartment.name) &&
                lecturers.equals(otherDepartment.lecturers); // Set checks if all items are equal
    }
}
