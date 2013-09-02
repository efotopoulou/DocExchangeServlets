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
import org.apache.commons.codec.binary.Base64;


import java.security.SecureRandom;
import java.math.BigInteger;

public class Fetch extends HttpServlet {

    private SecureRandom random = new SecureRandom();
    private static String pdf_rep_path;



    public Fetch() throws IOException {
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

            //String docid = request.getParameter("docid");
            // to change it when post
            String docid = "elf039mfoif350384gLLLL898989";

            java.security.Principal user =  request.getUserPrincipal();
            String user_name = user.getName();

            EndUsersSingleton endUsers = EndUsersSingleton.getInstance();
            JSONObject EndUsers = endUsers.getEndUsers();

            //if OTP null call invoke getOTP servlet
            String OTP = EndUsers.getString(user_name);

            System.out.println("user_name: "+ user.getName() +" OTP  "+ OTP);


            String filepath = pdf_rep_path + docid + ".pdf";
            File file = new File(filepath);
            byte[] data =  getBytesFromFile(file);

            String base64FileString = Base64.encodeBase64String(data);
            String DocContentBase64Enc = AESencrp.encrypt(base64FileString);


            //have to convert it to byte array

           // return DocContentBase64Enc;




            //String DocContentBase64 = "abcd";

            //this value i will get from post
            //String DocContentBase64Enc = AESencrp.encrypt(password);


            String DocContentBase64Dec = AESencrp.decrypt(DocContentBase64Enc);

            //System.out.println("Plain Text : " + password);
            System.out.println("Encrypted Text : " + DocContentBase64Enc);
            System.out.println("Decrypted Text : " + DocContentBase64Dec);


            Map<String, String> data1 = new HashMap<String, String>();
            data1.put("docid", DocContentBase64Enc);
            out.write(new Gson().toJson(data1));



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

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }


}