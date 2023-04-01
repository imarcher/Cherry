package leveldb_sax;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Java calls C++
 */
public class db {

    static {
        System.loadLibrary("leveldbj");
    }

    /**
     * Print compaction time
     */
    public native void print_time();

//    @Deprecated
//    public native byte[] leaftimekey_from_tskey(byte[] tskeys, int hash, long offset, boolean issort);
//
//    @Deprecated
//    public native void leaftimekey_sort(byte[] leaftimekeys);

    /**
     * Insert ts into the index.
     * @param tskeys_num number of ts keys.
     * @param tskeys tskeys_num ts keys. Each ts keys consists of TS and timestamp.
     * @param leaftimekeys  a empty byteBuffer, the size is Parameters.leafTimeKeysSize * tskeys_num.
     * @param hash  file number
     * @param offset    location of the first ts in the file.
     * @return  TS convert to saxT time
     */
    public native long put_buffer(int tskeys_num, ByteBuffer tskeys, ByteBuffer leaftimekeys, int hash, long offset);

    /**
     * Provide initialization size information.
     * @param tskeys_num  Number of ts required for initialization
     */
    public native void init_malloc(int tskeys_num);

    /**
     * Insert ts into the index for initialization.
     */
    public native void init_putbuffer(int tskeys_num, int this_num, ByteBuffer tskeys, int hash, long offset);

    /**
     * Called after initialization.
     * @param tskeys_num number of tskeys
     */
    public native void init_finish(int tskeys_num);


    //    @Deprecated
    public native void paa_saxt_from_ts(byte[] ts, byte[] saxt, float[] paa);

    /**
     * Input ts. Return the corresponding saxt and paa.
     * @param ts TS
     * @param saxt a empty byteBuffer, the size is Parameters.saxTSize.
     * @param paa a empty byteBuffer, the size is Parameters.paaNum * 4.
     */
    public native void paa_saxt_from_ts_buffer(ByteBuffer ts, ByteBuffer saxt, ByteBuffer paa);


    /**
     * Open the database.
     * @param dbname folder name for database
     */
    public native void open(String dbname);

    /**
     * Close the database.
     */
    public native void close();


//    @Deprecated
//    public native void init(byte[] leafTimeKeys, int leafKeysNum);

//    @Deprecated
//    public native void put(byte[] leafTimeKey);

    //    @Deprecated
    public native byte[] Get(byte[] aquery, boolean is_use_am, int am_version_id, int st_version_id, long[] st_number);

    /**
     * Provide a query and corresponding information, perform approximate query answering, and return results.
     * @param aquery query content.
     *               aquery(has timestamp): ts 256*4, startTime 8, endTime 8, k 4，paa 4*paaSize, saxt 8/16, empty 4(because time is long type, need alignment)
     *               aquery(no timestamp): ts 256*4, k 4，paa 4*paaSize, saxt 8/16
     * @param is_use_am query memtable or not
     * @param am_version_id memory version id
     * @param st_version_id disk version id
     * @param st_number_num number of sstable
     * @param st_number a list contains sstable id
     * @param appro_res approximate query answering results. It is a empty byteBuffer. The size is k * Parameters.approximateResSize + 4.
     *                  The last 4 bytes are sstable id. It is used to record which table in the current memory version is queried approximate query answering.
     *                  In order to remove duplicates in exact query answering.
     * @param info  a empty byteBuffer. Used to access the raw time series during the query process.
     *              info(has timestamp): ts 256*4，starttime 8， endtime 8, heap 8， k 4, needNum 4, topdist 4, n 4，p*n 8*n. Where n is a Parameters.infoMaxPSize.
     *              info(no timestamp): ts 256*4, heap 8， k 4, needNum 4, topdist 4,n 4, p*n 8*n. Where n is a Parameters.infoMaxPSize.
     * @return  number of results.
     */
    public native int Get(ByteBuffer aquery, boolean is_use_am, int am_version_id, int st_version_id,
                          int st_number_num, ByteBuffer st_number, ByteBuffer appro_res, ByteBuffer info);


    //    @Deprecated
    public native byte[] Get_exact(byte[] aquery, int am_version_id, int st_version_id, long[] st_number, byte[] appro_res, long[] appro_num);

    /**
     * Provide a query and corresponding information, perform exact query answering, and return results.
     * @param aquery query content.
     * @param am_version_id memory version id
     * @param st_version_id disk version id
     * @param st_number_num number of sstable
     * @param st_number a list contains sstable id
     * @param appro_res_num number of approximate query answering results
     * @param appro_res approximate query answering results
     * @param appro_st_number_num number of the sstables using in approximate query answering
     * @param appro_st_number sstables using in approximate query answering
     * @param exact_res exact query answering results. It is a empty byteBuffer. The size is k * Parameters.exactResSize.
     * @param info a empty byteBuffer. Used to access the raw time series during the query process.
     * @return number of results.
     */
    public native int Get_exact(ByteBuffer aquery, int am_version_id, int st_version_id,
                                int st_number_num, ByteBuffer st_number, int appro_res_num, ByteBuffer appro_res,
                                int appro_st_number_num, ByteBuffer appro_st_number, ByteBuffer exact_res, ByteBuffer info);



    /**
     * Push ts to the heap when accessing the raw time series.
     * @param ares the accessing ts and euclidean distance between ts and query.
     * @param heap the pointer of heap
     * @return  topdis, which is the distance of the k-th largest answer in the current situation.
     */
    public native float heap_push_buffer(ByteBuffer ares, byte[] heap);
    public native float heap_push_exact_buffer(ByteBuffer ares_exact, byte[] heap);

//    @Deprecated
//    public native float heap_push(byte[] ares, byte[] heap);
//    @Deprecated
//    public native float heap_push_exact(byte[] ares_exact, byte[] heap);

    /**
     * unref memory version
     * @param version_id memory version id
     */
    public native void unref_am(int version_id);

    /**
     * unref disk version
     * @param version_id disk version id
     */
    public native void unref_st(int version_id);


    //    @Deprecated
    public native float dist_ts(byte[] ts1, byte[] ts2);
    /**
     * compute euclidean distance
     */
    public native float dist_ts_buffer(ByteBuffer ts1, ByteBuffer ts2);
}