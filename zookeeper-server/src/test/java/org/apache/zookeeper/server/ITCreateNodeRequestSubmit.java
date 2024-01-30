package org.apache.zookeeper.server;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import org.apache.zookeeper.KeeperException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ITCreateNodeRequestSubmit {
    private Object expectedOutput;
    private String endpoint;
    private int timeout;
    private String nodePath;
    private byte[] data;

    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][]{ // endpoint, timeout, path, data, expectedOutput
                {"localhost:2181", 3000, "/src/test/resources/it", "test".getBytes(), new KeeperException.ConnectionLossException()},
        });
    }

    public ITCreateNodeRequestSubmit(String endpoint, int timeout, String nodePath, byte[] data, Object expectedOutput){
        this.endpoint = endpoint;
        this.timeout = timeout;
        this.nodePath = nodePath;
        this.data = data;
        this.expectedOutput = expectedOutput;
    }

    @Test
    public void requestSubmitTest() throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper(endpoint, timeout, null);

        try{
            String out = zooKeeper.create(nodePath, data, new ArrayList<>(Arrays.asList(Mockito.mock(ACL.class))), CreateMode.PERSISTENT_SEQUENTIAL);
            assertEquals(expectedOutput, out);
        }
        catch(Exception e){
            assertEquals(expectedOutput.getClass(), e.getClass());
            e.printStackTrace();
        }
    }
}
