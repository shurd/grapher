/*
 * Created on Dec 9, 2009
 *
 */
package vandyapps.com.wolfram;

import com.wolfram.alpha.visitor.Visitable;


public interface WAInfo extends Visitable {

    String getText();
    Visitable[] getContents();

}
