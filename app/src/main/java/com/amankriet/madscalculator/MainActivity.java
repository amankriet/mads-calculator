package com.amankriet.madscalculator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.amankriet.madscalculator.databinding.ActivityMainBinding;
import com.amankriet.madscalculator.models.HistoryData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private final String NUMBER = "Number";
    private final String OPERATOR = "Operator";
    private final String DELETE = "Delete";
    public static String HISTORY_ARRAYLIST = "HistoryArrayList";
    public static String CALCULATION_HISTORY = "History";
    private boolean processed = false;
    public String operations = "";
    public String result = "";
    private ActivityMainBinding binding;
    public ArrayList<HistoryData> historyDataArrayList;
    Map<String, ArrayList<HistoryData>> calculatorHistoryData = new HashMap<>();

    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        historyDataArrayList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.menu_history) {
            Intent historyIntent = new Intent(this, HistoryActivity.class);
            historyIntent.putParcelableArrayListExtra(HISTORY_ARRAYLIST,
                    (ArrayList) historyDataArrayList);
            startActivity(historyIntent);
            return true;
        } else if(item.getItemId() == R.id.menu_logout) {
            mAuth.signOut();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    public void keyDown(View view) {
        switch (view.getId()) {
            case R.id.button_zero:
                setOperations(getResources().getString(R.string.zero), NUMBER);
                break;
            case R.id.button_one:
                setOperations(getResources().getString(R.string.one), NUMBER);
                break;
            case R.id.button_two:
                setOperations(getResources().getString(R.string.two), NUMBER);
                break;
            case R.id.button_three:
                setOperations(getResources().getString(R.string.three), NUMBER);
                break;
            case R.id.button_four:
                setOperations(getResources().getString(R.string.four), NUMBER);
                break;
            case R.id.button_five:
                setOperations(getResources().getString(R.string.five), NUMBER);
                break;
            case R.id.button_six:
                setOperations(getResources().getString(R.string.six), NUMBER);
                break;
            case R.id.button_seven:
                setOperations(getResources().getString(R.string.seven), NUMBER);
                break;
            case R.id.button_eight:
                setOperations(getResources().getString(R.string.eight), NUMBER);
                break;
            case R.id.button_nine:
                setOperations(getResources().getString(R.string.nine), NUMBER);
                break;
            case R.id.button_minus:
                setOperations(getResources().getString(R.string.minus), OPERATOR);
                break;
            case R.id.button_divide:
                setOperations(getResources().getString(R.string.divide), OPERATOR);
                break;
            case R.id.button_multiply:
                setOperations(getResources().getString(R.string.multiply), OPERATOR);
                break;
            case R.id.button_plus:
                setOperations(getResources().getString(R.string.plus), OPERATOR);
                break;
            case R.id.button_all_clear:
                clearOperations();
                break;
            case R.id.button_delete:
                setOperations(getResources().getString(R.string.delete), DELETE);
                break;
            case R.id.button_equals_to:
                calculateMADS();
                break;
            default:
                break;
        }
    }

    private void calculateMADS() {
        operations = binding.textViewOperations.getText().toString();
        String ops = operations;

        if(operations.matches(".*[-+×÷]$")) {
            operations = operations.substring(0, operations.length() - 1);
        }

        Pattern patternMultiply = Pattern.compile("\\d+×\\d+");
        Pattern patternAdd = Pattern.compile("\\d+\\+\\d+");
        Pattern patternDivide = Pattern.compile("\\d+÷\\d+");
        Pattern patternSub = Pattern.compile("\\d+-\\d+");

        Matcher matcher = patternMultiply.matcher(operations);
        while(matcher.find()) {
            Log.d(TAG, "calculateMADS: "+matcher.group());
            int num1 = Integer.parseInt(matcher.group().split("×")[0]);
            int num2 = Integer.parseInt(matcher.group().split("×")[1]);
            int res = num1*num2;
            operations = operations.replace(matcher.group(), String.valueOf(res));
            matcher = patternMultiply.matcher(operations);
        }

        Log.d(TAG, "calculateMADS: "+operations);
        matcher = patternAdd.matcher(operations);
        while(matcher.find()) {
            Log.d(TAG, "calculateMADS: "+matcher.group());
            int num1 = Integer.parseInt(matcher.group().split("\\+")[0]);
            int num2 = Integer.parseInt(matcher.group().split("\\+")[1]);
            int res = num1+num2;
            operations = operations.replace(matcher.group(), String.valueOf(res));
            matcher = patternAdd.matcher(operations);
        }

        Log.d(TAG, "calculateMADS: "+operations);
        matcher = patternDivide.matcher(operations);
        while(matcher.find()) {
            Log.d(TAG, "calculateMADS: "+matcher.group());
            int num1 = Integer.parseInt(matcher.group().split("÷")[0]);
            int num2 = Integer.parseInt(matcher.group().split("÷")[1]);
            int res = Math.floorDiv(num1, num2);
            operations = operations.replace(matcher.group(), String.valueOf(res));
            matcher = patternDivide.matcher(operations);
        }

        Log.d(TAG, "calculateMADS: "+operations);
        matcher = patternSub.matcher(operations);
        while(matcher.find()) {
            Log.d(TAG, "calculateMADS: "+matcher.group());
            int num1 = Integer.parseInt(matcher.group().split("-")[0]);
            int num2 = Integer.parseInt(matcher.group().split("-")[1]);
            int res = num1-num2;
            operations = operations.replace(matcher.group(), String.valueOf(res));
            matcher = patternSub.matcher(operations);
        }
        result = operations;

        if (historyDataArrayList.size() >= 10) {
            historyDataArrayList.remove(0);
        }
        historyDataArrayList.add(new HistoryData(ops, result));

        String dbCollectionName = HISTORY_ARRAYLIST;
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null && user.getEmail() != null) {
            dbCollectionName = user.getEmail();
        }
        calculatorHistoryData.put(dbCollectionName, historyDataArrayList);

        db.collection(CALCULATION_HISTORY)
                .document(HISTORY_ARRAYLIST).set(calculatorHistoryData);

//        db.collection(dbCollectionName)
//                .add(calculatorHistoryData)
//                .addOnSuccessListener(documentReference ->
//                        Log.d(TAG, "DocumentSnapshot added with ID: "
//                                + documentReference.getId()))
//                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

        binding.textViewResult.setText(result);
        processed = true;
    }

    private void clearOperations() {
        binding.textViewOperations.setText("");
        binding.textViewResult.setText("0");
        operations = "";
    }

    private void setOperations(String s, String type) {
        operations = binding.textViewOperations.getText().toString();

        Log.d(TAG, "setOperations: "+operations);
        if(type.contentEquals(NUMBER)) {
            binding.textViewOperations.setText(operations.concat(s));
            if(processed) {
                binding.textViewOperations.setText(s);
                binding.textViewResult.setText("0");
            }
        } else if(type.contentEquals(OPERATOR) && !operations.matches(".*[-+×÷]$")
                && !operations.contentEquals("")) {
            if(processed) {
                operations = result;
            }
            binding.textViewOperations.setText(operations.concat(s));
        } else if(type.contentEquals(DELETE)) {
            if(operations.length()>1) {
                binding.textViewOperations.setText(operations
                        .substring(0,operations.length()-1));
            } else {
                clearOperations();
            }

            if(processed) {
                binding.textViewResult.setText("0");
            }
        }
        processed = false;
    }
}