package hello;

public class ValidateOTPVO {
	private String otpCode;

	/**
	 * @return the otpCode
	 */
	public String getOtpCode() {
		return otpCode;
	}

	/**
	 * @param otpCode the otpCode to set
	 */
	public void setOtpCode(String otpCode) {
		this.otpCode = otpCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((otpCode == null) ? 0 : otpCode.hashCode());
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
		ValidateOTPVO other = (ValidateOTPVO) obj;
		if (otpCode == null) {
			if (other.otpCode != null)
				return false;
		} else if (!otpCode.equals(other.otpCode))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ValidateOTPVO [otpCode=" + otpCode + "]";
	}
	
	
}
