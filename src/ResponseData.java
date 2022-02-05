/**
 * ResponseData.java
 * 
 * Wrapper class to contain response data.
 * @author Zinah, Mack, Vilmos
 *
 */
public class ResponseData {

	private String response;

	/**
	 * base constructor
	 */
	public ResponseData() {

	}

	/**
	 * getter for response
	 * @return response, value of response
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * setter for response
	 * @param response, new value of response
	 */
	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * clears the response
	 */
	public void clear() {
		this.response = null;
	}
}
