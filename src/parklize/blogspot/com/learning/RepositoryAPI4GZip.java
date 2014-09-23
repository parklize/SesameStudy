/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parklize.blogspot.com.learning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashSet;
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
import org.openrdf.rio.RioSetting;
import org.openrdf.rio.helpers.BasicParserSettings;
import org.openrdf.sail.nativerdf.NativeStore;
import parklize.blogspot.com.util.ChunkCommitter;
import parklize.blogspot.com.util.DataManager;

/**
 *
 * @author Luhus
 */
public class RepositoryAPI4GZip {
    
    public static void main(String[] args){
        
        RepositoryConnection con = null;
        try {
            String prefix = "PREFIX sioc:<http://rdfs.org/sioc/ns#>\n"
                    + "PREFIX sioct:<http://rdfs.org/sioc/types#>\n"
                    + "PREFIX dc:<http://purl.org/dc/terms/>\n"
                    + "PREFIX foaf:<http://xmlns.com/foaf/0.1/>\n"
                    + "PREFIX og:<http://ogp.me/ns#>\n"
                    + "PREFIX ogp:<http://opengraphprotocol.org/schema/>\n"
                    + "PREFIX schema:<http://schema.org/>\n"
                    + "PREFIX fn:<http://www.w3.org/2005/xpath-functions/>\n";
            //// Initialize a local sesame native store
            String dbPath = "C:\\Users\\GoFor2014\\Downloads\\SesameStore";
            Repository nrp = new SailRepository(new NativeStore(new File(dbPath)));
            nrp.initialize();
            con = nrp.getConnection();
            
            // Refine gz file to nq file
            String filePath = "C:\\Users\\GoFor2014\\Downloads\\ccrdf.html-rdfa.351.nq.gz";
            String nqFilePath = filePath.substring(0, filePath.length()-3);
            File file = new File(filePath);
//            InputStream is = new GZIPInputStream(new FileInputStream(file));
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            
//            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.substring(0, filePath.length()-3)));
//            String str;
//            while((str=reader.readLine())!=null){
//                DataManager.refine(reader, writer);
//            }
            
            // Add refined nq file to repository
            FileReader nqReader = new FileReader(nqFilePath);
            
            // before add file, you could set the config manually (for example, reserve bnodeid.. sesmae by default create own bnodeid while loading data
            ParserConfig config = new ParserConfig();
            config.set(BasicParserSettings.PRESERVE_BNODE_IDS, Boolean.TRUE);
//            config.set(BasicParserSettings.VERIFY_DATATYPE_VALUES, Boolean.TRUE);
//            config.set(BasicParserSettings.FAIL_ON_UNKNOWN_DATATYPES, Boolean.TRUE);
            con.setParserConfig(config);
//            con.getParserConfig().addNonFatalError(BasicParserSettings.VERIFY_DATATYPE_VALUES);
//            con.getParserConfig().setNonFatalErrors(new HashSet<RioSetting<?>>(Arrays.asList(BasicParserSettings.VERIFY_DATATYPE_VALUES,BasicParserSettings.FAIL_ON_UNKNOWN_DATATYPES)));
            
//            RDFParser parser = Rio.createParser(RDFFormat.NQUADS);
//            parser.setRDFHandler(new ChunkCommitter(con));
//            con.begin();
//            parser.parse(nqReader, "file://"+file.getCanonicalPath());
//            con.commit();
//            con.add(nqReader, "http://parklize.blogspot.com", RDFFormat.NQUADS);
            
            String queryString = "SELECT (COUNT(*) AS ?count) WHERE {?x ?p ?y}";
            queryString = prefix + "SELECT DISTINCT ?com ?time WHERE { "
                    + "{?com rdf:type ?type .} "
                    + "{?com sioc:reply_of ?post .} "
                    + "{?post ?ctype ?creator .} "
                    + "{?com ?ctime ?time .}"
//                    + "{?person foaf:knows ?person2 . } "
//                    + "{?s og:type \"article\" .} UNION "
//                    + "{?s og:type \"blog\" .} UNION "
//                    + "{?s ogp:type \"article\" .} UNION "
//                    + "{?s ogp:type \"blog\" .} "
//                    + "?s ?p1 ?content . "
//                    + "?s ?p2 ?time . 
                      + "?creator ?p ?o . "
                    + "VALUES ?ctype {dc:creator sioc:has_creator dc:author} "
                    + "VALUES ?type {sioct:Comment} "
//                    + "VALUES ?p1 {dc:title og:description ogp:description ogp:title schema:articleBody sioc:content "
//                    + "schema:blogPosts schema:blogPost schema:articleSection schema:keywords "
//                    + "schema:description <http://purl.org/dc/elements/1.1/title> <http://purl.org/rss/1.0/modules/content/encoded>} "
                    + "VALUES ?ctime {dc:created dc:date dc:issued schema:datePublished schema:startDate} "
//                    + "FILTER (year(?time) = 2012)"
                    + "}";
            TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
            TupleQueryResult result = tupleQuery.evaluate();
            
            while(result.hasNext()){
                BindingSet bindingSet = result.next();
//                System.out.println(bindingSet.getValue("s")+"->"+bindingSet.getValue("p")+"->"+bindingSet.getValue("o"));
                System.out.println(bindingSet.getValue("com")+"->"+bindingSet.getValue("time")+"->"+bindingSet.getValue("page"));
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
