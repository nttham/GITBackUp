package hello;

import java.io.Serializable;

public class RequestOtpVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String toRecipient;
	
	private String fromMail;

	public String getToRecipient() {
		return toRecipient;
	}

	public void setToRecipient(String toRecipient) {
		this.toRecipient = toRecipient;
	}

	public String getFromMail() {
		return fromMail;
	}

	public void setFromMail(String fromMail) {
		this.fromMail = fromMail;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fromMail == null) ? 0 : fromMail.hashCode());
		result = prime * result + ((toRecipient == null) ? 0 : toRecipient.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequestOtpVO other = (RequestOtpVO) obj;
		if (fromMail == null) {
			if (other.fromMail != null)
				return false;
		} else if (!fromMail.equals(other.fromMail))
			return false;
		if (toRecipient == null) {
			if (other.toRecipient != null)
				return false;
		} else if (!toRecipient.equals(other.toRecipient))
			return false;
		return true;
	}
	
}
