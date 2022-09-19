package com.nova.design.builder;

import com.nova.design.builder.domain.IntegrationData;
import com.nova.design.builder.service.impl.XHFIntegrationBuilder;

/**
 * @author landyl
 * @create 5:17 PM 05/12/2018
 */
public class Test {

    public static void main(String[] args) {

        //create the data integration builder
        XHFIntegrationBuilder integrationBuilder = new XHFIntegrationBuilder();

        IntegrationDirector director = new IntegrationDirector();

        //build integration data object
        IntegrationData integrationData = director.buildIntegrationData(integrationBuilder);

        System.out.println(integrationData);

    }

}
