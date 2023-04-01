# Cherry: A Data Series Index with Efficient Writing and Low-Latency Querying

## Abstract: 

We propose Cherry, an index with extremely fast write performance and superior query capability through careful design.
Compared with the ParIS+ on a 1 billion dataset, Cherry utilizes two orders of magnitude less memory and half the index size, 
achieves an index construction speed that is 0.42x faster, 
approximate query speed that is 4.2x faster (with 25% lower distance of answers) and exact query speed that is 10.8x faster.

# 1. Project Structure

## Code
This folder contains the code for implementing Cherry.

## Data
This folder contains the code for the synthetic dataset generation, as well as the oneDrive links to the real dataset.
These datasets are used in the construction and query answering of cherry in paper experiments.

For details, see [README.md](https://github.com/imarcher/Cherry/blob/master/data/README.md) under this folder.

## Paper
This folder contains the pdf of the Cherry.



# 2. Requirements

Ubuntu 20.04 \
GCC 9.4.0 \
JDK 8 \
Boost 1.81.0 \
Tcmalloc (libunwind 1.1 and gperftools 2.1)

The data is better stored on SSD, otherwise IO time will overlap CPU time and will not reflect Cherry's superior performance.


# 3. Build and Run
Cherry is written on the basis of [leveldb](https://github.com/google/leveldb), mainly cmake projects (in `leveldb_sax`), and provides JNI for java calls.
The compilation options for the c++ part of the code are in `/leveldb_sax/CMakeLists.txt`.

We recommend running with Idea and Clion.
Compile `leveldb_sax` with Clion to generate `libleveldbj.so` and add its path to the VM options of java.
For example: `-Djava.library.path=./leveldb_sax/cmake-build-release`. Then you can run `Main.java` in the Idea.
You can find Idea's VM options settings in: top right corner select `Edit Run/Debug configurations` -> `alt+v`

Of course, If you don't want to install Idea and Clion, it can also be compiled and run only using the following command：
```shell
# Move to the code folder
# 1. Compile the leveldb_sax
mkdir -p leveldb_sax/build
cd leveldb_sax/build
cmake -DCMAKE_BUILD_TYPE=Release .. 
cmake --build . --target leveldbj -j 3

# 2. Compile the java part
cd ../.. && mkdir -p out
javac -d out -cp "lib/*" -sourcepath src/main/java src/main/java/com/cherry/Main.java src/main/java/leveldb_sax/db_send.java

# 3. Run the Cherry
java -cp out:"lib/*"  -Djava.library.path=leveldb_sax/build/  com.cherry.Main
```

Please make sure that both `inputPath` and `queryFilePath` have files, see the Parameter for specific settings. 

Enter 0 or 1 in the console to select the approximate or exact query, and then enter the number of queries and the k value of k-nn.
The program will automatically build index and perform queries.

# 4. Parameter
Our parameters are set in the following two files.

1. [`leveldb_sax/sax/include/globals.h`](https://github.com/imarcher/Cherry/blob/master/code/leveldb_sax/sax/include/globals.h)
2. [`src/main/java/com/cherry/Parameters.java`](https://github.com/imarcher/Cherry/blob/master/code/src/main/java/com/cherry/Parameters.java)

Please update it to the appropriate setting by yourself before running.

Note that some parameters in these two files are corresponding and need to be modified together.

Some important parameters in the paper are as follows:

| Parameter |                 Definition                  | Parameter in code (global.h / Parameters.java) |
|-----------|:-------------------------------------------:|-----------------------------------------------:|
| TS Size   | Size of data series or time series in bytes |                     `Ts_length` / `tsDataSize` |
| PS        |        Number of PAA or SAX segments        |                  `is8_segment` / `segmentSize` |
| CB        | Cardinality bits of a single segment of SAX |           `Bit_cardinality` / `bitCardinality` |
| SAXT Size |      Size of transpose of SAX summary       |                               `-` / `saxTSize` |
| LN        | Lower bound on the number of keys in a node |                           `Leaf_minnum`  / `-` |
| UN        | Upper bound on the number of keys in a node |                           `Leaf_maxnum`  / `-` |

Some parameters necessary for running are as follows:

| Parameter in code (global.h / Parameters .java) |                          Definition                          | 
|-------------------------------------------------|:------------------------------------------------------------:|
| `-`/`inputPath`                                 |           The folder where time series are stored            |
| `-`/`queryFilePath`                             |                 File path of the query file                  |
| `init_num`/`initNum`                            | Number of time series inserted in the initialization process |
| `pool_size`/`insertNumThread`                   |        Number of memtables (Number of insert threads)        |
| `pool_get_size` /`-`                            |                   Number of query threads                    |

# 5. Java Interface
For details, see [`src/main/java/leveldb_sax`](https://github.com/imarcher/Cherry/tree/master/code/src/main/java/leveldb_sax)

