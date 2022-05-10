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
import com.amankriet.madscalculator.models.CalculatorHistoryList;
import com.amankriet.madscalculator.models.HistoryData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public static String CALCULATOR_HISTORY_LIST = "CalculatorHistoryList";
    public static String CALCULATION_HISTORY = "calculator-history";
    public static final String TAG = MainActivity.class.getSimpleName();
    private final String NUMBER = "Number";
    private final String OPERATOR = "Operator";
    private final String DELETE = "Delete";
    public static String[] patterns = {"\\d+×\\d+","\\d+\\+\\d+","\\d+÷\\d+","\\d+-\\d+"};
    public static String[] operators = {"×","\\+","÷","-"};
    public static String operations = "";
    public static String result = "";
    public CalculatorHistoryList calculatorHistoryList;
    List<HistoryData> historyDataList = new ArrayList<>();
    String dbCollectionName;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean processed = false;
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;

    public Map<String, CalculatorHistoryList> calculatorHistoryListMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        calculatorHistoryList = new CalculatorHistoryList();
        calculatorHistoryListMap = new HashMap<>();

        dbCollectionName = CALCULATOR_HISTORY_LIST;
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.getEmail() != null) {
            dbCollectionName = user.getEmail();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getCalculatorHistory();
    }

    private void getCalculatorHistory() {
        db.collection(CALCULATION_HISTORY)
                .document(dbCollectionName).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists() && Objects.requireNonNull(document.getData())
                                .get(CALCULATOR_HISTORY_LIST) != null) {
                            calculatorHistoryList = new Gson()
                                    .fromJson(Objects.requireNonNull(Objects
                                                    .requireNonNull(document.getData()).get(CALCULATOR_HISTORY_LIST))
                                            .toString(), CalculatorHistoryList.class);
                            if (calculatorHistoryList != null) {
                                calculatorHistoryListMap.put(CALCULATOR_HISTORY_LIST, calculatorHistoryList);
                                historyDataList = calculatorHistoryList.getHistoryDataList();
                            }
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });
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
            historyIntent.putExtra(CALCULATOR_HISTORY_LIST, calculatorHistoryList);
            startActivity(historyIntent);
            return true;
        } else if (item.getItemId() == R.id.menu_logout) {
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

        if (operations.contentEquals("")) {
            Log.d(TAG, "calculateMADS: "+ops);
            return;
        }

        if (operations.matches(".*[-+×÷]$")) {
            operations = operations.substring(0, operations.length() - 1);
        }

        performMADSCalculation();

        if (!(historyDataList == null || historyDataList.isEmpty()) &&
                historyDataList.size() >= 10) {
            while (historyDataList.size() >= 10) {
                calculatorHistoryList.getHistoryDataList().remove(0);
            }
        } else if (historyDataList == null) {
            historyDataList = new ArrayList<>();
        }
        historyDataList.add(new HistoryData(ops, result));
        calculatorHistoryList.setHistoryDataList(historyDataList);
        calculatorHistoryListMap.put(CALCULATOR_HISTORY_LIST, calculatorHistoryList);

        db.collection(CALCULATION_HISTORY)
                .document(dbCollectionName).set(calculatorHistoryListMap);

        binding.textViewResult.setText(result);
        processed = true;
    }

    public static void performMADSCalculation() {
        if (operations.matches("[Ero]+.*")) {
            result = "Error";
            return;
        }

        for (int i=0; i<patterns.length; i++) {
            Pattern pattern = Pattern.compile(patterns[i]);
            Matcher matcher = pattern.matcher(operations);
            while (matcher.find()) {
//                Log.d(TAG, "performMADSCalculation: " + matcher.group());
                int num1 = Integer.parseInt(matcher.group().split(operators[i])[0]);
                int num2 = Integer.parseInt(matcher.group().split(operators[i])[1]);
                int res;
                switch (operators[i]) {
                    case "×":
                        res = num1 * num2;
                        break;
                    case "\\+":
                        res = num1 + num2;
                        break;
                    case "÷":
                        if (num2 == 0) {
                            result = "Error";
                            return;
                        }
                        res = Math.floorDiv(num1, num2);
                        break;
                    default:
                        res = num1 - num2;
                        break;
                }
                operations = operations.replace(matcher.group(), String.valueOf(res));
                matcher = pattern.matcher(operations);
            }
        }
        if (operations.matches(".*[-+×÷]$")) {
            operations = operations.substring(0,operations.length()-1);
        }
        result = operations;
    }

    private void clearOperations() {
        binding.textViewOperations.setText("");
        binding.textViewResult.setText("0");
        operations = "";
    }

    private void setOperations(String s, String type) {
        operations = binding.textViewOperations.getText().toString();

        Log.d(TAG, "setOperations: " + operations);
        if (type.contentEquals(NUMBER)) {
            binding.textViewOperations.setText(operations.concat(s));
            if (processed) {
                binding.textViewOperations.setText(s);
                binding.textViewResult.setText("0");
            }
        } else if (type.contentEquals(OPERATOR) && !operations.contentEquals("")) {
            if (processed) {
                operations = result;
            }
            if (operations.matches(".*[-+×÷]$")) {
                operations = operations.substring(0,operations.length()-1).concat(s);
                binding.textViewOperations.setText(operations);
            } else {
                binding.textViewOperations.setText(operations.concat(s));
            }
        } else if (type.contentEquals(DELETE)) {
            if (operations.length() > 1) {
                binding.textViewOperations.setText(operations
                        .substring(0, operations.length() - 1));
            } else {
                clearOperations();
            }

            if (processed) {
                binding.textViewResult.setText("0");
            }
        }
        processed = false;
    }
}