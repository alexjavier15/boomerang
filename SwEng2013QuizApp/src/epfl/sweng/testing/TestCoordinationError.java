package epfl.sweng.testing;
//@formatter:off
/**
 * An exception that is thrown whenever the TestCoordinator detects an inconsistency.
 */
public class TestCoordinationError extends AssertionError {
    private static final long serialVersionUID = 1L;

    public TestCoordinationError(String msg) {
        super(msg);
    }
}