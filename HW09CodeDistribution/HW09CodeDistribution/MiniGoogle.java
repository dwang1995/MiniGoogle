/*
 * MiniGoogle.java
 *
 * A client program that uses the DatabaseIterator
 * and Article classes, along with additional data
 * structures, to allow a user to create, modify
 * and interact with a encyclopedia database.
 *
 * Author : Dayuan Wang
 * this file has been edite by Dayuan Wang(wangdayu@bu.edu)
 * Date: 4.13.2016
 */

import java.util.*;
//import java.lang.Object; 


public class MiniGoogle {
    
    private static Article[] getArticleList(DatabaseIterator db) {
        
        // count how many articles are in the directory
        int count = db.getNumArticles(); 
        
        // now create array
        Article[] list = new Article[count];
        for(int i = 0; i < count; ++i)
            list[i] = db.next();
        
        return list; 
    }
    
    private static DatabaseIterator setupDatabase(String path) {
        return new DatabaseIterator(path);
    }
    
    private static void addArticle(Scanner s, ArticleTable D) {
        System.out.println();
        System.out.println("Add an article");
        System.out.println("==============");
        
        System.out.print("Enter article title: ");
        String title = s.nextLine();
        
        System.out.println("You may now enter the body of the article.");
        System.out.println("Press return two times when you are done.");
        
        String body = "";
        String line = "";
        do {
            line = s.nextLine();
            body += line + "\n";
        } while (!line.equals(""));
        
        D.insert(new Article(title, body));
    }
    
    
    private static void removeArticle(Scanner s, ArticleTable D) {
        System.out.println();
        System.out.println("Remove an article");
        System.out.println("=================");
        
        System.out.print("Enter article title: ");
        String title = s.nextLine();
        
        
        D.delete(title);
    }
    
    
    private static void titleSearch(Scanner s, ArticleTable D) {
        System.out.println();
        System.out.println("Search by article title");
        System.out.println("=======================");
        
        System.out.print("Enter article title: ");
        String title = s.nextLine();
        
        Article a = D.lookup(title);
        if(a != null)
            System.out.println(a);
        else {
            System.out.println("Article not found!"); 
            return; 
        }
        
        System.out.println("Press return when finished reading.");
        s.nextLine();
    }
    
    public static String preprocess(String s){
        char[] charsToRemove = {'\'','.',',',':',';','!','?','"','/','-','(',')','~'};
        s = s.toLowerCase();
        for( int i = 0; i< charsToRemove.length; ++i){
                String a = Character.toString(charsToRemove[i]);
                s = s.replace(a,"");
//                String b = Character.toString('\n');
//                s = s.replace(b," ");
            }
        return s;
    }
    
    private static final String [] blackList = { "the", "of", "and", "a", "to", "in", "is", 
    "you", "that", "it", "he", "was", "for", "on", "are", "as", "with", 
    "his", "they", "i", "at", "be", "this", "have", "from", "or", "one", 
    "had", "by", "word", "but", "not", "what", "all", "were", "we", "when", 
    "your", "can", "said", "there", "use", "an", "each", "which", "she", 
    "do", "how", "their", "if", "will", "up", "other", "about", "out", "many", 
    "then", "them", "these", "so", "some", "her", "would", "make", "like", 
    "him", "into", "time", "has", "look", "two", "more", "write", "go", "see", 
    "number", "no", "way", "could", "people",  "my", "than", "first", "water", 
    "been", "call", "who", "oil", "its", "now", "find", "long", "down", "day", 
    "did", "get", "come", "made", "may", "part" }; 
    
    private static boolean blacklisted(String s){
        for(int i = 0; i < blackList.length ; ++i){
            if(s.compareTo(blackList[i]) == 0){
                return true;
            }
        }
        return false;
    }
    
    private static double getCosineSimilarity(String s, String t){
        s = preprocess(s);
        t = preprocess(t);
//        String[] s_1 = s.split("\\s+");
//        String[] t_1 = t.split("\\s+");
        TermFrequencyTable A = new TermFrequencyTable();
//        for(int i = 0; i< s_1.length; ++i){
//            if(blacklisted(s_1[i]) == false){
//                A.insert(s_1[i],0);}
//        }
//        for(int j = 0; j< t_1.length; ++j){
//            if(blacklisted(t_1[j]) == false){
//                A.insert(t_1[j],1);}
//        }
        StringTokenizer st = new StringTokenizer(s);
        while(st.hasMoreTokens()){
            String Q = st.nextToken();
            if(blacklisted(Q) == false){
                A.insert(Q,0);
            }
        }
        StringTokenizer tt = new StringTokenizer(t);
        while(tt.hasMoreTokens()){
            String P = tt.nextToken();
            if(blacklisted(P) == false){
                A.insert(P,1);
            }
        }
        double result = A.cosineSimilarity();
        return result;
    }
    
    public static void phrase(Scanner S, ArticleTable D){
        System.out.println();
        System.out.println("Search by article content");
        System.out.println("=================");
        
        System.out.print("Enter search phrase: ");
        String title = S.nextLine();
        //phraseSearch(title,D);
        System.out.println(phraseSearch(title, D));
        
        
        
    }
    
    public static int ArticleSize(ArticleTable T){
        T.reset();
        int C = 0;
        while(T.hasNext() == true){
            C += 1;
            T.next();           
        }
        return C;
    }
    
    public static String  phraseSearch(String phrase, ArticleTable T){
        MaxHeap H = new MaxHeap();
        phrase = preprocess(phrase);
        T.reset();
        while(T.hasNext() == true){
            //System.out.println("A");
            Article a = T.next();
            String b = a.getBody();
            b = preprocess(b);
            double CS = getCosineSimilarity(phrase, b);
            a.putCS(CS);
            if(CS > 0.001){
                //System.out.println(a);
                H.insert(a);
            }
        }
        if(H.size() == 0){
            String U = "There are no matching articles.";
            return U;
            //return "";
        }
        else if(H.size() == 1){
            //System.out.println("1");
            Article one = H.getMax();
            String U = "Top 1 Matches: \n\n";
            U += "Match one with cosine similarity of " + one.getCS() + ":\n\n";
            U += one.toString();
            //String U = (H.getMax()).toString();
            return U;
            //return "";
        }
        else if(H.size() == 2){
            //System.out.println("2");
            Article one = H.getMax();
            Article two = H.getMax();
            String U = "Top 2 Matches: \n\n";
            U += "Match one with cosine similarity of " + one.getCS() + ":\n\n";
            U += one.toString();
            U += "Match two with cosine similarity of " + two.getCS() + ":\n\n";
            U += two.toString();
//            String U = (H.getMax()).toString();
//            U += (H.getMax()).toString();
            return U;
            //return "";
        }
        else{
            Article one = H.getMax();
            Article two = H.getMax();
            Article three = H.getMax();
            String U = "Top 3 Matches: \n\n";
            U += "Match one with cosine similarity of " + one.getCS() + ":\n\n";
            U += one.toString();
            U += "Match two with cosine similarity of " + two.getCS() + ":\n\n";
            U += two.toString();
            U += "Match three with cosine similarity of " + three.getCS() + ":\n\n";
            U += three.toString();
//            System.out.println("3");
//            String U = (H.getMax()).toString();
//            U += (H.getMax()).toString();
//            U += (H.getMax()).toString();
            return U;
            //return "";
        }
    }
    
    public static void main(String[] args) {
        
        Scanner user = new Scanner(System.in);
        
        String dbPath = "articles/";
        
        DatabaseIterator db = setupDatabase(dbPath);
        
        System.out.println("Read " + db.getNumArticles() + 
                           " articles from disk.");
        
        //DumbList L = new DumbList();
        ArticleTable L = new ArticleTable();
        Article[] A = getArticleList(db);
        L.initialize(A);
        int P = ArticleSize(L);
        System.out.println("Inserted " + P + " articles");
        
        int choice = -1;
        do {
            System.out.println();
            System.out.println("Welcome to Minipedia!");
            System.out.println("=====================");
            System.out.println("Make a selection from the " +
                               "following options:");
            System.out.println();
            System.out.println("Manipulating the database");
            System.out.println("-------------------------");
            System.out.println("    1. add a new article");
            System.out.println("    2. remove an article");
            System.out.println();
            System.out.println("Searching the database");
            System.out.println("----------------------");
            System.out.println("    3. search by exact article title");
            System.out.println("    4. search by phrase");
            System.out.println();
            System.out.println("Checking the Article Table Size");
            System.out.println("----------------------");
            System.out.println("    5. article table size");
            System.out.println();
            
            System.out.print("Enter a selection (1-5, or 0 to quit): ");
            
            choice = user.nextInt();
            user.nextLine();
            
            switch (choice) {
                case 0:
                    System.out.println("Bye!");
                    return;
                    
                case 1:
                    addArticle(user, L);
                    break;
                    
                case 2:
                    removeArticle(user, L);
                    break;
                    
                case 3:
                    titleSearch(user, L);
                    break;
                    
                case 4:
                    phrase(user, L);
                    break;
                case 5:
                    int O = ArticleSize(L);
                    System.out.println(O);
                    break;
                default:
                    break;
            }
            
            choice = -1;
            
        } while (choice < 0 || choice > 4);
        /*
        
        String s = "I't is, tHe SHOW time!!!";
        String t = preprocess(s);
        System.out.println(t);
        
        String a = "the";
        String b = "make";
        String c = "Dayuan";
        System.out.println(blacklisted(a));
        System.out.println(blacklisted(b));
        System.out.println(blacklisted(c));
        
        double testcos = getCosineSimilarity("IT IS 1000 SHOW TIME!!","i't is. 1000 show time");
        System.out.println(testcos);
        */
    }
    
    
}
