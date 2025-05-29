/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.grammartocnf;
import java.util.*;

public class Rule {
    //assential attriputes of our rule class
    private String left;
    private List<String> right;
    
    //parameterize constructor to give inisial value 
    public Rule(String left , List<String> right)
    {
       this.left = left ;
       this.right = right ;
    }
    
    // Getters and setters
    public String getLeft()
    {
        return left;
    }

    public List<String> getRight() 
    {
        return right;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public void setRight(List<String> right) {
        this.right = right;
    }
    
    //printing the right side of rule to user
    public void printRight()
    {
        int rSize = right.size();
        for(int i=0 ; i<rSize; i++)
        {
            System.out.print(right.get(i));
            if(i!=rSize-1)
            {
                System.out.print("|");
            }
        }
    }
    
    //printing all the of rules thet we have in list of rules
    public static void printRules(List<Rule> rules )
    {
        for(int i=0 ; i<rules.size(); i++)
        {
            Rule rule = rules.get(i);
            System.out.print(rule.getLeft() + " -> ");
            rule.printRight();
            System.out.println("");
        }
    }
    
    //deleting similar items in rules if there are 
    public static void deleteSimilar(List<Rule> rules)
    {
        for (int i = 0; i < rules.size(); i++) {
            Rule rule = rules.get(i);
            List<String> right = rule.getRight();
            for(int j=0 ; j< right.size() ; j++)
            {
                for(int k=j+1 ; k<right.size() ; k++)
                {
                    if((right.get(j).equals(right.get(k)))||(right.get(k).equals("ep")))
                    {
                        right.remove(k);
                        k--;
                    }
                }
            }
        }
    }
}