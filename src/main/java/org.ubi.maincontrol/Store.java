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


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import sun.misc.BASE64Decoder;


import java.security.SecureRandom;
import java.math.BigInteger;

public class Store extends HttpServlet {

    private SecureRandom random = new SecureRandom();
    private static String pdf_rep_path;



    public Store() throws IOException {
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

        PrintWriter out = response.getWriter();


        session = request.getSession(true);
        try {

            //String docid = request.getParameter("docid");
            //byte[] DocContentBase64Enc = request.getParameter("encryped_base64");

            String docid = "elf039mfoif350384gLLLL898989";
            String DocContentBase64Enc = "2t3jcv4Q9ze5CqJrLUnmIQ==";

            java.security.Principal user =  request.getUserPrincipal();
            String user_name = user.getName();

            EndUsersSingleton endUsers = EndUsersSingleton.getInstance();
            JSONObject EndUsers = endUsers.getEndUsers();

            //if OTP null call invoke getOTP servlet
            String OTP = EndUsers.getString(user_name);

            System.out.println("user_name: "+ user.getName() +" OTP  "+ OTP);


            //String DocContentBase64 = "abcd";

             //this value i will get from post
            //String DocContentBase64Enc = AESencrp.encrypt(password);


            String DocContentBase64Dec = AESencrp.decrypt(DocContentBase64Enc);

            //System.out.println("Plain Text : " + password);
            System.out.println("Encrypted Text : " + DocContentBase64Enc);
            System.out.println("Decrypted Text : " + DocContentBase64Dec);

            /*

            byte[] decoded = Base64.decodeBase64(encryped_base64);
            String decodedContent = new String(decoded, "UTF-8");
             */

            Document document = new Document(PageSize.A4, 36, 72, 108, 180);
            PdfWriter.getInstance(document, new FileOutputStream(pdf_rep_path + docid + ".pdf"));
            document.open();
            document.add(new Paragraph(DocContentBase64Dec));
            document.close();

            response.setStatus(response.SC_OK);



        } catch (Throwable t) {
            t.printStackTrace();
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR
            );
        } finally {
            out.close();
        }
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

    }


}