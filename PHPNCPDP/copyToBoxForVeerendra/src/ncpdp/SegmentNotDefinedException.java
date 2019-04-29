package ncpdp;

public class SegmentNotDefinedException extends RuntimeException {

	private static final long serialVersionUID = -697403322994905138L;

	public SegmentNotDefinedException() {
		super();
	}

	public SegmentNotDefinedException(String message, Throwable cause) {
		super(message, cause);
	}

	public SegmentNotDefinedException(String message) {
		super(message);
	}

	public SegmentNotDefinedException(Throwable cause) {
		super(cause);
	}
}
