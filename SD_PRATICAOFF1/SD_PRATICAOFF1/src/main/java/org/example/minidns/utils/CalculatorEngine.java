package org.example.minidns.utils;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class CalculatorEngine {

    public static double calcExpression(String expression){
        Expression exp = new ExpressionBuilder(expression).build();
        return exp.evaluate();
    }
}
