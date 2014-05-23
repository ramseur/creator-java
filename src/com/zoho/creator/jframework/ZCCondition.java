// $Id$

package com.zoho.creator.jframework;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Operators;

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
    private String startValue = null;
    private String endValue = null;
    private int operator;
    
	public ZCCondition(String value, int operator) {
		this.value = value;
		this.operator = operator;
	}
	
	public ZCCondition(String startValue, String endValue, int operator){
		this.startValue = startValue;
		this.endValue=endValue;
		this.operator = operator;
	}

	public String getValue() {
		if(operator == BETWEEN){
			return startValue + ";" + endValue ;
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
			operatorString = "Is";
			break;
		case NOT_EQUAL:
			operatorString ="Is Not" ;
			break;
		case EMPTY:
			operatorString = "Is Empty" ;
			break;
		case  NOT_EMPTY :
			operatorString ="Is Not Empty";
			break;
		case LESS_THAN :
			operatorString = "Less Than";
			break;
		case GREATER_THAN:
			operatorString =  "Greater Than";
			break;
		case LESS_EQUAL:
			operatorString =  "Less than or equal to";
			break;
		case GREATER_EQUAL :
			operatorString = "Greater than or equal to";
			break;	
		case BETWEEN:
			operatorString =  "Between";
			break;
		case  TRUE:
			operatorString = "True";
			break;
		case FALSE:
			operatorString =  "False";
			break;	
		case  CONTAINS:
			operatorString = "Contains";
			break;
		case NOT_CONTAINS:
			operatorString =  "Not Contains";
			break;
		case STARTS_WITH:
			operatorString =  "Starts With";
			break;
		case ENDS_WITH :
			operatorString = "Ends With";
			break;
		case  LIKE :
			operatorString ="Like";
			break;
		
		case  YESTERDAY :
			operatorString ="Yesterday";
			break;
		case  TODAY :
			operatorString ="Today";
			break;
		case  TOMORROW :
			operatorString ="Tomorrow";
			break;
		case  LAST_7_DAYS :
			operatorString ="Last 7 Days";
			break;
		case  LAST_30_DAYS :
			operatorString ="Last 30 Days";
			break;
		case  LAST_60_DAYS :
			operatorString ="Last 60 Days";
			break;
		case  LAST_90_DAYS :
			operatorString ="Last 90 Days";
			break;
		case  LAST_120_DAYS :
			operatorString ="Last 120 Days";
			break;
		case  LAST_N_DAYS :
			operatorString ="Last N Days";
			break;
		case  NEXT_7_DAYS :
			operatorString ="Next 7 Days";
			break;	
		case  NEXT_30_DAYS :
			operatorString ="Next 30 Days";
			break;
		case  NEXT_60_DAYS :
			operatorString ="Next 60 Days";
			break;
		case  NEXT_90_DAYS :
			operatorString ="Next 90 Days";
			break;
		case  NEXT_120_DAYS :
			operatorString ="Next 120 Days";
			break;
		case  NEXT_N_DAYS :
			operatorString ="Next N Days";
			break;
			
		case  LAST_WEEK :
			operatorString ="Last Week";
			break;
		case  THIS_WEEK :
			operatorString ="This Week";
			break;
		case  NEXT_WEEK :
			operatorString ="Next Week";
			break;
		case  CURRENT_PREVIOUS_WEEK :
			operatorString ="Current and Previous Week";
			break;
		case  CURRENT_NEXT_WEEK :
			operatorString ="Current and Next Week";
			break;
		case  LAST_N_WEEK :
			operatorString ="Last N Week";
			break;
		case  NEXT_N_WEEK :
			operatorString ="Next N Week";
			break;
			
		case  LAST_MONTH :
			operatorString ="Last Month";
			break;
		case  THIS_MONTH :
			operatorString ="This Month";
			break;
		case  NEXT_MONTH :
			operatorString ="Next Month";
			break;
		case  CURR_PREV_MONTH :
			operatorString ="Current and Previous Month";
			break;
		case  CURR_NEXT_MONTH :
			operatorString ="Current and Next Month";
			break;	
		case  LAST_N_MONTH :
			operatorString ="Last N Month";
			break;	
		case  NEXT_N_MONTH :
			operatorString ="Next N Month";
			break;	
			
		case  LAST_YEAR :
			operatorString ="Last Year";
			break;	
		case  CURRENT_YEAR :
			operatorString ="This Year";
			break;
		case  NEXT_YEAR :
			operatorString ="Next Year";
			break;
		case  PREVIOUS_2_YEAR :
			operatorString ="Last 2 Year";
			break;	
		case  NEXT_2_YEAR :
			operatorString ="Next 2 Year";
			break;
		case  CURRENT_PREVIOUS_YEAR :
			operatorString ="Current and Previous Year";
			break;
		case  CURRENT_NEXT_YEAR :
			operatorString ="Current and Next Year";
			break;
		case  LAST_N_YEAR :
			operatorString ="Last N Year";
			break;
		case  NEXT_N_YEAR :
			operatorString ="Next N Year";
			break;			
		}
		return operatorString;
	}
	
	public static Boolean isDateFieldWithoutValues(int operator){
		
		Boolean flag = false;
		
		switch(operator){
		
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
