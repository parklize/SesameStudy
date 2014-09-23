/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parklize.blogspot.com.runner;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openrdf.model.Resource;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.sail.nativerdf.NativeStore;
import parklize.blogspot.com.util.SesameManager;

/**
 *
 * @author GoFor2014
 */
public class SesameInteractor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //// Prefix definition 
            String prefix = "PREFIX sioc:<http://rdfs.org/sioc/ns#>\n"
                    + "PREFIX dc:<http://purl.org/dc/terms/>\n"
                    + "PREFIX foaf:<http://xmlns.com/foaf/0.1/>\n"
                    + "PREFIX og:<http://ogp.me/ns#>\n"
                    + "PREFIX ogp:<http://opengraphprotocol.org/schema/>\n"
                    + "PREFIX schema:<http://schema.org/>\n"
                    + "PREFIX fn:<http://www.w3.org/2005/xpath-functions/>\n";
            //// Repository connection
            RepositoryConnection con = SesameManager.repositoryConnect("C:\\Users\\GoFor2014\\Documents\\NetBeansProjects\\SesameStudy");
//            SesameManager.insertNQDataToRepo(con, "C:\\Users\\GoFor2014\\Downloads\\socialdata.nq");
//            SesameManager.clearRepository(con);
            //// Query String
            // Query for check no. of quads
            String queryString = "SELECT (COUNT(*) AS ?count) WHERE {?x ?p ?y}";
//            queryString = prefix+"SELECT (COUNT(*) AS ?count) WHERE {"
//                    + "{?s dc:created ?t .} UNION "
//                    + "{?s dc:date ?t .} UNION "
//                    + "{?s dc:issued ?t .} UNION "
//                    + "{?s schema:datePublished ?t .} UNION "
//                    + "{?s schema:startDate ?t .} "
//                    + "{?s <http://purl.org/rss/1.0/modules/content/encoded> ?c .} UNION "
//                    + "{?s dc:title ?c .} UNION "
//                    + "{?s og:description ?c .} UNION "
//                    + "{?s schema:articleBody ?c .} UNION "
//                    + "{?s sioc:content ?c .} UNION "
//                    + "{?s schema:blogPosts ?c .} UNION "
//                    + "{?s schema:blogPost ?c .} UNION "
//                    + "{?s ogp:description ?s .} UNION "
//                    + "{?s ogp:title ?s .} UNION "
//                    + "{?s schema:articleSection ?s .} UNION "
//                    + "{?s schema:keywords ?s .} UNION "
//                    + "{?s schema:description ?s .} UNION "
//                    + "{?s <http://purl.org/dc/elements/1.1/title> ?c .}"
//                    + "{?s rdf:type sioc:Post .} UNION "
//                    + "{?s rdf:type foaf:Document .} UNION "
//                    + "{?s rdf:type sioc:Item .} UNION "
//                    + "{?s rdf:type sioc:BlogPost .} "
//                    + "FILTER (year(?t) = 2012 && CONTAINS(?c,\"gangnam\"))"
//                    + "}";
            queryString = prefix + "SELECT DISTINCT ?s ?time WHERE { "
                    + "{?s rdf:type ?type .} UNION "
                    + "{?s og:type \"article\" .} UNION "
                    + "{?s og:type \"blog\" .} UNION "
                    + "{?s ogp:type \"article\" .} UNION "
                    + "{?s ogp:type \"blog\" .} "
                    + "?s ?p1 ?content . "
                    + "?s ?p2 ?time . "
                    + "VALUES ?type {sioc:BlogPost sioc:Item sioc:Post foaf:Document} "
                    + "VALUES ?p1 {dc:title og:description ogp:description ogp:title schema:articleBody sioc:content "
                    + "schema:blogPosts schema:blogPost schema:articleSection schema:keywords "
                    + "schema:description <http://purl.org/dc/elements/1.1/title> <http://purl.org/rss/1.0/modules/content/encoded>} "
                    + "VALUES ?p2 {dc:created dc:date dc:issued schema:datePublished schema:startDate} "
                    + "FILTER (year(?time) = 2012 && CONTAINS(LCASE(?content),\"gangnam style\")) "
                    + "} "
                    + "ORDER BY ?time";
            try {
                TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                TupleQueryResult result = tupleQuery.evaluate();
                while(result.hasNext()){
                    BindingSet bindingSet = result.next();
                    System.out.print(bindingSet.getValue("time")+"---");
                    System.out.println(bindingSet.getValue("s"));
                }
            } catch (MalformedQueryException ex) {
                Logger.getLogger(SesameInteractor.class.getName()).log(Level.SEVERE, null, ex);
                
            } catch (QueryEvaluationException ex) {
                Logger.getLogger(SesameInteractor.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (RepositoryException ex) {
            Logger.getLogger(SesameInteractor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
