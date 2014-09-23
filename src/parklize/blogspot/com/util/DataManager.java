/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parklize.blogspot.com.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author GoFor2014
 */
public class DataManager {
    /**
     * 
     * @param reader
     * @param writer 
     * Function write to a new file with writer with refined data
     */
    public static void refine(BufferedReader reader, BufferedWriter writer){
        
        try {
            String str;
            while((str=reader.readLine())!=null){
                Pattern pattern = Pattern.compile("[\"\'](\\d{4}-\\d{2}-\\d{2}).(\\d{2}:\\d{2}:\\d{2}).*[\"\'](\\^\\^<http://www.w3.org/2001/XMLSchema#dateTime>)");
                Pattern pattern1 = Pattern.compile("\".*\"@.* <");
                Pattern pattern2 = Pattern.compile("[\"\'](\\d{4}-\\d{2}-\\d{2}).*[\"\'](\\^\\^<http://www.w3.org/2001/XMLSchema#date>)");
                Pattern pattern3 = Pattern.compile("(\".*\"@\\w{2}).* <");
                Pattern pattern4 = Pattern.compile("\"\"\\^\\^<http://www.w3.org/2001/XMLSchema#float>");
                Pattern pattern5 = Pattern.compile("\"\"\\^\\^<http://www.w3.org/2001/XMLSchema#integer>");
                Pattern pattern6 = Pattern.compile("\"\\d+\\.\\d+\"\\^\\^<http://www.w3.org/2001/XMLSchema#integer>");
                Matcher matcher = pattern.matcher(str);
                Matcher matcher1 = pattern1.matcher(str);
                Matcher matcher2 = pattern2.matcher(str);
                Matcher matcher3 = pattern3.matcher(str);
                Matcher matcher4 = pattern4.matcher(str);
                Matcher matcher5 = pattern5.matcher(str);
                Matcher matcher6 = pattern6.matcher(str);
                if(str.contains("<http://www.w3.org/2001/XMLSchema#dateTime>") || str.contains("<http://www.w3.org/2001/XMLSchema#date>")){
                    if(matcher.find()){
//                            System.out.println(str);
                        str = str.replaceAll("[\"\'](\\d{4}-\\d{2}-\\d{2}).(\\d{2}:\\d{2}:\\d{2}).*[\"\'](\\^\\^<http://www.w3.org/2001/XMLSchema#dateTime>)", "\"$1T$2\"$3");
                        writer.append(str);
                        writer.newLine();                           
//                            System.out.println("-------"+str);
                    }else if(matcher2.find()){
                        str = str.replaceAll("[\"\'](\\d{4}-\\d{2}-\\d{2}).*[\"\'](\\^\\^<http://www.w3.org/2001/XMLSchema#date>)", "\"$1\"$2");
                        writer.append(str);
                        writer.newLine();                           
                    }
                } else if (str.contains("<http://www.w3.org/2001/XMLSchema#integer>") || str.contains("<http://www.w3.org/2001/XMLSchema#float>")) {
                    if(matcher4.find()){
//                        System.out.println("matcher4"+str);
                        str = str.replaceAll("\"\"(\\^\\^<http://www.w3.org/2001/XMLSchema#float>)", "\"0\"$1");
                        writer.append(str);
                        writer.newLine();  
//                        System.out.println("matcher4"+str);
                    } else if (matcher5.find()){
//                        System.out.println("matcher5:"+str);
                        str = str.replaceAll("\"\"(\\^\\^<http://www.w3.org/2001/XMLSchema#integer>)", "\"0\"$1");
                        writer.append(str);
                        writer.newLine();  
//                        System.out.println("matcher5:"+str);
                    } else if (matcher6.find()){
//                        System.out.println("matcher6:"+str);
                        str = str.replaceAll("(\"\\d+\\.\\d+\"\\^\\^<http://www.w3.org/2001/XMLSchema#)integer>", "$1float>");
                        writer.append(str);
                        writer.newLine();
//                        System.out.println("matcher6:"+str);
                    }
                } else {
                    // find pattern "*****"@*** <
                    if(matcher1.find()){
                        // "*****"@en**** -> "*****"@en
                        if(matcher3.find()){
                            str = str.replaceAll("(\".*\"@\\w{2}).* <", "$1 <");
                            writer.append(str);
                            writer.newLine();                           
                        } 
                        // else filter out
                    } else {
                            writer.append(str);
                            writer.newLine();                           
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
        
