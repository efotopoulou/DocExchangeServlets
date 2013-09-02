package org.ubi.maincontrol;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * An extension for the HTTPServletRequest that overrides the getUserPrincipal() and isUserInRole().
 *  We supply these implementations here, where they are not normally populated unless we are going through
 *  the facility provided by the container.
 * <p>If he user or roles are null on this wrapper, the parent request is consulted to try to fetch what ever the container has set for us.
 * This is intended to be created and used by the UserAuthFilter.
 * @author thein
 *
 */
public class UserRoleRequestWrapper extends HttpServletRequestWrapper {


    String user;
    List<String> roles = null;
    HttpServletRequest realRequest;

    public UserRoleRequestWrapper(String user, LinkedList<String> roles, HttpServletRequest request) {
        super(request);
        this.user = user;
        this.roles = roles;
        this.realRequest = request;
    }
    @Override
    public Principal getUserPrincipal() {
        if (this.user == null) {
            return realRequest.getUserPrincipal();
        }

        return new Principal() {
            @Override
            public String getName() {
                return user;
            }
        };
    }
    @Override
    public boolean isUserInRole(String role) {
        if (roles == null) {
            return this.realRequest.isUserInRole(role);
        }
        return roles.contains(role);
    }
}