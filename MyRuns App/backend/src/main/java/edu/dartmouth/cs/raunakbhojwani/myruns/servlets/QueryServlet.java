package edu.dartmouth.cs.raunakbhojwani.myruns.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.dartmouth.cs.raunakbhojwani.myruns.data.ExerciseEntry;
import edu.dartmouth.cs.raunakbhojwani.myruns.data.ExerciseEntryDatastore;

/**
 * Created by RaunakBhojwani on 2/22/17.
 */

public class QueryServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        List<ExerciseEntry> retrievedEntries = ExerciseEntryDatastore.retrieveEntries();
        request.setAttribute("ALL_ENTRIES", retrievedEntries);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.write("<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n" +
                "<title>My Runs - Raunak Bhojwani</title>\n" +
                "</head>\n" +
                "<body>\n");

        out.write("<body>\n" +
                "      <div class=\"container\">\n" +
                "        <h1>MyRuns Entries</h1>\n" +
                "        <table class=\"table table-hover\">\n" +
                "          <thead>\n" +
                "            <tr>\n" +
                "              <th>ID</th>\n" +
                "              <th>Input Type</th>\n" +
                "              <th>Activity Type</th>\n" +
                "              <th>Date Time</th>\n" +
                "              <th>Duration</th>\n" +
                "              <th>Distance</th>\n" +
                "              <th>Calories</th>\n" +
                "              <th>Heart Rate</th>\n" +
                "              <th>Average Speed</th>\n" +
                "              <th>Climb</th>\n" +
                "              <th>Comment</th>\n" +
                "              <th>Action</th>\n" +
                "            </tr>\n" +
                "          </thead>\n" +
                "          <tbody>\n" +
                "           <tr>\n");

        if(retrievedEntries != null){
            for(ExerciseEntry entry : retrievedEntries) {
                out.write("<td>" + entry.mId + "</td>\n" +
                        "                    <td>" + entry.mInputType + "</td>\n" +
                        "                    <td>" + entry.mActivityType + "</td>\n" +
                        "                    <td>" + entry.formattedDate + "</td>\n" +
                        "                    <td>" + entry.mDuration + "</td>\n" +
                        "                    <td>" + entry.mDistance + "</td>\n" +
                        "                    <td>" + entry.mAvgSpeed + "</td>\n" +
                        "                    <td>" + entry.mCalorie + "</td>\n" +
                        "                    <td>" + entry.mClimb + "</td>\n" +
                        "                    <td>" + entry.mHeartRate + "</td>\n" +
                        "                    <td>" + entry.mComment + "</td>\n" +
                        "                    <td>\n" +
                        "                        <a href=/delete.do?mId=" + entry.mId + ">Delete</a>\n" +
                        "                    </td>\n" +
                        "                </tr>");
            }
        }

        out.write("\n" +
                "          </tbody>\n" +
                "        </table>\n" +
                "      </div>\n" +
                "    </body>");

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doGet(request, response);
    }

}
