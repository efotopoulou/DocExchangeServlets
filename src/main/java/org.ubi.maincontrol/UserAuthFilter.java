package org.ubi.maincontrol;

import java.io.IOException;
import java.lang.String;
import java.lang.System;
import java.util.*;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * A servlet filter to replace the request object with our own request wrapper that implements our isUserInRole() method.
 * This makes the subsequent (jsp page, servlet) our handler to read the database information for the user when isUserInRole() is called.

 * <p>This should be installed as a filter mapped to /* events. Or maybe *.jsp and *.do, etc. any where we would want to call the isUserInRole method.</p>
 * <p>Depends on the profile manager</p>
 *
 * @author thein
 *
 */
public class UserAuthFilter implements Filter {

    public void init(FilterConfig cfg) throws ServletException {

    }

    public void doFilter(ServletRequest req, ServletResponse response,
                         FilterChain next) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        //String user = "efotopoulou";

        String user = (String) request.getParameter("user");

        if (user==null)   user = "efotopoulou";
        System.out.println("user" + user);

        LinkedList<String> roles = new LinkedList<String>();
        roles.add("manager");


        next.doFilter(new UserRoleRequestWrapper(user, roles, request), response);
    }

    public void destroy() {


    }
}
