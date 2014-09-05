/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parklize.blogspot.com.learning;

import org.openrdf.model.Literal;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.vocabulary.FOAF;
import org.openrdf.model.vocabulary.RDF;

/**
 *
 * @author guangyuan
 */
public class SesameAPI {
    public static void main(String[] args){
        
        ValueFactory factory = ValueFactoryImpl.getInstance();
        /* create personal profile ontology in example http://parklize.blogspot.ie/2014/05/semantic-web-practice-sparql-in-protege.html */
        URI pgy = factory.createURI("http://parklize.blogspot.com/guangyuan-piao"); // Subject
        URI name = factory.createURI("http://xmlns.com/foaf/0.1/name"); // Predict
        Literal pgyName = factory.createLiteral("Guangyuan Piao"); // Object
        Statement nameStat = factory.createStatement(pgy, name, pgyName); // Create statement
        
        // Use predefined vocabularies from Sesame (RDF, RDFS, DC, FOAF ...)
        Statement typeStat = factory.createStatement(pgy, RDF.TYPE, FOAF.PERSON); // state me as foaf person
        Literal age = factory.createLiteral(28);
        Statement ageStat = factory.createStatement(pgy, FOAF.AGE, age);
        // Actual collection of RDF data is just that: a collection. org.openrdf.model.Model is an extension of the default Java Collection class java.util.Set<Statement>
        Model model = new LinkedHashModel();
        // Add RDF statements
        model.add(nameStat);
        model.add(typeStat);
        model.add((ageStat));
        // Or add by simply providing S,P,O
        Literal fn = factory.createLiteral("Guangyuan");
        Literal ln = factory.createLiteral("Piao");
        model.add(pgy, FOAF.FIRST_NAME, fn);
        model.add(pgy, FOAF.LAST_NAME, ln);
        
        for(Statement s : model){
            System.out.println(s);
        }
    }
}
