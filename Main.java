/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.grammartocnf;
import java.util.*;// to using List and ArrayList

public class Main {
    public static void main(String []args){
        
        /////////////////////////////getting inputs from user//////////////////////////////
        
        List<Rule> rules = new ArrayList<>();//the list that will contain rules of grammar
        Scanner in = new Scanner(System.in);
        System.out.print("Please enter how many rules you need to enter:");
        int numOfRules=in.nextInt();//get a nuber of rules 
        short stepCounter=1;
        
        //traverse in rules
        for(int i=0 ; i<numOfRules ; i++)
        {
            System.out.println(" Enter details for rule " + (i+1) + " :");
            
            System.out.print("Enter  left-hand-side of rule LHS :");
            String left = in.next();//get a lhs of rule
                        
            System.out.print("Enter  right-hand-side of rule RHS :");
            //get a rhs of rule by array of string because 
            //its not possible to get it directly to list of string
            String[] rightArray = in.next().split("\\|");
            
            // the list of string will contain a rhs that is consists of string
            List<String> right = new ArrayList<>();
            
            //convert array of string to list of string
            for(String rhs : rightArray)
            {
                right.add(rhs.trim());
            }
            
            //create a new rule object by sending these components to par. constructor 
            Rule rule = new Rule(left , right);
            rules.add(rule);//then add it to our list of rules
        }
        
        /////////////////////////////printing the current form of rules///////////////////////////
        System.out.println("\n---------------------- ("+stepCounter+")-Rules -----------------------");
        stepCounter++;
        int rulesSize=rules.size();
        
        //traverse in rules and print them
        for(int i=0 ; i<rulesSize; i++)
        {
            Rule rule = rules.get(i);
            System.out.println("");
            System.out.print(rule.getLeft() + " -> ");
            rule.printRight();
            System.out.print("");
        }
        
        //////////////checing if S is in the right , adding a new start symbol///////////////////
        
        //consider the right sides of rules dosn't contain start symbol S
        boolean flag = false; 
        
        //traverse in rules
        for(int i=0 ; i<rulesSize ; i++)
        {
            Rule rule = rules.get(i);
            List<String> right = rule.getRight();
            
            //traverse in right sides
            for(int j=0 ; j<right.size() ; j++)
            {
                if(right.get(j).contains("S"))
                {
                    flag = true;//there is S in the right
                    break;//get out of the loop
                }
            }
        }
        
        //is right sides are contain "S"
        if(flag==true)
        {
            //adding a new start symbol
            System.out.println("\n\n------------- ("+stepCounter+")-Adding a New Start Symbol--------------\n");
            stepCounter++;
            
            //new star symbol's right side is S
            String startRight ="S";
            List<String> strList = new ArrayList<>();
            strList.add(startRight);
            //add a new start symbol
            rules.add( 0 , new Rule("S0",strList));
            System.out.println("");
            Rule.printRules(rules);
            System.out.println("");
        }
        
        //////////////////////////////eliminating epsilon produtions//////////////////////////////
        System.out.println("\n----------- ("+stepCounter+")-Eliminating Epsilon Produtions-----------\n");
        stepCounter++;
        //travers in rules
        for(int i=0 ; i<rules.size() ; i++)
        {
            //the variables that will use if we substitute epsilon for epsilon production 
            byte count1=0;
            byte count2=0;
            
            Rule rule = rules.get(i);
            List<String> right = rule.getRight();
            String myLeft = rule.getLeft();
            
            //traverse in right sides
            for(int j=0 ; j<right.size() ; j++)
            {
                //is the string that in j. index in right side equals epsilon
                if((right.get(j)).equals("ep"))
                {
                    //go to start and traverse in rules again
                    for(int k=0 ; k<rules.size() ; k++)
                    {
                        Rule rule1 = rules.get(k);
                        List<String> rht = rule1.getRight();
                        //int rSize = rht.size();
                        
                        //traverse in right sides
                        for(int l=0 ; l<rht.size() ; l++)
                        {
                            String myWord = rht.get(l);
                            
                            //remove the epsilons that they alone
                            if(myLeft.equals(rule1.getLeft())&& (rht.get(l)).equals("ep"))
                            {
                                rht.remove(l);
                            }
                            
                            //char list to convert a word to charlist to know indexes of myLeft
                            List<Character> charlist = new ArrayList<>();
                            
                            //indexes of myLeft is available 
                            List<Integer> index = new ArrayList<>();
                            
                            //is myWord contains myLeft
                            if(myWord.contains(myLeft))
                            {
                                //convert myWord to array of char.
                                for(char m:myWord.toCharArray())
                                {
                                    charlist.add(m);
                                }
                                
                                //traverse in char list
                                for(int n=0 ; n<charlist.size() ; n++)
                                {
                                    if(charlist.get(n)==myLeft.charAt(0))
                                    {
                                       index.add(n);
                                    }
                                }
                            }
                            
                            //is myWords length equals 1 and myLeft equls myWord
                            if((myWord.length()==1)&&(myLeft.equals(myWord)))
                            {
                                rht.add("ep");//add epsilon 
                                count2++;// there is a new epsilon in rules
                            }
                            
                            
                            if((myWord.length()>1)&&(myWord.contains(myLeft)))
                            {
                                if(index.size()>1)
                                {
                                    rht.add(myWord.replaceAll(myLeft,""));
                                }
                                
                                for(int p=0 ; p<index.size() ; p++)
                                {
                                    //call StringBuilder class to be able doing operation to strings
                                    StringBuilder modified = new StringBuilder(myWord);
                                    
                                    //delete epsilon prodution in order
                                    modified.deleteCharAt(index.get(p));
                                    String result = modified.toString();
                                    rht.add(result);//add a new modified word to rhs
                                }                                                               
                            }                            
                        }                        
                    }  
                }
            }
            
            //is there a new epsilon in rhs and we have traversed in for till the end
            if((count1!=count2)&&(i==(rules.size()-1)))
            {
                i=-1;
            }
        }
        
        //calling print and delete, methodes
        //printing exist shape of rules
        Rule.printRules(rules);
        Rule.deleteSimilar(rules);
        System.out.println("\n--------------- ("+stepCounter+")-Deleting Similar Items---------------\n");
        stepCounter++;
        Rule.printRules(rules);
        
        
        //////////////////////////////eliminating unit produtions//////////////////////////////
        
        //traverse in rules descending to prevent generating a new unit produtions
        for(int i=rules.size()-1 ; i>=0 ; i--)
        {
            Rule ruleUnit = rules.get(i);
            List<String> rightUnit = ruleUnit.getRight();
            String leftUnit = ruleUnit.getLeft();
            boolean keyAdd = false;//consider adding unit prod's right after remove it is false
            
            //remove unit prodution that be as A->A
            for(int j=0 ; j<rightUnit.size() ; j++)
            {
                if(leftUnit.equals(rightUnit.get(j)))
                {
                    rightUnit.remove(j);
                    j--;
                }
            }
            
            //travers in right sides of rules
            for(int k=0 ; k<rightUnit.size() ; k++)
            {
                //is there a unit production
                if((rightUnit.get(k).length()==1)&&(rightUnit.get(k).equals(rightUnit.get(k).toUpperCase())))
                {
                    //there is a unit prod. in the rule that its left side is myLeft
                    String myLeft = rightUnit.get(k);
                    //remove unit production
                    rightUnit.remove(k);
                    //decrease k because after deleting, items have shifted to left one index
                    k--;
                    
                    for(int l=rules.size()-1 ; l>=0 ; l--)
                    {
                        Rule ruleAdd = rules.get(l);
                        List<String> rightAdd = ruleAdd.getRight();
                        String leftAdd = ruleAdd.getLeft(); 
                        if(myLeft.equals(leftAdd))
                        {
                            rightUnit.addAll(rightAdd);
                            keyAdd=true; //added unit prod's right after remove it
                            
                            break;//get out from rules dont continue to searching
                        }
                    }
                }
            }
            if(keyAdd==true)
            {
                i++;//increase i to traverse again to this rule's right side bucause...
                //after adding unit prodution's right side maybe it was has a unit prod.
            }
        }
        
        //printing exist shape of rules
        System.out.println("\n------------ ("+stepCounter+")-Eliminating Unit Produtions-------------\n");
        stepCounter++;
        Rule.printRules(rules);
        
        //////////////////////////////eliminating  useless symbls//////////////////////////////
        //the list will be contain all non terminals in left sides
            List<String> leftElements = new ArrayList<>();
            
            //the list will be contain all non terminals in right sides
            List<String> rightElements = new ArrayList<>();
            
            ////the list will be contain only non terminals in right sides
            List<String> nonGenElements = new ArrayList<>();
            
            ////the list will be contain only non terminals in left sides
            List<String> nonReaElements = new ArrayList<>();
            
        //traverse in rules to collect all nonterminal elements in left sides
        for(int i=0 ; i<rules.size() ; i++)
        {
            Rule ruleUseLeft = rules.get(i);
            String leftUseless = ruleUseLeft.getLeft();
            
            if(!leftUseless.equals("S0"))
            {
                leftElements.add(leftUseless);
                //if(rules.get(0).equals("S"))
                //{
                //    leftElements.remove("S");
               // }
            }
        }
        
        //traverse in rules to collect all nonterminal elements in right sides
        for(int i=0 ; i<rules.size() ; i++ )
        {
            Rule ruleUseRight = rules.get(i);
            List<String> rightUseless = ruleUseRight.getRight();
            String LeftUseless = ruleUseRight.getLeft();
            //List<Character> letterList = new ArrayList<>();
            //traverse in right sides
            for(int j=0 ; j<rightUseless.size() ; j++)
            {
                String word = rightUseless.get(j);
                for(char k:word.toCharArray())
                {
                    String adding = Character.toString(k);
                    if(Character.isUpperCase(k)&&(!rightElements.contains(String.valueOf(k)))&&(!String.valueOf(k).equals(LeftUseless)))
                    {
                        
                        rightElements.add(adding);
                    }
                }
            }
        }
        
        //find unique element in leftElements that will be nonGenElements list
        for(String element:rightElements)
        {
            if(!leftElements.contains(element))
            {
                nonGenElements.add(element);
            }
        }
        
        //find unique element in rightElements that will be nonReaElements list
        for(String element:leftElements)
        {
            if(!rightElements.contains(element))
            {
                nonReaElements.add(element);
            }
        }
        
        for(int h=0 ; h<2 ; h++)
        {
            //elimenate non generetion rules
            for(String element:nonGenElements)
            {
                for(int i=0 ; i<rules.size() ; i++)
                {
                    Rule ruleNonGen = rules.get(i);
                    List<String> rightSide = ruleNonGen.getRight();
                    for(int j=0 ; j<rightSide.size() ; j++)
                    {
                        //Ex: A->aCA  and C is non generetion
                        if(rightSide.size()==1 && rightSide.get(j).equals(element))
                        {
                            rules.remove(i);
                            i--;
                            break;
                        }

                        //Ex: A-> aCA|aa  and C is non generetion
                        if(rightSide.size() > 1 && rightSide.get(j).contains(element))
                        {
                            rightSide.remove(j);
                            j--;
                        }
                    }
                }
            }

            //elimenate non reachable rules
            for(String element:nonReaElements)
            {
                for(int i=0 ; i<rules.size() ; i++)
                {
                    Rule ruleNonRea = rules.get(i);
                    //if you find non reachable rule
                    if(ruleNonRea.getLeft().equals(element))
                    {
                        rules.remove(i);
                        i--;
                    }
                }
            }
        }
        
        //printing exsist shape of rules
        System.out.println("\n------------ ("+stepCounter+")-eliminating  useless symblos------------\n");
        stepCounter++;
        Rule.printRules(rules);
        
        //////////////////////////////convert rules to CNF//////////////////////////////
        
        //capital letters container for replacing
        List<String> capitalLetters = new ArrayList<>(Arrays.asList("Z","Y","X","W","V","U","T","R","Q","O","N"));
        
        //convert right sides be like: aa , bA to ZZ , YA
        for(int i=0 ; i<rules.size() ; i++)
        {
            Rule rule = rules.get(i);
            for(int j=0 ; j<rule.getRight().size() ; j++)
            {
                String myRight = rule.getRight().get(j);
                //consider there is no terminal variables in our word
                boolean terminalFlag=false;
                //list of string to contain terminals that is available in word
                List<String> terminalList = new ArrayList<>();
                
                //traverse in letter of word 
                for(char c:myRight.toCharArray())
                {
                    //if you find a terminal(lower) letter 
                    if(Character.isLowerCase(c))
                    {
                        terminalFlag = true;//ther is a terminal letter in word
                        //is the terminal is not exsit in terminal container
                        if(!terminalList.contains(c))
                        {
                            terminalList.add(Character.toString(c));//add it
                        }
                    }
                }
                //is our word's size bigger than 1 and is contain a terminal letter like:abAa
                if((myRight.length()>1)&&(terminalFlag==true))
                {
                    //traverse in terminalList
                    for(String rht:terminalList)
                    {
                        //convert string to list of string
                        List<String> rightSide = new ArrayList<>();
                        rightSide.add(rht);
                        //add a new rule 
                        rules.add(new Rule(capitalLetters.get(0),rightSide));
                        //traverse in rules
                        for(int k=0 ; k<rules.size() ; k++)
                        {
                            Rule rule1 = rules.get(k);
                            List<String> right1 = rule1.getRight();
                            //traverse in right sides
                            for(int l=0 ; l<right1.size() ; l++)
                            {
                                //is our word contain terminal and his size bigger than one
                                if(right1.get(l).contains(rht)&& right1.get(l).length()>1)
                                {
                                    //replace terminal to non-terminal that in first index of capitalLetters
                                    right1.set(l,right1.get(l).replaceAll(rht,capitalLetters.get(0)));
                                    
                                }
                            }
                        }
                        capitalLetters.remove(0);//remove first item 
                    }
                }
            }
        }
        //printing exsist shape of rules
        Rule.deleteSimilar(rules);
        System.out.println("\n--------------- ("+stepCounter+")-convert rules to CNF 1---------------\n");
        stepCounter++;
        Rule.printRules(rules);
        //-------------------------------------------------------------------------------------
        //convert right sides be like: AAA , BBA to ZA , YA
        boolean whileFlag = true;
        while(whileFlag)
        {
            //consider after traverse in rules converting will be finished
            whileFlag=false;
            //traverse in rules
            for(int i=0 ; i<rules.size() ; i++)
            {
                Rule rule = rules.get(i);
                List<String> right = rule.getRight();
                //traverse in right sides
                for(int j=0 ; j<right.size() ; j++)
                {
                    //is our word's length bigger than 2 like AAB
                    if(right.get(j).length()>2)
                    {
                        //take firs and second letters and put them in temp. string
                       String myString = right.get(j).substring(0,2);
                       
                       //convert temp to list of string because it will be the right side of a new rule
                       List<String> newRight = new ArrayList<>();
                       
                       newRight.add(myString);
                       
                       //edit our words that be like AAB to ZB
                       right.set(j,capitalLetters.get(0)+right.get(j).substring(2));
                       
                       //add a new rule their left is Z their right side is ZB
                       rules.add(new Rule(capitalLetters.get(0),newRight));
                       
                       //delete first iterm from capitalLetters
                       capitalLetters.remove(0);
                       
                       whileFlag=true;//check rules again
                    }
                }
            }
        }
        
        //printing end shape of rules
        System.out.println("\n--------------- ("+stepCounter+")-convert rules to CNF 2---------------\n");
        Rule.printRules(rules);
    }
}
///////////////////////////////////////////////////finish//////////////////////////////////////////////////////