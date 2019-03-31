package hu.bme.aut.moodernize.c2j.util;

import org.eclipse.cdt.core.dom.ast.IArrayType;
import org.eclipse.cdt.core.dom.ast.IBasicType;
import org.eclipse.cdt.core.dom.ast.IBasicType.Kind;
import org.eclipse.cdt.core.dom.ast.ICompositeType;
import org.eclipse.cdt.core.dom.ast.IPointerType;
import org.eclipse.cdt.core.dom.ast.IType;
import org.eclipse.cdt.core.dom.ast.ITypedef;

import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OogenFactory;

public class TypeConverter {
	
	public static OOType convertCDTTypeToOOgenType(IType cdtType) {
		OogenFactory factory = OogenFactory.eINSTANCE;
		OOType ooType = factory.createOOType();
		
		//More dimensions not implemented yet in OOGen
		//To process all dimensions, use while instead of if
		if (cdtType instanceof IArrayType) {
			ooType.setArray(true);
			cdtType = ((IArrayType) cdtType).getType();
		}
		handleType(ooType, cdtType);

		return ooType;
	}
	
	private static void handleType(OOType ooType, IType cdtType) {
		//More than one indirection is not supported yet
		if(cdtType instanceof IPointerType) {
			handleType(ooType, ((IPointerType) cdtType).getType());
		} 
		
		else if (cdtType instanceof IBasicType) {
			setOOBaseType(ooType, (IBasicType) cdtType); 
		} else if (cdtType instanceof ICompositeType) {
			setOOReferenceType(ooType, (ICompositeType) cdtType);
		} else if (cdtType instanceof ITypedef && (((ITypedef) cdtType).getType()) instanceof ICompositeType) {
			setOOReferenceType(ooType, (ICompositeType)(((ITypedef) cdtType).getType()));
		} else {
			ooType.setBaseType(OOBaseType.OBJECT);
		}
	}

	private static void setOOBaseType(OOType ooType, IBasicType type) {
		IBasicType.Kind kind = type.getKind();
		if (kind == Kind.eBoolean) {
			ooType.setBaseType(OOBaseType.BOOLEAN);
		} else if (kind == Kind.eInt || kind == Kind.eInt128
				   || kind == Kind.eDecimal32
				   || kind == Kind.eDecimal64
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
		} else if (kind == Kind.eVoid) {
			ooType.setBaseType(OOBaseType.OBJECT);
		}
	}

	private static void setOOReferenceType(OOType ooType, ICompositeType struct) {
		OogenFactory factory = OogenFactory.eINSTANCE;
		ooType.setBaseType(OOBaseType.OBJECT);
		OOClass classType = factory.createOOClass();
		classType.setName(struct.getName());
		ooType.setClassType(classType);
	}
}
