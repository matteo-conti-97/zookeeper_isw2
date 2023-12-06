package org.apache.zookeeper.server.auth;

import org.apache.zookeeper.server.ServerCnxn;
import org.apache.zookeeper.server.ServerMetrics;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.mockito.Mockito;
import org.apache.zookeeper.server.auth.EnsembleAuthenticationProviderUtils.RetType;

@RunWith(Parameterized.class)
public class EnsembleAuthenticationProviderTest {
    private ServerCnxn cnxn;
    private byte[] authData;
    private Set<String> ensembleNames;
    private Object expected;

    private RetType expectedRetType;

    @After
    public void clearEnv() {
        System.out.println("ENSEMBLE AUT SKIP "+ ServerMetrics.getMetrics().ENSEMBLE_AUTH_SKIP.get());
        System.out.println("ENSEMBLE AUT FAIL "+ServerMetrics.getMetrics().ENSEMBLE_AUTH_FAIL.get());
        System.out.println("ENSEMBLE AUT SUCCESS "+ServerMetrics.getMetrics().ENSEMBLE_AUTH_SUCCESS.get());
        if(ServerMetrics.getMetrics().ENSEMBLE_AUTH_SKIP.get()>0)
               ServerMetrics.getMetrics().ENSEMBLE_AUTH_SKIP.add(-1);
        else if(ServerMetrics.getMetrics().ENSEMBLE_AUTH_FAIL.get()>0)
            ServerMetrics.getMetrics().ENSEMBLE_AUTH_FAIL.add(-1);
        else if(ServerMetrics.getMetrics().ENSEMBLE_AUTH_SUCCESS.get()>0)
            ServerMetrics.getMetrics().ENSEMBLE_AUTH_SUCCESS.add(-1);
        System.out.println("ENSEMBLE AUT SKIP after clear "+ServerMetrics.getMetrics().ENSEMBLE_AUTH_SKIP.get());
        System.out.println("ENSEMBLE AUT FAIL after clear "+ServerMetrics.getMetrics().ENSEMBLE_AUTH_FAIL.get());
        System.out.println("ENSEMBLE AUT SUCCESS after clear "+ServerMetrics.getMetrics().ENSEMBLE_AUTH_SUCCESS.get());
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(new Object[][]{ //cnxn, authData, ensembleNames, expectedOutput
                //0- {null}, {array contenente la codifica in byte di un solo nome di ensemble presente in ensembleNames}, {set contenente una sola stringa non vuota} -> NullPointerException
                {null, "presente".getBytes(), new HashSet<String>() {{add("presente");}}, new NullPointerException(), RetType.NullPointerException},
                //1- {istanza di serverCnxn}, {null}, {set contenente una sola stringa non vuota} -> ServerMetrics.getMetrics().ENSEMBLE_AUTH_SKIP.add(1) -> 1
                {Mockito.mock(ServerCnxn.class), null, new HashSet<String>() {{add("presente");}}, 1L, RetType.ENSEMBLE_AUTH_SKIP},
                //2- {istanza di serverCnxn}, {array contenente la codifica in byte di un solo nome di ensemble non presente in ensembleNames}, {null}  -> ServerMetrics.getMetrics().ENSEMBLE_AUTH_SKIP.add(1) -> 1
                {Mockito.mock(ServerCnxn.class), "non-presente".getBytes(), null, 1L,  RetType.ENSEMBLE_AUTH_SKIP},
                //3- {istanza di serverCnxn}, {array contenente la codifica in byte di un solo nome di ensemble non presente in ensembleNames}, {set contenente una sola stringa non vuota} -> ServerMetrics.getMetrics().ENSEMBLE_AUTH_FAIL.add(1) -> 1
                {Mockito.mock(ServerCnxn.class), "non-presente".getBytes(), new HashSet<String>() {{add("presente");}}, 1L,  RetType.ENSEMBLE_AUTH_FAIL},
                //4- {istanza di serverCnxn}, {array contenente la codifica in byte di un solo nome di ensemble presente in ensembleNames}, {set contenente una sola stringa vuota} -> ServerMetrics.getMetrics().ENSEMBLE_AUTH_SKIP.add(1) -> 1
                {Mockito.mock(ServerCnxn.class), "".getBytes(), new HashSet<String>() {{add("");}}, 1L, RetType.ENSEMBLE_AUTH_SKIP},
                //5- {istanza di serverCnxn}, {array contenente la codifica in byte di un solo nome di ensemble presente in ensembleNames}, {set contenente una sola stringa non vuota} -> ServerMetrics.getMetrics().ENSEMBLE_AUTH_SUCCESS.add(1) -> 1
                {Mockito.mock(ServerCnxn.class), "presente".getBytes(), new HashSet<String>() {{add("presente");}}, 1L, RetType.ENSEMBLE_AUTH_SUCCESS},
                //6- {istanza di serverCnxn}, {array contenente la codifica in byte di un solo nome di ensemble non presente in ensembleNames}, {set vuoto} -> ServerMetrics.getMetrics().ENSEMBLE_AUTH_FAIL.add(1) -> 1
                {Mockito.mock(ServerCnxn.class), "non-presente".getBytes(), new HashSet<String>(), 1L, RetType.ENSEMBLE_AUTH_FAIL},
                //7- {istanza di serverCnxn}, {array vuoto}, {set contenente una sola stringa vuota} -> ServerMetrics.getMetrics().ENSEMBLE_AUTH_SKIP.add(1) -> 1
                {Mockito.mock(ServerCnxn.class), new byte[0], new HashSet<String>() {{add("");}}, 1L, RetType.ENSEMBLE_AUTH_SKIP},
                //8- {istanza di serverCnxn}, {array vuoto}, {set contenente una sola stringa non vuota} -> ServerMetrics.getMetrics().ENSEMBLE_AUTH_SKIP.add(1) -> 1
                {Mockito.mock(ServerCnxn.class), new byte[0], new HashSet<String>() {{add("presente");}}, 1L, RetType.ENSEMBLE_AUTH_SKIP},
        });
    }

    public EnsembleAuthenticationProviderTest(ServerCnxn cnxn, byte[] authData, Set<String> ensembleNames, Object expectedOutput, RetType retType) {
        this.cnxn = cnxn;
        this.authData = authData;
        this.ensembleNames = ensembleNames;
        this.expected = expectedOutput;
        this.expectedRetType = retType;
    }

    @Test
    public void handleAuthenticationTest() throws NoSuchFieldException, IllegalAccessException {
        //Use reflection to set ensembleNames attribute
        Field ensembleNamesAttribute = EnsembleAuthenticationProvider.class.getDeclaredField("ensembleNames");
        ensembleNamesAttribute.setAccessible(true);
        if(cnxn!=null) {
            InetAddress ia=Mockito.mock(InetAddress.class);
            Mockito.when(ia.getHostAddress()).thenReturn("mockedAddress");
            //Not doing mock cause isa calls getAddress which is final and cannot be mocked easily
            InetSocketAddress isa = new InetSocketAddress(ia, 0);
            Mockito.when(cnxn.getRemoteSocketAddress()).thenReturn(isa);
        }
        try{
            EnsembleAuthenticationProvider eap = new EnsembleAuthenticationProvider();
            ensembleNamesAttribute.set(eap, ensembleNames);
            eap.handleAuthentication(cnxn, authData);
            if(expectedRetType== RetType.ENSEMBLE_AUTH_SKIP)
                Assert.assertEquals(expected, ServerMetrics.getMetrics().ENSEMBLE_AUTH_SKIP.get());
            else if(expectedRetType== RetType.ENSEMBLE_AUTH_FAIL)
                Assert.assertEquals(expected, ServerMetrics.getMetrics().ENSEMBLE_AUTH_FAIL.get());
            else if(expectedRetType== RetType.ENSEMBLE_AUTH_SUCCESS)
                Assert.assertEquals(expected, ServerMetrics.getMetrics().ENSEMBLE_AUTH_SUCCESS.get());
        } catch (NullPointerException e) {
            e.printStackTrace();
            Assert.assertEquals(expected.getClass(), e.getClass());
        }

    }
}
