package org.ubi.maincontrol;

import java.io.*;
import java.io.StringBufferInputStream;
import java.lang.String;
import java.lang.System;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import com.google.gson.Gson;


import java.security.SecureRandom;
import java.math.BigInteger;

public class getOTP extends HttpServlet {

    private SecureRandom random = new SecureRandom();
    private static String pdf_rep_path;



    public getOTP() throws IOException {
        Properties properties = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream in =classLoader.getResourceAsStream("DocExchangeProperties.properties");
        properties.load(in);
        this.pdf_rep_path= properties.getProperty("repo.path").toString();

    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String operation;
        HttpSession session;
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();


        session = request.getSession(true);
        try {

            java.security.Principal    user =  request.getUserPrincipal();
            String user_name = user.getName();

            System.out.println("user_name: "+ user.getName() +" dd  "+ user.toString()+ " skatoules?");

            String OTP = new BigInteger(130, random).toString(32);

            System.out.println("OTP"+OTP );

            EndUsersSingleton endUsers = EndUsersSingleton.getInstance();

            JSONObject EndUsers = endUsers.getEndUsers();
            System.out.println("EndUsers size before " + EndUsers.size());
            EndUsers.put(user.getName(),OTP);
            System.out.println("EndUsers size after" + EndUsers.size());



            Map<String, String> data = new HashMap<String, String>();
            data.put("userOTP", OTP);
            out.write(new Gson().toJson(data));

        } catch (Throwable t) {
            t.printStackTrace();
            this.forwardToPage("/error/generic_error.jsp?errormsg= " + t.getMessage(), request, response);
        } finally {
            out.close();
        }
    }


    private void forwardToPage(String url, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        RequestDispatcher dis;

        dis = getServletContext().getRequestDispatcher(url);
        dis.forward(req, resp);
        return;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    public void destroy() {
        //Shut down the scheduler before destroy the servlet
        EndUsersSingleton endUsers = EndUsersSingleton.getInstance();
        endUsers.shutdownScheduler();
    }


}