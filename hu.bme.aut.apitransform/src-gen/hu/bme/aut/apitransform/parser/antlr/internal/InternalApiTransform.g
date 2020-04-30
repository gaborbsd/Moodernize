/*
 * generated by Xtext 2.16.0
 */
grammar InternalApiTransform;

options {
	superClass=AbstractInternalAntlrParser;
}

@lexer::header {
package hu.bme.aut.apitransform.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;
}

@parser::header {
package hu.bme.aut.apitransform.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import hu.bme.aut.apitransform.services.ApiTransformGrammarAccess;

}

@parser::members {

 	private ApiTransformGrammarAccess grammarAccess;

    public InternalApiTransformParser(TokenStream input, ApiTransformGrammarAccess grammarAccess) {
        this(input);
        this.grammarAccess = grammarAccess;
        registerRules(grammarAccess.getGrammar());
    }

    @Override
    protected String getFirstRuleName() {
    	return "Model";
   	}

   	@Override
   	protected ApiTransformGrammarAccess getGrammarAccess() {
   		return grammarAccess;
   	}

}

@rulecatch {
    catch (RecognitionException re) {
        recover(input,re);
        appendSkippedTokens();
    }
}

// Entry rule entryRuleModel
entryRuleModel returns [EObject current=null]:
	{ newCompositeNode(grammarAccess.getModelRule()); }
	iv_ruleModel=ruleModel
	{ $current=$iv_ruleModel.current; }
	EOF;

// Rule Model
ruleModel returns [EObject current=null]
@init {
	enterRule();
}
@after {
	leaveRule();
}:
	(
		(
			{
				newCompositeNode(grammarAccess.getModelAccess().getTransformationsTransformationParserRuleCall_0());
			}
			lv_transformations_0_0=ruleTransformation
			{
				if ($current==null) {
					$current = createModelElementForParent(grammarAccess.getModelRule());
				}
				add(
					$current,
					"transformations",
					lv_transformations_0_0,
					"hu.bme.aut.apitransform.ApiTransform.Transformation");
				afterParserOrEnumRuleCall();
			}
		)
	)*
;

// Entry rule entryRuleTransformation
entryRuleTransformation returns [EObject current=null]:
	{ newCompositeNode(grammarAccess.getTransformationRule()); }
	iv_ruleTransformation=ruleTransformation
	{ $current=$iv_ruleTransformation.current; }
	EOF;

// Rule Transformation
ruleTransformation returns [EObject current=null]
@init {
	enterRule();
}
@after {
	leaveRule();
}:
	(
		(
			(
				{
					newCompositeNode(grammarAccess.getTransformationAccess().getSourceFunctionParserRuleCall_0_0());
				}
				lv_source_0_0=ruleFunction
				{
					if ($current==null) {
						$current = createModelElementForParent(grammarAccess.getTransformationRule());
					}
					set(
						$current,
						"source",
						lv_source_0_0,
						"hu.bme.aut.apitransform.ApiTransform.Function");
					afterParserOrEnumRuleCall();
				}
			)
		)
		otherlv_1='=>'
		{
			newLeafNode(otherlv_1, grammarAccess.getTransformationAccess().getEqualsSignGreaterThanSignKeyword_1());
		}
		(
			(
				{
					newCompositeNode(grammarAccess.getTransformationAccess().getTargetTargetParserRuleCall_2_0());
				}
				lv_target_2_0=ruleTarget
				{
					if ($current==null) {
						$current = createModelElementForParent(grammarAccess.getTransformationRule());
					}
					set(
						$current,
						"target",
						lv_target_2_0,
						"hu.bme.aut.apitransform.ApiTransform.Target");
					afterParserOrEnumRuleCall();
				}
			)
		)
	)
;

// Entry rule entryRuleTarget
entryRuleTarget returns [EObject current=null]:
	{ newCompositeNode(grammarAccess.getTargetRule()); }
	iv_ruleTarget=ruleTarget
	{ $current=$iv_ruleTarget.current; }
	EOF;

// Rule Target
ruleTarget returns [EObject current=null]
@init {
	enterRule();
}
@after {
	leaveRule();
}:
	(
		(
			(
				lv_static_0_0='static'
				{
					newLeafNode(lv_static_0_0, grammarAccess.getTargetAccess().getStaticStaticKeyword_0_0());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getTargetRule());
					}
					setWithLastConsumed($current, "static", true, "static");
				}
			)
		)?
		(
			(
				{
					newCompositeNode(grammarAccess.getTargetAccess().getPrefixFunctionPrefixParserRuleCall_1_0());
				}
				lv_prefix_1_0=ruleFunctionPrefix
				{
					if ($current==null) {
						$current = createModelElementForParent(grammarAccess.getTargetRule());
					}
					set(
						$current,
						"prefix",
						lv_prefix_1_0,
						"hu.bme.aut.apitransform.ApiTransform.FunctionPrefix");
					afterParserOrEnumRuleCall();
				}
			)
		)?
		(
			(
				{
					newCompositeNode(grammarAccess.getTargetAccess().getFunctionFunctionParserRuleCall_2_0());
				}
				lv_function_2_0=ruleFunction
				{
					if ($current==null) {
						$current = createModelElementForParent(grammarAccess.getTargetRule());
					}
					set(
						$current,
						"function",
						lv_function_2_0,
						"hu.bme.aut.apitransform.ApiTransform.Function");
					afterParserOrEnumRuleCall();
				}
			)
		)
	)
;

// Entry rule entryRuleFunctionPrefix
entryRuleFunctionPrefix returns [EObject current=null]:
	{ newCompositeNode(grammarAccess.getFunctionPrefixRule()); }
	iv_ruleFunctionPrefix=ruleFunctionPrefix
	{ $current=$iv_ruleFunctionPrefix.current; }
	EOF;

// Rule FunctionPrefix
ruleFunctionPrefix returns [EObject current=null]
@init {
	enterRule();
}
@after {
	leaveRule();
}:
	(
		(
			(
				lv_prefixes_0_0=RULE_ID
				{
					newLeafNode(lv_prefixes_0_0, grammarAccess.getFunctionPrefixAccess().getPrefixesIDTerminalRuleCall_0_0());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getFunctionPrefixRule());
					}
					addWithLastConsumed(
						$current,
						"prefixes",
						lv_prefixes_0_0,
						"org.eclipse.xtext.common.Terminals.ID");
				}
			)
		)
		(
			otherlv_1='.'
			{
				newLeafNode(otherlv_1, grammarAccess.getFunctionPrefixAccess().getFullStopKeyword_1_0());
			}
			(
				(
					lv_prefixes_2_0=RULE_ID
					{
						newLeafNode(lv_prefixes_2_0, grammarAccess.getFunctionPrefixAccess().getPrefixesIDTerminalRuleCall_1_1_0());
					}
					{
						if ($current==null) {
							$current = createModelElement(grammarAccess.getFunctionPrefixRule());
						}
						addWithLastConsumed(
							$current,
							"prefixes",
							lv_prefixes_2_0,
							"org.eclipse.xtext.common.Terminals.ID");
					}
				)
			)
		)*
		otherlv_3='.'
		{
			newLeafNode(otherlv_3, grammarAccess.getFunctionPrefixAccess().getFullStopKeyword_2());
		}
	)
;

// Entry rule entryRuleFunction
entryRuleFunction returns [EObject current=null]:
	{ newCompositeNode(grammarAccess.getFunctionRule()); }
	iv_ruleFunction=ruleFunction
	{ $current=$iv_ruleFunction.current; }
	EOF;

// Rule Function
ruleFunction returns [EObject current=null]
@init {
	enterRule();
}
@after {
	leaveRule();
}:
	(
		(
			(
				lv_name_0_0=RULE_ID
				{
					newLeafNode(lv_name_0_0, grammarAccess.getFunctionAccess().getNameIDTerminalRuleCall_0_0());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getFunctionRule());
					}
					setWithLastConsumed(
						$current,
						"name",
						lv_name_0_0,
						"org.eclipse.xtext.common.Terminals.ID");
				}
			)
		)
		otherlv_1='('
		{
			newLeafNode(otherlv_1, grammarAccess.getFunctionAccess().getLeftParenthesisKeyword_1());
		}
		(
			(
				{
					newCompositeNode(grammarAccess.getFunctionAccess().getParametersParameterParserRuleCall_2_0());
				}
				lv_parameters_2_0=ruleParameter
				{
					if ($current==null) {
						$current = createModelElementForParent(grammarAccess.getFunctionRule());
					}
					add(
						$current,
						"parameters",
						lv_parameters_2_0,
						"hu.bme.aut.apitransform.ApiTransform.Parameter");
					afterParserOrEnumRuleCall();
				}
			)
		)*
		otherlv_3=')'
		{
			newLeafNode(otherlv_3, grammarAccess.getFunctionAccess().getRightParenthesisKeyword_3());
		}
	)
;

// Entry rule entryRuleParameter
entryRuleParameter returns [EObject current=null]:
	{ newCompositeNode(grammarAccess.getParameterRule()); }
	iv_ruleParameter=ruleParameter
	{ $current=$iv_ruleParameter.current; }
	EOF;

// Rule Parameter
ruleParameter returns [EObject current=null]
@init {
	enterRule();
}
@after {
	leaveRule();
}:
	(
		(
			lv_name_0_0=RULE_ID
			{
				newLeafNode(lv_name_0_0, grammarAccess.getParameterAccess().getNameIDTerminalRuleCall_0());
			}
			{
				if ($current==null) {
					$current = createModelElement(grammarAccess.getParameterRule());
				}
				setWithLastConsumed(
					$current,
					"name",
					lv_name_0_0,
					"org.eclipse.xtext.common.Terminals.ID");
			}
		)
	)
;

RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

RULE_INT : ('0'..'9')+;

RULE_STRING : ('"' ('\\' .|~(('\\'|'"')))* '"'|'\'' ('\\' .|~(('\\'|'\'')))* '\'');

RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

RULE_WS : (' '|'\t'|'\r'|'\n')+;

RULE_ANY_OTHER : .;
