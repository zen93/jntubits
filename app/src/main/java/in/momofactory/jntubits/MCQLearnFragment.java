package in.momofactory.jntubits;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MCQLearnFragment extends Fragment {
    public final static String ATTEMPT_QUESTION = "in.momofactory.jntubits.ATTEMPTQUESTION";

    TextView questionTextView, correctTextView;
    ListView ansListView;
    Button button;

    String[] questionIds;
    int count;
    boolean attemptQuestion;

    public static MCQLearnFragment newInstance(boolean attemptQuestion, String[] questionIds, int pos) {
        MCQLearnFragment fragment = new MCQLearnFragment();
        Bundle args = new Bundle();
        args.putBoolean(ATTEMPT_QUESTION, attemptQuestion);
        args.putStringArray("questionIds", questionIds);
        args.putInt("position", pos);
        fragment.setArguments(args);
        return fragment;
    }

    public MCQLearnFragment() {   }

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        attemptQuestion = getArguments().getBoolean(ATTEMPT_QUESTION);
        questionIds = getArguments().getStringArray("questionIds");
        count = getArguments().getInt("position", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Title through bundle

        View rootView = inflater.inflate(R.layout.fragment_mcqlearn, container, false);
        questionTextView = (TextView) rootView.findViewById(R.id.mcqLearnQuestionTextView);
        correctTextView = (TextView) rootView.findViewById(R.id.mcqLearnCorrectTV);
        ansListView = (ListView) rootView.findViewById(R.id.mcqLearnAnswerChoicesList);
        button = (Button) rootView.findViewById(R.id.mcqLearnSubmitButton);

        loadNextQuestion();

        //if(attemptQuestion)
        correctTextView.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        /*else {
            attemptQuestion = false;
            button.setVisibility(View.VISIBLE);
            button.setText("Next");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadNextQuestion();
                }
            });
        }*/
        return rootView;
    }

    private void loadNextQuestion() {
        if(!(count < questionIds.length)) {
            return;
        }
        if(!(count < questionIds.length-1)) {
            button.setVisibility(View.GONE);
        }

        String question = "";
        List<String> answers = new ArrayList<String>();
        String correct = "";
        final DBHelper dbHelper;
        try {
            dbHelper = new DBHelper(getContext());
            question = dbHelper.getQuestion(questionIds[count]);
//            question = question.replace(":b:", "_____");
            answers = dbHelper.getAnswers(questionIds[count]);
            correct = dbHelper.getCorrectAnswer(questionIds[count]);
        }
        catch (IOException e) {
            Log.e("jntubits.", "Oops");
        }
        final String correctAnswer = correct;
        questionTextView.setText(question);
        String[] answer_choices = new String[answers.size()];
        answers.toArray(answer_choices);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, answer_choices);
        ansListView.setAdapter(adapter);

        ansListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                if(attemptQuestion) {
                    //((MCQ) getActivity()).replaceFragment(MCQLearnFragment.newInstance(false));
                    ansListView.setItemChecked(pos, true);
                    final String selectedAnswer = ansListView.getItemAtPosition(pos).toString();
                    if(selectedAnswer.contentEquals(correctAnswer)) {
                        try {
                            DBHelper dbHelper = new DBHelper(getContext());
                            dbHelper.setAnswered(questionIds[count], "1", "1");
                        }
                        catch(IOException e) {
                            Log.e("jntubits", "DB Error");
                        }
                        correctTextView.setText("Correct!");
                        correctTextView.setTextAppearance(getContext(), R.style.CorrectAnswerText);
                    }
                    else {
                        try {
                            DBHelper dbHelper = new DBHelper(getContext());
                            dbHelper.setAnswered(questionIds[count], "1", "0");
                        }
                        catch(IOException e) {
                            Log.e("jntubits", "DB Error");
                        }
                        correctTextView.setText("Incorrect! Correct answer: " + correctAnswer);
                        correctTextView.setTextAppearance(getContext(), R.style.IncorrectAnswerText);
                    }
                    correctTextView.setVisibility(View.VISIBLE);
                    attemptQuestion = false;
                    if(count < questionIds.length) {
                        count++;
                    }
                    if(count < questionIds.length)
                        button.setVisibility(View.VISIBLE);
                    button.setText("Next");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadNextQuestion();
                            button.setVisibility(View.GONE);
                            correctTextView.setVisibility(View.GONE);
                            attemptQuestion = true;
                        }
                    });

                }
                else {
                    //((MCQ) getActivity()).replaceFragment(MCQLearnFragment.newInstance(true));
                }
            }
        });
    }
}
