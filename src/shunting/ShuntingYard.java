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
    
    //private final String operators = "-+/*^";
    private HashMap<String,Integer> operators;
    private LinkedList<String> output;
    private Stack<String> operatorStack;
    private LinkedList<Stack> stacks;
    
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
        stacks = new LinkedList<Stack>();
    }
    
    public String convert(String input){
        //constraints
        if(input.isEmpty()){ //input.matches("^[\\d| -| +| *| /| ^]" 
            return "Please, enter a valid input.";
        }
        String[] tokens = input.split("\\s");
        //int n = steps == 0? tokens.length : steps;
        
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
                try{
                    while(operatorStack.peek().compareTo("(") != 0)
                       output.add(operatorStack.pop());
                    operatorStack.pop();
                } catch (EmptyStackException e){
                    String error = "Mismatched parentheses!";
                    System.err.println(error);
                    return error;
                }
            }
            stacks.add(operatorStack);
        }
        while(!operatorStack.isEmpty()){
            try{
                if(operatorStack.peek().compareTo("(") != 0)
                    output.add(operatorStack.pop());
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
    
    public String convertBySteps(String input, int n){
        String[] result = convert(input).split("\\s");
        try{
            return result[n];
        }
        catch(IndexOutOfBoundsException ioobe){
            return " final expression";
        }
    }
    
    public String showStack(int n){ //constraints!!
        Stack s = stacks.get(n);
        StringBuilder result = new StringBuilder();
        try{
            result.append(s.pop()).append("\\n");
        }
        catch (EmptyStackException e){
            return "Empty";
        }
        return result.toString();
    }
    
    private boolean isGreater(String top, String token){
        return ((operators.get(top) >= operators.get(token)) && (operators.get(top) != 2));
    }
}
