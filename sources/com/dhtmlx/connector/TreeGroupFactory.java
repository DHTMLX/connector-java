package com.dhtmlx.connector;

public class TreeGroupFactory extends TreeFactory {

	public RenderStrategy createRenderStrategy() {
		return new GroupRenderStrategy();
	}
	
}
