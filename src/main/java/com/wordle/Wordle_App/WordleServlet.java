package com.wordle.Wordle_App;

import com.wordle.Wordle_App.Logic.WordleHints;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/getHint")
public class WordleServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> guesses = new HashMap<>();
        String guess1 = request.getParameter("guess1").toLowerCase();
        String result1 = request.getParameter("result1").toLowerCase();
        if (guess1 != null && !guess1.equals("")) {
            guesses.put(guess1, result1);
        }
        String guess2 = request.getParameter("guess2").toLowerCase();
        String result2 = request.getParameter("result2").toLowerCase();
        if (guess2 != null && !guess2.equals("")) {
            guesses.put(guess2, result2);
        }
        String guess3 = request.getParameter("guess3").toLowerCase();
        String result3 = request.getParameter("result3").toLowerCase();
        if (guess3 != null && !guess3.equals("")) {
            guesses.put(guess3, result3);
        }
        String guess4 = request.getParameter("guess4").toLowerCase();
        String result4 = request.getParameter("result4").toLowerCase();
        if (guess4 != null && !guess4.equals("")) {
            guesses.put(guess4, result4);
        }
        String guess5 = request.getParameter("guess5").toLowerCase();
        String result5 = request.getParameter("result5").toLowerCase();
        if (guess5 != null && !guess5.equals("")) {
            guesses.put(guess5, result5);
        }

        List<String> hints = WordleHints.smoothInput(guesses);
        if (hints.size() > 0) {
            response.getWriter().println("Zack recommends you try this word: " + hints.get(0) + "\n");
            response.getWriter().println("Click the Back button to enter the results of this guess for an updated hint");
        } else {
            response.getWriter().println("Looks like we're out of possible hints. Verify your input is correct.");
        }
    }
}
