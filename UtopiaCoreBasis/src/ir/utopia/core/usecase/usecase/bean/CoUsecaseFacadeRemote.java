package ir.utopia.core.usecase.usecase.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.usecase.usecase.persistence.CoUsecase;
import ir.utopia.core.usecase.usecase.persistence.CoVUsecase;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CoUsecaseFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CoUsecaseFacadeRemote extends UtopiaBasicUsecaseBean<CoUsecase,CoVUsecase> {

	

	

	public List<CoUsecase> findByName(Object name, int... rowStartIdxAndCount);
	
	public CoUsecase findByUscsRemoteClass(String uscsRemoteClass);

	public CoUsecase loadUsecase(String uscsRemoteClass);
	
	public List<CoUsecase> loadUsecases(List<String> uscsRemoteClasses);
	
	/**
	 * find useCase related to system and subSystem with following abbreviations with its actions related properties
	 * @param uscaseName
	 * @param systemAbrivation
	 * @param subSystemAbrivation
	 * @return
	 */
	public CoUsecase loadUsecase(String uscaseName,String systemAbbreviation, String subSystemAbbreviation);
	/**
	 * 
	 * @param usecaseId
	 * @return
	 */
	public CoUsecase loadUsecase(Long usecaseId);
}