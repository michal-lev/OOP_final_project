package michal_liora;

import java.util.Comparator;

public class SortCommitteeByNumMembers implements Comparator<Committee> {
    @Override
    public int compare(Committee committee1, Committee committee2) {
        int memberCount1 = committee1.getMemberCount(),memberCount2 = committee2.getMemberCount();

        if (memberCount1 > memberCount2) {
            return 1;
        }
        if (memberCount2 > memberCount1) {
            return -1;
        }
        return 0;
    }
}
