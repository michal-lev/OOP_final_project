package michal_liora;

import michal_liora.Enums.degreeLevel;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class Committee implements Comparable<Committee>, Serializable {
    private String name;
    private Lecturer chair;
    private Set<Lecturer> members;
    private degreeLevel memberType;

    public Committee(String name, Lecturer chair, String MemberType) {
        setName(name);
        setChair(chair);
        setMembers(new HashSet<>());
        setMemberType(degreeLevel.valueOf(MemberType.toUpperCase()));
    }

    public Committee(Committee toCopy){
        setName("new-" + toCopy.getName());
        setChair(toCopy.getChair());
        setMembers(new HashSet<>(toCopy.members));
        setMemberType(degreeLevel.valueOf(toCopy.getMemberType()));
    }

    public String getMemberType() {
        return memberType.toString();
    }

    public void setMemberType(degreeLevel memberType) {
        this.memberType = memberType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChair(Lecturer lecturer) {
        this.chair = lecturer;
    }

    public void setMembers(Set<Lecturer> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public Lecturer getChair() {
        return chair;
    }

    public Set<Lecturer> getMembers() {
        return members;
    }

    public void addMember(Lecturer newLecturer){
        members.add(newLecturer);
    }

    public void removeMember(Lecturer memberToRemove){
        members.remove(memberToRemove);
    }

    public int getTotalArticleCount(){
        Iterator<Lecturer> it = members.iterator();
        Lecturer currMember;
        int totalNumArticles = 0;

        while(it.hasNext()) {
            currMember = it.next();
            if(currMember instanceof Doctor){
                totalNumArticles += ((Doctor)currMember).articles.size();
            }
        }
        return totalNumArticles;
    }

    @Override
    public String toString() {
        return "{" +
                "name=" + name +
                ", chair=" + chair.getName() +
                ", memberType=" + memberType.toString() +
                ", members=" + getLecturerNamesWithIterator(members) +
                "}";
    }
    private String getLecturerNamesWithIterator(Set<Lecturer> lecturers) { //put in College, refer to from Department
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
    public boolean equals(Object toCompare) {
        if (toCompare == null || toCompare.getClass() != getClass())
            return false;
        Committee otherCommittee = (Committee) toCompare;
        return name.equals(otherCommittee.name) &&
                chair.equals(otherCommittee.chair) &&
                memberType.equals(otherCommittee.memberType) &&
                members.equals(otherCommittee.members); // Check that Lecturer has HashCode()
    }

    @Override
    public int compareTo(Committee other) {
        int totalArticleCount1 = getTotalArticleCount(), totalArticleCount2 = other.getTotalArticleCount();

        if (totalArticleCount1 > totalArticleCount2) {
            return 1;
        }
        if (totalArticleCount2 > totalArticleCount1) {
            return -1;
        }
        return 0;
    }
}
