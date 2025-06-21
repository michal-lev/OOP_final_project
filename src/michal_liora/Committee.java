package michal_liora;

import java.util.Objects;

public class Committee implements Comparable<Committee> {
    private String name;
    private Lecturer chair;
    private Lecturer[] members;
    private int memberCount;

    public Committee(String name, Lecturer chair) {
        setName(name);
        setChair(chair);
        setMembers(new Lecturer[1]);
        setMemberCount(0);
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

    public void setMembers(Lecturer[] members) {
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

    public Lecturer[] getMembers() {
        return members;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void removeMember(Lecturer memberToRemove){
        boolean runOverFlag = false;
        int i = 0;
        for (; i < memberCount - 1; i++) {
            if(members[i].equals(memberToRemove)){
                runOverFlag = true;
            }
            if (runOverFlag){
                members[i] = members[i + 1];
            }
        }
        members[i] = null;
        setMemberCount(memberCount - 1);
    }

    public int getTotalArticleCount(){
        int totalNumArticles = 0;
        for(int i = 0; i < memberCount; i++){
            if(members[i] instanceof Doctor){
                totalNumArticles += ((Doctor)members[i]).getArticleCount();
            }
        }
        return totalNumArticles;
    }

    @Override
    public String toString() {
        return "{" +
                "name=" + name +
                ", chair=" + chair.getName() +
                ", members=" + College.lecturerNamesToString(members,memberCount) +
                "}";
    }

    @Override
    public boolean equals(Object toCompare) {
        if (toCompare == null || toCompare.getClass() != getClass())
            return false;
        Committee otherCommittee = (Committee) toCompare;
        return name.equals(otherCommittee.name) &&
                chair.equals(otherCommittee.chair) &&
                memberCount == otherCommittee.memberCount &&
                College.LecturerArrEqualsByName(members,otherCommittee.members, memberCount);
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
