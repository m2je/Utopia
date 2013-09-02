package ir.utopia.core.util.tags.datainputform.converter;

import ir.utopia.core.constants.Constants;
import ir.utopia.core.constants.Constants.predefindedActions;
import ir.utopia.core.persistent.UtopiaBasicPersistent;
import ir.utopia.core.persistent.lookup.model.NamePair;
import ir.utopia.core.struts.UtopiaBasicForm;
import ir.utopia.core.struts.UtopiaFormMetaData;
import ir.utopia.core.struts.UtopiaFormMethodMetaData;
import ir.utopia.core.usecase.actionmodel.UseCase;
import ir.utopia.core.util.tags.dashboard.client.model.UsecaseSearchCriteria;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageData;
import ir.utopia.core.util.tags.datainputform.client.grid.model.SearchPageModel;
import ir.utopia.core.util.tags.datainputform.client.model.AttachmentInfo;
import ir.utopia.core.util.tags.datainputform.client.model.DataInputDataModel;
import ir.utopia.core.util.tags.datainputform.client.model.DataInputFormModel;
import ir.utopia.core.util.tags.datainputform.client.model.InputItem;
import ir.utopia.core.util.tags.datainputform.client.model.UILookupInfo;
import ir.utopia.core.util.tags.datainputform.client.tree.model.TreeNode;
import ir.utopia.core.util.tags.datainputform.server.ReportModel;
import ir.utopia.core.util.tags.model.ViewPageModel;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.security.auth.Subject;

public interface UtopiaUIHandler {
	/**
	 * 
	 * @param meta
	 */
	public void setMetaData(UtopiaFormMetaData meta);
	/**
	 * 
	 * @param language
	 * @return
	 */
	public InputItem[] getInputItems(UseCase usecase,predefindedActions action,String language,boolean loadLookups);
	/**
	 * 
	 * @return
	 */
	public SearchPageModel getSearchPageModel(String language,UseCase usecase,Subject user);
	/**
	 * 
	 * @param language
	 * @param searchConditions
	 * @return
	 */
	public SearchPageData getSearchResult(String []searchConditions,String []conditionValues,String defaultCondition,String []orderby,int fromIndex,int fetchSize,String language);
	/**
	 * 
	 * @param searchConditions
	 * @param conditionValues
	 * @param andOr
	 * @param defaultCondition
	 * @param orderby
	 * @param fromIndex
	 * @param fetchSize
	 * @param language
	 * @return
	 */
	public SearchPageData getSearchResult(String []searchConditions,String []conditionValues,boolean []andOr,String defaultCondition,String []orderby,int fromIndex,int fetchSize,String language);
	/**
	 * 
	 * @param searchConditions
	 * @param language
	 * @return
	 */
	public int getResultCount(String []searchConditions,String []conditionValues,String language);
	/**
	 * 
	 * @param searchConditions
	 * @param conditionValues
	 * @param defaultConditionStr
	 * @param language
	 * @return
	 */
	public int getResultCount(String []searchConditions,String []conditionValues,String defaultConditionStr,
			 String language) ;
	/**
	 * 
	 * @param searchConditions
	 * @param conditionValues
	 * @param andOr
	 * @param defaultConditionStr
	 * @param language
	 * @return
	 */
	public int getResultCount(String []searchConditions,String []conditionValues,boolean []andOr,String defaultConditionStr,
			 String language) ;
	/**
	 * 
	 * @param item
	 * @param source
	 * @return
	 */
	public Object getValueOf(InputItem item,UtopiaBasicForm<?> source);
	/**
	 * 
	 * @param item
	 * @param source
	 * @return
	 */
	public Object getDefaultValueOf(InputItem item,UtopiaBasicForm<?> source,predefindedActions action);
	/**
	 * 
	 * @param meta
	 * @param source
	 * @return
	 */
	public Object getDefaultValueOf(UtopiaFormMethodMetaData meta,UtopiaBasicForm<?> source,predefindedActions action);
	/**
	 * 
	 * @param recordId
	 * @param action
	 * @return
	 */
	public UtopiaBasicForm<?> LoadForm(Long recordId,predefindedActions action);
	/**
	 * 
	 * @param formClass
	 * @param usecase
	 * @param actionName
	 * @param language
	 * @return
	 */
	public DataInputFormModel getDataInputFormModel(UseCase usecase,predefindedActions action,String language,boolean loadLookups);
	/**
	 * 
	 * @param usecase
	 * @param actionName
	 * @param currentData
	 * @param recordId
	 * @param language
	 * @param reload
	 * @return
	 */
	public DataInputDataModel getFormData(UseCase usecase,predefindedActions action,UtopiaBasicForm<?> currentData,Long recordId,String language,boolean reload) ;
	/**
	 * 
	 * @param action
	 * @param usecase
	 * @return
	 */
	public String getActionUrl(Constants.predefindedActions action, String usecaseName);
	/**
	 * 
	 * @param model
	 * @return
	 */
	public int getReportRecordCount(ReportModel model);
	/**
	 * 
	 * @param recordIds
	 * @return
	 */
	public Vector<Map<String,Object>> getReportData(ReportModel model,int from,int to);
	/**
	 * 
	 * @param mMeta
	 * @param language
	 * @return
	 */
	public List<NamePair> LoadLookupNamePairs(UtopiaFormMethodMetaData mMeta,Map<String,Object>context,String language);
	/**
	 * 
	 * @param language
	 * @param usecase
	 * @param user
	 * @return
	 */
	public ViewPageModel getViewPageModel(String language,UseCase usecase,Subject user);
	/**
	 * 
	 * @return
	 */
	public UtopiaBasicForm<? extends UtopiaBasicPersistent> getViewPageData(Long recordId);
	/**
	 * 
	 * @param recordId
	 * @param usecase
	 * @return
	 */
	public abstract List<AttachmentInfo> getAttachments(Long recordId, UseCase usecase);
	/**
	 * 
	 * @param usecase
	 * @param parentId
	 * @return
	 */
	public abstract TreeNode[] loadUsecaseTreeNodes(UseCase usecase, Long parentId
			);
	/**
	 * 
	 * @param fieldName
	 * @param dependentFields
	 * @param dependesValue
	 * @return
	 */
	public UILookupInfo reloadLookup(String fieldName,
		String[] dependentFields, String[] dependesValue);
	/**
	 * 
	 * @param input
	 * @param item
	 * @param gridColName
	 * @return
	 */
	public String loadLOVValue(Object input,String name,String gridColName);
	/**
	 * 
	 * @param criteria
	 * @return
	 */
	public SearchPageData getDashBoardRecords(
			UsecaseSearchCriteria criteria) ;
}
