/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shunting;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author anasollano
 */

public class ShuntingYard {
    
    //private final String operators = "-+/*^";
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
    
    public String convert(String input){
        //consider constraints! empty, null, no letters, no funny symbols
        
        String[] tokens = input.split("\\s");
        
        for(int i=0; i<tokens.length; i++){
            
            if(tokens[i].matches("\\d+")){
                output.add(tokens[i]);
            }
           else if(tokens[i].matches("[-,+,/,*,^]")){
                while(!operatorStack.isEmpty() && isGreater(operatorStack.peek(), tokens[i])){
                    output.add(operatorStack.pop());
                }
                operatorStack.push(tokens[i]);
            }
            else if(tokens[i].compareTo("(") == 0){
                operatorStack.push(tokens[i]);
            }
            else if(tokens[i].compareTo(")") == 0){
                while(operatorStack.peek().compareTo("(") != 0){ 
                   output.add(operatorStack.pop());
                } /* if the stack runs out without finding a left bracket, then there are
		mismatched parentheses. */
                operatorStack.pop();
            }
        }
        
        while(!operatorStack.isEmpty())
            output.add(operatorStack.pop());
        
        StringBuilder result = new StringBuilder();
        
        while(!output.isEmpty())
            result.append(output.pop()).append(' ');
        
        return result.toString();
    }
    
    private boolean isGreater(String top, String token){
        return ((operators.get(top) >= operators.get(token)) && (operators.get(top) != 2));
    }
}
