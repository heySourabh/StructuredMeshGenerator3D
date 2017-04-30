package mesh;

/**
 *
 * @author Sourabh Bhat <sourabh.bhat@iitb.ac.in>
 */
public class Parameter {

    public final double val;

    public Parameter(double val) {
        if (val < 0.0 || val > 1.0) {
            throw new IllegalArgumentException(
                    "The value of parameter must be within the range 0 and 1");
        }

        this.val = val;
    }
}
