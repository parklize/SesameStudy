/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parklize.blogspot.com.learning;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.ParserConfig;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser.DatatypeHandling;
import org.openrdf.rio.helpers.BasicParserSettings;
import org.openrdf.sail.nativerdf.NativeStore;

/**
 *
 * @author Luhus
 */
public class RepositoryAPI {
    public static void main(String[] args){
        RepositoryConnection con = null;
        try {
            // Practice#3 : Create sesame triple store database with your local triple file
            // create a local sesame native store
            Repository nrp = new SailRepository(new NativeStore());
            nrp.initialize();
            
            // make connection and add your triples from local file to repository(store)
            String fn = "C:\\\\Users\\\\GoFor2014\\\\Downloads\\\\socialdata.nq";// use n-quads file in my case
            File file = new File(fn);
            con = nrp.getConnection();
            
            // before add file, you could set the config manually (for example, reserve bnodeid.. sesmae by default create own bnodeid while loading data
            boolean verifyData = true;
            boolean stopAtFirstError = true;
            boolean preserveBnodeIds = true;
            ParserConfig config = new ParserConfig();
            config.set(BasicParserSettings.PRESERVE_BNODE_IDS, true);
            con.setParserConfig(config);
            con.add(file, "file://"+file,RDFFormat.NQUADS);// add n-quads to connected repository, parameter1: datafile, parameter2: baseURI, parameter3: triple file type
        } catch (RepositoryException ex) {
            Logger.getLogger(RepositoryAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RepositoryAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RDFParseException ex) {
            Logger.getLogger(RepositoryAPI.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();// close connection!
            } catch (RepositoryException ex) {
                Logger.getLogger(RepositoryAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
