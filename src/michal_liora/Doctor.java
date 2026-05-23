package michal_liora;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class Doctor extends Lecturer implements Comparable<Doctor> {
    protected Set<String> articles;
    public Doctor(String name, String id, String degreeLevel, String degreeTitle, double salary, Department department, Set<String> articles) {
        super(name, id, degreeLevel, degreeTitle, salary, department);
        setArticles(articles);
    }

    public void setArticles(Set<String> articles) {
        this.articles = articles;
    }

    public String getArticles() {
        Iterator<String> it = articles.iterator();
        StringBuilder articleNames = new StringBuilder("[");

        while (it.hasNext()) {
            articleNames.append(it.next());
            if (it.hasNext()) 
                articleNames.append(", ");
        }
        articleNames.append("]");
        return articleNames.toString();
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
                ", committees=" + College.namesToString(committees) +
                ", articles=" + getArticles() +
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
    public int hashCode() {
        return Objects.hash(super.hashCode(), articles);
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
