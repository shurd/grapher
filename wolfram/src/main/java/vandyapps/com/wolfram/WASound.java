/*
 * Created on Dec 9, 2009
 *
 */
package vandyapps.com.wolfram;

import java.io.File;

import com.wolfram.alpha.visitor.Visitable;


public interface WASound extends Visitable {
    
    String getURL();
    
    String getFormat();
    
    File getFile();
    
    void acquireSound();

}
