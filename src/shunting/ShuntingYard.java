/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shunting;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author anasollano
 */

public class ShuntingYard {
    
    private HashMap<String,Integer> operators;
    private LinkedList<String> output;
    private Stack<String> operatorStack;

    public ShuntingYard(){
        operators = new HashMap<String,Integer>(){{
            put("-",new Integer(0));
            put("+",new Integer(0));
            put("/",new Integer(1));
            put("*",new Integer(1));
            put("^",new Integer(2));
            put("(",new Integer(-1));
            put(")",new Integer(-1));
        }};
        output = new LinkedList<String>();
        operatorStack = new Stack<String>();
    }
    
    private void convert(String input){
        String[] tokens = input.split("\\s");
        
        for (String token : tokens) {
            if (token.matches("\\d+")) {
                output.add(token);
            } else if (token.matches("[-,+,/,*,^]")) {
                while (!operatorStack.isEmpty() && isGreater(operatorStack.peek(), token)) {
                    output.add(operatorStack.pop());
                }
                operatorStack.push(token);
            } else if (token.compareTo("(") == 0) {
                operatorStack.push(token);
            } else if (token.compareTo(")") == 0) {
                try{
                    while(operatorStack.peek().compareTo("(") != 0)
                        output.add(operatorStack.pop());
                    operatorStack.pop();
                } catch (EmptyStackException e){
                    System.err.println("Mismatched parentheses!");
                }
            }
            String temp = "";
            for(int h=0; h<operatorStack.size();h++){
                temp = temp + operatorStack.get(h) + " ";
            }
        }
    }
    
    public String convertFull(String input){
        if(input.isEmpty()){
            return "Please, enter a valid input.";
        }
        convert(input);
        return showFullResult();
    }
    
    //returns [0]: output, [1]: operator stack
    public String[] convertBySteps(String input, int n){
        if(input.isEmpty())
            return new String[]{"Please, enter a valid input.", ""};
        
        output = new LinkedList<String>();
        operatorStack = new Stack<String>();
        String[] tokens = input.split("\\s");
        
        if(n < tokens.length){
            StringBuilder token = new StringBuilder();
            for(int i=0; i<=n;i++)
                token.append(tokens[i]).append(" ");
            convert(token.toString());
            return showPartialResult();
        }
        else{
            convert(input);
            return new String[] {showFullResult(), ""};
        }
    }
    
    private String showFullResult(){
        while(!operatorStack.isEmpty()){
            try{
                if(operatorStack.peek().compareTo("(") != 0){
                    output.add(operatorStack.pop());
                }
                else
                    throw new Exception();
            }
            catch(Exception e){
                String error = "Mismatched parentheses!";
                System.err.println(error);
                return error;
            }
        }
        StringBuilder result = new StringBuilder();
        
        while(!output.isEmpty())
            result.append(output.pop()).append(' ');
        
        return result.toString();
    }
    
    private String[] showPartialResult(){
        StringBuilder[] r = new StringBuilder[2];
        r[0] = new StringBuilder("");
        r[1] = new StringBuilder("");
        
        for(int i=0; i<output.size(); i++){
            r[0].append(output.get(i)).append(" ");
        }
        for(int i=0; i<operatorStack.size(); i++){
            r[1].insert(0, " ").insert(0,operatorStack.get(i));
        }
        return new String[] {r[0].toString(), r[1].toString()};
    }
    
    private boolean isGreater(String top, String token){
        return ((operators.get(top) >= operators.get(token)) && (operators.get(top) != 2));
    }
}