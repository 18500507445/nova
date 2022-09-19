package com.nova.design.builder;


import com.nova.design.builder.domain.Member;
import com.nova.design.builder.domain.Order;
import com.nova.design.builder.domain.SelectedPlans;
import com.nova.design.builder.domain.UserProfile;

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
