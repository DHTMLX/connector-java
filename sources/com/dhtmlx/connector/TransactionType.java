/*
 * Copyright (c) 2009 - DHTMLX, All rights reserved
 */
package com.dhtmlx.connector;

/**
 * The Enum TransactionType.
 * 
 * Possible types of transactions
 */
public enum TransactionType {
	
/** Transactions not used */
NONE, 
 /** Each operation in separate transaction */
 OPERATION, 
 /** Single transaction for all operations */
 GLOBAL
}
