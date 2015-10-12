package <%=packageNameGenerated%>;

import <%=packageNameGenerated%>.config.TitanConfiguration
import com.thinkaurelius.titan.core.TitanGraph

/**
 * Created by rparry on 10/10/15.
 */
class MakeMeHappy {

    public static void main(String[] a) {
        TitanGraph g = new TitanConfiguration().getTitanGraph();
    }
}