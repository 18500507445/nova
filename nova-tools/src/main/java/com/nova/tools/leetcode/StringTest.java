package com.nova.tools.leetcode;

/**
 * @description:
 * @author: wzh
 * @date: 2021/2/25 11:14
 */

class StringTest {

    public static void main(String[] args) {
        System.out.println(ip4Invalid());
    }

    /**
     * 给你一个有效的 IPv4 地址 address，返回这个 IP 地址的无效化版本。
     * 所谓无效化 IP 地址，其实就是用 "[.]" 代替了每个 "."。
     *
     * replace和replaceAll
     * 相同点：都是全部替换，即把源字符串中的某一字符或字符串全部换成指定的字符或字符串
     * 不同点：replaceAll支持正则表达式，因此会对参数进行解析（两个参数均是），如replaceAll("\d", “")，而replace则不会，replace("\d","”)就是替换"\d"的字符串，而不会解析为正则
     * @return
     */
    static String ip4Invalid() {
        String address = "1.1.1.1";
        return address.replace("\\.", "[.]");
    }
}
