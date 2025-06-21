package michal_liora;

import java.util.ArrayList;

public class Committee implements Comparable<Committee> {
    private String name;
    private Lecturer chair;
    private ArrayList<Lecturer> members;

    public Committee(String name, Lecturer chair) {
        setName(name);
        setChair(chair);
        setMembers(new ArrayList<>());
    }

    public Committee(Committee toCopy){
        setName("new-" + toCopy.getName());
        setChair(toCopy.getChair());
        Lecturer[] membersCopy = new Lecturer[toCopy.members.length];
        for(int i = 0; i < toCopy.memberCount; i++){
            membersCopy[i] = toCopy.members[i];
        }
        setMembers(membersCopy);
        setMemberCount(toCopy.memberCount);
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

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
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

    public int getMemberCount() {
        return memberCount;
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
                totalNumArticles += ((Doctor)member).getArticleCount();
            }
        }
        return totalNumArticles;
    }

    @Override
    public String toString() {
        return "{" +
                "name=" + name +
                ", chair=" + chair.getName() +
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
