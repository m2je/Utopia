package ir.utopia.core.basicinformation.menu.bean;

import ir.utopia.core.basicinformation.menu.persistent.CoMenu;
import ir.utopia.core.basicinformation.menu.persistent.CoVMenu;
import ir.utopia.core.bean.AbstractBasicUsecaseBean;

import java.util.List;

import javax.ejb.Stateless;

/**
 * Facade for entity CoMenu.
 * 
 * 
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CoMenuFacade extends AbstractBasicUsecaseBean<CoMenu,CoVMenu> implements CoMenuFacadeRemote  {
	// property constants
	public static final String NAME = "name";

	

	public List<CoMenu> findByName(Object name, int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}
	
	

}