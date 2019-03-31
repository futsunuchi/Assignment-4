import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Searches for specific authors and produces the bibliography
 * @author <p>Juan Sebastian Hoyos <br/>40087920</p>
 */
public class AuthorBibCreator1 {
    public static void  main(String[] args)
    {
        Scanner sc=new Scanner(System.in);
        Scanner[] scanners=new Scanner[10];
        PrintWriter[] out_pw=new PrintWriter[3],backup_pw=new PrintWriter[3];
        File[] output_files=new File[3],backup_files=new File[3];
        String[] output_names,backup_names;
        String name;
        int file_error=0;

        //Asking the user for the author they would like to search for
        System.out.print("Enter the name of the author you would like to search: ");
        name=sc.next();

        //opening all the input files
        scanners=OpenFiles(scanners);

        //generating the name of the output files and the output files
        output_names=GenerateFileName(name,false);
        backup_names=GenerateFileName(name,true);
        for (int i=0;i<output_names.length;i++)
        {
            output_files[i]=new File(output_names[i]);
            backup_files[i]=new File(backup_names[i]);
        }
        try {
            if (FileExists(output_files)) {
                System.out.println("The files already exists");
                throw new FileExistsException();
            }
            System.out.print("Creating the output files");
            for (int j = 0; j < output_files.length; j++) {
                file_error=j;
                out_pw[j]=new PrintWriter(output_files[j]);
            }
            ProcessBibFile(out_pw,scanners,name);
        }
        catch (FileExistsException e)
        {
            if (FileExists(backup_files))
            {
                int counter=0;
                System.out.print("The backup files already exist. We'll update them!");
                try {
                    for (int j = 0; j < backup_files.length; j++) {
                        backup_files[j].delete();
                        output_files[j].renameTo(new File(backup_names[j]));
                        backup_files[j] = new File(output_names[j]);
                        counter++;
                        out_pw[j] = new PrintWriter(backup_files[j]);
                        ProcessBibFile(out_pw,scanners,name);
                    }
                }catch (FileNotFoundException e1)
                {
                    FileError(counter,out_pw);
                }
            }
            else{
                int counter=0;

                //Creating the print writers for each backup file
                try {

                    for (int j = 0; j < backup_files.length; j++) {
                        counter=j;
                        backup_pw[j] = new PrintWriter(backup_files[j]);
                    }
                    ProcessBibFile(backup_pw,scanners,name);
                }catch (FileNotFoundException e1)
                {
                    FileError(counter,backup_pw);
                }
            }
        }
        catch (FileNotFoundException e)
        {
            FileError(file_error,out_pw);
        }
        finally {
            for(int i=0;i<out_pw.length;i++)
            {
                try {
                    out_pw[i].close();
                    if (output_files[0].length()==0)
                    {
                        System.out.println("\ndeleting empty files");
                        output_files[0].delete();
                        output_files[1].delete();
                        output_files[2].delete();
                        System.exit(0);
                    }
                    sc.close();
                }
                catch(NullPointerException e)
                {
                    System.exit(0);
                }
            }

        }

    }

    /**
     * <p>This method opens all the files and handles the exception if thrown when trying to open the file<br/>
     * Only used for input files</p>
     * @param scanners Scanner[]
     * @return RandomAccessFile
     */
    public static Scanner[] OpenFiles(Scanner[] scanners) {
        String file_name="C:\\Users\\juans\\Desktop\\a3_40087920\\A3_249\\Latex";
        int counter=0;
        try{
            for (int i=1;i<=scanners.length;i++)
            {
                scanners[i-1]=new Scanner(new FileInputStream(file_name+i+".bib"));
                counter=i;
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Could not open input file Latex"+counter+".bib\nPlease check if file exists! Program will terminate" +
                    "after closing open files");

                System.exit(0);

        }
        return scanners;
    }
    public static Boolean FileExists(File[] files){
        for (int i=0;i<files.length;i++){
            if (files[i].exists())
            {
                return true;
            }
        }
        return false;
    }
    public  static String[] GenerateFileName(String name,boolean backup){
        String[] file_names=new String[3];
        if (!backup) {
            file_names[0] = name + "-IEEE.json";
            file_names[1] = name + "-ACM.json";
            file_names[2] = name + "-NJ.json";
        }
        else{
            file_names[0] = name + "-IEEE-BU.json";
            file_names[1] = name + "-ACM-BU.json";
            file_names[2] = name + "-NJ-BU.json";
        }
        return file_names;
    }
    public static void FileError(int file_error,PrintWriter[] out_pw){
        switch (file_error){
            case 0: System.out.println("There was an error creating IEEE File.The program will terminate!");
                System.exit(0);
            case 1:
                System.out.println("There was an error creating ACM File.The program will close the IEEE file and terminate!");
                out_pw[0].close();
                System.exit(0);
            case 2:
                System.out.println("There was an error creating NJ File.The program will close the IEEE and ACM file and terminate!");
                out_pw[0].close();
                out_pw[1].close();
                System.exit(0);
        }
    }
    public static void ProcessBibFile(PrintWriter[] printers,Scanner[] scanners,String name) {
        String author="",journal="",title="",year="",volume="",number="",pages="",doi="",month="",temp;
        //StringTokenizer tokenizer=new StringTokenizer("");
        int counter=0;
        for (int i=0;i<scanners.length;i++)
        {

            while(scanners[i].hasNextLine())
            {
                temp=scanners[i].nextLine();
                int index=temp.indexOf('{')+1;
                //if(temp.equals("")) continue;
                //reference=temp.substring(temp.indexOf('{'),temp.indexOf('}'));
                if(temp.contains("author")) {
                    //temp=tokenizer.nextToken("{");
                    author=temp.substring(index,temp.indexOf('}'));
                }
                if(temp.contains("journal")){
                   // temp=tokenizer.nextToken("{");
                    journal=temp.substring(index,temp.indexOf('}'));
                }
                if(temp.contains("title"))  {
                   // temp=tokenizer.nextToken("{");
                    title=temp.substring(index,temp.indexOf('}'));
                }
                if(temp.contains("year"))   {
                   // temp=tokenizer.nextToken("{");
                    year=temp.substring(index,temp.indexOf('}'));
                }
                if(temp.contains("volume")) {
                  //  temp=tokenizer.nextToken("{");
                    volume=temp.substring(index,temp.indexOf('}'));
                }
                if(temp.contains("number")) {
                    //temp=tokenizer.nextToken("{");
                    number=temp.substring(index,temp.indexOf('}'));
                }
                if(temp.contains("pages"))  {
                   // temp=tokenizer.nextToken("{");
                    pages=temp.substring(index,temp.indexOf('}'));
                }
                if(temp.contains("doi"))    {
                   // temp=tokenizer.nextToken("{");
                    doi=temp.substring(index,temp.indexOf('}'));
                }
                if(temp.contains("month"))  {
                    //temp=tokenizer.nextToken("{");
                    month=temp.substring(index,temp.indexOf('}'));
                }
                if(temp.equals("}"))
                {
                   if(author.contains(name))
                   {
                       counter++;
                       printers[0].write(author.replace("and", ",") + " \"" + title + "\", "
                               + journal + ",vol. " + volume + ", no" + number + ",p." + pages + ", " + month + " " + year + ".\n\n");
                        printers[0].flush();
                        printers[1].write("["+counter+"]\t"+author.substring(0,author.indexOf("and"))+" et al."+ year
                        +". " +title+". "+ journal+ volume + ", (" + year + ")," + pages + ". " +  "DOI:https://doi.org/"+doi.substring(0,doi.indexOf('/'))+
                                doi.substring(doi.indexOf('/'))+".\n\n");
                        printers[1].flush();

                        printers[2].write(author.replace("and", "&") + ". " + title + ". "
                                + journal+". "+volume+", "+pages+"("+year+")\n");
                        printers[2].flush();
                   }
                }
            }
        }
    }
}