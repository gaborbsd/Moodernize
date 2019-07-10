package hu.bme.aut.moodernize.c2j.util;

import org.eclipse.cdt.core.dom.ast.IASTElaboratedTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTNamedTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IArrayType;
import org.eclipse.cdt.core.dom.ast.IBasicType;
import org.eclipse.cdt.core.dom.ast.ICompositeType;
import org.eclipse.cdt.core.dom.ast.IPointerType;
import org.eclipse.cdt.core.dom.ast.IType;
import org.eclipse.cdt.core.dom.ast.ITypedef;

import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OogenFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class TypeConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public static OOType convertCDTTypeToOOgenType(IType cdtType) {
	OOType ooType = factory.createOOType();

	handleArrayType(ooType, cdtType);
	handleType(ooType, cdtType);

	return ooType;
    }

    private static void handleType(OOType ooType, IType cdtType) {
	// TODO: More than one indirection is not supported yet
	if (cdtType instanceof IPointerType) {
	    cdtType = ((IPointerType) cdtType).getType();
	}

	if (cdtType instanceof IBasicType) {
	    setOOBaseType(ooType, (IBasicType) cdtType);
	} else if (cdtType instanceof ICompositeType) {
	    setOOReferenceType(ooType, ((ICompositeType) cdtType).getName());
	} else if (cdtType instanceof ITypedef && (((ITypedef) cdtType).getType()) instanceof ICompositeType) {
	    setOOReferenceType(ooType, ((ICompositeType) (((ITypedef) cdtType).getType())).getName());
	} else {
	    ooType.setBaseType(OOBaseType.OBJECT);
	}
    }
    
    private static void handleArrayType(OOType ooType, IType arrayType) {
	while (arrayType instanceof IArrayType) {
	    ooType.setArrayDimensions(ooType.getArrayDimensions() + 1);
	    arrayType = ((IArrayType) arrayType).getType();
	}
    }

    private static void setOOBaseType(OOType ooType, IBasicType type) {

	switch (type.getKind()) {
	case eBoolean:
	    ooType.setBaseType(OOBaseType.BOOLEAN);
	    break;
	case eInt:
	case eInt128:
	case eDecimal32:
	case eDecimal64:
	case eDecimal128:
	    if (type.isLong()) {
		ooType.setBaseType(OOBaseType.LONG);
	    } else {
		ooType.setBaseType(OOBaseType.INT);
	    }
	    break;
	case eDouble:
	case eFloat:
	case eFloat128:
	    ooType.setBaseType(OOBaseType.DOUBLE);
	    break;
	case eChar:
	case eChar16:
	case eChar32:
	    ooType.setBaseType(OOBaseType.BYTE);
	    break;
	case eVoid:
	    ooType.setBaseType(OOBaseType.OBJECT);
	    break;
	default:
	    throw new UnsupportedOperationException("Unsupported IBasicType encountered: " + type);
	}
    }

    private static void setOOReferenceType(OOType ooType, String className) {
	ooType.setBaseType(OOBaseType.OBJECT);
	OOClass classType = factory.createOOClass();
	classType.setName(className);
	ooType.setClassType(classType);
    }

    public static OOType convertSimpleDeclSpecifierType(IASTSimpleDeclSpecifier specifier) {
	OOType type = factory.createOOType();
	type.setClassType(null);
	
	switch (specifier.getType()) {
	case IASTSimpleDeclSpecifier.t_auto:
	    break;
	case IASTSimpleDeclSpecifier.t_decltype:
	    break;
	case IASTSimpleDeclSpecifier.t_decltype_auto:
	    break;
	case IASTSimpleDeclSpecifier.t_typeof:
	    break;
	case IASTSimpleDeclSpecifier.t_unspecified:
	    if (specifier.isLong() || specifier.isLongLong()) {
		type.setBaseType(OOBaseType.LONG);
	    } else if (specifier.isShort()) {
		type.setBaseType(OOBaseType.INT);
	    }
	    break;
	case IASTSimpleDeclSpecifier.t_wchar_t:
	    break;
	case IASTSimpleDeclSpecifier.t_bool:
	    type.setBaseType(OOBaseType.BOOLEAN);
	    break;
	case IASTSimpleDeclSpecifier.t_char:
	case IASTSimpleDeclSpecifier.t_char16_t:
	case IASTSimpleDeclSpecifier.t_char32_t:
	    type.setBaseType(OOBaseType.BYTE);
	    break;
	case IASTSimpleDeclSpecifier.t_int:
	case IASTSimpleDeclSpecifier.t_int128:
	case IASTSimpleDeclSpecifier.t_decimal32:
	case IASTSimpleDeclSpecifier.t_decimal64:
	case IASTSimpleDeclSpecifier.t_decimal128:
	    if (specifier.isLong() || specifier.isLongLong()) {
		type.setBaseType(OOBaseType.LONG);
	    } else {
		type.setBaseType(OOBaseType.INT);
	    }
	    break;
	case IASTSimpleDeclSpecifier.t_float:
	case IASTSimpleDeclSpecifier.t_float128:
	case IASTSimpleDeclSpecifier.t_double:
	    type.setBaseType(OOBaseType.DOUBLE);
	    break;
	case IASTSimpleDeclSpecifier.t_void:
	    type.setBaseType(OOBaseType.OBJECT);
	    break;
	default:
	    throw new UnsupportedOperationException("Unsupported SimpleDeclSpecifier type" + specifier.getType());
	}

	return type;
    }

    public static OOType convertElaboratedTypeSpecifierType(IASTElaboratedTypeSpecifier specifier) {
	OOType type = factory.createOOType();
	String typeName = specifier.getName().resolveBinding().getName();
	// TODO: Enum
	switch (specifier.getKind()) {
	case IASTElaboratedTypeSpecifier.k_struct:
	    setOOReferenceType(type, typeName);
	    return type;
	default:
	    throw new NotImplementedException();
	}
    }

    public static OOType convertNamedTypeSpecifierType(IASTNamedTypeSpecifier specifier) {
	OOType type = factory.createOOType();
	String typeName = specifier.getName().resolveBinding().getName();
	setOOReferenceType(type, typeName);
	return type;
    }
}