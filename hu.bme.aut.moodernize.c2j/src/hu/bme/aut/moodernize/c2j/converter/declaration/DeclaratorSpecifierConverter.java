package hu.bme.aut.moodernize.c2j.converter.declaration;

import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTElaboratedTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTNamedTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclSpecifier;

import hu.bme.aut.moodernize.c2j.util.TypeConverter;
import hu.bme.aut.oogen.OOType;

public class DeclaratorSpecifierConverter {
    public OOType convertSpecifier(IASTDeclSpecifier specifier) {
	if (specifier instanceof IASTSimpleDeclSpecifier) {
	    return TypeConverter.convertSimpleDeclSpecifierType((IASTSimpleDeclSpecifier) specifier);
	} else if (specifier instanceof IASTElaboratedTypeSpecifier) {
	    return TypeConverter.convertElaboratedTypeSpecifierType((IASTElaboratedTypeSpecifier) specifier);
	} else if (specifier instanceof IASTNamedTypeSpecifier) {
	    return TypeConverter.convertNamedTypeSpecifierType((IASTNamedTypeSpecifier) specifier);
	} else
	    throw new UnsupportedOperationException("Unsupported DeclSpecifier encountered: " + specifier);
    }
}
