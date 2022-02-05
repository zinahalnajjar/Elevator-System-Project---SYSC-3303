/**
 * Wrapper class to contain response data.
 * 
 * @author zeena
 *
 */
public class ResponseData {

	private String response;

	public ResponseData() {

	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	// clear response
	public void clear() {
		this.response = null;
	}
}
