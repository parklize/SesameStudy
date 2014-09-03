/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parklize.blogspot.com.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openrdf.model.Resource;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.sail.nativerdf.NativeStore;

/**
 *
 * @author GoFor2014
 */
public class SesameManager {
    
    /*
        Get repository connection
    */
    public static RepositoryConnection repositoryConnect(String repoPath){
        RepositoryConnection con = null;
        try {
            File dataDir = new File(repoPath);
            String indexes = "spoc,posc,cosp";
            Repository repo = new SailRepository(new NativeStore(dataDir,indexes));
            
            repo.initialize();
            con = repo.getConnection();
        } catch (RepositoryException ex) {
            Logger.getLogger(SesameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }
    
    /*
        Insert nquads triples to repository
    */
    public static void insertNQDataToRepo(RepositoryConnection con, String dataFilePath){
            File file = new File(dataFilePath);
            String baseURI = "http://parklize.blogspot.com";
        try {
            con.add(file, baseURI, RDFFormat.NQUADS);
        } catch (IOException ex) {
            Logger.getLogger(SesameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RDFParseException ex) {
            Logger.getLogger(SesameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RepositoryException ex) {
            Logger.getLogger(SesameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
        Clear repository
    */
    public static void clearRepository(RepositoryConnection con){
        try {
            Resource[] rArray = new Resource[10000000];
            RepositoryResult rr = con.getContextIDs();
            int i = 0;
            while(rr.hasNext()){
                rArray[i] = (Resource) rr.next();
                i++;
            }
            con.clear(rArray);
        } catch (RepositoryException ex) {
            Logger.getLogger(SesameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
