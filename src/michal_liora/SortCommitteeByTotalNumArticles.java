package michal_liora;

import java.util.Comparator;

public class SortCommitteeByTotalNumArticles implements Comparator<Committee> {
    @Override
    public int compare(Committee committee1, Committee committee2) {
        int numArticles1 = committee1.getTotalArticleCount(), numArticles2 = committee2.getTotalArticleCount();

        if (numArticles1 > numArticles2) {
            return 1;
        }
        if (numArticles2 > numArticles1) {
            return -1;
        }
        return 0;
    }
}
