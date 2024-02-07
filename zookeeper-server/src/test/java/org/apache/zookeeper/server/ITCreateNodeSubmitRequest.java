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
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ITCreateNodeSubmitRequest {
    private Object expectedOutput;
    private String connectionString;
    private int timeout;
    private String path;
    private byte[] data;

    private List<ACL> acl;

    private CreateMode creationMode;

    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][]{ // connectionString, timeout, path, data, acl, creationMod, expectedOutput
                {"localhost:9999", 3000, "/src/test/resources/it", "test".getBytes(), new ArrayList<>(Arrays.asList(Mockito.mock(ACL.class))),
                        CreateMode.PERSISTENT_SEQUENTIAL, new KeeperException.ConnectionLossException()},
        });
    }

    public ITCreateNodeSubmitRequest(String connectionString, int timeout, String path, byte[] data, List<ACL> acl, CreateMode creationMode, Object expectedOutput){
        this.connectionString = connectionString;
        this.timeout = timeout;
        this.path = path;
        this.data = data;
        this.acl = acl;
        this.creationMode = creationMode;
        this.expectedOutput = expectedOutput;
    }

    @Test
    public void createRequestSubmitTest() throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper(connectionString, timeout, null);
        try{
            zooKeeper.create(path, data, acl, creationMode);
        }
        catch(Exception e){
            assertEquals(expectedOutput.getClass(), e.getClass());
            e.printStackTrace();
        }
    }
}
