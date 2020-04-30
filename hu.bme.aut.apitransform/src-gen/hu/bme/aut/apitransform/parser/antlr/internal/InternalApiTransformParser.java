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



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalApiTransformParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_INT", "RULE_STRING", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'=>'", "'static'", "'.'", "'('", "')'"
    };
    public static final int RULE_ID=4;
    public static final int RULE_WS=9;
    public static final int RULE_STRING=6;
    public static final int RULE_ANY_OTHER=10;
    public static final int RULE_SL_COMMENT=8;
    public static final int T__15=15;
    public static final int RULE_INT=5;
    public static final int T__11=11;
    public static final int RULE_ML_COMMENT=7;
    public static final int T__12=12;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int EOF=-1;

    // delegates
    // delegators


        public InternalApiTransformParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalApiTransformParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalApiTransformParser.tokenNames; }
    public String getGrammarFileName() { return "InternalApiTransform.g"; }



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




    // $ANTLR start "entryRuleModel"
    // InternalApiTransform.g:64:1: entryRuleModel returns [EObject current=null] : iv_ruleModel= ruleModel EOF ;
    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModel = null;


        try {
            // InternalApiTransform.g:64:46: (iv_ruleModel= ruleModel EOF )
            // InternalApiTransform.g:65:2: iv_ruleModel= ruleModel EOF
            {
             newCompositeNode(grammarAccess.getModelRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleModel=ruleModel();

            state._fsp--;

             current =iv_ruleModel; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleModel"


    // $ANTLR start "ruleModel"
    // InternalApiTransform.g:71:1: ruleModel returns [EObject current=null] : ( (lv_transformations_0_0= ruleTransformation ) )* ;
    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;

        EObject lv_transformations_0_0 = null;



        	enterRule();

        try {
            // InternalApiTransform.g:77:2: ( ( (lv_transformations_0_0= ruleTransformation ) )* )
            // InternalApiTransform.g:78:2: ( (lv_transformations_0_0= ruleTransformation ) )*
            {
            // InternalApiTransform.g:78:2: ( (lv_transformations_0_0= ruleTransformation ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==RULE_ID) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalApiTransform.g:79:3: (lv_transformations_0_0= ruleTransformation )
            	    {
            	    // InternalApiTransform.g:79:3: (lv_transformations_0_0= ruleTransformation )
            	    // InternalApiTransform.g:80:4: lv_transformations_0_0= ruleTransformation
            	    {

            	    				newCompositeNode(grammarAccess.getModelAccess().getTransformationsTransformationParserRuleCall_0());
            	    			
            	    pushFollow(FOLLOW_3);
            	    lv_transformations_0_0=ruleTransformation();

            	    state._fsp--;


            	    				if (current==null) {
            	    					current = createModelElementForParent(grammarAccess.getModelRule());
            	    				}
            	    				add(
            	    					current,
            	    					"transformations",
            	    					lv_transformations_0_0,
            	    					"hu.bme.aut.apitransform.ApiTransform.Transformation");
            	    				afterParserOrEnumRuleCall();
            	    			

            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleModel"


    // $ANTLR start "entryRuleTransformation"
    // InternalApiTransform.g:100:1: entryRuleTransformation returns [EObject current=null] : iv_ruleTransformation= ruleTransformation EOF ;
    public final EObject entryRuleTransformation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTransformation = null;


        try {
            // InternalApiTransform.g:100:55: (iv_ruleTransformation= ruleTransformation EOF )
            // InternalApiTransform.g:101:2: iv_ruleTransformation= ruleTransformation EOF
            {
             newCompositeNode(grammarAccess.getTransformationRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleTransformation=ruleTransformation();

            state._fsp--;

             current =iv_ruleTransformation; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleTransformation"


    // $ANTLR start "ruleTransformation"
    // InternalApiTransform.g:107:1: ruleTransformation returns [EObject current=null] : ( ( (lv_source_0_0= ruleFunction ) ) otherlv_1= '=>' ( (lv_target_2_0= ruleTarget ) ) ) ;
    public final EObject ruleTransformation() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_source_0_0 = null;

        EObject lv_target_2_0 = null;



        	enterRule();

        try {
            // InternalApiTransform.g:113:2: ( ( ( (lv_source_0_0= ruleFunction ) ) otherlv_1= '=>' ( (lv_target_2_0= ruleTarget ) ) ) )
            // InternalApiTransform.g:114:2: ( ( (lv_source_0_0= ruleFunction ) ) otherlv_1= '=>' ( (lv_target_2_0= ruleTarget ) ) )
            {
            // InternalApiTransform.g:114:2: ( ( (lv_source_0_0= ruleFunction ) ) otherlv_1= '=>' ( (lv_target_2_0= ruleTarget ) ) )
            // InternalApiTransform.g:115:3: ( (lv_source_0_0= ruleFunction ) ) otherlv_1= '=>' ( (lv_target_2_0= ruleTarget ) )
            {
            // InternalApiTransform.g:115:3: ( (lv_source_0_0= ruleFunction ) )
            // InternalApiTransform.g:116:4: (lv_source_0_0= ruleFunction )
            {
            // InternalApiTransform.g:116:4: (lv_source_0_0= ruleFunction )
            // InternalApiTransform.g:117:5: lv_source_0_0= ruleFunction
            {

            					newCompositeNode(grammarAccess.getTransformationAccess().getSourceFunctionParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_4);
            lv_source_0_0=ruleFunction();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getTransformationRule());
            					}
            					set(
            						current,
            						"source",
            						lv_source_0_0,
            						"hu.bme.aut.apitransform.ApiTransform.Function");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_1=(Token)match(input,11,FOLLOW_5); 

            			newLeafNode(otherlv_1, grammarAccess.getTransformationAccess().getEqualsSignGreaterThanSignKeyword_1());
            		
            // InternalApiTransform.g:138:3: ( (lv_target_2_0= ruleTarget ) )
            // InternalApiTransform.g:139:4: (lv_target_2_0= ruleTarget )
            {
            // InternalApiTransform.g:139:4: (lv_target_2_0= ruleTarget )
            // InternalApiTransform.g:140:5: lv_target_2_0= ruleTarget
            {

            					newCompositeNode(grammarAccess.getTransformationAccess().getTargetTargetParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_2);
            lv_target_2_0=ruleTarget();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getTransformationRule());
            					}
            					set(
            						current,
            						"target",
            						lv_target_2_0,
            						"hu.bme.aut.apitransform.ApiTransform.Target");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleTransformation"


    // $ANTLR start "entryRuleTarget"
    // InternalApiTransform.g:161:1: entryRuleTarget returns [EObject current=null] : iv_ruleTarget= ruleTarget EOF ;
    public final EObject entryRuleTarget() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTarget = null;


        try {
            // InternalApiTransform.g:161:47: (iv_ruleTarget= ruleTarget EOF )
            // InternalApiTransform.g:162:2: iv_ruleTarget= ruleTarget EOF
            {
             newCompositeNode(grammarAccess.getTargetRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleTarget=ruleTarget();

            state._fsp--;

             current =iv_ruleTarget; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleTarget"


    // $ANTLR start "ruleTarget"
    // InternalApiTransform.g:168:1: ruleTarget returns [EObject current=null] : ( ( (lv_static_0_0= 'static' ) )? ( (lv_prefix_1_0= ruleFunctionPrefix ) )? ( (lv_function_2_0= ruleFunction ) ) ) ;
    public final EObject ruleTarget() throws RecognitionException {
        EObject current = null;

        Token lv_static_0_0=null;
        EObject lv_prefix_1_0 = null;

        EObject lv_function_2_0 = null;



        	enterRule();

        try {
            // InternalApiTransform.g:174:2: ( ( ( (lv_static_0_0= 'static' ) )? ( (lv_prefix_1_0= ruleFunctionPrefix ) )? ( (lv_function_2_0= ruleFunction ) ) ) )
            // InternalApiTransform.g:175:2: ( ( (lv_static_0_0= 'static' ) )? ( (lv_prefix_1_0= ruleFunctionPrefix ) )? ( (lv_function_2_0= ruleFunction ) ) )
            {
            // InternalApiTransform.g:175:2: ( ( (lv_static_0_0= 'static' ) )? ( (lv_prefix_1_0= ruleFunctionPrefix ) )? ( (lv_function_2_0= ruleFunction ) ) )
            // InternalApiTransform.g:176:3: ( (lv_static_0_0= 'static' ) )? ( (lv_prefix_1_0= ruleFunctionPrefix ) )? ( (lv_function_2_0= ruleFunction ) )
            {
            // InternalApiTransform.g:176:3: ( (lv_static_0_0= 'static' ) )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==12) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // InternalApiTransform.g:177:4: (lv_static_0_0= 'static' )
                    {
                    // InternalApiTransform.g:177:4: (lv_static_0_0= 'static' )
                    // InternalApiTransform.g:178:5: lv_static_0_0= 'static'
                    {
                    lv_static_0_0=(Token)match(input,12,FOLLOW_6); 

                    					newLeafNode(lv_static_0_0, grammarAccess.getTargetAccess().getStaticStaticKeyword_0_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getTargetRule());
                    					}
                    					setWithLastConsumed(current, "static", true, "static");
                    				

                    }


                    }
                    break;

            }

            // InternalApiTransform.g:190:3: ( (lv_prefix_1_0= ruleFunctionPrefix ) )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==RULE_ID) ) {
                int LA3_1 = input.LA(2);

                if ( (LA3_1==13) ) {
                    alt3=1;
                }
            }
            switch (alt3) {
                case 1 :
                    // InternalApiTransform.g:191:4: (lv_prefix_1_0= ruleFunctionPrefix )
                    {
                    // InternalApiTransform.g:191:4: (lv_prefix_1_0= ruleFunctionPrefix )
                    // InternalApiTransform.g:192:5: lv_prefix_1_0= ruleFunctionPrefix
                    {

                    					newCompositeNode(grammarAccess.getTargetAccess().getPrefixFunctionPrefixParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_6);
                    lv_prefix_1_0=ruleFunctionPrefix();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getTargetRule());
                    					}
                    					set(
                    						current,
                    						"prefix",
                    						lv_prefix_1_0,
                    						"hu.bme.aut.apitransform.ApiTransform.FunctionPrefix");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }

            // InternalApiTransform.g:209:3: ( (lv_function_2_0= ruleFunction ) )
            // InternalApiTransform.g:210:4: (lv_function_2_0= ruleFunction )
            {
            // InternalApiTransform.g:210:4: (lv_function_2_0= ruleFunction )
            // InternalApiTransform.g:211:5: lv_function_2_0= ruleFunction
            {

            					newCompositeNode(grammarAccess.getTargetAccess().getFunctionFunctionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_2);
            lv_function_2_0=ruleFunction();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getTargetRule());
            					}
            					set(
            						current,
            						"function",
            						lv_function_2_0,
            						"hu.bme.aut.apitransform.ApiTransform.Function");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleTarget"


    // $ANTLR start "entryRuleFunctionPrefix"
    // InternalApiTransform.g:232:1: entryRuleFunctionPrefix returns [EObject current=null] : iv_ruleFunctionPrefix= ruleFunctionPrefix EOF ;
    public final EObject entryRuleFunctionPrefix() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFunctionPrefix = null;


        try {
            // InternalApiTransform.g:232:55: (iv_ruleFunctionPrefix= ruleFunctionPrefix EOF )
            // InternalApiTransform.g:233:2: iv_ruleFunctionPrefix= ruleFunctionPrefix EOF
            {
             newCompositeNode(grammarAccess.getFunctionPrefixRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleFunctionPrefix=ruleFunctionPrefix();

            state._fsp--;

             current =iv_ruleFunctionPrefix; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleFunctionPrefix"


    // $ANTLR start "ruleFunctionPrefix"
    // InternalApiTransform.g:239:1: ruleFunctionPrefix returns [EObject current=null] : ( ( (lv_prefixes_0_0= RULE_ID ) ) (otherlv_1= '.' ( (lv_prefixes_2_0= RULE_ID ) ) )* otherlv_3= '.' ) ;
    public final EObject ruleFunctionPrefix() throws RecognitionException {
        EObject current = null;

        Token lv_prefixes_0_0=null;
        Token otherlv_1=null;
        Token lv_prefixes_2_0=null;
        Token otherlv_3=null;


        	enterRule();

        try {
            // InternalApiTransform.g:245:2: ( ( ( (lv_prefixes_0_0= RULE_ID ) ) (otherlv_1= '.' ( (lv_prefixes_2_0= RULE_ID ) ) )* otherlv_3= '.' ) )
            // InternalApiTransform.g:246:2: ( ( (lv_prefixes_0_0= RULE_ID ) ) (otherlv_1= '.' ( (lv_prefixes_2_0= RULE_ID ) ) )* otherlv_3= '.' )
            {
            // InternalApiTransform.g:246:2: ( ( (lv_prefixes_0_0= RULE_ID ) ) (otherlv_1= '.' ( (lv_prefixes_2_0= RULE_ID ) ) )* otherlv_3= '.' )
            // InternalApiTransform.g:247:3: ( (lv_prefixes_0_0= RULE_ID ) ) (otherlv_1= '.' ( (lv_prefixes_2_0= RULE_ID ) ) )* otherlv_3= '.'
            {
            // InternalApiTransform.g:247:3: ( (lv_prefixes_0_0= RULE_ID ) )
            // InternalApiTransform.g:248:4: (lv_prefixes_0_0= RULE_ID )
            {
            // InternalApiTransform.g:248:4: (lv_prefixes_0_0= RULE_ID )
            // InternalApiTransform.g:249:5: lv_prefixes_0_0= RULE_ID
            {
            lv_prefixes_0_0=(Token)match(input,RULE_ID,FOLLOW_7); 

            					newLeafNode(lv_prefixes_0_0, grammarAccess.getFunctionPrefixAccess().getPrefixesIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getFunctionPrefixRule());
            					}
            					addWithLastConsumed(
            						current,
            						"prefixes",
            						lv_prefixes_0_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            // InternalApiTransform.g:265:3: (otherlv_1= '.' ( (lv_prefixes_2_0= RULE_ID ) ) )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==13) ) {
                    int LA4_1 = input.LA(2);

                    if ( (LA4_1==RULE_ID) ) {
                        int LA4_2 = input.LA(3);

                        if ( (LA4_2==13) ) {
                            alt4=1;
                        }


                    }


                }


                switch (alt4) {
            	case 1 :
            	    // InternalApiTransform.g:266:4: otherlv_1= '.' ( (lv_prefixes_2_0= RULE_ID ) )
            	    {
            	    otherlv_1=(Token)match(input,13,FOLLOW_6); 

            	    				newLeafNode(otherlv_1, grammarAccess.getFunctionPrefixAccess().getFullStopKeyword_1_0());
            	    			
            	    // InternalApiTransform.g:270:4: ( (lv_prefixes_2_0= RULE_ID ) )
            	    // InternalApiTransform.g:271:5: (lv_prefixes_2_0= RULE_ID )
            	    {
            	    // InternalApiTransform.g:271:5: (lv_prefixes_2_0= RULE_ID )
            	    // InternalApiTransform.g:272:6: lv_prefixes_2_0= RULE_ID
            	    {
            	    lv_prefixes_2_0=(Token)match(input,RULE_ID,FOLLOW_7); 

            	    						newLeafNode(lv_prefixes_2_0, grammarAccess.getFunctionPrefixAccess().getPrefixesIDTerminalRuleCall_1_1_0());
            	    					

            	    						if (current==null) {
            	    							current = createModelElement(grammarAccess.getFunctionPrefixRule());
            	    						}
            	    						addWithLastConsumed(
            	    							current,
            	    							"prefixes",
            	    							lv_prefixes_2_0,
            	    							"org.eclipse.xtext.common.Terminals.ID");
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);

            otherlv_3=(Token)match(input,13,FOLLOW_2); 

            			newLeafNode(otherlv_3, grammarAccess.getFunctionPrefixAccess().getFullStopKeyword_2());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleFunctionPrefix"


    // $ANTLR start "entryRuleFunction"
    // InternalApiTransform.g:297:1: entryRuleFunction returns [EObject current=null] : iv_ruleFunction= ruleFunction EOF ;
    public final EObject entryRuleFunction() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleFunction = null;


        try {
            // InternalApiTransform.g:297:49: (iv_ruleFunction= ruleFunction EOF )
            // InternalApiTransform.g:298:2: iv_ruleFunction= ruleFunction EOF
            {
             newCompositeNode(grammarAccess.getFunctionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleFunction=ruleFunction();

            state._fsp--;

             current =iv_ruleFunction; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleFunction"


    // $ANTLR start "ruleFunction"
    // InternalApiTransform.g:304:1: ruleFunction returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_parameters_2_0= ruleParameter ) )* otherlv_3= ')' ) ;
    public final EObject ruleFunction() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        EObject lv_parameters_2_0 = null;



        	enterRule();

        try {
            // InternalApiTransform.g:310:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_parameters_2_0= ruleParameter ) )* otherlv_3= ')' ) )
            // InternalApiTransform.g:311:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_parameters_2_0= ruleParameter ) )* otherlv_3= ')' )
            {
            // InternalApiTransform.g:311:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_parameters_2_0= ruleParameter ) )* otherlv_3= ')' )
            // InternalApiTransform.g:312:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_parameters_2_0= ruleParameter ) )* otherlv_3= ')'
            {
            // InternalApiTransform.g:312:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalApiTransform.g:313:4: (lv_name_0_0= RULE_ID )
            {
            // InternalApiTransform.g:313:4: (lv_name_0_0= RULE_ID )
            // InternalApiTransform.g:314:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_8); 

            					newLeafNode(lv_name_0_0, grammarAccess.getFunctionAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getFunctionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,14,FOLLOW_9); 

            			newLeafNode(otherlv_1, grammarAccess.getFunctionAccess().getLeftParenthesisKeyword_1());
            		
            // InternalApiTransform.g:334:3: ( (lv_parameters_2_0= ruleParameter ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==RULE_ID) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // InternalApiTransform.g:335:4: (lv_parameters_2_0= ruleParameter )
            	    {
            	    // InternalApiTransform.g:335:4: (lv_parameters_2_0= ruleParameter )
            	    // InternalApiTransform.g:336:5: lv_parameters_2_0= ruleParameter
            	    {

            	    					newCompositeNode(grammarAccess.getFunctionAccess().getParametersParameterParserRuleCall_2_0());
            	    				
            	    pushFollow(FOLLOW_9);
            	    lv_parameters_2_0=ruleParameter();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getFunctionRule());
            	    					}
            	    					add(
            	    						current,
            	    						"parameters",
            	    						lv_parameters_2_0,
            	    						"hu.bme.aut.apitransform.ApiTransform.Parameter");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            otherlv_3=(Token)match(input,15,FOLLOW_2); 

            			newLeafNode(otherlv_3, grammarAccess.getFunctionAccess().getRightParenthesisKeyword_3());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleFunction"


    // $ANTLR start "entryRuleParameter"
    // InternalApiTransform.g:361:1: entryRuleParameter returns [EObject current=null] : iv_ruleParameter= ruleParameter EOF ;
    public final EObject entryRuleParameter() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleParameter = null;


        try {
            // InternalApiTransform.g:361:50: (iv_ruleParameter= ruleParameter EOF )
            // InternalApiTransform.g:362:2: iv_ruleParameter= ruleParameter EOF
            {
             newCompositeNode(grammarAccess.getParameterRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleParameter=ruleParameter();

            state._fsp--;

             current =iv_ruleParameter; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleParameter"


    // $ANTLR start "ruleParameter"
    // InternalApiTransform.g:368:1: ruleParameter returns [EObject current=null] : ( (lv_name_0_0= RULE_ID ) ) ;
    public final EObject ruleParameter() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;


        	enterRule();

        try {
            // InternalApiTransform.g:374:2: ( ( (lv_name_0_0= RULE_ID ) ) )
            // InternalApiTransform.g:375:2: ( (lv_name_0_0= RULE_ID ) )
            {
            // InternalApiTransform.g:375:2: ( (lv_name_0_0= RULE_ID ) )
            // InternalApiTransform.g:376:3: (lv_name_0_0= RULE_ID )
            {
            // InternalApiTransform.g:376:3: (lv_name_0_0= RULE_ID )
            // InternalApiTransform.g:377:4: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            				newLeafNode(lv_name_0_0, grammarAccess.getParameterAccess().getNameIDTerminalRuleCall_0());
            			

            				if (current==null) {
            					current = createModelElement(grammarAccess.getParameterRule());
            				}
            				setWithLastConsumed(
            					current,
            					"name",
            					lv_name_0_0,
            					"org.eclipse.xtext.common.Terminals.ID");
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleParameter"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000000800L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000001010L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000008010L});

}