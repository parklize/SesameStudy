/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parklize.blogspot.com.learning;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.ParserConfig;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.BasicParserSettings;
import org.openrdf.sail.nativerdf.NativeStore;
import parklize.blogspot.com.util.ChunkCommitter;

/**
 *
 * @author Luhus
 */
public class RepositoryAPI4GZip {
    
    public static void main(String[] args){
        RepositoryConnection con = null;
        try {
            // Practice#4 : Create sesame triple store database with your big triple file (Gzip)
            // create a local sesame native store
            Repository nrp = new SailRepository(new NativeStore(new File("C:\\Users\\Luhus\\Documents\\Temp\\SesameSotre")));
            nrp.initialize();
            con = nrp.getConnection();
            
            File file = new File("C:\\Users\\Luhus\\Documents\\Temp\\ccrdf.html-rdfa.351.nq.gz");
            InputStream is = new GZIPInputStream(new FileInputStream(file));
            
            RDFParser parser = Rio.createParser(RDFFormat.NQUADS);
            parser.setRDFHandler(new ChunkCommitter(con));
            
            // before add file, you could set the config manually (for example, reserve bnodeid.. sesmae by default create own bnodeid while loading data
            boolean verifyData = true;
            boolean stopAtFirstError = true;
            boolean preserveBnodeIds = true;
            ParserConfig config = new ParserConfig();
            config.set(BasicParserSettings.PRESERVE_BNODE_IDS, true);
            con.setParserConfig(config);
            
            con.begin();
            parser.parse(is, "file://"+file.getCanonicalPath());
            con.commit();
            
            String queryString = "SELECT (COUNT(*) AS ?count) WHERE {?x ?p ?y}";
            TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();
            while(result.hasNext()){
                BindingSet bindingSet = result.next();
                System.out.println(bindingSet.getValue("count"));
//                    String content = bindingSet.getValue("c").toString().toLowerCase();
//                    if(content.contains("gangnam") || content.contains(" psy ")){
//                        System.out.println(bindingSet.getValue("s"));
//                        System.out.print(bindingSet.getValue("time")+"-----");
//                        System.out.println(bindingSet.getValue("content"));
//                    }
            }
        } catch (RepositoryException ex) {
            Logger.getLogger(RepositoryAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RepositoryAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RDFParseException ex) {
            Logger.getLogger(RepositoryAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RDFHandlerException ex) {
            Logger.getLogger(RepositoryAPI4GZip.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedQueryException ex) {
            Logger.getLogger(RepositoryAPI4GZip.class.getName()).log(Level.SEVERE, null, ex);
        } catch (QueryEvaluationException ex) {
            Logger.getLogger(RepositoryAPI4GZip.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();// close connection!
            } catch (RepositoryException ex) {
                Logger.getLogger(RepositoryAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
