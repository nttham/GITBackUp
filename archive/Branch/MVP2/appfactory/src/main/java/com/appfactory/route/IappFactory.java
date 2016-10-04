package com.appfactory.route;

import java.util.List;

import com.appfactory.exceptions.MyException;
import com.appfactory.model.UIModelJson;

public interface IappFactory {
	//public List<String> doAction(BluePrint blueprint,String whatAction, String messageID) throws MyException;
	public List<String> doAction(UIModelJson uIModelJson,String whatAction, String messageID) throws MyException;
	public void delete();
}
