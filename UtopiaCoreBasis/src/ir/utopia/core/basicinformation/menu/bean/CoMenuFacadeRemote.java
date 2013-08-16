package ir.utopia.core.basicinformation.menu.bean;

import ir.utopia.core.basicinformation.menu.persistent.CoMenu;
import ir.utopia.core.basicinformation.menu.persistent.CoVMenu;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CoMenuFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CoMenuFacadeRemote extends UtopiaBasicUsecaseBean<CoMenu,CoVMenu>{
	
	
	
	public List<CoMenu> findByName(Object name, int... rowStartIdxAndCount);

}