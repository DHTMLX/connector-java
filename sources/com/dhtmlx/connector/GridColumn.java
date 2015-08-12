package com.dhtmlx.connector;

import java.util.HashMap;

public class GridColumn {
	private String header;
	private String type;
	private String id;
	private int width;
	private String align;
	private String sort;
	private String color;
	private String valign;
	private Boolean hidden;
	private String source;
	private Boolean autocomplete;
	private HashMap<String,String> attributes;

	public String getHeader(){
		return header; 
	}
	public void setHeader(String value){
		header=value; 
	}
	
	public String getType(){
		return type; 
	}
	public void setType(String value){
		type=value; 
	}
	
	public String getId(){
		return id; 
	}
	public void setId(String value){
		id=value; 
	}
	
	
	public int getWidth(){
		return width; 
	}
	public void setWidth(int value){
		width=value; 
	}
	
	
	public String getAlign(){
		return align; 
	}
	public void setAlign(String value){
		align=value; 
	}
	
	public String getVAlign(){
		return valign; 
	}
	public void setVAlign(String value){
		valign=value; 
	}
	
	public String getSort(){
		return sort; 
	}
	public void setSort(String value){
		sort=value; 
	}
	
	
	public String getColor(){
		return color; 
	}
	public void setColor(String value){
		color=value; 
	}
	
	
	public Boolean isHidden(){
		return hidden; 
	}
	public void setHidden(Boolean value){
		hidden=value; 
	}
	
	public void setSource(String value) {
		source = value;
	}
	
	public String getSource() {
		return source;
	}
	
	public void setAutocomplete(Boolean value) {
		autocomplete = value;
	}
	
	public Boolean getAutocomplete() {
		return autocomplete;
	}
	
	public void setAttribute(String name, String value) {
		attributes.put(name, value);
	}
	
	public GridColumn(){
		header="";
		type="ro";
		id="";
		width=100;
		align="left";
		sort="str";
		color="";
		valign="";
		hidden=false;
		source="";
		autocomplete=false;
		attributes = new HashMap<String,String>();
	}

	public GridColumn(String header, String type, String id, int width, String align, String valign, String sort, String  color, Boolean hidden){
		this();
		if (header!=null)
			this.header=header;
		
		if (type!=null)
			this.type=type;
		
		if (id!=null)
			this.id=id;
		
		if (width>0)
			this.width=width;
		
		if (align!=null)
			this.align=align;
	
		if (valign!=null)
			this.valign=valign;
		
		if (sort!=null)
			this.sort=sort;
		
		if (color!=null)
			this.color=color;
		
		if (hidden!=null)
			this.hidden=hidden;

	}
	public String toXML() {
		StringBuffer xml = new StringBuffer();
		
		xml.append("<column ");
		
		if (!type.equals("")) xml.append(" type='"+type+"'");
		if (width>0) xml.append(" width='"+Integer.toString(width)+"'");
		if (!id.equals("")) xml.append(" id='"+id+"'");
		if (!align.equals("")) xml.append(" align='"+align+"'");
		if (!valign.equals("")) xml.append(" valign='"+valign+"'");
		if (!sort.equals("")) xml.append(" sort='"+sort+"'");
		if (!color.equals("")) xml.append(" color='"+color+"'");
		if (hidden) xml.append(" hidden='true'");
		if (!source.equals("")) xml.append(" source='"+source+"'");
		if (autocomplete) xml.append(" autocomplete='true'");

		for (Object key : attributes.keySet()) {
			String name = key.toString();
			Object value = attributes.get(key);
			xml.append(" "+name+"='"+value+"'");
		}

		xml.append(">");
		xml.append(header);
		xml.append("</column>");
		
		return xml.toString();
	}
	
	
}
