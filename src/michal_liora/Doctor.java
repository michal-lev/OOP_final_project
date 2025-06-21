package michal_liora;

public class Doctor extends Lecturer implements Comparable<Doctor> {
    // fix everything
    protected String[] articles;
    protected int articleCount;
    public Doctor(String name, String id, String degreeLevel, String degreeTitle, double salary, Department department, int articleCount, String[] articles) {
        super(name, id, degreeLevel, degreeTitle, salary, department);
        setArticleCount(articleCount);
        setArticles(articles);
    }

    public void setArticles(String[] articles) {
        this.articles = articles;
    }

    public String[] getArticles() {
        return articles;
    }


    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }


    public int getArticleCount(){
        return this.articleCount;
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
                ", articles=" + articlesToString() +
                "}";
    }

    public String articlesToString(){
        String articlesStr = "[";
        int i = 0;
        for (; i < (articleCount - 1); i++){
            articlesStr += articles[i] + ", " ;
        }
        if(articleCount != 0) {
            articlesStr += articles[i];
        }
        articlesStr += "]";
        return articlesStr;
    }

    public boolean articlesEquals(String[] otherArticles){
        for (int i = 0; i < articleCount; i++){
            if (!articles[i].equals(otherArticles[i])){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object toCompare) {
        if (toCompare == null || getClass() != toCompare.getClass() || !super.equals(toCompare))
            return false;
        Doctor otherDoctor = (Doctor) toCompare;
        return articleCount == otherDoctor.articleCount &&
                articlesEquals(otherDoctor.articles);
    }

    @Override
    public int compareTo(Doctor other) {
        int thisArticleCount = getArticleCount(), otherArticleCount = other.getArticleCount();

        if (thisArticleCount > otherArticleCount) {
            return 1;
        }
        if (otherArticleCount > thisArticleCount) {
            return -1;
        }
        return 0;
    }

}
