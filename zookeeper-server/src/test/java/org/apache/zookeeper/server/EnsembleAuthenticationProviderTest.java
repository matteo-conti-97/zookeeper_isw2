package org.apache.zookeeper.server;

import org.apache.zookeeper.server.auth.EnsembleAuthenticationProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

@RunWith(Parameterized.class)
public class EnsembleAuthenticationProviderTest {
    private ServerCnxn cnxn;
    private byte[] authData;
    private Set<String> ensembleNames;
    private Object expectedOutput;


    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][]{ //cnxn, authData, ensembleNames, expectedOutput
                {null, null, null, null},
        });
    }

    public EnsembleAuthenticationProviderTest(ServerCnxn cnxn, byte[] authData, Set<String> ensembleNames, Object expectedOutput) {
        this.cnxn = cnxn;
        this.authData = authData;
        this.ensembleNames = ensembleNames;
        this.expectedOutput = expectedOutput;
    }

    @Test
    public void handleAuthenticationTest() {
    }
}
