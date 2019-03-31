import java.util.ArrayList;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * <p>Public Class Dictionary</p>
 * <p>Reads a file and creates sub-dictionary with all the words from the file</p>
 * <p>methods in this class do not throw any exception</p>
 * @author <p>Juan Sebastian Hoyos id:40087920</p>
 */
public class Dictionary {

    /**
     * <p>main method</p>
     * @param args
     */
    public static void main(String[] args)
    {
        Scanner sc;
        String file;
        Scanner kb=new Scanner(System.in);
        PrintWriter pw=null;
        ArrayList<String> list=new ArrayList();

        String temp=null;
        int entries=0;

        // Asking the user for the name of the file they would like to use
        System.out.println("Please enter the name of the file you wish to read");
        file=kb.next();
        if(!(file.contains(".txt")))
            file = file+".txt";

        try{
            //Opening the file and checking if it opens properly
            System.out.println("Opening file to read");
            sc=new Scanner(new FileInputStream(file));

            //Reading the entire File
            while(sc.hasNext()){
                temp=sc.next();
                temp=IsAWord(temp);
               // System.out.println(temp);
                list=AddToArray(temp,list);
                entries=list.size();

            }

            //FOR TESTING ONLY!!!!!!
            System.out.println("There are "+entries+" entries\n");
          /*  for (int i=0;i<list.size();i++){
                System.out.println(list.get(i));
            }
*/
            Sort(list,list.size());
            AddHeader(list);
            //FOR TESTING ONLY !!!!!
            System.out.println("Printing sorted array");
            for (int i=0;i<list.size();i++){
                System.out.println(list.get(i));
            }
            pw=new PrintWriter(new FileOutputStream("SubDictionary.txt"));
            pw.println("The document produced this sub-dictionary, which includes "+entries + " entries.");
            for (String word:list) {
                pw.println(word);
            }
            pw.close();


        }catch (FileNotFoundException e){
            System.out.println("The file could not be opened! Please verify the file exists.");
        }
    }

    //All the methods needed to add or sort the words in the array list
    /**
     * <p>This method will check if the word can be part of the dictionary and if it can return the word corrected</p>
     * @param temp
     * @return String:the word that will be in the dictionary
     */
    public static String IsAWord(String temp){
        char last_char=temp.charAt(temp.length()-1);
        int length=temp.length();

        if(!(temp.contains("0")||temp.contains("1")||temp.contains("2")||temp.contains("3")||
                temp.contains("4")||temp.contains("5")||temp.contains("6")||temp.contains("7")||
                temp.contains("8")||temp.contains("9")|| temp.contains("="))) {
            if (length > 1) {
                if (last_char == '?' || last_char == ':' || last_char == ',' || last_char == ';' || last_char == '.') {
                    temp = temp.substring(0, temp.length() - 1);

                    if (temp.contains("\'"))
                    {
                        if (temp.charAt(temp.indexOf('\'') + 1) == 'm' || temp.charAt(temp.indexOf('\'') + 1) == 's')
                        {
                            temp = temp.substring(0, temp.indexOf("\'"));
                            return temp;
                        }
                        else return null;
                    }
                    return temp;
                } else if (temp.contains("\'")) {
                    if (temp.charAt(temp.indexOf('\'') + 1) == 's' || temp.charAt(temp.indexOf('\'') + 1) == 'm') {
                        temp = temp.substring(0, temp.indexOf("\'"));
                        return temp;
                    }
                    return null;
                }
                else
                    return temp;

            }
            else if (temp.equalsIgnoreCase("A") || temp.equalsIgnoreCase("I")) {
                return temp;
            }
            else return null;
        }

        return null;
    }

    /**
     * <p>This method will add all the accepted words into an ArrayList to then sort this list</p>
     * @param temp
     * @return ArrayList a list of all the words that were taken from the file excluding the numbers and/or non words
     */
    public static ArrayList<String> AddToArray(String temp,ArrayList<String> list){
        //ArrayList<String> list=new ArrayList<String>();
        boolean exists=false;
        if(temp!=null) {
            temp = temp.toUpperCase();
        }
        for (int i=0;i<list.size();i++){
            //checks if the word is already on the list
            if (list.get(i).equals(temp)||temp==null){
                exists=true;
                break;
            }
           exists=false;
        }
        //checks if the word was putted as null (meaning it wasn't an accepted word) or if it exists already if not it adds it to the list
        if(temp!=null && !exists){
            list.add(temp);
        }
        return list;
    }

    /**
     * <p>This method receives the list of words and sorts it in alphabetical order</p>
     * @param list the list of words recorded
     * @param n the length of the list(How many words are recorded)
     */
    public static /*ArrayList<String> */ void Sort(ArrayList<String> list,int n){
        if(n==1){
            return;
        }
        for(int i=0;i<n-1;i++){
            if (list.get(i).compareTo(list.get(i+1))>0){
                String temp=list.get(i);
                list.set(i,list.get(i+1));
                list.set(i+1,temp);
            }
        }
        Sort(list,n-1);
        //return list;
    }

    /**
     * <p>This method adds a header indicating the first letter of the word</p>
     * @param list the sorted list of words
     */
    public static void AddHeader(ArrayList<String> list){

        list.add(0,"A\n==");
        for (int i=0;i<list.size()-1;i++){
            String first_word=list.get(i),second_word=list.get(i+1);
            int difference=second_word.charAt(0)-first_word.charAt(0);
            char headers=first_word.charAt(0);
            if (difference>0 && second_word.charAt(0)!='A'){
                headers+=difference;
                list.add(i+1,headers+"\n==");
            }
        }
    }

}
