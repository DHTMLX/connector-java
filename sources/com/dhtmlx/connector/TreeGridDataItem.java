/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

import java.util.HashMap;

/**
 * The Class TreeGridDataItem.
 */
public class TreeGridDataItem extends GridDataItem {
	
	/** The count of kids */
	private int kids = -1;
	 
	/**
	 * Instantiates a new tree grid data item.
	 * 
	 * @param data the data
	 * @param config the config
	 * @param index the index
	 */
	public TreeGridDataItem(HashMap<String,String> data, DataConfig config, int index){
		super(data,config,index);
	}
	
	/**
	 * Gets the parent id.
	 * 
	 * @return the parent id
	 */
	public String get_parent_id(){
		return get_value(config.relation_id.name);
	}
	
	/**
	 * Sets the image of the item
	 * 
	 * @param image_path the new image of the item
	 */
	public void set_image(String image_path){
		set_cell_attribute(config.text.get(0).name, "image", image_path);
	}
	
	/**
	 * Count of child items
	 * 
	 * @return the count of child items
	 */
	public int has_kids(){
		return kids;
	}
	
	/**
	 * Sets the count of child items
	 * 
	 * @param count the count of child items
	 */
	public void set_kids(int count){
		kids = count;
		if (count!=0) 
			set_row_attribute("xmlkids",Integer.toString(count));
	}
}
