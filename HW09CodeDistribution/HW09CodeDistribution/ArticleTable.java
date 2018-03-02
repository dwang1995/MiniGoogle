/*
 * ArticleTable.java
 * name: Dayuan Wang
 * This is the helper file for the Minipedia.java
 * it will create a hash table and adjust the article into the hash table
 * Date: 4.7.2016
 */

public class ArticleTable{
    // this is the define for the Node class
    // this Node class will contain four things in it
    // it will have the String key is to save the title of the article
    // the Article datum will save the article itself
    // the pointer next will use to link the hash table
    // the pointer next2 will use to link the head as a link list for everything I have inserted
    private class Node{
        String key;
        Article datum;
        Node next;
        Node next2;
        
        // when we only use a article to create a Node
        // set the key as the title of the article
        // set the datum as the article
        // set both pointer next and next2 to null
        public Node(Article data){
            this.key = data.getTitle();
            this.datum = data;
        }
        
        // when there is a article and a node as input
        // set the key as the title of the article
        // set the datum as the article
        // set the next2 to the input Node
        // this is made for the link list head!! so I am going to use next2
        public Node(Article data, Node p){
            this.key = data.getTitle();
            this.datum = data;
            this.next2 = p;
        }  
    }
    
    public Node head = null; 
    
    private final int SIZE = 2503;
    
    private Node [] T = new Node[SIZE];
    
    // this is the function to hash a String
    // this is the code I get from the website that professor recommanded 
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
    
    // this is the function for initilize
    // It will have a input as a list of Article
    // It will use a while loop to process each Article in that list
    // and insert each Article to the hash table
    public void initialize(Article[] A) {
        for(int i = 0; i < A.length; ++i) 
            insert(A[i]); 
    }
    
    // this is the method that I am going to use to insert the Article into the hash table
    // first, I am going to check if the title is already in the hash table or not
    // if it is not, I am going to insert it into the hash table.
    // I am going to hash it first, find which Node I am going to put it in
    // and then use the recursion to add the article to the end of the link list.
    // after than, also add it into the link list head to keep tracking all the Article I have inserted
    public void insert(Article a){     
        String k = a.getTitle();
        if(memberHelper(k,T[hash(k)]) == false){
        T[hash(k)]= insertHelper(a,T[hash(k)]);
        head = new Node(a,head);                  // the head here is the pointer for the next2
        }
    }
    private Node insertHelper(Article a, Node p){
        if(p == null){
            p = new Node(a);
            return p;
        }
        else{
            p.next = insertHelper(a,p.next);      // the pointer next is using here, because i am going to use this in the hash table
            return p;
        }
    }
    
    // this is the method for delete a Article in the hash table
    // the input of this should be the title
    // hash the input title first to find the correct link list I am going to delete from
    // use the recursion about delete a Node that we talked about in the lecture to delete
    // because the head link list and the hash table link list are using two differetn pointer (next and next2)
    // so i am going to use deleteHelper2 just to delete the Node in the head link link
    public void delete(String k){
        T[hash(k)]= deleteHelper(k,T[hash(k)]);
        head = deleteHelper2(k,head);
    }
    private Node deleteHelper(String k, Node p){  // this is the process in the hash table, so I am going to use next to get the next thing
        if(p == null){
            return p;
        }
        else if((p.key).compareTo(k) == 0){
            return deleteHelper(k,p.next);
        }
        else{
            p.next = deleteHelper(k,p.next);
            return p;
        }
    }
    private Node deleteHelper2(String k, Node p){  // this is the deletion for the head, so i am going to use the next2 to get the next thing in the link list
        if(p == null){
            return p;
        }
        else if((p.key).compareTo(k) == 0){
            return deleteHelper2(k,p.next2);
        }
        else{
            p.next2 = deleteHelper2(k,p.next2);
            return p;
        }
    }
    
    // this is the method for the lookup
    // if the title is not in the link list, return null
    // else, hash it first and find the right link list it should be in
    // go through every node in that link list
    // once we find it, I am going to return the Node's datum(as a article type)
    public Article lookup(String k){
        if(memberHelper(k,T[hash(k)]) == false){
            return null;
        }
        else{
            return lookupHelper(k,T[hash(k)]);
        }
    }
    private Article lookupHelper(String k, Node p){
        if((p.key).compareTo(k) == 0){
            return p.datum;
        }
        else{
            return lookupHelper(k,p.next);           // this is the process in the hash table, so i am going to use the pointer next
        }
    }
    
    // this is the method to check if the title is in the member of the link list or not
    // I am just going to use this method for the hash table's link list, so I am going to use the pointer next to get the 
    // next thing in the link list
    public Boolean memberHelper(String k, Node p){
        if(p == null){
            return false;
        }
        else if((p.key).compareTo(k) == 0){
            return true;
        }
        else{
            return memberHelper(k,p.next);
        }
    }
    
    // from here I am going to write the iterator
    // first I am going to set a Node R to null
    public Node R = null;
   
    // this is the reset for the iterator
    // just simply set the R to head
    // everything for iterator is work for the head Node, so i am going to use the next2 as the pointer
    public void reset(){
        R = head;
    }
    
    // This is to check if there is next think in the link list
    // if the R == null, return false
    public Boolean hasNext(){
        if(R == null){
            return false;
        }
        else{
            return true;
        }
    }
    
    // This the the method for get the next thing in the link list
    // create a varible to save the article that R is pointing to right now
    // set R = R.next2, and return the Article that we saved to a variable
    public Article next(){
        Article b = R.datum;
        R = R.next2;
        return b;
    }
    
    // This is the method toString for the link list
    // Since I do not want to print out the whole hash table
    // I am just going to change the Node to the String
    public String toString(){
        return toStringAux(head);
    }
    public String toStringAux(Node p){
        if(p== null){
            return "";
        }
        else if(p.next == null){
            System.out.print(p.key + ":" + p.datum);
            return toStringAux(p.next2);
        }
        else{
            System.out.print(p.key + ":" + p.datum);
            return toStringAux(p.next2);
        }
    }
    
    
    
    public static void main(String[] args){
        ArticleTable A = new ArticleTable();
        Article C = new Article("Java","This is the homework for Java");
        Article B = new Article("Java","This is to test for duplicate");
        Article D = new Article("HW09","This homework set is just too long");
        Article E = new Article("HW08","This homework set is short but not that easy");
        
        A.insert(C);
        System.out.println("This is the test case for insert");
        System.out.println("It should be only print one one article with the title Java");
        System.out.println(A);
        
        A.insert(B);
        System.out.println("This is the test for insert the duplicates");
        System.out.println("It should be only print out one article with the title Java");
        System.out.println("but the article should be talking about homework");
        System.out.println(A);
        
        
        A.insert(D);
        A.insert(E);
        System.out.println("After insert two more article in, it should be print out three articles");
        System.out.println("it should print out the article with Java, HW08, and HW09");
        System.out.println(A);
        
        System.out.println("This is the test for lookup");
        System.out.println("It should give me the article about Java");
        System.out.println(A.lookup("Java"));
        //System.out.println(A.lookup("java"));
       
        A.delete("Java");
        System.out.println("This is the test for delete");
        System.out.println("After delete Java, I should have two article be printed");
        System.out.println("One shoud be HW08 and one should be HW09");
        System.out.println(A);
        
        A.delete("HW06");
        A.delete("HW09");
        A.delete("Java");
        System.out.println("After delete one thing, I should not get only one thing now");
        System.out.println("It should be HW08");
        System.out.println(A);
        
        A.delete("HW08");
        System.out.println("Now I have delete everything, it should not give me anything to be print");
        System.out.println(A);
        
        A.insert(B);
        A.insert(D);
        System.out.println("insert back two articles");
        System.out.println("it should be two thing, one java, one HW09");
        System.out.println("They should be compaining about this homework");
        System.out.println(A);
        
        System.out.println("Now I am going to test the iterator");
        System.out.println("testing for reset(), hasNext(), and next()");
        System.out.println("It should have two things print out");
        System.out.println("one should be Java, one should be HW09");
        A.reset();
        while(A.hasNext() == true){
            Article a = A.next();
            System.out.println(a);
        }
    }
}

