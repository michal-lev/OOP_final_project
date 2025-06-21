package michal_liora;

import java.util.ArrayList;

public class Doctor extends Lecturer implements Comparable<Doctor> {
    protected ArrayList<String> articles;
    public Doctor(String name, String id, String degreeLevel, String degreeTitle, double salary, Department department, ArrayList<String> articles) {
        super(name, id, degreeLevel, degreeTitle, salary, department);
        setArticles(articles);
    }

    public void setArticles(ArrayList<String> articles) {
        this.articles = articles;
    }

    public ArrayList<String> getArticles() {
        return articles;
    }

    @Override
    public String toString() {
        return "{" +
                "name=" + name +
                ", id=" + id +
                ", degreeLevel=" + degreeLevel +
                ", degreeTitle=" + degreeTitle +
                ", salary=" + salary +
                ", department=" + ((department != null) ? department.getName() : "(None)") +
                ", committees=" + committeesNamesToString() +
                ", articles=" + articles +
                "}";
    }

    @Override
    public boolean equals(Object toCompare) {
        if (toCompare == null || getClass() != toCompare.getClass() || !super.equals(toCompare))
            return false;
        Doctor otherDoctor = (Doctor) toCompare;
        return articles.equals(otherDoctor.articles);
    }

    @Override
    public int compareTo(Doctor other) {
        int thisArticleCount = articles.size();
        int otherArticleCount = other.articles.size();

        if (thisArticleCount > otherArticleCount) {
            return 1;
        }
        if (otherArticleCount > thisArticleCount) {
            return -1;
        }
        return 0;
    }

}
