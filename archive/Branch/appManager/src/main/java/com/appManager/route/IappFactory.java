package com.appManager.route;

import java.util.List;

import com.appManager.exceptions.MyException;
import com.appManager.model.BluePrint;

public interface IappFactory {
	public List<String> doAction(BluePrint blueprint,String whatAction) throws MyException;
}
