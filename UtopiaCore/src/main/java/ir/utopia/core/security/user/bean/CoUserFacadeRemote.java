package ir.utopia.core.security.user.bean;

import ir.utopia.core.bean.ActionParameterTypes;
import ir.utopia.core.bean.UtopiaBasicUsecaseBean;
import ir.utopia.core.constants.Constants.DisplayTypes;
import ir.utopia.core.persistent.annotation.LookupConfiguration;
import ir.utopia.core.process.annotation.Process;
import ir.utopia.core.process.annotation.ProcessParameter;
import ir.utopia.core.process.annotation.ProcessUIConfiguration;
import ir.utopia.core.process.annotation.Processes;
import ir.utopia.core.security.user.persistence.CoUser;
import ir.utopia.core.security.user.persistence.CoVUser;
import ir.utopia.core.security.userorganizationaccess.persistent.CoVValidOrgAccss;

import java.util.List;

import javax.ejb.Remote;

/**
 * Remote interface for CoUserFacade.
 * 
 * @author 
 */
@Processes(processList={@Process(name=CoUserFacadeRemote.CHANGE_PASSWORD_METHODNAME,
		parameters={
			@ProcessParameter(name="currentPassword",type=ActionParameterTypes.processParameter,paramDisplayType=DisplayTypes.password, displayIndex=1,index=1),
			@ProcessParameter(name="newPassword",type=ActionParameterTypes.processParameter,paramDisplayType=DisplayTypes.password,displayIndex=2,index=2,shouldConfirm=true)
			}
		,UIConfiguration=@ProcessUIConfiguration(notifyForExecutionSuccess=true)),
		@Process(name=CoUserFacadeRemote.CHANGE_ORGANIZATION_METHODNAME,
		parameters={
			@ProcessParameter(name="showAllOrganization",type=ActionParameterTypes.processParameter,paramDisplayType=DisplayTypes.checkBox,defaultValue="False",
						displayIndex=1,index=1,isMandatory=false		)	
			,@ProcessParameter(name="organizationId",type=ActionParameterTypes.processParameter,paramDisplayType=DisplayTypes.lookup,
					lookupConfiguration=@LookupConfiguration(persistentClass=CoVValidOrgAccss.class,displayColumns={"name"},primaryKeyColumnName="id.cmOrganizationId",condition=" CoVValidOrgAccss.id.coUserId=@userId@ "),
					displayIndex=2,index=2,readOnlyLogic="@showAllOrganization@=true")
			,@ProcessParameter(name="setAsDefaultOrganization",type=ActionParameterTypes.processParameter,paramDisplayType=DisplayTypes.checkBox,defaultValue="True",
					displayIndex=3,index=3,readOnlyLogic="@showAllOrganization@=true",isMandatory=false)
			}
		,UIConfiguration=@ProcessUIConfiguration(notifyForExecutionSuccess=false,refreshPageAfterProcess=true))
		})
@Remote
public interface CoUserFacadeRemote extends UtopiaBasicUsecaseBean<CoUser,CoVUser> {
	
	public static final String CHANGE_PASSWORD_METHODNAME="changePassword";
	public static final String CHANGE_ORGANIZATION_METHODNAME="changeOrganization";
	public static final String USER_MANAGMENT_USECASE_NAME="User";
	public CoUser findByUsername(Object username
			);

	public List<CoUser> findByUserImage(Object userImage,
			int... rowStartIdxAndCount);

	public List<CoUser> findByCmBpartnerId(Object cmBpartnerId,
			int... rowStartIdxAndCount);

	public CoVUser getById(Long id);
	
	public void setDefaultOrganization(Long userId,Long organizationId);
}