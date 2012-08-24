/* CustomizeFormula.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Feb 29, 2012 3:29:49 PM , Created by sam
}}IS_NOTE

Copyright (C) 2012 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package test;

import java.util.List;

import org.zkoss.poi.ss.formula.eval.ErrorEval;
import org.zkoss.poi.ss.formula.eval.EvaluationException;
import org.zkoss.poi.ss.formula.eval.ValueEval;
import org.zkoss.poi.ss.formula.functions.Function;
import org.zkoss.poi.ss.formula.functions.MultiOperandNumericFunction;
import org.zkoss.poi.ss.formula.functions.NumericFunction;
import org.zkoss.zss.ui.fn.UtilFns;

/**
 * @author sam
 *
 */
public class CustomizeFormula {
	
	public static final ValueEval bar(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
		return BAR.evaluate(args, srcRowIndex, srcColumnIndex);
	}
	
	public static final Function BAR = new MultiOperandNumericFunction(false, false) {

		@Override
		protected double evaluate(double[] values) throws EvaluationException {
			for (int i = 0; i < values.length; i++) {
				System.out.println(values[i]);
			}
			
			return 3.1415;
		}
	};
}
