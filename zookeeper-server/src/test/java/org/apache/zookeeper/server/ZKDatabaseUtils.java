package org.apache.zookeeper.server;

import java.util.Iterator;

public class ZKDatabaseUtils {

    public static final String NON_EXISTING_LOG_PATH = "non_existing_log_dir";
    public enum LogStatus{
        EXISTING_LOG,
        NULL_LOG,
        NON_EXISTING_LOG,
    }
    public static int checkIteratorSize(Iterator iterator){
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }
}
