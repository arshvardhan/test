package com.kelltontech.framework.parser;

import com.kelltontech.framework.model.IModel;

public interface IParser {
	public IModel parse(String payload) throws Exception;

	public String toString();
}
