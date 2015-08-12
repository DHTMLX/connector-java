package com.dhtmlx.connector;

public class TreeCommonGroupFactory extends TreeCommonFactory {

	public RenderStrategy createRenderStrategy() {
		return new GroupRenderStrategy();
	}

}
