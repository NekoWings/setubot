package com.kyuubiran.setubot;

import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.GlobalScope;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.internal.EventInternalJvmKt;
import net.mamoe.mirai.message.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.utils.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class MainApplication {
    private static final String help = "指令:\n" +
            "[主人级]\n" +
            "!sudo aa/da <QQ> 添加/删除管理\n" +
            "!sudo am/dm <QQ> 添加/删除主人\n" +
            "!sudo setH <0/1/2> 打开神奇的开关\n" +
            "[管理级]\n" +
            "!sudo setcd <sec> 设置贤者时间(秒) 范围[0-86400]\n" +
            "[群员]\n" +
            "!gkd 搞快点!\n" +
            "!setu 来点色图.gif";


    public static void main(String[] args) throws Throwable {
        //初始化
        Utils.INSTANCE.initialization();
        if (Utils.INSTANCE.getLoginQQ() == 0 || Utils.INSTANCE.getLoginPsw().length() < 6)
            throw new IllegalAccessException("账号或者密码不能为空!请先在config/config.json设置账号密码!");

        //尝试登录
        try {
            //获取QQ账号密码
            long loginQQ = Utils.INSTANCE.getLoginQQ();
            String loginPsw = Utils.INSTANCE.getLoginPsw();
            //获取登录配置文件
            BotConfiguration cfg = new BotConfiguration();
            cfg.setDeviceInfo(new Function1<Context, DeviceInfo>() {
                net.mamoe.mirai.utils.DeviceInfo info = null;

                @Override
                public net.mamoe.mirai.utils.DeviceInfo invoke(Context context) {
                    if (info == null) info = new PhoneDeviceInfo(context);
                    return info;
                }
            });
            //登录
            Bot bot = BotFactoryJvm.newBot(loginQQ, loginPsw, cfg);
            bot.login();

            //CD控制
            AtomicLong sendSetuCurrentTime = new AtomicLong(0);

            //监听相关
            Method register = EventInternalJvmKt.class.getDeclaredMethod("_subscribeEventForJaptOnly", Class.class, CoroutineScope.class, Consumer.class);
            register.setAccessible(true);
            register.invoke(null, MessageEvent.class, GlobalScope.INSTANCE, (Consumer<MessageEvent>) event -> {
                MessageChain msgc = event.getMessage();
                Contact target = event.getSubject();
                String msg = msgc.contentToString();
                if (msg.toLowerCase().equals("!setu") || msg.toLowerCase().equals("！setu") || msg.toLowerCase().equals("!gkd") || msg.toLowerCase().equals("！gkd")) {
                    if (System.currentTimeMillis() - sendSetuCurrentTime.get() > Utils.INSTANCE.getCD() * 1000) {
                        Setu setu = new Setu();
                        String s = setu.get();
                        if (s == null) {
                            sendSetuCurrentTime.set(System.currentTimeMillis());
                            target.sendMessage(MessageUtils.newChain(target.uploadImage(new File(Objects.requireNonNull(setu.getPicLocation()))))
                                    .plus(setu.getFormat()));
                        } else {
                            target.sendMessage(s);
                        }
                    } else {
                        target.sendMessage("冷却时间还未结束哦~");
                    }
                }
                if (msg.toLowerCase().contains("/test")) {
                    target.sendMessage("测试通过");
                }
                if (msg.startsWith("!sudo setkey ") && Auth.INSTANCE.checkAuthLevel(target.getId()) == 3) {
                    Utils.INSTANCE.writeCfg("apiKey", msg.replace("!sudo setkey ", ""));
                    target.sendMessage("ApiKey设置成功~");
                }
                if (msg.startsWith("!sudo aa ") && Auth.INSTANCE.checkAuthLevel(target.getId()) == 3) {
                    long qq = 0;
                    try {
                        qq = Long.parseLong(msg.replace("!sudo aa ", ""));
                    } catch (Throwable t) {
                        target.sendMessage("输入的QQ不正确!");
                    }
                    if (Auth.INSTANCE.checkIsQQ(qq)) {
                        Auth.INSTANCE.authManager(true, false, qq);
                        target.sendMessage("添加管理" + qq + "成功!");
                    } else {
                        target.sendMessage("输入的QQ不正确!");
                    }
                }
                if (msg.startsWith("!sudo da ") && Auth.INSTANCE.checkAuthLevel(target.getId()) == 3) {
                    long qq = 0;
                    try {
                        qq = Long.parseLong(msg.replace("!sudo da ", ""));
                    } catch (Throwable t) {
                        target.sendMessage("输入的QQ不正确!");
                    }
                    if (Auth.INSTANCE.checkIsQQ(qq)) {
                        Auth.INSTANCE.authManager(false, false, qq);
                        target.sendMessage("删除管理" + qq + "成功!");
                    } else {
                        target.sendMessage("输入的QQ不正确!");
                    }
                }
                if (msg.startsWith("!sudo am ") && Auth.INSTANCE.checkAuthLevel(target.getId()) == 3) {
                    long qq = 0;
                    try {
                        qq = Long.parseLong(msg.replace("!sudo am ", ""));
                    } catch (Throwable t) {
                        target.sendMessage("输入的QQ不正确!");
                    }
                    if (Auth.INSTANCE.checkIsQQ(qq)) {
                        Auth.INSTANCE.authManager(true, true, qq);
                        target.sendMessage("添加主人" + qq + "成功!");
                    } else {
                        target.sendMessage("输入的QQ不正确!");
                    }
                }
                if (msg.startsWith("!sudo dm ") && Auth.INSTANCE.checkAuthLevel(target.getId()) == 3) {
                    long qq = 0;
                    try {
                        qq = Long.parseLong(msg.replace("!sudo dm ", ""));
                    } catch (Throwable t) {
                        target.sendMessage("输入的QQ不正确!");
                    }
                    if (Auth.INSTANCE.checkIsQQ(qq)) {
                        Auth.INSTANCE.authManager(false, true, qq);
                        target.sendMessage("删除主人" + qq + "成功!");
                    } else {
                        target.sendMessage("输入的QQ不正确!");
                    }
                }
                if (msg.startsWith("!sudo setcd ") && Auth.INSTANCE.checkAuthLevel(target.getId()) >= 2) {
                    int cd = Integer.parseInt(msg.replace("!sudo setcd ", ""));
                    if (cd >= 0 && cd <= 84600) {
                        Utils.INSTANCE.writeCfg("cd", cd);
                        target.sendMessage("设置图片获取cd间隔为:" + cd + "秒");
                    } else {
                        target.sendMessage("输入的时间不正确!单位:秒 范围[0-86400]");
                    }
                }
                if (msg.startsWith("!sudo setH ") && Auth.INSTANCE.checkAuthLevel(target.getId()) >= 3) {
                    int r18 = Integer.parseInt(msg.replace("!sudo setH ", ""));
                    if (r18 >= 0 && r18 <= 2) {
                        Utils.INSTANCE.writeCfg("r-18", r18);
                        target.sendMessage("设置图片获取范围成功!当前范围:" + r18);
                    } else {
                        target.sendMessage("输入的值不合法!只能是0(H是不行的)||1(高伤害 好帅哦!)||2(我全都要!)");
                    }
                }
                if (msg.toLowerCase().equals("!help") || msg.toLowerCase().equals("！help")) {
                    target.sendMessage(help);
                }
            });
            bot.join();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
