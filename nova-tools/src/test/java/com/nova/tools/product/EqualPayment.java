package com.nova.tools.product;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EqualPayment {
    // 支付记录结构

    @Data
    public static class PaymentRecord {
        private String productName;
        private Map<String, BigDecimal> paymentDetails; // 账户→金额
    }

    /**
     * 处理支付拆分和退款冲销全流程
     *
     * @param products 商品金额映射（商品名→金额）
     * @param payments 支付账户金额映射（账户名→金额）
     * @param refunds  退款商品映射（商品名→退款金额）
     * @return 处理后各商品的净支付明细
     */
    public static List<PaymentRecord> process(
            Map<String, BigDecimal> products,
            Map<String, BigDecimal> payments,
            Map<String, BigDecimal> refunds) {

        // 阶段1：正向支付拆分
        List<PaymentRecord> records = splitPayments(products, payments);

        // 阶段2：逆向退款冲销
        applyRefunds(records, refunds);

        return records;
    }

    // 支付拆分（等比分配）
    private static List<PaymentRecord> splitPayments(Map<String, BigDecimal> products, Map<String, BigDecimal> payments) {
        BigDecimal totalAmount = products.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal paymentTotal = payments.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalAmount.compareTo(paymentTotal) != 0) {
            throw new IllegalArgumentException("支付总额与商品总额不匹配");
        }

        List<PaymentRecord> records = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : products.entrySet()) {
            PaymentRecord record = new PaymentRecord();
            record.setProductName(entry.getKey());
            BigDecimal productAmount = entry.getValue();

            // 计算各支付账户的分摊金额
            Map<String, BigDecimal> paymentDetails = new LinkedHashMap<>();
            for (Map.Entry<String, BigDecimal> payment : payments.entrySet()) {
                BigDecimal ratio = payment.getValue().divide(totalAmount, 10, RoundingMode.HALF_UP);
                BigDecimal splitAmount = productAmount.multiply(ratio).setScale(2, RoundingMode.HALF_UP);
                paymentDetails.put(payment.getKey(), splitAmount);
                record.setPaymentDetails(paymentDetails);
            }
            records.add(record);
        }
        return records;
    }

    // 退款冲销（按原支付比例逆向）
    private static void applyRefunds(List<PaymentRecord> records, Map<String, BigDecimal> refunds) {
        for (PaymentRecord record : records) {
            BigDecimal refundAmount = refunds.getOrDefault(record.getProductName(), BigDecimal.ZERO);
            if (refundAmount.compareTo(BigDecimal.ZERO) <= 0) continue;
            // 计算该商品原支付总额
            BigDecimal totalPaid = record.getPaymentDetails().values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

            // 按原支付比例冲销
            for (Map.Entry<String, BigDecimal> entry : record.getPaymentDetails().entrySet()) {
                BigDecimal originalRatio = entry.getValue().divide(totalPaid, 10, RoundingMode.HALF_UP);
                BigDecimal refundPortion = refundAmount.multiply(originalRatio).setScale(2, RoundingMode.HALF_UP);
                entry.setValue(entry.getValue().subtract(refundPortion));
            }
        }
    }

    // 测试案例
    public static void main(String[] args) {
        // 商品定义（商品名→价格）
        Map<String, BigDecimal> products = new LinkedHashMap<>();
        products.put("手机", new BigDecimal("20"));
        products.put("耳机", new BigDecimal("30"));
        products.put("保护壳", new BigDecimal("50"));

        // 支付账户（账户名→金额）
        Map<String, BigDecimal> payments = new LinkedHashMap<>();
        payments.put("账户a", new BigDecimal("56"));
        payments.put("账户b", new BigDecimal("23.5"));
        payments.put("微信", new BigDecimal("20.5"));

        // 退款申请（商品名→退款金额）
        Map<String, BigDecimal> refunds = new LinkedHashMap<>();
        refunds.put("耳机", new BigDecimal("30")); // 全额退耳机

        // 执行处理流程
        List<PaymentRecord> results = process(products, payments, refunds);

        // 打印净支付明细
        System.out.println("【最终净支付明细】");
        for (PaymentRecord record : results) {
            System.out.printf("%-8s: %s\n", record.productName, record.paymentDetails.entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue() + "元")
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("无支付"));
        }
    }
}