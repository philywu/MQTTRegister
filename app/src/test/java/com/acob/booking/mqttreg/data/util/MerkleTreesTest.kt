package com.acob.booking.mqttreg.data.util

import org.junit.Test

/**
 * Created by wugang00 on 15/01/2018.
 */
class MerkleTreesTest {

    @Test
    fun testMerkelTrees() {
        val tempTxList = ArrayList<String>()
        tempTxList.add("a")
        tempTxList.add("b")
        tempTxList.add("c")
        tempTxList.add("d")
        tempTxList.add("e")
        tempTxList.toMutableList()
        val merkleTrees = MerkleTrees(tempTxList)
        merkleTrees.merkle_tree()
        println("root : " + merkleTrees.root)
    }
}