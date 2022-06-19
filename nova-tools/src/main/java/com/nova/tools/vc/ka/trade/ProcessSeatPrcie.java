package com.nova.tools.vc.ka.trade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2019/5/28 10:44
 */

public class ProcessSeatPrcie {

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    public static List<String> fullReductionProcess(String ticketPrice, ActivityData data, int count) {
        List<String> strings = new ArrayList<>();
        List<DataList> dataList1 = data.getTicketSupplementAttributeData();
        List<DataList> dataList = dataList1.stream().sorted(Comparator.comparing(DataList::getActivityLimit).reversed()).collect(Collectors.toList());
        for (int i = 1; i < 5; i++) {
            BigDecimal price = ZERO;
            for (DataList listData : dataList) {
                if (i > count) {
                    BigDecimal totalPrice = new BigDecimal(ticketPrice).multiply(new BigDecimal(count));
                    if (totalPrice.compareTo(new BigDecimal(listData.getActivityLimit()).divide(new BigDecimal(100))) > -1) {
                        price = (new BigDecimal(ticketPrice).multiply(new BigDecimal(i))).subtract(new BigDecimal(listData.getActivityLimitValue()).divide(new BigDecimal(100)));
                        break;
                    } else {
                        price = new BigDecimal(ticketPrice).multiply(new BigDecimal(i));
                    }
                } else {
                    BigDecimal totalPrice = new BigDecimal(ticketPrice).multiply(new BigDecimal(i));
                    if (totalPrice.compareTo(new BigDecimal(listData.getActivityLimit()).divide(new BigDecimal(100))) > -1) {
                        price = (new BigDecimal(ticketPrice).multiply(new BigDecimal(i))).subtract(new BigDecimal(listData.getActivityLimitValue()).divide(new BigDecimal(100)));
                        break;
                    } else {
                        price = new BigDecimal(ticketPrice).multiply(new BigDecimal(i));
                    }
                }
            }
            strings.add(checkPrice(price));
        }
        return strings;
    }


    public static List<String> fullReductionProcess1(String ticketPrice, ActivityData data, int count) {
        List<String> strings = new ArrayList<>();
        List<DataList> dataList = data.getTicketSupplementAttributeData();
        for (int i = 1; i < 5; i++) {
            if (i > count) {
                BigDecimal activityNum = (new BigDecimal(ticketPrice).multiply(new BigDecimal(count))).divide(new BigDecimal(dataList.get(0).getActivityLimit()).divide(new BigDecimal(100))).setScale(0, BigDecimal.ROUND_DOWN);
                BigDecimal offerAmount = activityNum.multiply(new BigDecimal(dataList.get(0).getActivityLimitValue())).divide(new BigDecimal(100));
                BigDecimal totalPrice = (new BigDecimal(ticketPrice).multiply(new BigDecimal(i))).subtract(offerAmount);
                strings.add(checkPrice(totalPrice));
            } else {
                BigDecimal activityNum = (new BigDecimal(ticketPrice).multiply(new BigDecimal(i))).divide(new BigDecimal(dataList.get(0).getActivityLimit()).divide(new BigDecimal(100))).setScale(0, BigDecimal.ROUND_DOWN);
                BigDecimal offerAmount = activityNum.multiply(new BigDecimal(dataList.get(0).getActivityLimitValue())).divide(new BigDecimal(100));
                BigDecimal totalPrice = (new BigDecimal(ticketPrice).multiply(new BigDecimal(i))).subtract(offerAmount);
                strings.add(checkPrice(totalPrice));
            }
        }
        return strings;
    }


    private static List<String> verticalReductionProcess(String ticketPrice, ActivityData data, int count) {
        String[] areaNo = {"1", "2", "3", "4"};
        List<String> strings = new ArrayList<>();
        DataList listData = data.getTicketSupplementAttributeData().get(0);
        BigDecimal price;
        if (2 == (data.getActivityType())) {
            price = new BigDecimal(listData.getActivityLimitValue()).divide(new BigDecimal(100));
        } else {
            price = new BigDecimal(ticketPrice).subtract(new BigDecimal(listData.getActivityLimitValue()).divide(new BigDecimal(100)));
        }

        //优惠后单价低于0元，单价为0
        String s = checkPrice(price);

        BigDecimal totalPrice = ZERO;

        for (int i = 0; i < 4; i++) {
            if (i + 1 > count) {
                totalPrice = totalPrice.add(new BigDecimal(ticketPrice));
                strings.add(checkPrice(totalPrice));
            } else {
                totalPrice = new BigDecimal(s).multiply(new BigDecimal(areaNo[i]));
                strings.add(checkPrice(totalPrice));
            }
        }
        return strings;
    }

    /**
     * 和0做校验并且末尾舍弃多余0
     *
     * @param price
     * @return
     */
    private static String checkPrice(BigDecimal price) {
        if (price.compareTo(ZERO) <= 0) {
            price = ZERO;
        }
        return price.stripTrailingZeros().toPlainString();
    }

    public static void main(String[] args) {
        ActivityData data = new ActivityData();
        data.setActivityType(3);
        ArrayList<DataList> list = new ArrayList<>();
        DataList listData1 = new DataList();
        listData1.setActivityLimit(1000L);
        listData1.setActivityLimitValue(1990L);
        DataList listData2 = new DataList();
        listData2.setActivityLimit(2000L);
        listData2.setActivityLimitValue(2500L);
        DataList listData3 = new DataList();
        listData3.setActivityLimit(2500L);
        listData3.setActivityLimitValue(3000L);
        list.add(listData1);
        list.add(listData2);
        list.add(listData3);
        data.setTicketSupplementAttributeData(list);
        List<String> strings = verticalReductionProcess("25", data, 4);

//        List<String> strings = verticalReductionProcess("50", data, 4);
        for (int i = 0; i < strings.size(); i++) {
            System.out.println(strings.get(i));
        }

    }
}
