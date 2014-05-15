package response;

public class GetGenomeReleaseRespons extends Response {

	String body;
	public GetGenomeReleaseRespons(int code, String body) {
		this.code=code;
		this.body=body;
	}
	
	@Override
	public String getBody(){
		return body;
	}

}
