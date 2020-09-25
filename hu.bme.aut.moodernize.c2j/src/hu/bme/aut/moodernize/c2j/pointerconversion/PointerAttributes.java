package hu.bme.aut.moodernize.c2j.pointerconversion;

import hu.bme.aut.oogen.OOVariable;

public class PointerAttributes {
    public OOVariable pointerDeclaration;
    public String scopeIdentifier;
    
    public boolean indexOperatorUsed = false;
    public boolean pointerArithmeticsUsed = false;
    public boolean dereferOperatorUsed = false;
    public boolean isParameter = false;
    public boolean isReturnValue = false;
    public boolean isTransformedAsArray = false;

    public PointerAttributes(OOVariable variable, String scopeIdentifier) {
	this.pointerDeclaration = variable;
	this.scopeIdentifier = scopeIdentifier;
    }
}
