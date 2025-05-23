package com.nova.tools.utils.hutool.json.test.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: wangyan E-mail:wangyan@pospt.cn
 * @version 创建时间：2017年9月13日 下午5:16:32
 * 类说明
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PerfectEvaluationProductResVo extends ProductResBase {
    private static final long serialVersionUID = 1L;

    public static final Map<String, String> KEY_TO_KEY = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put("HA001", "CDCA005");
            put("HA002", "CDCA002");
            put("HA003", "CDCA004");
            put("HA004", "CSSP002");
            put("HB001", "CSWC001");
            put("HB002", "CSRL001");
            put("HB003", "CSRL003");
            put("HC005", "CDTB016");
            put("HC006", "CDTB020");
            put("HC007", "CDTB021");
            put("HC008", "CDTB022");
            put("HC009", "CDTB023");
            put("HC010", "CDTB024");
            put("HC011", "CDTB001");
            put("HC012", "CDTB005");
            put("HC013", "CDTB006");
            put("HC014", "CDTB007");
            put("HC015", "CDTB008");
            put("HC016", "CDTB009");
            put("HC001", "CDTB018");
            put("HC002", "CDTB003");
            put("HC003", "CDTB063");
            put("HC004", "CDTB060");
            put("HD001", "CDMC005");
            put("HD007", "CDMC082");
            put("HD010", "CDMC164");
            put("HD016", "CSSP003");
            put("HD017", "CSSS003");
            put("HE001", "CDTT028");
            put("HE002", "CDTT029");
            put("HE003", "CDTT030");
            put("HE004", "CDTT031");
            put("HE005", "CDTT032");
            put("HE006", "CDTT033");
            put("HE007", "CDTT015");
            put("HE008", "CDTT016");
            put("HE009", "CDTT017");
            put("HE010", "CDTT018");
            put("HE011", "CDTT019");
            put("HE012", "CDTT020");
            put("HE013", "CDTT055");
            put("HE014", "CDTT056");
            put("HE015", "CDTT057");
            put("HE016", "CDTT058");
            put("HE017", "CDTT059");
            put("HE018", "CDTT060");
            put("HE019", "CDTT043");
            put("HE020", "CDTT044");
            put("HE021", "CDTT045");
            put("HE022", "CDTT046");
            put("HE023", "CDTT047");
            put("HE024", "CDTT048");
            put("HE025", "CDTT080");
            put("HE026", "CDTT081");
            put("HE027", "CDTT082");
            put("HE028", "CDTT083");
            put("HE029", "CDTT084");
            put("HE030", "CDTT085");
            put("HE031", "CDTT067");
            put("HE032", "CDTT068");
            put("HE033", "CDTT069");
            put("HE034", "CDTT070");
            put("HE035", "CDTT071");
            put("HE036", "CDTT072");
            put("HF001", "CDTB272");
            put("HF002", "CDTB273");
            put("HF003", "CDTB277");
            put("HF004", "CDTB278");
            put("HF005", "CDTB282");
            put("HF006", "CDTB283");
            put("HF007", "CDTC058");
            put("HF008", "CDTC059");
            put("HF009", "CDTC060");
            put("HF010", "CDTC014");
            put("HG001", "CDMC294");
            put("HG002", "CDMC293");
            put("HG003", "CDMC301");
            put("HG004", "CDMC300");
            put("HG005", "CDMC308");
            put("HG006", "CDMC307");
            put("HG007", "CDMC315");
            put("HG008", "CDMC314");
            put("HG009", "CDMC322");
            put("HG010", "CDMC321");
            put("HG011", "CDMC287");
            put("HH001", "CDTT001");
            put("HH002", "CDTT002");
            put("HH003", "CDTT003");
            put("HH004", "CDTT004");
            put("HH005", "CDTT005");
            put("HH006", "CDTT006");
            put("HH007", "CDTT007");
            put("HH008", "CDTT008");
            put("HH009", "CDTT009");
            put("HH010", "CDTT010");
            put("HH011", "CDTT011");
            put("HH012", "CDTT012");
            put("HH013", "CDTT013");
            put("HH014", "CDTT014");
        }
    };

    private String XT_NO;
    private String CARD_HOLDER;
    private String CARD_NO;
    private String HA001;
    private String HA002;
    private String HA003;
    private String HA004;
    private String HB001;
    private String HB002;
    private String HB003;
    private String HC005;
    private String HC006;
    private String HC007;
    private String HC008;
    private String HC009;
    private String HC010;
    private String HC011;
    private String HC012;
    private String HC013;
    private String HC014;
    private String HC015;
    private String HC016;
    private String HC001;
    private String HC002;
    private String HC003;
    private String HC004;
    private String HD001;
    private String HD002;
    private String HD004;
    private String HD005;
    private String HD007;
    private String HD008;
    private String HD010;
    private String HD011;
    private String HD013;
    private String HD014;
    private String HD016;
    private String HD017;
    private String HE001;
    private String HE002;
    private String HE003;
    private String HE004;
    private String HE005;
    private String HE006;
    private String HE007;
    private String HE008;
    private String HE009;
    private String HE010;
    private String HE011;
    private String HE012;
    private String HE013;
    private String HE014;
    private String HE015;
    private String HE016;
    private String HE017;
    private String HE018;
    private String HE019;
    private String HE020;
    private String HE021;
    private String HE022;
    private String HE023;
    private String HE024;
    private String HE025;
    private String HE026;
    private String HE027;
    private String HE028;
    private String HE029;
    private String HE030;
    private String HE031;
    private String HE032;
    private String HE033;
    private String HE034;
    private String HE035;
    private String HE036;
    private String HF001;
    private String HF002;
    private String HF003;
    private String HF004;
    private String HF005;
    private String HF006;
    private String HF007;
    private String HF008;
    private String HF009;
    private String HF010;
    private String HG001;
    private String HG002;
    private String HG003;
    private String HG004;
    private String HG005;
    private String HG006;
    private String HG007;
    private String HG008;
    private String HG009;
    private String HG010;
    private String HG011;
    private String HG012;
    private String HG013;
    private String HG014;
    private String HG015;
    private String HG016;
    private String HH001;
    private String HH002;
    private String HH003;
    private String HH004;
    private String HH005;
    private String HH006;
    private String HH007;
    private String HH008;
    private String HH009;
    private String HH010;
    private String HH011;
    private String HH012;
    private String HH013;
    private String HH014;
    private String XT_MONTH01;
    private String XT_MONTH02;
    private String XT_MONTH03;
    private String XT_MONTH04;
    private String XT_MONTH05;
    private String XT_MONTH06;
}
