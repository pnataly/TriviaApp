package com.example.triviatest.data;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.triviatest.controller.AppController;
import com.example.triviatest.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    private ArrayList<Question> arrayQuestion = new ArrayList<>();
    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestions(final AnswerListAsyncResponse callBack) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url, (JSONArray) null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        for(int i =0; i<response.length(); i++){
                            try {
                                JSONArray jsonArray = response.getJSONArray(i);
                                Question question = new Question(jsonArray.get(0).toString(), jsonArray.getBoolean(1));
                                arrayQuestion.add(question);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(callBack != null){
                            callBack.processFinished(arrayQuestion);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return arrayQuestion;
    }
}
