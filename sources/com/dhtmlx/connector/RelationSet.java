package com.dhtmlx.connector;

import java.util.ArrayList;

public class RelationSet {

	protected ArrayList<RelationRule> rules;

	public RelationSet(RelationRule rule1) {
		this(rule1,null);
	}
	
	public RelationSet(RelationRule rule1, RelationRule rule2) {
		this(rule1,rule2,null);
	}
	
	public RelationSet(RelationRule rule1, RelationRule rule2, RelationRule rule3) {
		this(rule1,rule2,rule3,null);
	}
	
	public RelationSet(RelationRule rule1, RelationRule rule2, RelationRule rule3, RelationRule rule4) {
		this(rule1,rule2,rule3,rule4,null);
	}

	public RelationSet(RelationRule rule1, RelationRule rule2, RelationRule rule3, RelationRule rule4,
			RelationRule rule5) {
		this(rule1,rule2,rule3,rule4,rule5,null);
	}
	
	public RelationSet(RelationRule rule1, RelationRule rule2, RelationRule rule3, RelationRule rule4,
			RelationRule rule5, RelationRule rule6) {
		this(rule1,rule2,rule3,rule4,rule5,rule6,null);
	}

	public RelationSet(RelationRule rule1, RelationRule rule2, RelationRule rule3, RelationRule rule4,
			RelationRule rule5, RelationRule rule6, RelationRule rule7) {
		this(rule1,rule2,rule3,rule4,rule5,rule6,rule7,null);
	}

	public RelationSet(RelationRule rule1, RelationRule rule2, RelationRule rule3, RelationRule rule4,
			RelationRule rule5, RelationRule rule6, RelationRule rule7, RelationRule rule8) {
		this(rule1,rule2,rule3,rule4,rule5,rule6,rule7,rule8,null);
	}

	public RelationSet(RelationRule rule1, RelationRule rule2, RelationRule rule3, RelationRule rule4,
			RelationRule rule5, RelationRule rule6, RelationRule rule7, RelationRule rule8,
			RelationRule rule9) {
		this(rule1,rule2,rule3,rule4,rule5,rule6,rule7,rule8,rule9,null);
	}

	public RelationSet(RelationRule rule1, RelationRule rule2, RelationRule rule3, RelationRule rule4,
			RelationRule rule5, RelationRule rule6, RelationRule rule7, RelationRule rule8,
			RelationRule rule9,RelationRule rule10) {
		rules = new ArrayList<RelationRule>();
		if (rule1!=null) rules.add(rule1);
		if (rule2!=null) rules.add(rule2);
		if (rule3!=null) rules.add(rule3);
		if (rule4!=null) rules.add(rule4);
		if (rule5!=null) rules.add(rule5);
		if (rule6!=null) rules.add(rule6);
		if (rule7!=null) rules.add(rule7);
		if (rule8!=null) rules.add(rule8);
		if (rule9!=null) rules.add(rule9);
		if (rule10!=null) rules.add(rule10);
	}

	public ArrayList<RelationRule> get_relations() {
		return rules;
	}
	
}
