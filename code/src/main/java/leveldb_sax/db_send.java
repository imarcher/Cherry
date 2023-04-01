package leveldb_sax;


import com.cherry.Parameters;
import com.cherry.search.SearchActionOld;
import com.cherry.search.SearchAction;
import com.cherry.version.VersionAction;

import java.nio.ByteBuffer;

/**
 * C++ calls Java
 */
public class db_send {



    /**
     *  Send a request to change the version.
     *  @param edit Version change content
     *              0, versionid 4, amV_id 4, number 8，saxt_smallest 8，saxt_biggest 8，startTime 8，endTime 8
     *              1，versionid 4， number of delete n1 4，(sstable number 8, saxt_smallest 8, saxt_biggest 8, startTime 8, endTime 8) * n1
     *                              number of add n2 4，   (sstable number 8, saxt_smallest 8, saxt_biggest 8, startTime 8, endTime 8) * n2
     */
    public static void send_edit(byte[] edit) {
        VersionAction.changeVersion(edit, Parameters.hostName);
        VersionAction.checkWorkerVersion();
    }

    public static byte[] find_tskey(byte[] info) {
        return SearchActionOld.searchRawTs(info, false);
    }

    public static byte[] find_tskey_exact(byte[] info) {
        return SearchActionOld.searchRawTs(info, true);
    }


    /**
     *  Send a request to read the raw time series for approximate query answering.
     */
    public static void find_tskey_ap_buffer(ByteBuffer info) {
        SearchAction.searchRawTsHeapQueue(info, false);
    }

    /**
     * Send a request to read the raw time series for exact query answering.
     */
    public static void find_tskey_exact_ap_buffer(ByteBuffer info) {
        SearchAction.searchRawTsHeapQueue(info, true);
    }

}
