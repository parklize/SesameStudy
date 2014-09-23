/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parklize.blogspot.com.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.openrdf.model.Statement;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.util.RDFInserter;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;

/**
 *
 * @author Luhus
 */
public class ChunkCommitter implements RDFHandler {
    
    private RDFInserter inserter;
    private RepositoryConnection con;
    private long count = 0L;
    private long chunkSize = 500000L;// do commit every 500000line
    
    public ChunkCommitter(RepositoryConnection con){
        inserter = new RDFInserter(con);
        this.con = con;
    }
    
    @Override
    public void startRDF() throws RDFHandlerException {
        inserter.startRDF();
    }

    @Override
    public void endRDF() throws RDFHandlerException {
        inserter.endRDF();
    }

    @Override
    public void handleNamespace(String prefix, String uri) throws RDFHandlerException {
        inserter.handleNamespace(prefix, uri);
    }

    @Override
    public void handleStatement(Statement stmnt) throws RDFHandlerException {
        inserter.handleStatement(stmnt);
        count++;
        if(count % chunkSize == 0){
            try {
                con.commit();
            } catch (RepositoryException ex) {
                Logger.getLogger(ChunkCommitter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void handleComment(String string) throws RDFHandlerException {
        inserter.handleComment(string);
    }
    
}
