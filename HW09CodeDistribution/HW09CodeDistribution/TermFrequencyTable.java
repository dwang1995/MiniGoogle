/*
 * File name: TermFrequencyTable.java
 * Name: Dayuan Wang
 * this is the file that calculate the termFrequency from two list of words
 */

import java.lang.*;


public class TermFrequencyTable{
    // define the Node class
    private class Node{
        String term;
        int[] termFreq = new int[2];
        Node next;
        
        public Node(String key){
            this.term = key;
        }
    }
    
    private final int SIZE = 2503;
    
    // I am creating the hash table right now
    private Node [] T = new Node[SIZE];
    
    // this is the hash function 
    private int hash(String x){
        char ch[];
        ch = x.toCharArray();
        int xlength = x.length();
        
        int i;
        int sum;
        for (sum=0, i=0; i < x.length(); i++)
            sum += ch[i];
        return sum % SIZE;
    }
    
    private final String [] blackList = { "the", "of", "and", "a", "to", "in", "is", 
        "you", "that", "it", "he", "was", "for", "on", "are", "as", "with", 
        "his", "they", "i", "at", "be", "this", "have", "from", "or", "one", 
        "had", "by", "word", "but", "not", "what", "all", "were", "we", "when", 
        "your", "can", "said", "there", "use", "an", "each", "which", "she", 
        "do", "how", "their", "if", "will", "up", "other", "about", "out", "many", 
        "then", "them", "these", "so", "some", "her", "would", "make", "like", 
        "him", "into", "time", "has", "look", "two", "more", "write", "go", "see", 
        "number", "no", "way", "could", "people",  "my", "than", "first", "water", 
        "been", "call", "who", "oil", "its", "now", "find", "long", "down", "day", 
        "did", "get", "come", "made", "may", "part","up"}; 
    
    // this is the the method to check if a String is in the blacklist or not
    private Boolean memberOfBlackList(String term){
        for(int i = 0; i< blackList.length; ++i){
            if(term.compareTo(blackList[i]) == 0){
                return true;
            }
        }
        return false;
    }
    
    // this is the method to check if a key word is in the link list or not
    private Boolean memberHelper(String k, Node p){
        if(p == null){
            return false;
        }
        else if((p.term).compareTo(k) == 0){
            return true;
        }
        else{
            return memberHelper(k,p.next);
        }
    }
    // this is the method for insert each String into the hash table
    // I am going to use two helper functions
    public void insert(String term, int docNum){
        if(memberOfBlackList(term) == false){
            if(memberHelper(term,T[hash(term)]) == false){
                T[hash(term)] = insertHelper(term,docNum,T[hash(term)]);
            }
            else{
                T[hash(term)] = insertHelper2(term, docNum, T[hash(term)]);
            }
        }
    }
    // this is the helper function to insert string into the hash table
    // if the string is not inserted already, I am just going to find the hash place
    // and add it into the link list, with the frequence of 1
    private Node insertHelper(String term, int docNum, Node p){
        if(p == null){
            p = new Node(term);
            p.termFreq[docNum] = 1;
            return p;
        }
        else{
            p.next = insertHelper(term,docNum,p.next);
            return p;
        }
    }
    // this is the helper function to insert string into the hash table
    // if the String is in the link list already, just let the frequency add one
    private Node insertHelper2(String term, int docNum, Node p){
        if((p.term).compareTo(term) == 0){
            p.termFreq[docNum] += 1;
            return p;
        }
        else{
            p.next = insertHelper2(term, docNum, p.next);
            return p;
        }
    }
    
    // this is the helper function for the cosineSimilater
    // I am going to get the result of A*B from one link list
    private double getAB(Node p){
        if(p == null){
            return 0;
        }
        else{
            return (p.termFreq[0] * p.termFreq[1]) + getAB(p.next);
        }
    }
    
    // this is the helper function for the cosineSimilater
    // I am going to get the result of A^2 from one link list
    private double getA2(Node p){
        if(p == null){
            return 0;
        }
        else{
            return (p.termFreq[0] * p.termFreq[0]) + getA2(p.next);
        }
    }
    
    // this is the helper function for the cosineSimilater
    // I am going to get the result for B^2 from one link list
    private double getB2(Node p){
        if(p == null){
            return 0;
        }
        else{
            return (p.termFreq[1] * p.termFreq[1]) + getB2(p.next);
        }
    }
    
    // this is the method to get the cosineSimilartiy for the whole hash table
    // I am going throgh my hash table(which is the list of Nodes)
    // For each link list, I am going to get the value for A*B, A^2, and B^2
    // sum up the value for all of the link list in the hash table
    // use the formula to get the cosineSimilarity
    public double cosineSimilarity(){
        int AB = 0;
        int A2 = 0;
        int B2 = 0;
        for(int i = 0; i< T.length; ++i){
            AB += getAB(T[i]);
            A2 += getA2(T[i]);
            B2 += getB2(T[i]);
        }
        double result = AB / (Math.sqrt(A2) * Math.sqrt(B2));
        return result; 
    }
    
    
    public static void main(String[] args){
        // testing the TermFrequencyTable
        TermFrequencyTable A = new TermFrequencyTable();
        
        
        // I am going to insert the two example that the example give it to use
        System.out.println("Insert the article A to hashTable: the man with the hat ran up to the man with the dog");
        System.out.println("Insert the article B to hashtable: a man with a hat approached a dog and a man");
        String[] C = {"the","man","with","the","hat","ran","up","to","the","man","with","the","dog" };
        String[] B = {"a","man","with","a","hat","approached","a","dog","and","a","man"};
        
        //String[] C = {"dogs","cats"};
        //String[] B = {"cats","also", "called", "domestic", "cats", "or", "house", "cats", "are" ,"carnivorous",
//        "meat-eating", "mammals" ,"of" ,"the", "family" ,"felidae",
//        "cats", "have" ,"been", "domesticated", "tame" ,"for" ,"nearly" ,"10000", "years", "they", "are" ,"currently", "the" ,"most", 
//        "popular", "pets" ,"in", "the" ,"world", "their", "origin", "is", "probably", "the", "african", "wildcat", "felis" ,"silvestris", "lybica",
//        "cats", "were", "probably", "first", "kept", "because", "they", "ate", "mice", "and", "this", "is" ,"still", "their",
//        "main", "job", "in" ,"farms" ,"throughout", "the" ,"world" ,"later" ,"they", "were" ,"kept", "because" ,"they", "are", "friendly", "and", "good", "companions",
//        "a" ,"young" ,"cat", "is" ,"called", "a" ,"kitten", "cats", "are", "sometimes", "called", "kitty", "or", "pussycat", "an",
//        "entire", "female" ,"cat", "is", "a", "queen", "and", "an", "entire", "male", "cat", "is", "a" ,"tom",
//        "domestic", "cats" ,"are", "found" ,"in" ,"shorthair" ,"and","longhair", "breeds", "cats", "which", "are", "not",
//        "specific" ,"breeds", "can", "be" ,"referred", "to" ,"as", "domestic" ,"shorthair", "or", "domestic" ,"longhair",
//        "dsh", "dlh", "the", "word" ,"cat" ,"is", "also", "used" ,"for", "other" ,"felines" ,"felines" ,"are", "usually", "classed" ,"as", 
//        "either" ,"big" ,"cats", "or", "small", "cats" ,"the", "big", "cats", "are", "well", "know", "lions", "tigers", 
//        "leopards" ,"jaguars", "pumas", "and" ,"cheetahs" ,"there", "are" ,"small" ,"cats", "in" ,"most", "parts", "of", 
//        "the" ,"world" ,"such" ,"as" ,"the" ,"lynx", "in", "northern", "europe", "the" ,"big", "cats", "and", "wild", "cats", "are", 
//        "not" ,"tame", "and", "can" ,"be" ,"very", "dangerous"};
        
        for(int i = 0; i< C.length; ++i){
            A.insert(C[i],0);
        }
        for(int j = 0; j< B.length; ++j){
            A.insert(B[j],1);
        }
        
        System.out.println("The result should be: 0.8571");
        System.out.println("the result is: " + A.cosineSimilarity());
        
    }
}