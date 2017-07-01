package beans;

public class SubforumSearchRequest {
	
	private String subforumId;
	private String description;
	private String responsibleModeratorId;
	
	public SubforumSearchRequest() {
		
	}
	
	public SubforumSearchRequest(String subforumId, String description, String responsibleModeratorId) {
		super();
		this.subforumId = subforumId;
		this.description = description;
		this.responsibleModeratorId = responsibleModeratorId;
	}

	public String getSubforumId() {
		return subforumId;
	}

	public void setSubforumId(String subforumId) {
		this.subforumId = subforumId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getResponsibleModeratorId() {
		return responsibleModeratorId;
	}

	public void setResponsibleModeratorId(String responsibleModeratorId) {
		this.responsibleModeratorId = responsibleModeratorId;
	}
	
}
