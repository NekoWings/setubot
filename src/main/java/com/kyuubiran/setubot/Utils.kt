package com.kyuubiran.setubot

import com.alibaba.fastjson.JSONObject
import java.io.*
import kotlin.collections.ArrayList

object Utils {
    fun initialization() {
        val config = File("config").absoluteFile
        val botCfg = File("config/config.json")
        if (!config.exists()) config.mkdirs()
        if (!botCfg.exists()) {
            botCfg.createNewFile()
            val cfgIn: InputStream = MainApplication::class.java.classLoader.getResourceAsStream("config.json")
                    ?: throw IOException("InputStream must not be null")
            writeFile(botCfg, cfgIn)
        }
        if (getAndroidID().isEmpty()) writeCfg("androidID", makeAndroidID())
    }

    private fun writeFile(file: File) {
        writeFile(file, file.inputStream())
    }

    private fun writeFile(file: File, ins: InputStream) {
        val ous = FileOutputStream(file)
        val buf = ByteArray(128)
        var i: Int
        while (true) {
            i = ins.read(buf)
            if (i < 0) break
            ous.write(buf, 0, i)
        }
        ins.close()
        ous.close()
    }

    private fun readCfg(): JSONObject {
        val cfg = File("config/config.json")
        if (!cfg.exists()) throw IOException("No such File existed!")
        return JSONObject.parseObject(cfg.readText(Charsets.UTF_8))
    }

    fun writeCfg(name: String, value: Any) {
        val cfg = readCfg()
        val osw = OutputStreamWriter(FileOutputStream("config/config.json"), Charsets.UTF_8)
        when (name) {
            "cd" -> cfg["cd"] = value
            "apiKey" -> cfg["apiKey"] = value
            "r-18" -> cfg["r-18"] = value
            "admin" -> cfg["admin"] = value
            "master" -> cfg["master"] = value
            "loginQQ" -> cfg["loginQQ"] = value
            "loginPsw" -> cfg["loginPsw"] = value
            "androidID" -> cfg["androidID"] = value
        }
        osw.write(cfg.toString())
        osw.close()
    }

    private fun makeAndroidID(): String {
        val sb = StringBuilder()
        val s = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f")
        for (i in 0..15) {
            sb.append(s[(Math.random() * s.size).toInt()])
        }
        return sb.toString()
    }

    fun getAndroidID(): String {
        return readCfg().getString("androidID")
    }

    fun getApiKey(): String {
        return readCfg().getString("apiKey")
    }

    fun getR18(): Int {
        return readCfg().getIntValue("r-18")
    }

    fun getLoginQQ(): Long {
        return readCfg().getLongValue("loginQQ")
    }

    fun getLoginPsw(): String {
        return readCfg().getString("loginPsw")
    }

    fun getAdminList(): ArrayList<Long> {
        return getAuthList("admin")
    }

    fun getMasterList(): ArrayList<Long> {
        return getAuthList("master")
    }

    fun getCD(): Int {
        return readCfg().getIntValue("cd")
    }

    private fun getAuthList(type: String): ArrayList<Long> {
        val jsonArray = readCfg().getJSONArray(type)
        val list = ArrayList<Long>()
        for (i in jsonArray.indices) {
            list.add(jsonArray.getLong(i))
        }
        return list
    }
}