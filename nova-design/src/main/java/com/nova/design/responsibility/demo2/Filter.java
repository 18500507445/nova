package com.nova.design.responsibility.demo2;


import com.nova.design.responsibility.demo2.domain.Request;
import com.nova.design.responsibility.demo2.domain.Response;

/**
 * @author landyl
 * @create 2:15 PM 05/12/2018
 */
public interface Filter {

    public void doFilter(Request request, Response response, FilterChain filterChain);

}
