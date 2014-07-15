// $Id$

package com.zoho.creator.jframework;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;


public class ZCCondition {
	
	private static ResourceBundle resourceString = ResourceBundle.getBundle("com.zoho.creator.jframework.ResourceString", Locale.getDefault());

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
	private String startValueOfBetween = null;
	private String endValueOfBetween = null;
	private int operator = 0;
	
	 public static HashMap<String, Integer> conditionMap;
	    
	    static{
	    	conditionMap = new HashMap<String, Integer>();
	    	conditionMap.put(resourceString.getString("is"), EQUAL);
	    	conditionMap.put(resourceString.getString("is_not"), NOT_EQUAL);
	    	conditionMap.put(resourceString.getString("is_empty"), EMPTY);
	    	conditionMap.put(resourceString.getString("is_not_empty"), NOT_EMPTY);
	    	conditionMap.put(resourceString.getString("before"), LESS_THAN);
	    	conditionMap.put(resourceString.getString("less_than"), LESS_THAN);
	    	conditionMap.put(resourceString.getString("after"), GREATER_THAN);
	    	conditionMap.put(resourceString.getString("greater_then"), GREATER_THAN);
	    	conditionMap.put(resourceString.getString("less_than_or_equal_to"), LESS_EQUAL);
	    	conditionMap.put(resourceString.getString("greater_than_or_equal_to"), GREATER_EQUAL);
	    	conditionMap.put(resourceString.getString("between"), BETWEEN);
	    	conditionMap.put(resourceString.getString("true"), TRUE);
	    	conditionMap.put(resourceString.getString("false"), FALSE);
	    	conditionMap.put(resourceString.getString("contains"), CONTAINS);
	    	conditionMap.put(resourceString.getString("not_contains"), NOT_CONTAINS);
	    	conditionMap.put(resourceString.getString("starts_with"), STARTS_WITH);
	    	conditionMap.put(resourceString.getString("ends_with"), ENDS_WITH);
	    	conditionMap.put(resourceString.getString("like"), LIKE);
	    	conditionMap.put(resourceString.getString("yesterday"), YESTERDAY);
	    	conditionMap.put(resourceString.getString("today"), TODAY);
	    	conditionMap.put(resourceString.getString("tomorrow"), TOMORROW);
	    	conditionMap.put(resourceString.getString("last_7_days"), LAST_7_DAYS);
	    	conditionMap.put(resourceString.getString("last_30_days"), LAST_30_DAYS);
	    	conditionMap.put(resourceString.getString("last_60_days"), LAST_60_DAYS);
	    	conditionMap.put(resourceString.getString("last_90_days"), LAST_90_DAYS);
	    	conditionMap.put(resourceString.getString("last_120_days"), LAST_120_DAYS);
	    	conditionMap.put(resourceString.getString("last_n_days"), LAST_N_DAYS);
	    	conditionMap.put(resourceString.getString("next_7_days"), NEXT_7_DAYS);
	    	conditionMap.put(resourceString.getString("next_30_days"), NEXT_30_DAYS);
	    	conditionMap.put(resourceString.getString("next_60_days"), NEXT_60_DAYS);
	    	conditionMap.put(resourceString.getString("next_90_days"), NEXT_90_DAYS);
	    	conditionMap.put(resourceString.getString("next_120_days"), NEXT_120_DAYS);
	    	conditionMap.put(resourceString.getString("next_n_days"), NEXT_N_DAYS);
	    	conditionMap.put(resourceString.getString("last_week"), LAST_WEEK);
	    	conditionMap.put(resourceString.getString("this_week"), THIS_WEEK);
	    	conditionMap.put(resourceString.getString("next_week"), NEXT_WEEK);
	    	conditionMap.put(resourceString.getString("current_and_previous_week"), CURRENT_PREVIOUS_WEEK);
	    	conditionMap.put(resourceString.getString("current_and_next_week"), CURRENT_NEXT_WEEK);
	    	conditionMap.put(resourceString.getString("last_n_week"), LAST_N_WEEK);
	    	conditionMap.put(resourceString.getString("next_n_week"), NEXT_N_WEEK);
	    	conditionMap.put(resourceString.getString("last_month"), LAST_MONTH);
	    	conditionMap.put(resourceString.getString("this_month"), THIS_MONTH);
	    	conditionMap.put(resourceString.getString("next_month"), NEXT_MONTH);
	    	conditionMap.put(resourceString.getString("current_and_previous_month"), CURR_PREV_MONTH);
	    	conditionMap.put(resourceString.getString("current_and_next_month"), CURR_NEXT_MONTH);
	    	conditionMap.put(resourceString.getString("last_n_month"), LAST_N_MONTH);
	    	conditionMap.put(resourceString.getString("next_n_month"), NEXT_N_MONTH);
	    	conditionMap.put(resourceString.getString("last_year"), LAST_YEAR);
	    	conditionMap.put(resourceString.getString("this_year"), CURRENT_YEAR);
	    	conditionMap.put(resourceString.getString("next_year"), NEXT_YEAR);
	    	conditionMap.put(resourceString.getString("last_2_year"), PREVIOUS_2_YEAR);
	    	conditionMap.put(resourceString.getString("next_2_year"), NEXT_2_YEAR);
	    	conditionMap.put(resourceString.getString("current_and_previous_year"), CURRENT_PREVIOUS_YEAR);
	    	conditionMap.put(resourceString.getString("current_and_next_year"), CURRENT_NEXT_YEAR);
	    	conditionMap.put(resourceString.getString("last_n_year"), LAST_N_YEAR);
	    	conditionMap.put(resourceString.getString("next_n_year"), NEXT_N_YEAR);
	    }

	public ZCCondition(String value, int operator) {
		this.value = value;
		this.operator = operator;
	}

	public ZCCondition(String startValue, String endValue, int operator){
		this.startValueOfBetween = startValue;
		this.endValueOfBetween=endValue;
		this.operator = operator;
	}
	 
	public String getValue() {
		if(operator == BETWEEN){
			return startValueOfBetween + ";" + endValueOfBetween ;
		}
		else{
			return value;
		}

	}

	public int getOperator() {
		return operator;
	}

	public static int getDefaultSearchOperator(FieldType type){
		if(type.equals(FieldType.DATE) || type.equals(FieldType.NUMBER) || type.equals(FieldType.PERCENTAGE) || type.equals(FieldType.DATE_TIME) || type.equals(FieldType.DECIMAL) || type.equals(FieldType.CURRENCY) || type.equals(FieldType.AUTO_NUMBER)){
			return ZCCondition.EQUAL;
		}else if(type.equals(FieldType.DECISION_CHECK_BOX)){
			return ZCCondition.TRUE;
		}else{
			return ZCCondition.CONTAINS;
		}
	}

	public static int getOperatorValue(String operatorString){
		int operatorValue = 0;
		operatorValue = conditionMap.get(operatorString);
		
//		switch(operatorString){
//		case "Is":
//			operatorValue = EQUAL;
//			break;
//		case "Is Not":
//			operatorValue = NOT_EQUAL;
//			break;
//		case "Is Empty":
//			operatorValue = EMPTY;
//			break;
//		case "Is Not Empty":
//			operatorValue = NOT_EMPTY;
//			break;
//		case "Less Than":
//			operatorValue = LESS_THAN;
//			break;
//		case "Greater Than":
//			operatorValue = GREATER_THAN;
//			break;
//		case "Before" :
//			operatorValue = LESS_THAN;
//			break;
//		case "After":
//			operatorValue = GREATER_THAN;
//			break;
//		case "Less than or equal to":
//			operatorValue = LESS_EQUAL;
//			break;
//		case "Greater than or equal to":
//			operatorValue = GREATER_EQUAL;
//			break;	
//		case "Between":
//			operatorValue = BETWEEN;
//			break;
//		case "True":
//			operatorValue = TRUE;
//			break;
//		case "False":
//			operatorValue = FALSE;
//			break;	
//		case "Contains":
//			operatorValue = CONTAINS;
//			break;
//		case "Not Contains":
//			operatorValue = NOT_CONTAINS;
//			break;
//		case "Starts With":
//			operatorValue = STARTS_WITH;
//			break;
//		case "Ends With":
//			operatorValue = ENDS_WITH;
//			break;
//		case "Like":
//			operatorValue = LIKE;
//			break;
//		case "Yesterday":
//			operatorValue = YESTERDAY;
//			break;
//		case "Today":
//			operatorValue = TODAY;
//			break;
//		case "Tomorrow":
//			operatorValue = TOMORROW;
//			break;	
//		case "Last 7 Days":
//			operatorValue = LAST_7_DAYS;
//			break;	
//		case "Last 30 Days":
//			operatorValue = LAST_30_DAYS;
//			break;
//		case "Last 60 Days":
//			operatorValue = LAST_60_DAYS;
//			break;
//		case "Last 90 Days":
//			operatorValue = LAST_90_DAYS;
//			break;
//		case "Last 120 Days":
//			operatorValue = LAST_120_DAYS;
//			break;
//		case "Last N Days":
//			operatorValue = LAST_N_DAYS;
//			break;
//		case "Next 7 Days":
//			operatorValue =NEXT_7_DAYS;
//			break;
//		case "Next 30 Days":
//			operatorValue =NEXT_30_DAYS;
//			break;	
//		case "Next 60 Days":
//			operatorValue =NEXT_60_DAYS;
//			break;
//		case "Next 90 Days":
//			operatorValue =NEXT_90_DAYS;
//			break;
//		case "Next 120 Days":
//			operatorValue =NEXT_120_DAYS;
//			break;
//		case "Next N Days":
//			operatorValue =NEXT_N_DAYS;
//			break;
//		case "Last Week":
//			operatorValue =LAST_WEEK;
//			break;
//		case "This Week":
//			operatorValue =THIS_WEEK;
//			break;
//		case "Next Week":
//			operatorValue =NEXT_WEEK;
//			break;
//		case "Current and Previous Week":
//			operatorValue =CURRENT_PREVIOUS_WEEK;
//			break;
//		case "Current and Next Week":
//			operatorValue =CURRENT_NEXT_WEEK;
//			break;
//		case "Last N Week":
//			operatorValue =LAST_N_WEEK;
//			break;
//		case "Next N Week":
//			operatorValue =NEXT_N_WEEK;
//			break;	
//		case "Last Month":
//			operatorValue =LAST_MONTH;
//			break;
//		case "This Month":
//			operatorValue =THIS_MONTH;
//			break;
//		case "Next Month":
//			operatorValue =NEXT_MONTH;
//			break;	
//		case "Current and Previous Month":
//			operatorValue =CURR_PREV_MONTH;
//			break;
//		case "Current and Next Month":
//			operatorValue =CURR_NEXT_MONTH;
//			break;
//		case "Last N Month":
//			operatorValue =LAST_N_MONTH;
//			break;
//		case "Next N Month":
//			operatorValue =NEXT_N_MONTH;
//			break;
//		case "Last Year":
//			operatorValue =LAST_YEAR;
//			break;
//		case "This Year":
//			operatorValue = CURRENT_YEAR;
//			break;
//		case "Next Year":
//			operatorValue =NEXT_YEAR;
//			break;
//		case "Last 2 Year":
//			operatorValue =PREVIOUS_2_YEAR;
//			break;
//		case "Next 2 Year":
//			operatorValue =NEXT_2_YEAR;
//			break;
//		case "Current and Previous Year":
//			operatorValue =CURRENT_PREVIOUS_YEAR;
//			break;
//		case "Current and Next Year":
//			operatorValue =CURRENT_NEXT_YEAR;
//			break;
//		case "Last N Year":
//			operatorValue =LAST_N_YEAR;
//			break;
//		case "Next N Year":
//			operatorValue =NEXT_N_YEAR;
//			break;
//			
//		}
		
		return operatorValue;
	}

	public String getOperatorString(){
		String operatorString = null; 
		switch(operator){
		case EQUAL :
			operatorString = resourceString.getString("is");
			break;
		case NOT_EQUAL:
			operatorString =resourceString.getString("is_not") ;
			break;
		case EMPTY:
			operatorString = resourceString.getString("is_empty") ;
			break;
		case  NOT_EMPTY :
			operatorString =resourceString.getString("is_not_empty");
			break;
		case LESS_THAN :
			operatorString = resourceString.getString("less_than");
			break;
		case GREATER_THAN:
			operatorString =  resourceString.getString("greater_then");
			break;
		case LESS_EQUAL:
			operatorString =  resourceString.getString("less_than_or_equal_to");
			break;
		case GREATER_EQUAL :
			operatorString = resourceString.getString("greater_than_or_equal_to");
			break;	
		case BETWEEN:
			operatorString =  resourceString.getString("between");
			break;
		case  TRUE:
			operatorString = resourceString.getString("true");
			break;
		case FALSE:
			operatorString =  resourceString.getString("false");
			break;	
		case  CONTAINS:
			operatorString = resourceString.getString("contains");
			break;
		case NOT_CONTAINS:
			operatorString = resourceString.getString("not_contains");
			break;
		case STARTS_WITH:
			operatorString =  resourceString.getString("starts_with");
			break;
		case ENDS_WITH :
			operatorString = resourceString.getString("ends_with");
			break;
		case  LIKE :
			operatorString =resourceString.getString("like");
			break;

		case  YESTERDAY :
			operatorString =resourceString.getString("yesterday");
			break;
		case  TODAY :
			operatorString =resourceString.getString("today");
			break;
		case  TOMORROW :
			operatorString =resourceString.getString("tomorrow");
			break;
		case  LAST_7_DAYS :
			operatorString =resourceString.getString("last_7_days");
			break;
		case  LAST_30_DAYS :
			operatorString =resourceString.getString("last_30_days");
			break;
		case  LAST_60_DAYS :
			operatorString =resourceString.getString("last_60_days");
			break;
		case  LAST_90_DAYS :
			operatorString =resourceString.getString("last_90_days");
			break;
		case  LAST_120_DAYS :
			operatorString =resourceString.getString("last_120_days");
			break;
		case  LAST_N_DAYS :
			operatorString =resourceString.getString("last_n_days");
			break;
		case  NEXT_7_DAYS :
			operatorString =resourceString.getString("next_7_days");
			break;	
		case  NEXT_30_DAYS :
			operatorString =resourceString.getString("next_30_days");
			break;
		case  NEXT_60_DAYS :
			operatorString =resourceString.getString("next_60_days");
			break;
		case  NEXT_90_DAYS :
			operatorString =resourceString.getString("next_90_days");
			break;
		case  NEXT_120_DAYS :
			operatorString =resourceString.getString("next_120_days");
			break;
		case  NEXT_N_DAYS :
			operatorString =resourceString.getString("next_n_days");
			break;

		case  LAST_WEEK :
			operatorString =resourceString.getString("last_week");
			break;
		case  THIS_WEEK :
			operatorString =resourceString.getString("this_week");
			break;
		case  NEXT_WEEK :
			operatorString =resourceString.getString("next_week");
			break;
		case  CURRENT_PREVIOUS_WEEK :
			operatorString =resourceString.getString("current_and_previous_week");
			break;
		case  CURRENT_NEXT_WEEK :
			operatorString =resourceString.getString("current_and_next_week");
			break;
		case  LAST_N_WEEK :
			operatorString =resourceString.getString("last_n_week");
			break;
		case  NEXT_N_WEEK :
			operatorString =resourceString.getString("next_n_week");
			break;

		case  LAST_MONTH :
			operatorString =resourceString.getString("last_month");
			break;
		case  THIS_MONTH :
			operatorString =resourceString.getString("this_month");
			break;
		case  NEXT_MONTH :
			operatorString =resourceString.getString("next_month");
			break;
		case  CURR_PREV_MONTH :
			operatorString =resourceString.getString("current_and_previous_month");
			break;
		case  CURR_NEXT_MONTH :
			operatorString =resourceString.getString("current_and_next_month");
			break;	
		case  LAST_N_MONTH :
			operatorString =resourceString.getString("last_n_month");
			break;	
		case  NEXT_N_MONTH :
			operatorString =resourceString.getString("next_n_month");
			break;	

		case  LAST_YEAR :
			operatorString =resourceString.getString("last_year");
			break;	
		case  CURRENT_YEAR :
			operatorString =resourceString.getString("this_year");
			break;
		case  NEXT_YEAR :
			operatorString =resourceString.getString("next_year");
			break;
		case  PREVIOUS_2_YEAR :
			operatorString =resourceString.getString("last_2_year");
			break;	
		case  NEXT_2_YEAR :
			operatorString =resourceString.getString("next_2_year");
			break;
		case  CURRENT_PREVIOUS_YEAR :
			operatorString =resourceString.getString("current_and_previous_year");
			break;
		case  CURRENT_NEXT_YEAR :
			operatorString =resourceString.getString("current_and_next_year");
			break;
		case  LAST_N_YEAR :
			operatorString =resourceString.getString("last_n_year");
			break;
		case  NEXT_N_YEAR :
			operatorString =resourceString.getString("next_n_year");
			break;			
		}
		return operatorString;
	}

	public static Boolean isDateFieldWithoutValues(int operator){

		Boolean flag = false;

		switch(operator){

		case EMPTY :
			flag =true;
			break;
		case NOT_EMPTY:
			flag =true;
			break;

		case  YESTERDAY :
			flag =true;
			break;
		case  TODAY :
			flag =true;
			break;
		case  TOMORROW :
			flag =true;
			break;
		case  LAST_7_DAYS :
			flag =true;
			break;
		case  LAST_30_DAYS :
			flag =true;
			break;
		case  LAST_60_DAYS :
			flag =true;
			break;
		case  LAST_90_DAYS :
			flag =true;
			break;
		case  LAST_120_DAYS :
			flag =true;
			break;

		case  NEXT_7_DAYS :
			flag =true;
			break;	
		case  NEXT_30_DAYS :
			flag =true;
			break;
		case  NEXT_60_DAYS :
			flag =true;
			break;
		case  NEXT_90_DAYS :
			flag =true;
			break;
		case  NEXT_120_DAYS :
			flag =true;

		case  LAST_WEEK :
			flag =true;
			break;
		case  THIS_WEEK :
			flag =true;
			break;
		case  NEXT_WEEK :
			flag =true;
			break;
		case  CURRENT_PREVIOUS_WEEK :
			flag =true;
			break;
		case  CURRENT_NEXT_WEEK :
			flag =true;
			break;

		case  LAST_MONTH :
			flag =true;
			break;
		case  THIS_MONTH :
			flag =true;
			break;
		case  NEXT_MONTH :
			flag =true;
			break;
		case  CURR_PREV_MONTH :
			flag =true;
			break;
		case  CURR_NEXT_MONTH :
			flag =true;
			break;	

		case  LAST_YEAR :
			flag =true;
			break;	
		case  CURRENT_YEAR :
			flag =true;
			break;
		case  NEXT_YEAR :
			flag =true;
			break;
		case  PREVIOUS_2_YEAR :
			flag =true;
			break;	
		case  NEXT_2_YEAR :
			flag =true;
			break;
		case  CURRENT_PREVIOUS_YEAR :
			flag =true;
			break;
		case  CURRENT_NEXT_YEAR :
			flag =true;
			break;
		}

		return flag;
	}

	



}
