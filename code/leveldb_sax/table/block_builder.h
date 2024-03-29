// Copyright (c) 2011 The LevelDB Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file. See the AUTHORS file for names of contributors.

#ifndef STORAGE_LEVELDB_TABLE_BLOCK_BUILDER_H_
#define STORAGE_LEVELDB_TABLE_BLOCK_BUILDER_H_

#include <cstdint>
#include <vector>

#include "leveldb/slice.h"
#include "db/memtable.h"
namespace leveldb {

struct Options;

class BlockBuilder {
 public:
  explicit BlockBuilder(const Options* options);

  BlockBuilder(const BlockBuilder&) = delete;
  BlockBuilder& operator=(const BlockBuilder&) = delete;

  // Reset the contents as if the BlockBuilder was just constructed.
  void Reset();

  // REQUIRES: Finish() has not been called since the last call to Reset().
  // REQUIRES: key is larger than any previously added key
//  size_t Add(const Slice& key, const Slice& value);

  //返回压缩后的大小

  void AddLeaf(NonLeafKey* nonLeafKey, void* tocopy) ;
  inline void AddLeaf_(void* tocopy, size_t size_tocopy) {
    buffer_.append((char*)tocopy, size_tocopy);
  }
  inline void AddNonLeaf(void* tocopy, size_t size_tocopy) {
    buffer_.append((char*)tocopy, size_tocopy);
  }

  void Add(Leaf* leaf, LeafKey* copyleaf);

  void Add(NonLeaf* nonLeaf, vector<void*> &new_p);

  // Finish building the block and return a slice that refers to the
  // block contents.  The returned slice will remain valid for the
  // lifetime of this builder or until Reset() is called.
  Slice Finish();

  // Returns an estimate of the current (uncompressed) size of the block
  // we are building.
  size_t CurrentSizeEstimate() const;

  // Return true iff no entries have been added since the last Reset()
  bool empty() const { return buffer_.empty(); }

 private:
  const Options* options_;
  std::string buffer_;              // Destination buffer
//  std::vector<uint32_t> restarts_;  // Restart points
//  int counter_;                     // Number of entries emitted since restart
  bool finished_;                   // Has Finish() been called?
//  std::string last_key_;
};

}  // namespace leveldb

#endif  // STORAGE_LEVELDB_TABLE_BLOCK_BUILDER_H_
