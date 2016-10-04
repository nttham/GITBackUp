package com.appfactory.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class Condition implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4758053480793353059L;
	String key,operator,value,operand;
	
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}
	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the operand
	 */
	public String getOperand() {
		return operand;
	}
	/**
	 * @param operand the operand to set
	 */
	public void setOperand(String operand) {
		this.operand = operand;
	}

}
