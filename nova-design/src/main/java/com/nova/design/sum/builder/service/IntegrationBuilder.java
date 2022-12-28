package com.nova.design.sum.builder.service;


import com.nova.design.sum.builder.domain.IntegrationData;
import com.nova.design.sum.builder.domain.Member;
import com.nova.design.sum.builder.domain.Order;
import com.nova.design.sum.builder.domain.SelectedPlans;
import com.nova.design.sum.builder.domain.UserProfile;

/**
 * @author landyl
 * @create 10:16 AM 03/29/2018
 */
public interface IntegrationBuilder {

    UserProfile buildUserProfile() ;

    SelectedPlans buildSelectedPlans();

    Order buildOrder();

    Member buildMember() ;

    IntegrationData buildIntegrationData();

}
