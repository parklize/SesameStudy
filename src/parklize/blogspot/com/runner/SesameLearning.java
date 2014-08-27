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
public class SesameLearning {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            RepositoryConnection con = SesameManager.repositoryConnect("C:\\Users\\GoFor2014\\Documents\\NetBeansProjects\\SesameStudy");
//            SesameManager.insertNQDataToRepo(con, "C:\\sample.nq");
            String queryString = "SELECT (COUNT(*) AS ?count) WHERE {?x ?p ?y}";
            try {
                TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
                TupleQueryResult result = tupleQuery.evaluate();
                while(result.hasNext()){
                    BindingSet bindingSet = result.next();
                    System.out.println(bindingSet.getValue("count"));
                }
            } catch (MalformedQueryException ex) {
                Logger.getLogger(SesameLearning.class.getName()).log(Level.SEVERE, null, ex);
            } catch (QueryEvaluationException ex) {
                Logger.getLogger(SesameLearning.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (RepositoryException ex) {
            Logger.getLogger(SesameLearning.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
