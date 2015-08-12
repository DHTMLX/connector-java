package com.dhtmlx.connector;

public class TreeGridGroupFactory extends TreeGridFactory {

	public RenderStrategy createRenderStrategy() {
		return new GroupRenderStrategy();
	}

}
