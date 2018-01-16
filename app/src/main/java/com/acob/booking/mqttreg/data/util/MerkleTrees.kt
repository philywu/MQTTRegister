package com.acob.booking.mqttreg.data.util

import android.provider.SyncStateContract.Helpers.update
import java.nio.file.Files.size
import java.security.MessageDigest
import kotlin.experimental.and


/**
 * Created by wugang00 on 15/01/2018.
 */
class MerkleTrees
/**
 * constructor
 * @param txList transaction List
 */
(// transaction List
        internal var txList: List<String>) {
    // Merkle Root
    /**
     * Get Root
     * @return
     */
    var root: String
        internal set

    init {
        root = ""
    }

    /**
     * execute merkle_tree and set root.
     */
    fun merkle_tree() {

        val tempTxList = ArrayList<String>()

        for (i in this.txList.indices) {
            tempTxList.add(this.txList[i])
        }

        var newTxList = getNewTxList(tempTxList)
        while (newTxList.size != 1) {
            newTxList = getNewTxList(newTxList)
        }

        this.root = newTxList[0]
    }

    /**
     * return Node Hash List.
     * @param tempTxList
     * @return
     */
    private fun getNewTxList(tempTxList: List<String>): List<String> {

        val newTxList = ArrayList<String>()
        var index = 0
        while (index < tempTxList.size) {
            // left
            val left = tempTxList[index]
            index++

            // right
            var right = ""
            if (index != tempTxList.size) {
                right = tempTxList[index]
            }

            // sha2 hex value
            val sha2HexValue = getSHA2HexValue(left + right)
            newTxList.add(sha2HexValue)
            index++

        }

        return newTxList
    }

    /**
     * Return hex string
     * @param str
     * @return
     */
    fun getSHA2HexValue(str: String): String {
        val cipher_byte: ByteArray
        try {
            val md = MessageDigest.getInstance("SHA-256")
            md.update(str.toByteArray())
            cipher_byte = md.digest()
            val sb = StringBuilder(2 * cipher_byte.size)
            for (b in cipher_byte) {
                sb.append(String.format("%02x", b and 0xff.toByte()))
            }
            return sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

}