// $Id$

package com.zoho.creator.jframework;

public class ZCCondition {

    public static final int EQUAL=18;
    public static final int NOT_EQUAL=19;
    public static final int LESS_THAN=20;
    public static final int GREATER_THAN=21;
    public static final int LESS_EQUAL=22;
    public static final int GREATER_EQUAL=23;
    public static final int STARTS_WITH=24;
    public static final int ENDS_WITH=25;
    public static final int CONTAINS=26;
    public static final int NOT_CONTAINS=27;
    public static final int LIKE=28;
    public static final int EMPTY=29;
    public static final int NOT_EMPTY=30;
    public static final int YESTERDAY=31;
    public static final int TODAY=32;
    public static final int TOMORROW=33;
    public static final int LAST_7_DAYS=34;
    public static final int LAST_30_DAYS=35;
    public static final int LAST_60_DAYS=36;
    public static final int LAST_90_DAYS=37;
    public static final int LAST_120_DAYS=38;
    public static final int NEXT_7_DAYS=39;
    public static final int NEXT_30_DAYS=40;
    public static final int NEXT_60_DAYS=41;
    public static final int NEXT_90_DAYS=42;
    public static final int NEXT_120_DAYS=43;
    public static final int LAST_MONTH=44;
    public static final int THIS_MONTH=45;
    public static final int NEXT_MONTH=46;
    public static final int CURR_PREV_MONTH=47;
    public static final int CURR_NEXT_MONTH=48;
    public static final int TRUE=49;
    public static final int FALSE=50;
    public static final int LAST_YEAR=51;
    public static final int CURRENT_YEAR=52;
    public static final int NEXT_YEAR=53;
    public static final int PREVIOUS_2_YEAR=54;
    public static final int NEXT_2_YEAR=55;
    public static final int CURRENT_PREVIOUS_YEAR=56;
    public static final int CURRENT_NEXT_YEAR=57;
    public static final int BETWEEN=58;
    public static final int THIS_WEEK=59;
    public static final int LAST_WEEK=60;
    public static final int NEXT_WEEK=61;
    public static final int CURRENT_PREVIOUS_WEEK=62;
    public static final int CURRENT_NEXT_WEEK=63;
    public static final int LAST_N_DAYS=64;
    public static final int NEXT_N_DAYS=65;
    public static final int LAST_N_WEEK=66;
    public static final int NEXT_N_WEEK=67;
    public static final int LAST_N_MONTH=68;
    public static final int NEXT_N_MONTH=69;
    public static final int LAST_N_YEAR=70;
    public static final int NEXT_N_YEAR=71;
    public static final int IN=72;
    
    public static final int LAST_N_WEEKS=73;
    public static final int NEXT_N_WEEKS=74;
    public static final int LAST_N_MONTHS=75;
    public static final int LAST_N_YEARS=76;
    public static final int NEXT_N_MONTHS=77;
    public static final int NEXT_N_YEARS=78;

    
    private String value = null;
    private int operator;
    
	public ZCCondition(String value, int operator) {
		this.value = value;
		this.operator = operator;
	}

	public String getValue() {
		return value;
	}

	public int getOperator() {
		return operator;
	}
	
	public static int getDefaultSearchOperator(FieldType type){
		if(type.equals(FieldType.DATE) || type.equals(FieldType.NUMBER) || type.equals(FieldType.PERCENTAGE) || type.equals(FieldType.DATE_TIME) || type.equals(FieldType.DECIMAL) || type.equals(FieldType.CURRENCY)){
			return ZCCondition.EQUAL;
		}else if(type.equals(FieldType.DECISION_CHECK_BOX)){
			return ZCCondition.TRUE;
		}else{
			return ZCCondition.CONTAINS;
		}
	}
}
