/*
 * Created on Feb 8, 2010
 *
 */
package vandyapps.com.wolfram;

import com.wolfram.alpha.visitor.Visitable;


public interface WAUnits extends Visitable {

    String[] getLongNames();
    String[] getShortNames();
    WAImage getImage();
}
