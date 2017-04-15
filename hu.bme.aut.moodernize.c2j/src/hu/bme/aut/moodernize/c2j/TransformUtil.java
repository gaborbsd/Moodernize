package hu.bme.aut.moodernize.c2j;

import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OogenFactory;

import org.eclipse.cdt.core.dom.ast.IArrayType;
import org.eclipse.cdt.core.dom.ast.IBasicType;
import org.eclipse.cdt.core.dom.ast.IBasicType.Kind;
import org.eclipse.cdt.core.dom.ast.ICompositeType;
import org.eclipse.cdt.core.dom.ast.IType;
import org.eclipse.cdt.core.dom.ast.ITypedef;

public class TransformUtil {
	public static String capitalizeFirst(String s) {
		if (s.isEmpty())
			return s;
		else if (s.length() < 2)
			return s.toUpperCase();
		else
			return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}

	public static OOType convertCDTTypeToOOgen(IType type) {
		OogenFactory factory = OogenFactory.eINSTANCE;
		OOType ooType = factory.createOOType();
		
		if (type instanceof IArrayType) {
			ooType.setArray(true);
			type = ((IArrayType) type).getType();
		}
		handleType(ooType, type);
		
		/*if (type instanceof IBasicType) {
			setOOBaseType(ooType, (IBasicType) type);
		} else if (type instanceof IArrayType) {
			ooType.setArray(true);
			IType arrayType = ((IArrayType) type).getType();
			if (arrayType instanceof IBasicType) {
				setOOBaseType(ooType, (IBasicType) arrayType);
			} else {
				ooType.setBaseType(OOBaseType.OBJECT);
				
			}
		} else {
			ooType.setBaseType(OOBaseType.OBJECT);
		}*/

		return ooType;
	}
	
	private static void handleType(OOType ooType, IType type) {
		if (type instanceof IBasicType) {
			setOOBaseType(ooType, (IBasicType) type); 
		} else if (type instanceof ICompositeType) {
			setOOStructureType(ooType, (ICompositeType) type);
		} else if (type instanceof ITypedef && (((ITypedef) type).getType()) instanceof ICompositeType) {
			setOOStructureType(ooType, (ICompositeType)(((ITypedef) type).getType()));
		} else {
			ooType.setBaseType(OOBaseType.OBJECT);
		}
	}

	private static void setOOBaseType(OOType ooType, IBasicType type) {
		IBasicType.Kind kind = type.getKind();
		if (kind == Kind.eBoolean) {
			ooType.setBaseType(OOBaseType.BOOLEAN);
		} else if (kind == Kind.eInt || kind == Kind.eInt128 || kind == Kind.eDecimal32 || kind == Kind.eDecimal64
				|| kind == Kind.eDecimal128) {
			if (type.isLong()) {
				ooType.setBaseType(OOBaseType.LONG);
			} else {
				ooType.setBaseType(OOBaseType.INT);
			}
		} else if (kind == Kind.eDouble || kind == Kind.eFloat || kind == Kind.eFloat128) {
			ooType.setBaseType(OOBaseType.DOUBLE);
		} else if (kind == Kind.eChar || kind == Kind.eChar16 || kind == Kind.eChar32) {
			ooType.setBaseType(OOBaseType.BYTE);
		} 
	}
	
	private static void setOOStructureType(OOType ooType, ICompositeType struct) {
		OogenFactory factory = OogenFactory.eINSTANCE;
		ooType.setBaseType(OOBaseType.OBJECT);
		OOClass classType = factory.createOOClass();
		classType.setName(struct.getName());
		ooType.setClassType(classType);
	}
}
