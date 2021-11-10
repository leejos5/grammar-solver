// Joshua Lee
// CSE 143 BH
// TA: RanDair Porter
// Assignment #5: GrammarSolver

// This program will process a file of Backus-Naur Form (BNF) grammar to generate
// elements of grammar randomly.

import java.util.*;

public class GrammarSolver {
   private SortedMap<String, String[]> rules; // Map of nonterminals and respective rules.

/* pre: !grammar.isEmpty() && !grammar contains the same nonterminal twice or more
 *      (case-sensitive) (otherwise throw IllegalArgumentException()).
 *post: Uses the passed list of strings to conveniently store the nonterminals and 
 *      respective rules to be easily accessed. Nonterminals are case-sensitive, and
 *      are assumed to be not empty, without any '|'s, and without any whitespace.
 *      Duplicate rules are allowed, increasing the likelihood of it being chosen.
 */
   public GrammarSolver(List<String> grammar) {
      if (grammar.isEmpty()) {
         throw new IllegalArgumentException();
      }
      rules = new TreeMap<String, String[]>();
      for (String rule: grammar) {
         String[] key = rule.split("::=");
         if (grammarContains(key[0])) {
            throw new IllegalArgumentException();
         }
         String[] parts = key[1].split("[|]");
         rules.put(key[0], parts);       
      }
   }

//post: Returns true if the passed symbol is a nonterminal, false otherwise.    
   public boolean grammarContains(String symbol) {
      return rules.containsKey(symbol);
   }
// pre: rules.containsKey(symbol) && times >= 0 (otherwise throw IllegalArgumentException)
//post: Uses the grammar rules to randomly create the given symbol for the passed number
//      of times. Returns an array of the generated strings.
   public String[] generate(String symbol, int times) {
      if (!rules.containsKey(symbol) || times < 0) {
         throw new IllegalArgumentException();
      }
      Random r = new Random();
      String[] result = new String[times];
      for (int i = 0; i < times; i++) {
         result[i] = getString(symbol, r);  
      }       
      return result;
   }

//post: Uses the passed symbol and random variable to generate a string of the symbol.
//      If the symbol is a nonterminal, it will recursively continue through the nonterminal's
//      rules until it reaches a terminal, which it adds to the string, that is returned.
//      each terminal is added with a space between each, and no leading or trailing spaces.
   private String getString(String symbol, Random r) {
      if (!grammarContains(symbol)) {
         return symbol;
      }
      String result = "";
      String[] allRules = rules.get(symbol);
      String rule = allRules[r.nextInt(allRules.length)].trim();
      String[] parts = rule.trim().split("[^([E,]+)-[*]%/<>a-zA-Z0-9]+");
      for (String s: parts) {
         result += getString(s, r) + " ";
      }
      return result.trim();
   }
 
//post: Returns a string of all nonterminals from the grammar as an alphabetically
//      sorted bracketed list.  
   public String getSymbols() {
      return rules.keySet().toString();
   }
}
