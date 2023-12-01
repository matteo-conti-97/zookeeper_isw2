package org.apache.zookeeper.server;

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
import org.apache.zookeeper.server.ZKDatabaseUtils.LogStatus;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ZKDatabaseTest {
    private LogStatus logStatus;
    private int startZxid;
    private int sizeLimit;
    private Object expectedOutput;

    /*@Parameterized.Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][]{ //startZxidm, sizeLimit, expectedOutput
                //0-startZxid -1, sizeLimit -1, expectedOutput empty iterator
                {-1, -1, 0},
                //1-startZxid -1, sizeLimit 0, expectedOutput !empty iterator
                {-1, 0, 1},
                //2-startZxid -1, sizeLimit 1, expectedOutput !empty iterator
                {-1, 1, 1},
                //3-startZxid 0, sizeLimit -1, expectedOutput empty iterator
                {0, -1, 0},
                //4-startZxid 0, sizeLimit 0, expectedOutput !empty iterator
                {0, 0, 1},
                //5-startZxid 0, sizeLimit -1, expectedOutput empty iterator
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
    }*/

    /*//EVO1
    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][]{ //snapLog, startZxidm, sizeLimit, expectedOutput
                //0-snapLog null, startZxid 0, sizeLimit 1, expectedOutput NullPointerException
                {LogStatus.NULL_LOG, 0, 1, new NullPointerException()},
                //1-snapLog nonExisting, startZxid 0, sizeLimit 1, expectedOutput 0 -> la IOException viene gestita
                {LogStatus.NON_EXISTING_LOG, 0, 1, 0},
                //2-snapLog !empty, startZxid 0, sizeLimit -1, expectedOutput empty iterator -> -1 sizeLimit
                {LogStatus.EXISTING_LOG, 0, -1, 0},
                //3-snapLog !empty, startZxid 0, sizeLimit 0, expectedOutput iterator con 1 elemento
                {LogStatus.EXISTING_LOG, 0, 0, 1},
                //4-snapLog !empty, startZxid 0, sizeLimit 1, expectedOutput iterator con 1 elemento
                {LogStatus.EXISTING_LOG, 0, 1, 1},
                //5-snapLog !empty, startZxid -1, sizeLimit 1, expectedOutput empty iterator -> startZxid > logMaxZxid
                {LogStatus.EXISTING_LOG, -1, 1, 0},
                //6-snapLog !empty, startZxid -1, sizeLimit 0, expectedOutput empty iterator -> startZxid > logMaxZxid
                {LogStatus.EXISTING_LOG, -1, 0, 0},
                //7-snapLog !empty, startZxid -1, sizeLimit -1, expectedOutput empty iterator -> -1 sizeLimit
                {LogStatus.EXISTING_LOG, -1, -1, 0},
        });
    }

    public ZKDatabaseTest(LogStatus logStatus, int startZxid, int sizeLimit, Object expectedOutput) {
        this.logStatus=logStatus;
        this.startZxid = startZxid;
        this.sizeLimit = sizeLimit;
        this.expectedOutput = expectedOutput;
    }

    @Test
    public void getProposalsFromTxnLogTest(){
        Iterator res;
        FileTxnSnapLog snapLog;
        try {
            if (this.logStatus == LogStatus.NULL_LOG) {
                snapLog = null;
            }
            else {
                //Mock per lo snap del log di txn
                snapLog = Mockito.mock(FileTxnSnapLog.class);
                //Mock per l'iterator del log di txn
                FileTxnIterator txnIteratorMock = Mockito.mock(FileTxnIterator.class);
                if(this.logStatus==LogStatus.NON_EXISTING_LOG) {
                    Mockito.when(snapLog.readTxnLog(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean())).thenThrow(new IOException());
                }
                else {
                    //Mock per iterator
                    Mockito.when(txnIteratorMock.getStorageSize()).thenReturn((long) 1);
                    Mockito.when(txnIteratorMock.getHeader()).thenReturn(new TxnHeader(0, 0, 0, 0, 0));
                    Mockito.when(snapLog.readTxnLog(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean())).thenReturn(txnIteratorMock);
                }
            }
            ZKDatabase zkdb = new ZKDatabase(snapLog);
            res = zkdb.getProposalsFromTxnLog(startZxid, sizeLimit);
            assertEquals(expectedOutput, ZKDatabaseUtils.checkIteratorSize(res));
        }
        catch(Exception e){
            assertEquals(expectedOutput.getClass(), e.getClass());
            e.printStackTrace();
        }
    }*/

    //EVO2
    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][]{ //snapLog, startZxidm, sizeLimit, expectedOutput
                //0-snapLog null, startZxid 0, sizeLimit 1, expectedOutput NullPointerException
                {LogStatus.NULL_LOG, 0, 1, new NullPointerException()},
                //1-snapLog nonExisting, startZxid 0, sizeLimit 1, expectedOutput 0 -> la IOException viene gestita
                {LogStatus.NON_EXISTING_LOG, 0, 1, 0},
                //2-snapLog size 1, startZxid 0, sizeLimit -1, expectedOutput empty iterator -> -1 sizeLimit
                {LogStatus.EXISTING_LOG, 0, -1, 0},
                //3-snapLog size 1, startZxid 0, sizeLimit 0, expectedOutput iterator con 1 elemento
                {LogStatus.EXISTING_LOG, 0, 0, 1},
                //4-snapLog size 1, startZxid 0, sizeLimit 1, expectedOutput iterator con 1 elemento
                {LogStatus.EXISTING_LOG, 0, 1, 1},
                //5-snapLog size 1, startZxid -1, sizeLimit 1, expectedOutput empty iterator -> startZxid > logMaxZxid
                {LogStatus.EXISTING_LOG, -1, 1, 0},
                //6-snapLog size 1, startZxid -1, sizeLimit 0, expectedOutput empty iterator -> startZxid > logMaxZxid
                {LogStatus.EXISTING_LOG, -1, 0, 0},
                //7-snapLog size 1, startZxid -1, sizeLimit -1, expectedOutput empty iterator -> -1 sizeLimit
                {LogStatus.EXISTING_LOG, -1, -1, 0},
                //8-snapLog size 2, startZxid 0, sizeLimit 1, expectedOutput empty iterator -> sizeLimit<LogSize
                {LogStatus.EXISTING_LOG_WITH_2_ELEMENTS, 0, 1, 0},
        });
    }

    public ZKDatabaseTest(LogStatus logStatus, int startZxid, int sizeLimit, Object expectedOutput) {
        this.logStatus=logStatus;
        this.startZxid = startZxid;
        this.sizeLimit = sizeLimit;
        this.expectedOutput = expectedOutput;
    }

    @Test
    public void getProposalsFromTxnLogTest(){
        Iterator res;
        FileTxnSnapLog snapLog;
        try {
            if (this.logStatus == LogStatus.NULL_LOG) {
                snapLog = null;
            }
            else {
                //Mock per lo snap del log di txn
                snapLog = Mockito.mock(FileTxnSnapLog.class);
                //Mock per l'iterator del log di txn
                FileTxnIterator txnIteratorMock = Mockito.mock(FileTxnIterator.class);
                if(this.logStatus==LogStatus.NON_EXISTING_LOG) {
                    Mockito.when(snapLog.readTxnLog(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean())).thenThrow(new IOException());
                }
                else {
                    //Mock per iterator
                    if(this.logStatus==LogStatus.EXISTING_LOG_WITH_2_ELEMENTS)
                        Mockito.when(txnIteratorMock.getStorageSize()).thenReturn((long) 2);
                    else
                        Mockito.when(txnIteratorMock.getStorageSize()).thenReturn((long) 1);
                    Mockito.when(txnIteratorMock.getHeader()).thenReturn(new TxnHeader(0, 0, 0, 0, 0));
                    Mockito.when(snapLog.readTxnLog(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean())).thenReturn(txnIteratorMock);
                }
            }
            ZKDatabase zkdb = new ZKDatabase(snapLog);
            res = zkdb.getProposalsFromTxnLog(startZxid, sizeLimit);
            assertEquals(expectedOutput, ZKDatabaseUtils.checkIteratorSize(res));
        }
        catch(Exception e){
            assertEquals(expectedOutput.getClass(), e.getClass());
            e.printStackTrace();
        }
    }

}
