package edu.dartmouth.cs.raunakbhojwani.myruns.servlets;


import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.dartmouth.cs.raunakbhojwani.myruns.data.ExerciseEntryDatastore;

/**
 * Created by RaunakBhojwani on 2/22/17.
 */

public class SyncServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String incomingJSONString = request.getParameter("JSONKey");

        if ((incomingJSONString == null || incomingJSONString.equals(""))) {
            return;
        }

        ExerciseEntryDatastore.deleteDatastore();
        try {

            JSONArray jsonArray;
            jsonArray = new JSONArray(incomingJSONString);

            for (int i = 0 ; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ExerciseEntryDatastore.addToDatastore(jsonObject);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
