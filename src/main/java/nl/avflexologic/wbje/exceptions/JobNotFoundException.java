package nl.avflexologic.wbje.exceptions;

/**
 * Exception indicating that a job could not be found for the given id.
 */
public class JobNotFoundException extends RuntimeException {

    public JobNotFoundException(Long id) {
        super("Job not found for id: " + id);
    }
}
