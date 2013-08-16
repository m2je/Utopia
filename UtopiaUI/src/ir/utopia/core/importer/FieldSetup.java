package ir.utopia.core.importer;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ir.utopia.core.struts.UtopiaFormMethodMetaData;


/**
 * Keep user defined settings of persistent fields for importer.
 * 
 * @author Golnari
 */
public class FieldSetup extends UtopiaFormMethodMetaData {
	private Object defaultValue;
	private int index;
	/**
	 * 
	 */
	private static final long serialVersionUID = -411229381171116267L;

	/**
	 * The Enum Types.
	 */
	@XmlType()
	@XmlEnum(Integer.class)
	public enum Types{
		
		/** The CODE. */
		@XmlEnumValue("0") CODE, 
		/** The NAME. */
		@XmlEnumValue("1") NAME, 
		
		/** The VALU e_ text. */
		@XmlEnumValue("2") VALUE_TEXT,
		
		/** The VALU e_ number. */
		@XmlEnumValue("3") VALUE_NUMBER,
		
		/** The VALU e_ enum. */
		@XmlEnumValue("4") VALUE_ENUM,
		
		/** The VALU e_ date e_ gregorian. */
		@XmlEnumValue("5") VALUE_DATE_GREGORIAN, //	YYYY/MM/DD
		/** The VALU e_ date e_ salor. */
		@XmlEnumValue("6") VALUE_DATE_SOLAR,	  //	YYYY/MM/DD
		
		@XmlEnumValue("7") REFERENCE_PRIMARY_KEY,
		
		@XmlEnumValue("8") BOOLEAN_VALUE
		;
	
		public static Types getType(int t){
			switch (t) {
			case 0:
				return CODE;
			case 1:
				return NAME;
			case 2:
				return VALUE_TEXT;
			case 3:
				return VALUE_NUMBER;
			case 4:
				return VALUE_ENUM;
			case 5:
				return VALUE_DATE_GREGORIAN;
			case 6:
				return VALUE_DATE_SOLAR;
			case 7:
				return REFERENCE_PRIMARY_KEY;
			case 8:
				return BOOLEAN_VALUE;
			default:
				return null;
			}
		}
		
		/**
  	 * Checks if is code.
  	 * 
  	 * @return true, if is code
  	 */
  	public Boolean isCode(){
			return this.equals(CODE);
		}
		
		/**
		 * Checks if is name.
		 * 
		 * @return true, if is name
		 */
		public Boolean isName(){
			return this.equals(NAME);
		}
		
		/**
		 * Checks if is number value.
		 * 
		 * @return true, if is number value
		 */
		public Boolean isNumberValue(){
			return this.equals(VALUE_NUMBER)||this.equals(REFERENCE_PRIMARY_KEY);
		}
		
		/**
		 * Checks if is enum value.
		 * 
		 * @return true, if is enum value
		 */
		public Boolean isEnumValue(){
			return this.equals(VALUE_ENUM);
		}
		
		/**
		 * Checks if is ger date value.
		 * 
		 * @return true, if is ger date value
		 */
		public Boolean isGerDateValue(){
			return this.equals(VALUE_DATE_GREGORIAN);
		}
		
		/**
		 * Checks if is solar date value.
		 * 
		 * @return true, if is solar date value
		 */
		public Boolean isSolarDateValue(){
			return this.equals(VALUE_DATE_SOLAR);
		}
		
		/**
		 * Checks if is text value.
		 * 
		 * @return true, if is text value
		 */
		public Boolean isTextValue(){
			return this.equals(VALUE_TEXT);
		}
	}
	public FieldSetup (UtopiaFormMethodMetaData meta){
		super(meta.getMethodName(),meta.getAnnotations(),meta.getReturnType());
	}
	
	/** The import formula. */
	private String importFormula;
	
	/** The type. */
	private Types type;
	
	
	
	
	


	/**
	 * Gets the field class.
	 * 
	 * @return the field class
	 */
	@XmlTransient
	public Class<?> getFieldClass() {
		return getReturnType();
	}
	
	
	
	
	/**
	 * Checks for default value.
	 * 
	 * @return true, if successful
	 */
	public Boolean hasDefaultValue() {
		return getDefaultValue()!=null;
	}
	
	
	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public Types getType() {
		return type;
	}
	
	/**
	 * Sets the type.
	 * 
	 * @param type the new type
	 */
	public void setType(Types type) {
		this.type = type;
	}
	
	/**
	 * Sets the type.
	 * 
	 * @param type the new type
	 */
	public void setType(String type) {
		if(type != null)
			this.type = Types.valueOf(type);
	}




	public Object getDefaultValue() {
		return defaultValue;
	}




	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}




	public int getIndex() {
		return index;
	}




	public void setIndex(int index) {
		this.index = index;
	}
	
	
}
