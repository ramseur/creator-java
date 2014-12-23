package com.zoho.creator.jframework;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Stack;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

import com.zoho.workengine.parser.grammar.ExpressionLexer;
import com.zoho.deluge.execute.adaptor.WorkflowAdaptor;
import com.zoho.deluge.util.DataTypes;
import com.zoho.deluge.util.DataTypes.DelugeType;
import com.zoho.workengine.parser.grammar.ApplicationParser;
import com.zoho.workengine.parser.tool.ANTLRCaseInSensitiveStringStream;
import com.zoho.workengine.parser.util.OperatorConstants;

public class CriteriaExecutor 
{	

	public boolean evaluateCriteria(String expression, HashMap<String, Object> values) throws RecognitionException
	{
		CommonTree boolTree = parseCriteria(expression);
		return evaluateCriteria(boolTree, values);
	}

	private CommonTree parseCriteria(String criteriaExpression) throws RecognitionException
	{
		ExpressionLexer expLex = new ExpressionLexer(new ANTLRCaseInSensitiveStringStream(criteriaExpression));
		CommonTokenStream tokenStream = new CommonTokenStream(expLex);

		ApplicationParser expParser = new ApplicationParser(tokenStream);

		WorkflowAdaptor adaptor = new WorkflowAdaptor(tokenStream);
		expParser.setTreeAdaptor(adaptor);

		CommonTree workflowTree = (CommonTree)expParser.booleanExpression().getTree();

		return workflowTree;
	}

	private boolean evaluateCriteria(CommonTree boolTree, HashMap<String, Object> values)
	{
		if(boolTree.getType() == ExpressionLexer.NOT)
		{
			return !evaluateCriteria((CommonTree)boolTree.getChild(0), values);
		}
		else if(boolTree.getChildCount() == 2 && (boolTree.getType() == ExpressionLexer.LOGICALAND || boolTree.getType() == ExpressionLexer.LOGICALOR))
		{
			boolean leftOprnd = evaluateCriteria((CommonTree)boolTree.getChild(0), values);

			if(!leftOprnd && boolTree.getType() == ExpressionLexer.LOGICALAND)
			{
				return false;
			}
			else if(leftOprnd && boolTree.getType() == ExpressionLexer.LOGICALOR)
			{
				return true;
			}

			boolean rightOprnd = evaluateCriteria((CommonTree)boolTree.getChild(1), values);

			if(boolTree.getType() == ExpressionLexer.LOGICALOR)
			{
				return leftOprnd || rightOprnd;
			}
			else if(boolTree.getType() == ExpressionLexer.LOGICALAND)
			{
				return leftOprnd && rightOprnd;
			}
		}
		else if(boolTree.getChildCount() == 2)
		{
			int operatorInt = boolTree.getType();
			String operator = boolTree.getText();

			CommonTree leftExpr = (CommonTree) boolTree.getChild(0);
			CommonTree rightExpr = (CommonTree) boolTree.getChild(1);

			Object leftVal = evaluateExpression(leftExpr, values);
			Object rightVal = evaluateExpression(rightExpr, values);

			if(operator.equals("=="))
			{
				return leftVal.equals(rightVal);
			}
			else if(operator.equals("!="))
			{
				return !leftVal.equals(rightVal);
			}
			else if(operatorInt == ExpressionLexer.RELATIONAL_OPR)
			{
				if(!DataTypes.isNumeric(leftVal) || !DataTypes.isNumeric(rightVal))
				{
					//					getResultant().fileError(ErrorUtil.DATA_TYPE_ERROR_INT, "comparisonError", "comparisonDet", boolTree.getLine());//No I18N
				}
				BigDecimal leftNum = BigDecimal.valueOf(Long.valueOf(leftVal+"")); //No I18N
				BigDecimal rightNum = BigDecimal.valueOf(Long.valueOf(rightVal+"")); //No I18N

				if(operator.equals("<"))
				{
					return leftNum.compareTo(rightNum) < 0;
				}
				else if(operator.equals("<="))
				{
					return leftNum.compareTo(rightNum) <= 0;
				}
				else if(operator.equals(">"))
				{
					return leftNum.compareTo(rightNum) > 0;
				}
				else if(operator.equals(">="))
				{
					return leftNum.compareTo(rightNum) >= 0;
				}
			}
		}

		return false;
	}

	private Object evaluateExpression(CommonTree expressionTree, HashMap<String, Object> values)
	{
		Stack<Object> dataStack = new Stack<Object>();
		evaluateExpression(expressionTree, dataStack, values);
		return dataStack.pop();
	} 

	private void evaluateExpression(CommonTree expression, Stack<Object> valueStack, HashMap<String, Object> values)
	{
		if(expression.getChildCount() == 0)
		{
			if(OperatorConstants.isConstant(expression.getType()))
			{
				valueStack.push(typeCast(expression.getText(), expression.getType()));
			}
			else if(values.containsKey(expression.getText()))
			{
				valueStack.push(values.get(expression.getText()));
			}
		}
		else if(expression.getChildCount() == 2 && (expression.getType() == ExpressionLexer.RELATIONAL_OPR || OperatorConstants.isBooleanOperatorForLexer(expression.getType())))
		{
			boolean result = evaluateCriteria(expression, values);
			valueStack.push(result);
		}
		else if(expression.getChildCount() == 2)
		{
			int oprType = OperatorConstants.getOperatorForLexerType(expression.getType());

			evaluateExpression((CommonTree)expression.getChild(0), valueStack, values);
			evaluateExpression((CommonTree)expression.getChild(1), valueStack, values);

			Object rightVal = valueStack.pop();
			Object leftVal = valueStack.pop();

			if(OperatorConstants.isArithmeticOperator(oprType))
			{
				Object val = evalArithmetic(leftVal, rightVal, oprType);
				valueStack.push(val);
			}
		}
	}

	private Object typeCast(String val, int constType)
	{
		String dataType = OperatorConstants.getConstantDataType(constType, val);	
		if(dataType.equals(OperatorConstants.BIGINT_TYPE))
		{
			return Integer.valueOf(val);
		}
		else if(dataType.equals(OperatorConstants.DECIMAL_TYPE))
		{
			return BigDecimal.valueOf(Double.valueOf(val));
		}
		else if(dataType.equals(OperatorConstants.BOOLEAN_TYPE))
		{
			return Boolean.valueOf(val);
		}
		else if(dataType.equals(OperatorConstants.STRING_TYPE))
		{
			return replaceEscapeCharacters(removeQuotes(val));
		}
		else 
		{
			return val;
		}		
	}

	public static String removeQuotes(String str)
	{
		if((str.startsWith("\"") || str.startsWith("'")) && str.trim().length() > 1) //No I18N
		{
			str = str.substring(1, (str.length()-1));
		}
		return str;
	}


	public static String replaceEscapeCharacters(String str)
	{
		str = str.replace("\\\"", "\"");
		str = str.replace("\\n", System.getProperty("line.separator"));//No I18N
		return str;
	}

	private Object evalArithmetic(Object leftObj, Object rightObj, int operator)
	{
		if(operator == OperatorConstants.PLUS_OPR)
		{
			return add(leftObj, rightObj);
		}
		else if(operator == OperatorConstants.MULTIPLY_OPR)
		{
			return multiply(leftObj, rightObj);
		}
		else if(operator == OperatorConstants.DIVIDE_OPR)
		{
			return divide(leftObj, rightObj);
		}
		else if(operator == OperatorConstants.MINUS_OPR)
		{
			return subtract(leftObj, rightObj);
		}

		return 0;
	}

	private Object add(Object leftObj, Object rightObj)
	{
		if(DataTypes.isNumeric(leftObj) && DataTypes.isNumeric(rightObj))
		{
			BigDecimal leftVal = new BigDecimal(leftObj.toString());
			BigDecimal rightVal = new BigDecimal(rightObj.toString());

			BigDecimal retVal = leftVal.add(rightVal);

			if(DataTypes.isDecimal(leftObj) || DataTypes.isDecimal(rightObj))
			{
				return retVal;
			}
			else 
			{
				return retVal.intValue();
			}
		}
		else if(DataTypes.isString(leftObj) || DataTypes.isString(rightObj))
		{
			String leftVal = leftObj.toString();
			String rightVal = rightObj.toString();

			return leftVal + rightVal;
		}



		return null;
	}

	private Object multiply(Object leftObj, Object rightObj)
	{
		if(DataTypes.isNumeric(leftObj) && DataTypes.isNumeric(rightObj))
		{
			BigDecimal leftVal = new BigDecimal(leftObj.toString());
			BigDecimal rightVal = new BigDecimal(rightObj.toString());

			BigDecimal retVal = leftVal.multiply(rightVal);

			if(DataTypes.isDecimal(leftObj) || DataTypes.isDecimal(rightObj))
			{
				return retVal;
			}
			else 
			{
				return Integer.valueOf(retVal.intValue());
			}
		}
		else if((DataTypes.isString(rightObj) && DataTypes.isIntegeral(leftObj)) || (DataTypes.isString(leftObj) && DataTypes.isIntegeral(rightObj)))
		{
			Integer ind = null;
			String val = null;
			StringBuffer returnStr = new StringBuffer();

			if(DataTypes.isString(rightObj))
			{
				val = (String) rightObj;
				ind = (Integer) leftObj;
			}
			else
			{
				val = (String) leftObj;
				ind = (Integer) rightObj;
			}

			for(int i = 0; i < ind; i++)
			{
				returnStr = returnStr.append(val);
			}

			return returnStr;
		}

		//		getResultant().fileError(ErrorUtil.DATA_TYPE_ERROR_INT, "cannotMultiply", "cannotMultiplyDet", statement.getLine());//No I18N

		return null;
	}	

	private Object divide(Object leftObj, Object rightObj)
	{
		if(DataTypes.isNumeric(leftObj) && DataTypes.isNumeric(rightObj))
		{
			BigDecimal leftVal = null;
			BigDecimal rightVal = null;

			leftVal = new BigDecimal(leftObj.toString());			
			rightVal = new BigDecimal(rightObj.toString());

			return leftVal.divide(rightVal);
		}

		String[] causeArr = new String[]{DelugeType.getDataType(leftObj).getTypeAsString(), DelugeType.getDataType(rightObj).getTypeAsString()};

		//		getResultant().fileError(ErrorUtil.DATA_TYPE_ERROR_INT, "cannotDivide", causeArr, "cannotDivideDet", null, statement.getLine());//No I18N

		return null;
	}


	private Object subtract(Object leftObj, Object rightObj)
	{
		if(DataTypes.isNumeric(leftObj) && DataTypes.isNumeric(rightObj))
		{
			BigDecimal leftVal = new BigDecimal(leftObj.toString());
			BigDecimal rightVal = new BigDecimal(rightObj.toString());

			BigDecimal retVal = leftVal.subtract(rightVal);

			if(DataTypes.isDecimal(leftObj) || DataTypes.isDecimal(rightObj))
			{
				return retVal;
			}
			else 
			{
				return Integer.valueOf(retVal.intValue());
			}
		}

		//		getResultant().fileError(ErrorUtil.DATA_TYPE_ERROR_INT, "cannotSubtract", "cannotSubtractDet", statement.getLine());//No I18N

		return null;
	}

}
