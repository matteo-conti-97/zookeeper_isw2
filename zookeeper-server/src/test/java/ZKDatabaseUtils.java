import org.apache.zookeeper.server.TxnLogProposalIterator;

import java.util.Iterator;

public class ZKDatabaseUtils {
    public static int checkIteratorSize(Iterator iterator){
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }
}
