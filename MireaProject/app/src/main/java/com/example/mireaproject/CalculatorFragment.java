package com.example.mireaproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import java.math.BigInteger;
import java.text.ParseException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalculatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalculatorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int NONE = -1;
    private int opPosition = NONE;
    private String result = "";
    private TextView display;
    private boolean dividedByZero;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalculatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalculatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalculatorFragment newInstance(String param1, String param2) {
        CalculatorFragment fragment = new CalculatorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        final int DIGITS = 10;
        final int OPS = 4;
        super.onStart();
        FragmentActivity fa = getActivity();
        display = fa.findViewById(R.id.resultDisplay);
        int[] digitsIds = {R.id.button0, R.id.button1, R.id.button2,
                R.id.button3, R.id.button4, R.id.button5,
                R.id.button6, R.id.button7, R.id.button8, R.id.button9};
        for(int i = 0; i < DIGITS; ++i) {
            Button button = fa.findViewById(digitsIds[i]);
            button.setOnClickListener(this::onNumberClicked);
        }

        int[] opsIds = {R.id.buttonPlus, R.id.buttonMinus, R.id.buttonMult, R.id.buttonDivide};
        for(int i = 0; i < OPS; ++i) {
            Button button = fa.findViewById(opsIds[i]);
            button.setOnClickListener(this::onOpClicked);
        }

        fa.findViewById(R.id.buttonDelete).setOnClickListener(this::onDeleteClicked);
        fa.findViewById(R.id.buttonClear).setOnClickListener(this::onCancelClicked);
        fa.findViewById(R.id.buttonEquals).setOnClickListener(this::onCalcClicked);
    }

    private void updateDisplay() {
        display.setText(result);
    }

    private void checkDividedByZero() {
        if (dividedByZero) {
            result = "";
            dividedByZero = false;
            updateDisplay();
        }
    }

    private void onNumberClicked(View v) {
        checkDividedByZero();

        char digit = ((Button)v).getText().toString().charAt(0);
        result += digit;
        updateDisplay();
    }

    private void resultPopBack() {
        if (result.isEmpty())
            return;
        result = result.substring(0, result.length() - 1);
        if (opPosition >= result.length())
            opPosition = NONE;
    }

    private void onDeleteClicked(View v) {
        checkDividedByZero();

        resultPopBack();
        updateDisplay();
    }

    private void onCancelClicked(View v) {
        checkDividedByZero();

        result = "";
        opPosition = NONE;
        updateDisplay();
    }

    private void calcResult() {
        final BigInteger ZERO = new BigInteger("0");
        BigInteger a = new BigInteger(result.substring(0, opPosition));
        BigInteger b = new BigInteger(result.substring(opPosition + 1));
        char op = result.charAt(opPosition);

        switch (op) {
            case '+':
                result = a.add(b).toString();
                break;
            case '-':
                result = a.subtract(b).toString();
                break;
            case '*':
                result = a.multiply(b).toString();
                break;
            case '/':
                if (b.equals(new BigInteger("0"))) {
                    dividedByZero = true;
                    result = "Error";
                    break;
                }
                result = a.divide(b).toString();
                break;
            default:
                result = "Error";
                Log.wtf("calcResult()", "Unknown operation passed as argument");
        }

        opPosition = NONE;
    }

    private boolean tryRemoveTrailingOp() {
        boolean res = opPosition == (result.length() - 1);
        if (res) {
            resultPopBack();
            opPosition = NONE;
        }
        return res;
    }

    private void onOpClicked(View v) {
        checkDividedByZero();

        if (result.isEmpty()) {
            return;
        }

        tryRemoveTrailingOp();

        if (opPosition != NONE)
            calcResult();

        opPosition = result.length();
        char op = ((Button)v).getText().toString().charAt(0);
        result += op;
        updateDisplay();
    }

    private void onCalcClicked(View v) {
        checkDividedByZero();

        if (opPosition == NONE)
            return;

        if (!tryRemoveTrailingOp())
            calcResult();

        updateDisplay();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculator, container, false);
    }
}