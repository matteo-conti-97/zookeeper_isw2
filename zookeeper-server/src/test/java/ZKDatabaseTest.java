import org.apache.zookeeper.server.ZKDatabase;
import org.apache.zookeeper.server.persistence.FileTxnSnapLog;
import org.apache.zookeeper.txn.TxnHeader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.apache.zookeeper.server.persistence.FileTxnLog.FileTxnIterator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ZKDatabaseTest {
    private int startZxid;
    private int sizeLimit;
    private Object expectedOutput;

    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][]{ //startZxidm, sizeLimit, expectedOutput
                //1-startZxid -1, sizeLimit -1, expectedOutput empty iterator
                {-1, -1, 0},
                //2-startZxid -1, sizeLimit 0, expectedOutput !empty iterator
                {-1, 0, 1},
                //3-startZxid -1, sizeLimit 1, expectedOutput !empty iterator
                {-1, 1, 1},
                //4-startZxid 0, sizeLimit -1, expectedOutput empty iterator
                {0, -1, 0},
                //5-startZxid 0, sizeLimit 0, expectedOutput !empty iterator
                {0, 0, 1},
                //6-startZxid 0, sizeLimit -1, expectedOutput empty iterator
                {0, 1, 1},
        });
    }

    public ZKDatabaseTest(int startZxid, int sizeLimit, Object expectedOutput) {
        this.startZxid = startZxid;
        this.sizeLimit = sizeLimit;
        this.expectedOutput = expectedOutput;
    }

    @Test
    public void getProposalsFromTxnLogTest() throws IOException {
        Iterator res;
        //Mock per lo snap del log di txn
        FileTxnSnapLog snapLogMock = Mockito.mock(FileTxnSnapLog.class);
        //Mock per l'iterator del log di txn
        FileTxnIterator txnIteratorMock = Mockito.mock(FileTxnIterator.class);
        //Mock per l'header degli elementi dell'iterator
        Mockito.when(txnIteratorMock.getHeader()).thenReturn(new TxnHeader(1, 1, startZxid, 1, 1));
        //Mock per la grandezza dell'iterator
        Mockito.when(txnIteratorMock.getStorageSize()).thenReturn((long) sizeLimit);
        Mockito.when(snapLogMock.readTxnLog(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean())).thenReturn(txnIteratorMock);

        ZKDatabase zkdb = new ZKDatabase(snapLogMock);
        res = zkdb.getProposalsFromTxnLog(startZxid, sizeLimit);
        assertEquals(expectedOutput, ZKDatabaseUtils.checkIteratorSize(res));

    }
}
