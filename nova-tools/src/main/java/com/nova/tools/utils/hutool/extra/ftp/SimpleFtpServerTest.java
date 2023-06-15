package com.nova.tools.utils.hutool.extra.ftp;

import cn.hutool.extra.ftp.SimpleFtpServer;

public class SimpleFtpServerTest {

    public static void main(String[] args) {
        SimpleFtpServer
                .create()
                .addAnonymous("d:/test/ftp/")
                .start();
    }
}
