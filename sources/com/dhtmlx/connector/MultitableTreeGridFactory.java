package com.dhtmlx.connector;

public class MultitableTreeGridFactory extends TreeGridFactory {

	public RenderStrategy createRenderStrategy() {
		return new MultitableTreeRenderStrategy();
	}
	
}
