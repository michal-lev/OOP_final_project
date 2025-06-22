package michal_liora;

import michal_liora.Enums.degreeLevel;
import java.io.Serializable;
import java.util.ArrayList;

public class Committee implements Comparable<Committee>, Serializable {
    private String name;
    private Lecturer chair;
    private ArrayList<Lecturer> members;
    private degreeLevel memberType;

    public Committee(String name, Lecturer chair, String MemberType) {
        setName(name);
        setChair(chair);
        setMembers(new ArrayList<>());
        setMemberType(degreeLevel.valueOf(MemberType.toUpperCase()));
    }

    public Committee(Committee toCopy){
        ArrayList<Lecturer> membersCopy = (ArrayList<Lecturer>)toCopy.getMembers().clone();
        setName("new-" + toCopy.getName());
        setChair(toCopy.getChair());
        setMembers(membersCopy);
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

    public void setMembers(ArrayList<Lecturer> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public Lecturer getChair() {
        return chair;
    }

    public ArrayList<Lecturer> getMembers() {
        return members;
    }

    public void addMember(Lecturer newLecturer){
        members.add(newLecturer);
    }

    public void removeMember(Lecturer memberToRemove){
        members.remove(memberToRemove);
    }

    public int getTotalArticleCount(){
        int totalNumArticles = 0;
        for(int i = 0; i < members.size(); i++){
            Lecturer member = members.get(i);
            if(member instanceof Doctor){
                totalNumArticles += ((Doctor)member).articles.size();
            }
        }
        return totalNumArticles;
    }

    @Override
    public String toString() {
        return "{" +
                "name=" + name +
                ", chair=" + chair.getName() +
                ", member type=" + memberType.toString() +
                ", members=" + College.lecturerNamesToString(members) +
                "}";
    }

    @Override
    public boolean equals(Object toCompare) {
        if (toCompare == null || toCompare.getClass() != getClass())
            return false;
        Committee otherCommittee = (Committee) toCompare;
        return name.equals(otherCommittee.name) &&
                chair.equals(otherCommittee.chair) &&
                memberType.equals(otherCommittee.memberType) &&
                College.LecturerArrEqualsByName(members,otherCommittee.members);
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
