package com.nova.design.builder;


import com.nova.design.builder.domain.Member;
import com.nova.design.builder.domain.Order;
import com.nova.design.builder.domain.SelectedPlans;
import com.nova.design.builder.domain.UserProfile;

/**
 * @author landyl
 * @create 11:16 AM 03/28/2018
 */
public class XHFIntegrationBuilder implements IntegrationBuilder {

    private IntegrationData integrationData;

    public XHFIntegrationBuilder() {
        this.integrationData = new IntegrationData();
    }

    @Override
    public UserProfile buildUserProfile() {
        System.out.println("构建用户信息");
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId("userId");
        userProfile.setUserName("username");
        integrationData.setUserProfile(userProfile);
        return userProfile;
    }

    @Override
    public SelectedPlans buildSelectedPlans() {
        System.out.println("构建计划信息");
        SelectedPlans selectedPlans = new SelectedPlans();
        selectedPlans.setPlanId("plan id");
        selectedPlans.setPlanName("plan name");
        integrationData.setSelectedPlans(selectedPlans);
        return selectedPlans;
    }

    @Override
    public Order buildOrder() {
        System.out.println("构建订单信息");
        Order order = new Order();
        order.setOrderId("order id");
        order.setOrderName("order name");
        integrationData.setOrder(order);
        return order;
    }

    @Override
    public Member buildMember() {
        System.out.println("构建订单成员信息");
        Member member = new Member();
        member.setMemberId("member id");
        member.setMemberName("member name");
        integrationData.setMember(member);
        return member;
    }

    @Override
    public IntegrationData buildIntegrationData() {
        return integrationData;
    }


}
