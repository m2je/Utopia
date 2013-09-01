package ir.utopia.core.security.userusecaseacss.bean;

import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.security.userusecaseacss.persistent.CoUserUsecaseAcss;
import ir.utopia.core.security.userusecaseacss.persistent.CoVUserUsecaseAcss;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CoUserUsecaseAcssFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CoUserUsecaseAcssFacadeRemote extends UtopiaBasicUsecaseBean<CoUserUsecaseAcss,CoVUserUsecaseAcss> {

	public List<CoUserUsecaseAcss> findByCoUsecaseId(Object coUsecaseId,
			int... rowStartIdxAndCount);
}