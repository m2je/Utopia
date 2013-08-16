package ir.utopia.core.importer.setting.persistent;


import ir.utopia.core.persistent.annotation.LookupConfiguration;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
/**
 * CoImporterSetting entity.
 * 
 * @author Golnari@gmail
 */
@LookupConfiguration(displayColumns="name")
@Entity
@Table(name = "CO_IMPORTER_SETTING",uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" }) })
@TableGenerator(name = "ImporterSettingSequenceGenerator", 
		table = "CO_SEQUENCE", pkColumnName = "TABLENAME", valueColumnName = "CURRENTID", pkColumnValue = "CO_IMPORTER_SETTING")
public class CoImporterSetting extends AbstractCoImporterSetting implements java.io.Serializable , Comparable<Object>{


	/**
	 * 
	 */
	private static final long serialVersionUID = 369066546707811006L;

	/** default constructor */
	public CoImporterSetting() {
	}

	@Override
	public int compareTo(Object s) {
		if(this == null)
			return -1;
		if(s == null)
			return 1;
		return this.getName().compareToIgnoreCase(((CoImporterSetting)s).getName()); 
	}

}
