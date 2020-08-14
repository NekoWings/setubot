package com.kyuubiran.setubot

object Auth {
    fun checkIsQQ(qq: Long): Boolean {
        return qq in 10001..1999999999
    }

    fun checkAuthLevel(qq: Long): Int {
        //qq不正确
        if (!checkIsQQ(qq)) return -1
        //读取权限列表
        val masterList: List<Long>? = Utils.getMasterList()
        val adminList: List<Long>? = Utils.getAdminList()
        //主人级权限检测 等级3
        if (masterList != null) {
            for (s in masterList) {
                if (qq == s) return 3
            }
        }
        //管理级权限检测 等级2
        if (adminList != null) {
            for (s in adminList) {
                if (qq == s) return 2
            }
        }
        //其他用户均为等级1
        return 1
    }

    /**
     * @param isAdd 是否为添加 是:添加 否:删除
     * @param isMaster 是否为主人 是:主人 否:管理
     */
    fun authManager(isAdd: Boolean, isMaster: Boolean, qq: Long) {
        val master = Utils.getMasterList()
        val admin = Utils.getAdminList()
        if (isAdd) {
            if (isMaster) {
                master.add(qq)
            } else {
                admin.add(qq)
            }
        } else {
            if (isMaster) {
                master.remove(qq)
            } else {
                admin.remove(qq)
            }
        }
        Utils.writeCfg("admin", admin)
        Utils.writeCfg("master", master)
    }
}