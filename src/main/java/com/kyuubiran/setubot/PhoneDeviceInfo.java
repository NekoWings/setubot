package com.kyuubiran.setubot;

import net.mamoe.mirai.utils.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PhoneDeviceInfo extends net.mamoe.mirai.utils.DeviceInfo {
    final Context ctx;

    public PhoneDeviceInfo(Context c) {
        ctx = c;
    }

    @NotNull
    @Override
    public byte[] getAndroidId() {
        return Utils.INSTANCE.getAndroidID().getBytes();
    }

    @NotNull
    @Override
    public byte[] getApn() {
        return "wifi".getBytes();
    }

    @NotNull
    @Override
    public byte[] getBaseBand() {
        return "XIAOMI".getBytes();
    }

    @NotNull
    @Override
    public byte[] getBoard() {
        return "XIAOMI".getBytes();
    }

    @NotNull
    @Override
    public byte[] getBootId() {
        return new byte[]{20};
    }

    @NotNull
    @Override
    public byte[] getBootloader() {
        return "unknown".getBytes();
    }

    @NotNull
    @Override
    public byte[] getBrand() {
        return "XIAOMI".getBytes();
    }

    @NotNull
    @Override
    public Context getContext() {
        return ctx;
    }

    @NotNull
    @Override
    public byte[] getDevice() {
        return "Mi 10".getBytes();
    }

    @NotNull
    @Override
    public byte[] getDisplay() {
        return ("HUAWEI_25_43529").getBytes();
    }

    @NotNull
    @Override
    public byte[] getFingerprint() {
        return "HUAWEI/HUAWEI/IQCOO:10/HUAWEI.200122.001/4527869:user/release-keys".getBytes();
    }

    @NotNull
    @Override
    public String getImei() {
        return "345279634527869";
    }

    @NotNull
    @Override
    public byte[] getImsiMd5() {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update("AMD YES!".getBytes());
            return md5.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "7tfvs356gui78r5t".getBytes();
        }
    }

    @NotNull
    @Override
    public byte[] getMacAddress() {
        return "02:00:00:00:00:00".getBytes();
    }

    @NotNull
    @Override
    public byte[] getModel() {
        return "IQCOO".getBytes();
    }

    @NotNull
    @Override
    public byte[] getOsType() {
        return "android".getBytes();
    }

    @NotNull
    @Override
    public byte[] getProcVersion() {
        return "Linux version 3.0.31-g83d6749 (android-build@localhost)".getBytes();
    }

    @NotNull
    @Override
    public byte[] getProduct() {
        return "IQCOO".getBytes();
    }

    @NotNull
    @Override
    public byte[] getSimInfo() {
        return "unicom".getBytes();
    }

    @NotNull
    @Override
    public Version getVersion() {
        return new Version() {
            @NotNull
            @Override
            public byte[] getIncremental() {
                return new byte[]{24};
            }

            @NotNull
            @Override
            public byte[] getRelease() {
                return new byte[]{0};
            }

            @NotNull
            @Override
            public byte[] getCodename() {
                return "REL".getBytes();
            }

            @Override
            public int getSdk() {
                return 25;
            }
        };
    }

    @Nullable
    @Override
    public byte[] getWifiBSSID() {
        return "02:00:00:00:00:00".getBytes();
    }

    @Nullable
    @Override
    public byte[] getWifiSSID() {
        return "<unknown ssid>".getBytes();
    }
}
