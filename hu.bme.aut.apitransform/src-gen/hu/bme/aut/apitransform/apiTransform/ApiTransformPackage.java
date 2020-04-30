/**
 * generated by Xtext 2.16.0
 */
package hu.bme.aut.apitransform.apiTransform;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see hu.bme.aut.apitransform.apiTransform.ApiTransformFactory
 * @model kind="package"
 * @generated
 */
public interface ApiTransformPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "apiTransform";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.bme.hu/aut/apitransform/ApiTransform";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "apiTransform";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  ApiTransformPackage eINSTANCE = hu.bme.aut.apitransform.apiTransform.impl.ApiTransformPackageImpl.init();

  /**
   * The meta object id for the '{@link hu.bme.aut.apitransform.apiTransform.impl.ModelImpl <em>Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.bme.aut.apitransform.apiTransform.impl.ModelImpl
   * @see hu.bme.aut.apitransform.apiTransform.impl.ApiTransformPackageImpl#getModel()
   * @generated
   */
  int MODEL = 0;

  /**
   * The feature id for the '<em><b>Transformations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL__TRANSFORMATIONS = 0;

  /**
   * The number of structural features of the '<em>Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link hu.bme.aut.apitransform.apiTransform.impl.TransformationImpl <em>Transformation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.bme.aut.apitransform.apiTransform.impl.TransformationImpl
   * @see hu.bme.aut.apitransform.apiTransform.impl.ApiTransformPackageImpl#getTransformation()
   * @generated
   */
  int TRANSFORMATION = 1;

  /**
   * The feature id for the '<em><b>Source</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSFORMATION__SOURCE = 0;

  /**
   * The feature id for the '<em><b>Target</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSFORMATION__TARGET = 1;

  /**
   * The number of structural features of the '<em>Transformation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TRANSFORMATION_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link hu.bme.aut.apitransform.apiTransform.impl.TargetImpl <em>Target</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.bme.aut.apitransform.apiTransform.impl.TargetImpl
   * @see hu.bme.aut.apitransform.apiTransform.impl.ApiTransformPackageImpl#getTarget()
   * @generated
   */
  int TARGET = 2;

  /**
   * The feature id for the '<em><b>Static</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGET__STATIC = 0;

  /**
   * The feature id for the '<em><b>Prefix</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGET__PREFIX = 1;

  /**
   * The feature id for the '<em><b>Function</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGET__FUNCTION = 2;

  /**
   * The number of structural features of the '<em>Target</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TARGET_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link hu.bme.aut.apitransform.apiTransform.impl.FunctionPrefixImpl <em>Function Prefix</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.bme.aut.apitransform.apiTransform.impl.FunctionPrefixImpl
   * @see hu.bme.aut.apitransform.apiTransform.impl.ApiTransformPackageImpl#getFunctionPrefix()
   * @generated
   */
  int FUNCTION_PREFIX = 3;

  /**
   * The feature id for the '<em><b>Prefixes</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION_PREFIX__PREFIXES = 0;

  /**
   * The number of structural features of the '<em>Function Prefix</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION_PREFIX_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link hu.bme.aut.apitransform.apiTransform.impl.FunctionImpl <em>Function</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.bme.aut.apitransform.apiTransform.impl.FunctionImpl
   * @see hu.bme.aut.apitransform.apiTransform.impl.ApiTransformPackageImpl#getFunction()
   * @generated
   */
  int FUNCTION = 4;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION__NAME = 0;

  /**
   * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION__PARAMETERS = 1;

  /**
   * The number of structural features of the '<em>Function</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FUNCTION_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link hu.bme.aut.apitransform.apiTransform.impl.ParameterImpl <em>Parameter</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.bme.aut.apitransform.apiTransform.impl.ParameterImpl
   * @see hu.bme.aut.apitransform.apiTransform.impl.ApiTransformPackageImpl#getParameter()
   * @generated
   */
  int PARAMETER = 5;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETER__NAME = 0;

  /**
   * The number of structural features of the '<em>Parameter</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETER_FEATURE_COUNT = 1;


  /**
   * Returns the meta object for class '{@link hu.bme.aut.apitransform.apiTransform.Model <em>Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Model</em>'.
   * @see hu.bme.aut.apitransform.apiTransform.Model
   * @generated
   */
  EClass getModel();

  /**
   * Returns the meta object for the containment reference list '{@link hu.bme.aut.apitransform.apiTransform.Model#getTransformations <em>Transformations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Transformations</em>'.
   * @see hu.bme.aut.apitransform.apiTransform.Model#getTransformations()
   * @see #getModel()
   * @generated
   */
  EReference getModel_Transformations();

  /**
   * Returns the meta object for class '{@link hu.bme.aut.apitransform.apiTransform.Transformation <em>Transformation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Transformation</em>'.
   * @see hu.bme.aut.apitransform.apiTransform.Transformation
   * @generated
   */
  EClass getTransformation();

  /**
   * Returns the meta object for the containment reference '{@link hu.bme.aut.apitransform.apiTransform.Transformation#getSource <em>Source</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Source</em>'.
   * @see hu.bme.aut.apitransform.apiTransform.Transformation#getSource()
   * @see #getTransformation()
   * @generated
   */
  EReference getTransformation_Source();

  /**
   * Returns the meta object for the containment reference '{@link hu.bme.aut.apitransform.apiTransform.Transformation#getTarget <em>Target</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Target</em>'.
   * @see hu.bme.aut.apitransform.apiTransform.Transformation#getTarget()
   * @see #getTransformation()
   * @generated
   */
  EReference getTransformation_Target();

  /**
   * Returns the meta object for class '{@link hu.bme.aut.apitransform.apiTransform.Target <em>Target</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Target</em>'.
   * @see hu.bme.aut.apitransform.apiTransform.Target
   * @generated
   */
  EClass getTarget();

  /**
   * Returns the meta object for the attribute '{@link hu.bme.aut.apitransform.apiTransform.Target#isStatic <em>Static</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Static</em>'.
   * @see hu.bme.aut.apitransform.apiTransform.Target#isStatic()
   * @see #getTarget()
   * @generated
   */
  EAttribute getTarget_Static();

  /**
   * Returns the meta object for the containment reference '{@link hu.bme.aut.apitransform.apiTransform.Target#getPrefix <em>Prefix</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Prefix</em>'.
   * @see hu.bme.aut.apitransform.apiTransform.Target#getPrefix()
   * @see #getTarget()
   * @generated
   */
  EReference getTarget_Prefix();

  /**
   * Returns the meta object for the containment reference '{@link hu.bme.aut.apitransform.apiTransform.Target#getFunction <em>Function</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Function</em>'.
   * @see hu.bme.aut.apitransform.apiTransform.Target#getFunction()
   * @see #getTarget()
   * @generated
   */
  EReference getTarget_Function();

  /**
   * Returns the meta object for class '{@link hu.bme.aut.apitransform.apiTransform.FunctionPrefix <em>Function Prefix</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Function Prefix</em>'.
   * @see hu.bme.aut.apitransform.apiTransform.FunctionPrefix
   * @generated
   */
  EClass getFunctionPrefix();

  /**
   * Returns the meta object for the attribute list '{@link hu.bme.aut.apitransform.apiTransform.FunctionPrefix#getPrefixes <em>Prefixes</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Prefixes</em>'.
   * @see hu.bme.aut.apitransform.apiTransform.FunctionPrefix#getPrefixes()
   * @see #getFunctionPrefix()
   * @generated
   */
  EAttribute getFunctionPrefix_Prefixes();

  /**
   * Returns the meta object for class '{@link hu.bme.aut.apitransform.apiTransform.Function <em>Function</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Function</em>'.
   * @see hu.bme.aut.apitransform.apiTransform.Function
   * @generated
   */
  EClass getFunction();

  /**
   * Returns the meta object for the attribute '{@link hu.bme.aut.apitransform.apiTransform.Function#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see hu.bme.aut.apitransform.apiTransform.Function#getName()
   * @see #getFunction()
   * @generated
   */
  EAttribute getFunction_Name();

  /**
   * Returns the meta object for the containment reference list '{@link hu.bme.aut.apitransform.apiTransform.Function#getParameters <em>Parameters</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Parameters</em>'.
   * @see hu.bme.aut.apitransform.apiTransform.Function#getParameters()
   * @see #getFunction()
   * @generated
   */
  EReference getFunction_Parameters();

  /**
   * Returns the meta object for class '{@link hu.bme.aut.apitransform.apiTransform.Parameter <em>Parameter</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Parameter</em>'.
   * @see hu.bme.aut.apitransform.apiTransform.Parameter
   * @generated
   */
  EClass getParameter();

  /**
   * Returns the meta object for the attribute '{@link hu.bme.aut.apitransform.apiTransform.Parameter#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see hu.bme.aut.apitransform.apiTransform.Parameter#getName()
   * @see #getParameter()
   * @generated
   */
  EAttribute getParameter_Name();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  ApiTransformFactory getApiTransformFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link hu.bme.aut.apitransform.apiTransform.impl.ModelImpl <em>Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.bme.aut.apitransform.apiTransform.impl.ModelImpl
     * @see hu.bme.aut.apitransform.apiTransform.impl.ApiTransformPackageImpl#getModel()
     * @generated
     */
    EClass MODEL = eINSTANCE.getModel();

    /**
     * The meta object literal for the '<em><b>Transformations</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MODEL__TRANSFORMATIONS = eINSTANCE.getModel_Transformations();

    /**
     * The meta object literal for the '{@link hu.bme.aut.apitransform.apiTransform.impl.TransformationImpl <em>Transformation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.bme.aut.apitransform.apiTransform.impl.TransformationImpl
     * @see hu.bme.aut.apitransform.apiTransform.impl.ApiTransformPackageImpl#getTransformation()
     * @generated
     */
    EClass TRANSFORMATION = eINSTANCE.getTransformation();

    /**
     * The meta object literal for the '<em><b>Source</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TRANSFORMATION__SOURCE = eINSTANCE.getTransformation_Source();

    /**
     * The meta object literal for the '<em><b>Target</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TRANSFORMATION__TARGET = eINSTANCE.getTransformation_Target();

    /**
     * The meta object literal for the '{@link hu.bme.aut.apitransform.apiTransform.impl.TargetImpl <em>Target</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.bme.aut.apitransform.apiTransform.impl.TargetImpl
     * @see hu.bme.aut.apitransform.apiTransform.impl.ApiTransformPackageImpl#getTarget()
     * @generated
     */
    EClass TARGET = eINSTANCE.getTarget();

    /**
     * The meta object literal for the '<em><b>Static</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TARGET__STATIC = eINSTANCE.getTarget_Static();

    /**
     * The meta object literal for the '<em><b>Prefix</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TARGET__PREFIX = eINSTANCE.getTarget_Prefix();

    /**
     * The meta object literal for the '<em><b>Function</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TARGET__FUNCTION = eINSTANCE.getTarget_Function();

    /**
     * The meta object literal for the '{@link hu.bme.aut.apitransform.apiTransform.impl.FunctionPrefixImpl <em>Function Prefix</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.bme.aut.apitransform.apiTransform.impl.FunctionPrefixImpl
     * @see hu.bme.aut.apitransform.apiTransform.impl.ApiTransformPackageImpl#getFunctionPrefix()
     * @generated
     */
    EClass FUNCTION_PREFIX = eINSTANCE.getFunctionPrefix();

    /**
     * The meta object literal for the '<em><b>Prefixes</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FUNCTION_PREFIX__PREFIXES = eINSTANCE.getFunctionPrefix_Prefixes();

    /**
     * The meta object literal for the '{@link hu.bme.aut.apitransform.apiTransform.impl.FunctionImpl <em>Function</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.bme.aut.apitransform.apiTransform.impl.FunctionImpl
     * @see hu.bme.aut.apitransform.apiTransform.impl.ApiTransformPackageImpl#getFunction()
     * @generated
     */
    EClass FUNCTION = eINSTANCE.getFunction();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FUNCTION__NAME = eINSTANCE.getFunction_Name();

    /**
     * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference FUNCTION__PARAMETERS = eINSTANCE.getFunction_Parameters();

    /**
     * The meta object literal for the '{@link hu.bme.aut.apitransform.apiTransform.impl.ParameterImpl <em>Parameter</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.bme.aut.apitransform.apiTransform.impl.ParameterImpl
     * @see hu.bme.aut.apitransform.apiTransform.impl.ApiTransformPackageImpl#getParameter()
     * @generated
     */
    EClass PARAMETER = eINSTANCE.getParameter();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PARAMETER__NAME = eINSTANCE.getParameter_Name();

  }

} //ApiTransformPackage
