package in.momofactory.jntubits;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class FBLearnFragment extends Fragment {
    TextView questionTextView, correctTextView;
    ListView ansListView;
    Button button;

    String[] questionIds;
    int count;
    boolean attemptQuestion;

    public static FBLearnFragment newInstance(boolean attemptQuestion, String[] questionIds, int pos) {
        FBLearnFragment fragment = new FBLearnFragment();
        Bundle args = new Bundle();
        args.putBoolean("attemptQuestion", attemptQuestion);
        args.putStringArray("questionIds", questionIds);
        args.putInt("position", pos);
        fragment.setArguments(args);
        return fragment;
    }


    public FBLearnFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        attemptQuestion = getArguments().getBoolean("attemptQuestion");
        questionIds = getArguments().getStringArray("questionIds");
        count = getArguments().getInt("position", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        //Title through bundle

        View rootView = inflater.inflate(R.layout.fragment_fblearn, container, false);

        questionTextView = (TextView) rootView.findViewById(R.id.fbLearnQuestionTextView);
        correctTextView = (TextView) rootView.findViewById(R.id.fbLearnCorrectTV);
        ansListView = (ListView) rootView.findViewById(R.id.fbLearnAnswersList);
        button = (Button) rootView.findViewById(R.id.fbLearnSubmitButton);
        loadNextQuestion();

        return rootView;
    }
    private boolean checkAnswers(String[] correctAnswers, String[] userAnswers) {
        for(int i=0;i<correctAnswers.length;i++) {
            if(!userAnswers[i].trim().equalsIgnoreCase(correctAnswers[i])) {
                return false;
            }
        }
        return true;
    }
    private void loadNextQuestion() {
        if(!(count < questionIds.length)) {
            return;
        }
        Log.i("jntubits", "Len: " + questionIds.length);
        Log.i("jntubits", "AttemptQ: " + attemptQuestion);
        Log.i("jntubits", "Count: " + count);
        if((count >= questionIds.length) && !attemptQuestion) {
            button.setVisibility(View.GONE);
        }

        String question = "";
        List<String> answers = new ArrayList<String>();
        DBHelper dbHelper;

        try {
            dbHelper = new DBHelper(getContext());
            question = dbHelper.getQuestion(questionIds[count]);
            answers = dbHelper.getAnswers(questionIds[count]);
        }
        catch (IOException e) {
            Log.e("jntubits.", "Oops");
        }

        questionTextView.setText(question);

        final String[] answer_choices = new String[answers.size()];
        answers.toArray(answer_choices);
        final String[] user_answers = new String[answers.size()];

        for(int i=0; i<answers.size();i++) {
            user_answers[i] = "";
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, answer_choices) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View row;

                if (null == convertView) {
                    row = getLayoutInflater(getArguments()).inflate(android.R.layout.simple_list_item_1, null);
                } else {
                    row = convertView;
                }

                TextView tv = (TextView) row.findViewById(android.R.id.text1);
                if(attemptQuestion) {
                    //tv.setText(Html.fromHtml(getItem(position)));
                    tv.setText(Html.fromHtml((position+1) +". _____"));
                    //tv.setText(Html.fromHtml((position+1)+ "<u>" + correctAnswer + "</u>"));
                } else {

                    String correctAnswer = answer_choices[position];
                    tv.setText(Html.fromHtml((position+1)+ ". <u>" + correctAnswer + "</u>"));
                    if(user_answers[position].trim().equalsIgnoreCase(correctAnswer)) {
                        tv.setTextAppearance(getContext(), R.style.CorrectAnswerText);
                    } else {
                        tv.setTextAppearance(getContext(), R.style.IncorrectAnswerText);
                    }
                }
                //tv.setText(getItem(position));

                return row;
            }

        };
        ansListView.setAdapter(adapter);
        ansListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int pos, long id) {
                        final View tv = view;

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Input Answer");
                        // Set up the input
                        final EditText input = new EditText(getContext());
                         // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_TEXT );
                        builder.setView(input);
                        // Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                user_answers[pos] = input.getText().toString();
                                ((TextView)tv).setText(Html.fromHtml((pos+1)+". <u>" +user_answers[pos] + "</u>"));
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                }
        );

        button.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              if(attemptQuestion) {
                                                  attemptQuestion = false;
                                                  ansListView.setAdapter(adapter);
                                                  if(checkAnswers(answer_choices, user_answers)) {
                                                      try {
                                                          DBHelper dbHelper = new DBHelper(getContext());
                                                          dbHelper.setAnswered(questionIds[count], "1", "1");
                                                      }
                                                      catch(IOException e) {
                                                          Log.e("jntubits", "DB Error");
                                                      }
                                                      correctTextView.setText("Correct!");
                                                      correctTextView.setTextAppearance(getContext(), R.style.CorrectAnswerText);
                                                  } else {
                                                      try {
                                                          DBHelper dbHelper = new DBHelper(getContext());
                                                          dbHelper.setAnswered(questionIds[count], "1", "0");
                                                      }
                                                      catch(IOException e) {
                                                          Log.e("jntubits", "DB Error");
                                                      }
                                                      correctTextView.setText("Incorrect!");
                                                      correctTextView.setTextAppearance(getContext(), R.style.IncorrectAnswerText);
                                                  }
                                                  if(count >= questionIds.length - 1) {
                                                      button.setVisibility(View.GONE);
                                                  }
                                                  button.setText("Next");
                                                  if(count < questionIds.length) {
                                                      count++;
                                                  }
                                              }
                                              else {
                                                  attemptQuestion = true;
                                                  correctTextView.setText("");
                                                  button.setText("Submit");
                                                  loadNextQuestion();
                                              }
                                          }
                                      }

        );
    }
}
