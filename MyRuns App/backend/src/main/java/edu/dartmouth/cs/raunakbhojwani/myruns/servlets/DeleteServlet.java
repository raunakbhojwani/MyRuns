package edu.dartmouth.cs.raunakbhojwani.myruns.servlets;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.dartmouth.cs.raunakbhojwani.myruns.MessagingEndpoint;
import edu.dartmouth.cs.raunakbhojwani.myruns.data.ExerciseEntryDatastore;

/**
 * Created by RaunakBhojwani on 2/22/17.
 */

public class DeleteServlet extends HttpServlet {

    private static final String TAG = "DebugTag";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String Id = request.getParameter("mId");

        if (Id == null || Id.equals("")) {
            return;
        }

        ExerciseEntryDatastore.deleteId(Id);

        MessagingEndpoint deleteMsg = new MessagingEndpoint();
        deleteMsg.sendMessage("DeleteThis:"+Id);

        response.sendRedirect("/query.do");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doGet(request, response);
    }
}
