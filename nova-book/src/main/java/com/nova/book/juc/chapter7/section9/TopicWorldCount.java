package com.nova.book.juc.chapter7.section9;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

/**
 * @description: 单词统计
 * @author: wzh
 * @date: 2023/3/31 08:32
 */
@Slf4j(topic = "WorldCount")
class TopicWorldCount {

    /**
     * 模拟五组字符串，放入list中
     */
    static final String WORLD_1 = "mkdxcyzuslpfbwtjagerhqnviovhruuqyypxsxidgcbvdmuwdmefxyncdfdxrphpmuvvdyaotoguqbhdjwcwfanhowmludlaoorqwchidbsjyfeqihownpxkrbpdffgmtbuclpvvonoxodchvhuftthenxoewxhgavxauizlyudplugbllsmjawhoiyavbqmkhhsdqgxvkeeptcmmogtkrjscrdafflztuftdmiwkibhdpawzrbyxtnoiztaacjt";
    static final String WORLD_2 = "yidszfvmlrkgnptxejohwbuaqcghohmsggbradaxrnrigxxekgfatvqlebghzfbibgymsadlhtefiibdajjxvgjahyagpiakqavfwwxvynqmcujouarrrygaijalygmzdkwnevhkkpguiejaqbiudnljvcqoaaampjxvdviiboqxtgjubzvoywkwadpxftzlqryidemidwzpzhoqwbortaxazxxebaobfuvidlwqnokqvmsnwxtijxkzptqhxkyu";
    static final String WORLD_3 = "idxmrleavbowsfhgcputqnzkyjhrdofjtnyypxjuftvgyuezggwlcthpmirjkeokmbqcrxjcfeaunkkxvttupokffzdihqubwiklxcvvwtkcqrnlkhckrbfjlxfcwgqmzbcujxupmijsgscubejwmtmqwltrvmaycggvfjpithsmrjrcdxjfugnmdrtqslhxkrlgxtrzfurhmwfaixdekaicgmuxykpucmcbfrzhxtwvagwaeofrwkjfqdblxvwg";
    static final String WORLD_4 = "imjctybdhfgwqvlouspxznakergjiulwjxvdzamnihymufocwglcyqiiyofgalwjwnxnfkggdgtznrbfrxhccmeriduvbxlgugbyzesbtztfxyhiavboxfzldbfqiblylnypyaslaksklprgtucxstvielhyiutmikntrpwtmfjqwplszgbzemxcedslgljoigpwqiiuuhshndpuictryuzduulhxweuczcnjohogywzfnhkuqtewvichyhqlcpm";
    static final String WORLD_5 = "xutlbkomszfrwycgapvijnhqedakaltfgjicivehjdoxbwgvkpvpctgwxymuvzzszmqoiieitevnefncvdioorvvpcooqkpylaycjxnbjxvbkbbzdurqwwzypbbiehrusucsobixtyoowcyhuxwvcbdzmpmubvrbwxsxsabmeotydizcyhiaemzuduhjgtppzeqwmhfdpowfbcvtslwwhytrvwzvktturkbgtaupulmfxkzlcusuurnxdibylxsx";

    static List<String> WORLD_LIST = new ArrayList<>();

    static {
        WORLD_LIST.add(WORLD_1);
        WORLD_LIST.add(WORLD_2);
        WORLD_LIST.add(WORLD_3);
        WORLD_LIST.add(WORLD_4);
        WORLD_LIST.add(WORLD_5);
    }

    /**
     * 开启五个线程，统计5个任务中的26个字母出现的频率
     *
     * @param args
     */
    public static void main(String[] args) {
        final TimeInterval timer = DateUtil.timer();
        ConcurrentHashMap<String, LongAdder> hashMap = new ConcurrentHashMap<>(16);
        final ExecutorService pool = Executors.newFixedThreadPool(5);

        for (String s : WORLD_LIST) {
            pool.submit(() -> {
                for (char aChar : s.toCharArray()) {
                    //如果没有key，生成一个key、value，放入map
                    LongAdder count = hashMap.computeIfAbsent(String.valueOf(aChar), (key) -> new LongAdder());
                    count.increment();
                }
            });
        }
        pool.shutdown();
        System.out.println("耗时：" + timer.interval() + " ms");
        System.out.println(JSONUtil.toJsonStr(hashMap));
    }

}
