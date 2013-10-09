package com.arcusys.learn.persistence.liferay.service.persistence;

import com.arcusys.learn.persistence.liferay.NoSuchLFGlobalObjectiveStateException;
import com.arcusys.learn.persistence.liferay.model.LFGlobalObjectiveState;
import com.arcusys.learn.persistence.liferay.model.impl.LFGlobalObjectiveStateImpl;
import com.arcusys.learn.persistence.liferay.model.impl.LFGlobalObjectiveStateModelImpl;
import com.arcusys.learn.persistence.liferay.service.persistence.LFActivityDataMapPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFActivityPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFActivityStateNodePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFActivityStatePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFActivityStateTreePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFAnswerPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFAttemptDataPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFAttemptPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFBigDecimalPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFCertificatePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFCertificateSitePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFCertificateUserPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFChildrenSelectionPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFConditionRulePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFCoursePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFFileStoragePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFGlobalObjectiveStatePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFObjectiveMapPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFObjectivePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFObjectiveStatePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFPackageCommentPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFPackagePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFPackageScopeRulePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFPackageVotePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFPlayerScopeRulePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFQuestionCategoryPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFQuestionPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFQuizPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFQuizQuestionCategoryPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFQuizQuestionPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFResourcePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFRolePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFRollupContributionPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFRollupRulePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFRuleConditionPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFSequencingPermissionsPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFSequencingPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFSequencingTrackingPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFSettingPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFSocialPackagePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFSocialPackageTagPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFTincanActivityPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFTincanLrsEndpointPersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFTincanPackagePersistence;
import com.arcusys.learn.persistence.liferay.service.persistence.LFUserPersistence;

import com.liferay.portal.NoSuchModelException;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.BatchSessionUtil;
import com.liferay.portal.service.persistence.ResourcePersistence;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The persistence implementation for the l f global objective state service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see LFGlobalObjectiveStatePersistence
 * @see LFGlobalObjectiveStateUtil
 * @generated
 */
public class LFGlobalObjectiveStatePersistenceImpl extends BasePersistenceImpl<LFGlobalObjectiveState>
    implements LFGlobalObjectiveStatePersistence {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never modify or reference this class directly. Always use {@link LFGlobalObjectiveStateUtil} to access the l f global objective state persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
     */
    public static final String FINDER_CLASS_NAME_ENTITY = LFGlobalObjectiveStateImpl.class.getName();
    public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
        ".List1";
    public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
        ".List2";
    public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_TREEID = new FinderPath(LFGlobalObjectiveStateModelImpl.ENTITY_CACHE_ENABLED,
            LFGlobalObjectiveStateModelImpl.FINDER_CACHE_ENABLED,
            LFGlobalObjectiveStateImpl.class,
            FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByTreeID",
            new String[] {
                Integer.class.getName(),
                
            "java.lang.Integer", "java.lang.Integer",
                "com.liferay.portal.kernel.util.OrderByComparator"
            });
    public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TREEID =
        new FinderPath(LFGlobalObjectiveStateModelImpl.ENTITY_CACHE_ENABLED,
            LFGlobalObjectiveStateModelImpl.FINDER_CACHE_ENABLED,
            LFGlobalObjectiveStateImpl.class,
            FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByTreeID",
            new String[] { Integer.class.getName() },
            LFGlobalObjectiveStateModelImpl.TREEID_COLUMN_BITMASK);
    public static final FinderPath FINDER_PATH_COUNT_BY_TREEID = new FinderPath(LFGlobalObjectiveStateModelImpl.ENTITY_CACHE_ENABLED,
            LFGlobalObjectiveStateModelImpl.FINDER_CACHE_ENABLED, Long.class,
            FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByTreeID",
            new String[] { Integer.class.getName() });
    public static final FinderPath FINDER_PATH_FETCH_BY_TREEIDANDMAPKEY = new FinderPath(LFGlobalObjectiveStateModelImpl.ENTITY_CACHE_ENABLED,
            LFGlobalObjectiveStateModelImpl.FINDER_CACHE_ENABLED,
            LFGlobalObjectiveStateImpl.class, FINDER_CLASS_NAME_ENTITY,
            "fetchByTreeIDAndMapKey",
            new String[] { Integer.class.getName(), String.class.getName() },
            LFGlobalObjectiveStateModelImpl.TREEID_COLUMN_BITMASK |
            LFGlobalObjectiveStateModelImpl.MAPKEY_COLUMN_BITMASK);
    public static final FinderPath FINDER_PATH_COUNT_BY_TREEIDANDMAPKEY = new FinderPath(LFGlobalObjectiveStateModelImpl.ENTITY_CACHE_ENABLED,
            LFGlobalObjectiveStateModelImpl.FINDER_CACHE_ENABLED, Long.class,
            FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
            "countByTreeIDAndMapKey",
            new String[] { Integer.class.getName(), String.class.getName() });
    public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(LFGlobalObjectiveStateModelImpl.ENTITY_CACHE_ENABLED,
            LFGlobalObjectiveStateModelImpl.FINDER_CACHE_ENABLED,
            LFGlobalObjectiveStateImpl.class,
            FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
    public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(LFGlobalObjectiveStateModelImpl.ENTITY_CACHE_ENABLED,
            LFGlobalObjectiveStateModelImpl.FINDER_CACHE_ENABLED,
            LFGlobalObjectiveStateImpl.class,
            FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
    public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(LFGlobalObjectiveStateModelImpl.ENTITY_CACHE_ENABLED,
            LFGlobalObjectiveStateModelImpl.FINDER_CACHE_ENABLED, Long.class,
            FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
    private static final String _SQL_SELECT_LFGLOBALOBJECTIVESTATE = "SELECT lfGlobalObjectiveState FROM LFGlobalObjectiveState lfGlobalObjectiveState";
    private static final String _SQL_SELECT_LFGLOBALOBJECTIVESTATE_WHERE = "SELECT lfGlobalObjectiveState FROM LFGlobalObjectiveState lfGlobalObjectiveState WHERE ";
    private static final String _SQL_COUNT_LFGLOBALOBJECTIVESTATE = "SELECT COUNT(lfGlobalObjectiveState) FROM LFGlobalObjectiveState lfGlobalObjectiveState";
    private static final String _SQL_COUNT_LFGLOBALOBJECTIVESTATE_WHERE = "SELECT COUNT(lfGlobalObjectiveState) FROM LFGlobalObjectiveState lfGlobalObjectiveState WHERE ";
    private static final String _FINDER_COLUMN_TREEID_TREEID_NULL = "lfGlobalObjectiveState.treeID IS NULL";
    private static final String _FINDER_COLUMN_TREEID_TREEID_NULL_2 = "lfGlobalObjectiveState.treeID IS NULL ";
    private static final String _FINDER_COLUMN_TREEID_TREEID_2 = "lfGlobalObjectiveState.treeID = ?";
    private static final String _FINDER_COLUMN_TREEIDANDMAPKEY_TREEID_NULL = "lfGlobalObjectiveState.treeID IS NULL";
    private static final String _FINDER_COLUMN_TREEIDANDMAPKEY_TREEID_NULL_2 = "lfGlobalObjectiveState.treeID IS NULL  AND ";
    private static final String _FINDER_COLUMN_TREEIDANDMAPKEY_TREEID_2 = "lfGlobalObjectiveState.treeID = ? AND ";
    private static final String _FINDER_COLUMN_TREEIDANDMAPKEY_MAPKEY_1 = "lfGlobalObjectiveState.mapKey IS NULL";
    private static final String _FINDER_COLUMN_TREEIDANDMAPKEY_MAPKEY_NULL = "lfGlobalObjectiveState.mapKey IS NULL";
    private static final String _FINDER_COLUMN_TREEIDANDMAPKEY_MAPKEY_NULL_2 = "lfGlobalObjectiveState.mapKey IS NULL ";
    private static final String _FINDER_COLUMN_TREEIDANDMAPKEY_MAPKEY_2 = "lfGlobalObjectiveState.mapKey = ?";
    private static final String _FINDER_COLUMN_TREEIDANDMAPKEY_MAPKEY_3 = "(lfGlobalObjectiveState.mapKey IS NULL OR lfGlobalObjectiveState.mapKey = ?)";
    private static final String _ORDER_BY_ENTITY_ALIAS = "lfGlobalObjectiveState.";
    private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No LFGlobalObjectiveState exists with the primary key ";
    private static final String _NO_SUCH_ENTITY_WITH_KEY = "No LFGlobalObjectiveState exists with the key {";
    private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
                PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
    private static Log _log = LogFactoryUtil.getLog(LFGlobalObjectiveStatePersistenceImpl.class);
    private static LFGlobalObjectiveState _nullLFGlobalObjectiveState = new LFGlobalObjectiveStateImpl() {
            @Override
            public Object clone() {
                return this;
            }

            @Override
            public CacheModel<LFGlobalObjectiveState> toCacheModel() {
                return _nullLFGlobalObjectiveStateCacheModel;
            }
        };

    private static CacheModel<LFGlobalObjectiveState> _nullLFGlobalObjectiveStateCacheModel =
        new CacheModel<LFGlobalObjectiveState>() {
            public LFGlobalObjectiveState toEntityModel() {
                return _nullLFGlobalObjectiveState;
            }
        };

    @BeanReference(type = LFActivityPersistence.class)
    protected LFActivityPersistence lfActivityPersistence;
    @BeanReference(type = LFActivityDataMapPersistence.class)
    protected LFActivityDataMapPersistence lfActivityDataMapPersistence;
    @BeanReference(type = LFActivityStatePersistence.class)
    protected LFActivityStatePersistence lfActivityStatePersistence;
    @BeanReference(type = LFActivityStateNodePersistence.class)
    protected LFActivityStateNodePersistence lfActivityStateNodePersistence;
    @BeanReference(type = LFActivityStateTreePersistence.class)
    protected LFActivityStateTreePersistence lfActivityStateTreePersistence;
    @BeanReference(type = LFAnswerPersistence.class)
    protected LFAnswerPersistence lfAnswerPersistence;
    @BeanReference(type = LFAttemptPersistence.class)
    protected LFAttemptPersistence lfAttemptPersistence;
    @BeanReference(type = LFAttemptDataPersistence.class)
    protected LFAttemptDataPersistence lfAttemptDataPersistence;
    @BeanReference(type = LFBigDecimalPersistence.class)
    protected LFBigDecimalPersistence lfBigDecimalPersistence;
    @BeanReference(type = LFCertificatePersistence.class)
    protected LFCertificatePersistence lfCertificatePersistence;
    @BeanReference(type = LFCertificateSitePersistence.class)
    protected LFCertificateSitePersistence lfCertificateSitePersistence;
    @BeanReference(type = LFCertificateUserPersistence.class)
    protected LFCertificateUserPersistence lfCertificateUserPersistence;
    @BeanReference(type = LFChildrenSelectionPersistence.class)
    protected LFChildrenSelectionPersistence lfChildrenSelectionPersistence;
    @BeanReference(type = LFConditionRulePersistence.class)
    protected LFConditionRulePersistence lfConditionRulePersistence;
    @BeanReference(type = LFCoursePersistence.class)
    protected LFCoursePersistence lfCoursePersistence;
    @BeanReference(type = LFFileStoragePersistence.class)
    protected LFFileStoragePersistence lfFileStoragePersistence;
    @BeanReference(type = LFGlobalObjectiveStatePersistence.class)
    protected LFGlobalObjectiveStatePersistence lfGlobalObjectiveStatePersistence;
    @BeanReference(type = LFObjectivePersistence.class)
    protected LFObjectivePersistence lfObjectivePersistence;
    @BeanReference(type = LFObjectiveMapPersistence.class)
    protected LFObjectiveMapPersistence lfObjectiveMapPersistence;
    @BeanReference(type = LFObjectiveStatePersistence.class)
    protected LFObjectiveStatePersistence lfObjectiveStatePersistence;
    @BeanReference(type = LFPackagePersistence.class)
    protected LFPackagePersistence lfPackagePersistence;
    @BeanReference(type = LFPackageCommentPersistence.class)
    protected LFPackageCommentPersistence lfPackageCommentPersistence;
    @BeanReference(type = LFPackageScopeRulePersistence.class)
    protected LFPackageScopeRulePersistence lfPackageScopeRulePersistence;
    @BeanReference(type = LFPackageVotePersistence.class)
    protected LFPackageVotePersistence lfPackageVotePersistence;
    @BeanReference(type = LFPlayerScopeRulePersistence.class)
    protected LFPlayerScopeRulePersistence lfPlayerScopeRulePersistence;
    @BeanReference(type = LFQuestionPersistence.class)
    protected LFQuestionPersistence lfQuestionPersistence;
    @BeanReference(type = LFQuestionCategoryPersistence.class)
    protected LFQuestionCategoryPersistence lfQuestionCategoryPersistence;
    @BeanReference(type = LFQuizPersistence.class)
    protected LFQuizPersistence lfQuizPersistence;
    @BeanReference(type = LFQuizQuestionPersistence.class)
    protected LFQuizQuestionPersistence lfQuizQuestionPersistence;
    @BeanReference(type = LFQuizQuestionCategoryPersistence.class)
    protected LFQuizQuestionCategoryPersistence lfQuizQuestionCategoryPersistence;
    @BeanReference(type = LFResourcePersistence.class)
    protected LFResourcePersistence lfResourcePersistence;
    @BeanReference(type = LFRolePersistence.class)
    protected LFRolePersistence lfRolePersistence;
    @BeanReference(type = LFRollupContributionPersistence.class)
    protected LFRollupContributionPersistence lfRollupContributionPersistence;
    @BeanReference(type = LFRollupRulePersistence.class)
    protected LFRollupRulePersistence lfRollupRulePersistence;
    @BeanReference(type = LFRuleConditionPersistence.class)
    protected LFRuleConditionPersistence lfRuleConditionPersistence;
    @BeanReference(type = LFSequencingPersistence.class)
    protected LFSequencingPersistence lfSequencingPersistence;
    @BeanReference(type = LFSequencingPermissionsPersistence.class)
    protected LFSequencingPermissionsPersistence lfSequencingPermissionsPersistence;
    @BeanReference(type = LFSequencingTrackingPersistence.class)
    protected LFSequencingTrackingPersistence lfSequencingTrackingPersistence;
    @BeanReference(type = LFSettingPersistence.class)
    protected LFSettingPersistence lfSettingPersistence;
    @BeanReference(type = LFSocialPackagePersistence.class)
    protected LFSocialPackagePersistence lfSocialPackagePersistence;
    @BeanReference(type = LFSocialPackageTagPersistence.class)
    protected LFSocialPackageTagPersistence lfSocialPackageTagPersistence;
    @BeanReference(type = LFTincanActivityPersistence.class)
    protected LFTincanActivityPersistence lfTincanActivityPersistence;
    @BeanReference(type = LFTincanLrsEndpointPersistence.class)
    protected LFTincanLrsEndpointPersistence lfTincanLrsEndpointPersistence;
    @BeanReference(type = LFTincanPackagePersistence.class)
    protected LFTincanPackagePersistence lfTincanPackagePersistence;
    @BeanReference(type = LFUserPersistence.class)
    protected LFUserPersistence lfUserPersistence;
    @BeanReference(type = ResourcePersistence.class)
    protected ResourcePersistence resourcePersistence;
    @BeanReference(type = UserPersistence.class)
    protected UserPersistence userPersistence;

    /**
     * Caches the l f global objective state in the entity cache if it is enabled.
     *
     * @param lfGlobalObjectiveState the l f global objective state
     */
    public void cacheResult(LFGlobalObjectiveState lfGlobalObjectiveState) {
        EntityCacheUtil.putResult(LFGlobalObjectiveStateModelImpl.ENTITY_CACHE_ENABLED,
            LFGlobalObjectiveStateImpl.class,
            lfGlobalObjectiveState.getPrimaryKey(), lfGlobalObjectiveState);

        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_TREEIDANDMAPKEY,
            new Object[] {
                Integer.valueOf(lfGlobalObjectiveState.getTreeID()),
                
            lfGlobalObjectiveState.getMapKey()
            }, lfGlobalObjectiveState);

        lfGlobalObjectiveState.resetOriginalValues();
    }

    /**
     * Caches the l f global objective states in the entity cache if it is enabled.
     *
     * @param lfGlobalObjectiveStates the l f global objective states
     */
    public void cacheResult(
        List<LFGlobalObjectiveState> lfGlobalObjectiveStates) {
        for (LFGlobalObjectiveState lfGlobalObjectiveState : lfGlobalObjectiveStates) {
            if (EntityCacheUtil.getResult(
                        LFGlobalObjectiveStateModelImpl.ENTITY_CACHE_ENABLED,
                        LFGlobalObjectiveStateImpl.class,
                        lfGlobalObjectiveState.getPrimaryKey()) == null) {
                cacheResult(lfGlobalObjectiveState);
            } else {
                lfGlobalObjectiveState.resetOriginalValues();
            }
        }
    }

    /**
     * Clears the cache for all l f global objective states.
     *
     * <p>
     * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
     * </p>
     */
    @Override
    public void clearCache() {
        if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
            CacheRegistryUtil.clear(LFGlobalObjectiveStateImpl.class.getName());
        }

        EntityCacheUtil.clearCache(LFGlobalObjectiveStateImpl.class.getName());

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
    }

    /**
     * Clears the cache for the l f global objective state.
     *
     * <p>
     * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
     * </p>
     */
    @Override
    public void clearCache(LFGlobalObjectiveState lfGlobalObjectiveState) {
        EntityCacheUtil.removeResult(LFGlobalObjectiveStateModelImpl.ENTITY_CACHE_ENABLED,
            LFGlobalObjectiveStateImpl.class,
            lfGlobalObjectiveState.getPrimaryKey());

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

        clearUniqueFindersCache(lfGlobalObjectiveState);
    }

    @Override
    public void clearCache(List<LFGlobalObjectiveState> lfGlobalObjectiveStates) {
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

        for (LFGlobalObjectiveState lfGlobalObjectiveState : lfGlobalObjectiveStates) {
            EntityCacheUtil.removeResult(LFGlobalObjectiveStateModelImpl.ENTITY_CACHE_ENABLED,
                LFGlobalObjectiveStateImpl.class,
                lfGlobalObjectiveState.getPrimaryKey());

            clearUniqueFindersCache(lfGlobalObjectiveState);
        }
    }

    protected void clearUniqueFindersCache(
        LFGlobalObjectiveState lfGlobalObjectiveState) {
        FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_TREEIDANDMAPKEY,
            new Object[] {
                Integer.valueOf(lfGlobalObjectiveState.getTreeID()),
                
            lfGlobalObjectiveState.getMapKey()
            });
    }

    /**
     * Creates a new l f global objective state with the primary key. Does not add the l f global objective state to the database.
     *
     * @param id the primary key for the new l f global objective state
     * @return the new l f global objective state
     */
    public LFGlobalObjectiveState create(long id) {
        LFGlobalObjectiveState lfGlobalObjectiveState = new LFGlobalObjectiveStateImpl();

        lfGlobalObjectiveState.setNew(true);
        lfGlobalObjectiveState.setPrimaryKey(id);

        return lfGlobalObjectiveState;
    }

    /**
     * Removes the l f global objective state with the primary key from the database. Also notifies the appropriate model listeners.
     *
     * @param id the primary key of the l f global objective state
     * @return the l f global objective state that was removed
     * @throws com.arcusys.learn.persistence.liferay.NoSuchLFGlobalObjectiveStateException if a l f global objective state with the primary key could not be found
     * @throws SystemException if a system exception occurred
     */
    public LFGlobalObjectiveState remove(long id)
        throws NoSuchLFGlobalObjectiveStateException, SystemException {
        return remove(Long.valueOf(id));
    }

    /**
     * Removes the l f global objective state with the primary key from the database. Also notifies the appropriate model listeners.
     *
     * @param primaryKey the primary key of the l f global objective state
     * @return the l f global objective state that was removed
     * @throws com.arcusys.learn.persistence.liferay.NoSuchLFGlobalObjectiveStateException if a l f global objective state with the primary key could not be found
     * @throws SystemException if a system exception occurred
     */
    @Override
    public LFGlobalObjectiveState remove(Serializable primaryKey)
        throws NoSuchLFGlobalObjectiveStateException, SystemException {
        Session session = null;

        try {
            session = openSession();

            LFGlobalObjectiveState lfGlobalObjectiveState = (LFGlobalObjectiveState) session.get(LFGlobalObjectiveStateImpl.class,
                    primaryKey);

            if (lfGlobalObjectiveState == null) {
                if (_log.isWarnEnabled()) {
                    _log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
                }

                throw new NoSuchLFGlobalObjectiveStateException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
                    primaryKey);
            }

            return remove(lfGlobalObjectiveState);
        } catch (NoSuchLFGlobalObjectiveStateException nsee) {
            throw nsee;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    @Override
    protected LFGlobalObjectiveState removeImpl(
        LFGlobalObjectiveState lfGlobalObjectiveState)
        throws SystemException {
        lfGlobalObjectiveState = toUnwrappedModel(lfGlobalObjectiveState);

        Session session = null;

        try {
            session = openSession();

            BatchSessionUtil.delete(session, lfGlobalObjectiveState);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        clearCache(lfGlobalObjectiveState);

        return lfGlobalObjectiveState;
    }

    @Override
    public LFGlobalObjectiveState updateImpl(
        com.arcusys.learn.persistence.liferay.model.LFGlobalObjectiveState lfGlobalObjectiveState,
        boolean merge) throws SystemException {
        lfGlobalObjectiveState = toUnwrappedModel(lfGlobalObjectiveState);

        boolean isNew = lfGlobalObjectiveState.isNew();

        LFGlobalObjectiveStateModelImpl lfGlobalObjectiveStateModelImpl = (LFGlobalObjectiveStateModelImpl) lfGlobalObjectiveState;

        Session session = null;

        try {
            session = openSession();

            BatchSessionUtil.update(session, lfGlobalObjectiveState, merge);

            lfGlobalObjectiveState.setNew(false);
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }

        FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

        if (isNew || !LFGlobalObjectiveStateModelImpl.COLUMN_BITMASK_ENABLED) {
            FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
        }
        else {
            if ((lfGlobalObjectiveStateModelImpl.getColumnBitmask() &
                    FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TREEID.getColumnBitmask()) != 0) {
                Object[] args = new Object[] {
                        /* Integer.valueOf(   */
                        lfGlobalObjectiveStateModelImpl.getOriginalTreeID()
                    /* ) */
                    };

                FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_TREEID, args);
                FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TREEID,
                    args);

                args = new Object[] { /* Integer.valueOf( */
                        lfGlobalObjectiveStateModelImpl.getTreeID()/* ) */
                    };

                FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_TREEID, args);
                FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TREEID,
                    args);
            }
        }

        EntityCacheUtil.putResult(LFGlobalObjectiveStateModelImpl.ENTITY_CACHE_ENABLED,
            LFGlobalObjectiveStateImpl.class,
            lfGlobalObjectiveState.getPrimaryKey(), lfGlobalObjectiveState);

        if (isNew) {
            FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_TREEIDANDMAPKEY,
                new Object[] {
                    /*Integer.valueOf( */
                lfGlobalObjectiveState.getTreeID(),
                    
                lfGlobalObjectiveState.getMapKey()
                }, lfGlobalObjectiveState);
        } else {
            if ((lfGlobalObjectiveStateModelImpl.getColumnBitmask() &
                    FINDER_PATH_FETCH_BY_TREEIDANDMAPKEY.getColumnBitmask()) != 0) {
                Object[] args = new Object[] {
                        /*        Integer.valueOf( */
                        lfGlobalObjectiveStateModelImpl.getOriginalTreeID(),
                        
                        lfGlobalObjectiveStateModelImpl.getOriginalMapKey()
                    };

                FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_TREEIDANDMAPKEY,
                    args);

                FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_TREEIDANDMAPKEY,
                    args);

                FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_TREEIDANDMAPKEY,
                    new Object[] {
                        /*        Integer.valueOf( */
                    lfGlobalObjectiveState.getTreeID(),
                        
                    lfGlobalObjectiveState.getMapKey()
                    }, lfGlobalObjectiveState);
            }
        }

        return lfGlobalObjectiveState;
    }

    protected LFGlobalObjectiveState toUnwrappedModel(
        LFGlobalObjectiveState lfGlobalObjectiveState) {
        if (lfGlobalObjectiveState instanceof LFGlobalObjectiveStateImpl) {
            return lfGlobalObjectiveState;
        }

        LFGlobalObjectiveStateImpl lfGlobalObjectiveStateImpl = new LFGlobalObjectiveStateImpl();

        lfGlobalObjectiveStateImpl.setNew(lfGlobalObjectiveState.isNew());
        lfGlobalObjectiveStateImpl.setPrimaryKey(lfGlobalObjectiveState.getPrimaryKey());

        lfGlobalObjectiveStateImpl.setId(lfGlobalObjectiveState.getId());
        lfGlobalObjectiveStateImpl.setSatisfied(lfGlobalObjectiveState.getSatisfied());
        lfGlobalObjectiveStateImpl.setNormalizedMeasure(lfGlobalObjectiveState.getNormalizedMeasure());
        lfGlobalObjectiveStateImpl.setAttemptCompleted(lfGlobalObjectiveState.getAttemptCompleted());
        lfGlobalObjectiveStateImpl.setMapKey(lfGlobalObjectiveState.getMapKey());
        lfGlobalObjectiveStateImpl.setTreeID(lfGlobalObjectiveState.getTreeID());

        return lfGlobalObjectiveStateImpl;
    }

    /**
     * Returns the l f global objective state with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
     *
     * @param primaryKey the primary key of the l f global objective state
     * @return the l f global objective state
     * @throws com.liferay.portal.NoSuchModelException if a l f global objective state with the primary key could not be found
     * @throws SystemException if a system exception occurred
     */
    @Override
    public LFGlobalObjectiveState findByPrimaryKey(Serializable primaryKey)
        throws NoSuchModelException, SystemException {
        return findByPrimaryKey(((Long) primaryKey).longValue());
    }

    /**
     * Returns the l f global objective state with the primary key or throws a {@link com.arcusys.learn.persistence.liferay.NoSuchLFGlobalObjectiveStateException} if it could not be found.
     *
     * @param id the primary key of the l f global objective state
     * @return the l f global objective state
     * @throws com.arcusys.learn.persistence.liferay.NoSuchLFGlobalObjectiveStateException if a l f global objective state with the primary key could not be found
     * @throws SystemException if a system exception occurred
     */
    public LFGlobalObjectiveState findByPrimaryKey(long id)
        throws NoSuchLFGlobalObjectiveStateException, SystemException {
        LFGlobalObjectiveState lfGlobalObjectiveState = fetchByPrimaryKey(id);

        if (lfGlobalObjectiveState == null) {
            if (_log.isWarnEnabled()) {
                _log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + id);
            }

            throw new NoSuchLFGlobalObjectiveStateException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
                id);
        }

        return lfGlobalObjectiveState;
    }

    /**
     * Returns the l f global objective state with the primary key or returns <code>null</code> if it could not be found.
     *
     * @param primaryKey the primary key of the l f global objective state
     * @return the l f global objective state, or <code>null</code> if a l f global objective state with the primary key could not be found
     * @throws SystemException if a system exception occurred
     */
    @Override
    public LFGlobalObjectiveState fetchByPrimaryKey(Serializable primaryKey)
        throws SystemException {
        return fetchByPrimaryKey(((Long) primaryKey).longValue());
    }

    /**
     * Returns the l f global objective state with the primary key or returns <code>null</code> if it could not be found.
     *
     * @param id the primary key of the l f global objective state
     * @return the l f global objective state, or <code>null</code> if a l f global objective state with the primary key could not be found
     * @throws SystemException if a system exception occurred
     */
    public LFGlobalObjectiveState fetchByPrimaryKey(long id)
        throws SystemException {
        LFGlobalObjectiveState lfGlobalObjectiveState = (LFGlobalObjectiveState) EntityCacheUtil.getResult(LFGlobalObjectiveStateModelImpl.ENTITY_CACHE_ENABLED,
                LFGlobalObjectiveStateImpl.class, id);

        if (lfGlobalObjectiveState == _nullLFGlobalObjectiveState) {
            return null;
        }

        if (lfGlobalObjectiveState == null) {
            Session session = null;

            boolean hasException = false;

            try {
                session = openSession();

                lfGlobalObjectiveState = (LFGlobalObjectiveState) session.get(LFGlobalObjectiveStateImpl.class,
                        Long.valueOf(id));
            } catch (Exception e) {
                hasException = true;

                throw processException(e);
            } finally {
                if (lfGlobalObjectiveState != null) {
                    cacheResult(lfGlobalObjectiveState);
                } else if (!hasException) {
                    EntityCacheUtil.putResult(LFGlobalObjectiveStateModelImpl.ENTITY_CACHE_ENABLED,
                        LFGlobalObjectiveStateImpl.class, id,
                        _nullLFGlobalObjectiveState);
                }

                closeSession(session);
            }
        }

        return lfGlobalObjectiveState;
    }

    /**
     * Returns all the l f global objective states where treeID = &#63;.
     *
     * @param treeID the tree i d
     * @return the matching l f global objective states
     * @throws SystemException if a system exception occurred
     */
    public List<LFGlobalObjectiveState> findByTreeID(Integer treeID)
        throws SystemException {
        return findByTreeID(treeID, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
    }

    /**
     * Returns a range of all the l f global objective states where treeID = &#63;.
     *
     * <p>
     * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
     * </p>
     *
     * @param treeID the tree i d
     * @param start the lower bound of the range of l f global objective states
     * @param end the upper bound of the range of l f global objective states (not inclusive)
     * @return the range of matching l f global objective states
     * @throws SystemException if a system exception occurred
     */
    public List<LFGlobalObjectiveState> findByTreeID(Integer treeID, int start,
        int end) throws SystemException {
        return findByTreeID(treeID, start, end, null);
    }

    /**
     * Returns an ordered range of all the l f global objective states where treeID = &#63;.
     *
     * <p>
     * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
     * </p>
     *
     * @param treeID the tree i d
     * @param start the lower bound of the range of l f global objective states
     * @param end the upper bound of the range of l f global objective states (not inclusive)
     * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
     * @return the ordered range of matching l f global objective states
     * @throws SystemException if a system exception occurred
     */
    public List<LFGlobalObjectiveState> findByTreeID(Integer treeID, int start,
        int end, OrderByComparator orderByComparator) throws SystemException {
        FinderPath finderPath = null;
        Object[] finderArgs = null;

        if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
                (orderByComparator == null)) {
            finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_TREEID;
            finderArgs = new Object[] { treeID };
        } else {
            finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_TREEID;
            finderArgs = new Object[] { treeID, start, end, orderByComparator };
        }

        List<LFGlobalObjectiveState> list = (List<LFGlobalObjectiveState>) FinderCacheUtil.getResult(finderPath,
                finderArgs, this);

        if ((list != null) && !list.isEmpty()) {
            for (LFGlobalObjectiveState lfGlobalObjectiveState : list) {
                if (!Validator.equals(treeID, lfGlobalObjectiveState.getTreeID())) {
                    list = null;

                    break;
                }
            }
        }

        if (list == null) {
            StringBundler query = null;

            if (orderByComparator != null) {
                query = new StringBundler(3 +
                        (orderByComparator.getOrderByFields().length * 3));
            } else {
                query = new StringBundler(2);
            }

            query.append(_SQL_SELECT_LFGLOBALOBJECTIVESTATE_WHERE);

            if (treeID == null) {
                query.append(_FINDER_COLUMN_TREEID_TREEID_NULL_2);
            } else {
                query.append(_FINDER_COLUMN_TREEID_TREEID_2);
            }

            if (orderByComparator != null) {
                appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
                    orderByComparator);
            }

            String sql = query.toString();

            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(sql);

                QueryPos qPos = QueryPos.getInstance(q);

                if (treeID != null) {
                    qPos.add(treeID.intValue());
                }

                list = (List<LFGlobalObjectiveState>) QueryUtil.list(q,
                        getDialect(), start, end);
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    FinderCacheUtil.removeResult(finderPath, finderArgs);
                } else {
                    cacheResult(list);

                    FinderCacheUtil.putResult(finderPath, finderArgs, list);
                }

                closeSession(session);
            }
        }

        return list;
    }

    /**
     * Returns the first l f global objective state in the ordered set where treeID = &#63;.
     *
     * @param treeID the tree i d
     * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
     * @return the first matching l f global objective state
     * @throws com.arcusys.learn.persistence.liferay.NoSuchLFGlobalObjectiveStateException if a matching l f global objective state could not be found
     * @throws SystemException if a system exception occurred
     */
    public LFGlobalObjectiveState findByTreeID_First(Integer treeID,
        OrderByComparator orderByComparator)
        throws NoSuchLFGlobalObjectiveStateException, SystemException {
        LFGlobalObjectiveState lfGlobalObjectiveState = fetchByTreeID_First(treeID,
                orderByComparator);

        if (lfGlobalObjectiveState != null) {
            return lfGlobalObjectiveState;
        }

        StringBundler msg = new StringBundler(4);

        msg.append(_NO_SUCH_ENTITY_WITH_KEY);

        msg.append("treeID=");
        msg.append(treeID);

        msg.append(StringPool.CLOSE_CURLY_BRACE);

        throw new NoSuchLFGlobalObjectiveStateException(msg.toString());
    }

    /**
     * Returns the first l f global objective state in the ordered set where treeID = &#63;.
     *
     * @param treeID the tree i d
     * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
     * @return the first matching l f global objective state, or <code>null</code> if a matching l f global objective state could not be found
     * @throws SystemException if a system exception occurred
     */
    public LFGlobalObjectiveState fetchByTreeID_First(Integer treeID,
        OrderByComparator orderByComparator) throws SystemException {
        List<LFGlobalObjectiveState> list = findByTreeID(treeID, 0, 1,
                orderByComparator);

        if (!list.isEmpty()) {
            return list.get(0);
        }

        return null;
    }

    /**
     * Returns the last l f global objective state in the ordered set where treeID = &#63;.
     *
     * @param treeID the tree i d
     * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
     * @return the last matching l f global objective state
     * @throws com.arcusys.learn.persistence.liferay.NoSuchLFGlobalObjectiveStateException if a matching l f global objective state could not be found
     * @throws SystemException if a system exception occurred
     */
    public LFGlobalObjectiveState findByTreeID_Last(Integer treeID,
        OrderByComparator orderByComparator)
        throws NoSuchLFGlobalObjectiveStateException, SystemException {
        LFGlobalObjectiveState lfGlobalObjectiveState = fetchByTreeID_Last(treeID,
                orderByComparator);

        if (lfGlobalObjectiveState != null) {
            return lfGlobalObjectiveState;
        }

        StringBundler msg = new StringBundler(4);

        msg.append(_NO_SUCH_ENTITY_WITH_KEY);

        msg.append("treeID=");
        msg.append(treeID);

        msg.append(StringPool.CLOSE_CURLY_BRACE);

        throw new NoSuchLFGlobalObjectiveStateException(msg.toString());
    }

    /**
     * Returns the last l f global objective state in the ordered set where treeID = &#63;.
     *
     * @param treeID the tree i d
     * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
     * @return the last matching l f global objective state, or <code>null</code> if a matching l f global objective state could not be found
     * @throws SystemException if a system exception occurred
     */
    public LFGlobalObjectiveState fetchByTreeID_Last(Integer treeID,
        OrderByComparator orderByComparator) throws SystemException {
        int count = countByTreeID(treeID);

        List<LFGlobalObjectiveState> list = findByTreeID(treeID, count - 1,
                count, orderByComparator);

        if (!list.isEmpty()) {
            return list.get(0);
        }

        return null;
    }

    /**
     * Returns the l f global objective states before and after the current l f global objective state in the ordered set where treeID = &#63;.
     *
     * @param id the primary key of the current l f global objective state
     * @param treeID the tree i d
     * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
     * @return the previous, current, and next l f global objective state
     * @throws com.arcusys.learn.persistence.liferay.NoSuchLFGlobalObjectiveStateException if a l f global objective state with the primary key could not be found
     * @throws SystemException if a system exception occurred
     */
    public LFGlobalObjectiveState[] findByTreeID_PrevAndNext(long id,
        Integer treeID, OrderByComparator orderByComparator)
        throws NoSuchLFGlobalObjectiveStateException, SystemException {
        LFGlobalObjectiveState lfGlobalObjectiveState = findByPrimaryKey(id);

        Session session = null;

        try {
            session = openSession();

            LFGlobalObjectiveState[] array = new LFGlobalObjectiveStateImpl[3];

            array[0] = getByTreeID_PrevAndNext(session, lfGlobalObjectiveState,
                    treeID, orderByComparator, true);

            array[1] = lfGlobalObjectiveState;

            array[2] = getByTreeID_PrevAndNext(session, lfGlobalObjectiveState,
                    treeID, orderByComparator, false);

            return array;
        } catch (Exception e) {
            throw processException(e);
        } finally {
            closeSession(session);
        }
    }

    protected LFGlobalObjectiveState getByTreeID_PrevAndNext(Session session,
        LFGlobalObjectiveState lfGlobalObjectiveState, Integer treeID,
        OrderByComparator orderByComparator, boolean previous) {
        StringBundler query = null;

        if (orderByComparator != null) {
            query = new StringBundler(6 +
                    (orderByComparator.getOrderByFields().length * 6));
        } else {
            query = new StringBundler(3);
        }

        query.append(_SQL_SELECT_LFGLOBALOBJECTIVESTATE_WHERE);

        if (treeID == null) {
            query.append(_FINDER_COLUMN_TREEID_TREEID_NULL_2);
        } else {
            query.append(_FINDER_COLUMN_TREEID_TREEID_2);
        }

        if (orderByComparator != null) {
            String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

            if (orderByConditionFields.length > 0) {
                query.append(WHERE_AND);
            }

            for (int i = 0; i < orderByConditionFields.length; i++) {
                query.append(_ORDER_BY_ENTITY_ALIAS);
                query.append(orderByConditionFields[i]);

                if ((i + 1) < orderByConditionFields.length) {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(WHERE_GREATER_THAN_HAS_NEXT);
                    } else {
                        query.append(WHERE_LESSER_THAN_HAS_NEXT);
                    }
                } else {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(WHERE_GREATER_THAN);
                    } else {
                        query.append(WHERE_LESSER_THAN);
                    }
                }
            }

            query.append(ORDER_BY_CLAUSE);

            String[] orderByFields = orderByComparator.getOrderByFields();

            for (int i = 0; i < orderByFields.length; i++) {
                query.append(_ORDER_BY_ENTITY_ALIAS);
                query.append(orderByFields[i]);

                if ((i + 1) < orderByFields.length) {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(ORDER_BY_ASC_HAS_NEXT);
                    } else {
                        query.append(ORDER_BY_DESC_HAS_NEXT);
                    }
                } else {
                    if (orderByComparator.isAscending() ^ previous) {
                        query.append(ORDER_BY_ASC);
                    } else {
                        query.append(ORDER_BY_DESC);
                    }
                }
            }
        }

        String sql = query.toString();

        Query q = session.createQuery(sql);

        q.setFirstResult(0);
        q.setMaxResults(2);

        QueryPos qPos = QueryPos.getInstance(q);

        if (treeID != null) {
            qPos.add(treeID.intValue());
        }

        if (orderByComparator != null) {
            Object[] values = orderByComparator.getOrderByConditionValues(lfGlobalObjectiveState);

            for (Object value : values) {
                qPos.add(value);
            }
        }

        List<LFGlobalObjectiveState> list = q.list();

        if (list.size() == 2) {
            return list.get(1);
        } else {
            return null;
        }
    }

    /**
     * Returns the l f global objective state where treeID = &#63; and mapKey = &#63; or throws a {@link com.arcusys.learn.persistence.liferay.NoSuchLFGlobalObjectiveStateException} if it could not be found.
     *
     * @param treeID the tree i d
     * @param mapKey the map key
     * @return the matching l f global objective state
     * @throws com.arcusys.learn.persistence.liferay.NoSuchLFGlobalObjectiveStateException if a matching l f global objective state could not be found
     * @throws SystemException if a system exception occurred
     */
    public LFGlobalObjectiveState findByTreeIDAndMapKey(Integer treeID,
        String mapKey)
        throws NoSuchLFGlobalObjectiveStateException, SystemException {
        LFGlobalObjectiveState lfGlobalObjectiveState = fetchByTreeIDAndMapKey(treeID,
                mapKey);

        if (lfGlobalObjectiveState == null) {
            StringBundler msg = new StringBundler(6);

            msg.append(_NO_SUCH_ENTITY_WITH_KEY);

            msg.append("treeID=");
            msg.append(treeID);

            msg.append(", mapKey=");
            msg.append(mapKey);

            msg.append(StringPool.CLOSE_CURLY_BRACE);

            if (_log.isWarnEnabled()) {
                _log.warn(msg.toString());
            }

            throw new NoSuchLFGlobalObjectiveStateException(msg.toString());
        }

        return lfGlobalObjectiveState;
    }

    /**
     * Returns the l f global objective state where treeID = &#63; and mapKey = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
     *
     * @param treeID the tree i d
     * @param mapKey the map key
     * @return the matching l f global objective state, or <code>null</code> if a matching l f global objective state could not be found
     * @throws SystemException if a system exception occurred
     */
    public LFGlobalObjectiveState fetchByTreeIDAndMapKey(Integer treeID,
        String mapKey) throws SystemException {
        return fetchByTreeIDAndMapKey(treeID, mapKey, true);
    }

    /**
     * Returns the l f global objective state where treeID = &#63; and mapKey = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
     *
     * @param treeID the tree i d
     * @param mapKey the map key
     * @param retrieveFromCache whether to use the finder cache
     * @return the matching l f global objective state, or <code>null</code> if a matching l f global objective state could not be found
     * @throws SystemException if a system exception occurred
     */
    public LFGlobalObjectiveState fetchByTreeIDAndMapKey(Integer treeID,
        String mapKey, boolean retrieveFromCache) throws SystemException {
        Object[] finderArgs = new Object[] { treeID, mapKey };

        Object result = null;

        if (retrieveFromCache) {
            result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_TREEIDANDMAPKEY,
                    finderArgs, this);
        }

        if (result instanceof LFGlobalObjectiveState) {
            LFGlobalObjectiveState lfGlobalObjectiveState = (LFGlobalObjectiveState) result;

            if (!Validator.equals(treeID, lfGlobalObjectiveState.getTreeID()) ||
                    !Validator.equals(mapKey, lfGlobalObjectiveState.getMapKey())) {
                result = null;
            }
        }

        if (result == null) {
            StringBundler query = new StringBundler(3);

            query.append(_SQL_SELECT_LFGLOBALOBJECTIVESTATE_WHERE);

            if (treeID == null) {
                query.append(_FINDER_COLUMN_TREEIDANDMAPKEY_TREEID_NULL_2);
            } else {
                query.append(_FINDER_COLUMN_TREEIDANDMAPKEY_TREEID_2);
            }

            if (mapKey == null) {
                query.append(_FINDER_COLUMN_TREEIDANDMAPKEY_MAPKEY_1);
            } else {
                if (mapKey.equals(StringPool.BLANK)) {
                    query.append(_FINDER_COLUMN_TREEIDANDMAPKEY_MAPKEY_3);
                } else {
                    query.append(_FINDER_COLUMN_TREEIDANDMAPKEY_MAPKEY_2);
                }
            }

            String sql = query.toString();

            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(sql);

                QueryPos qPos = QueryPos.getInstance(q);

                if (treeID != null) {
                    qPos.add(treeID.intValue());
                }

                if (mapKey != null) {
                    qPos.add(mapKey);
                }

                List<LFGlobalObjectiveState> list = q.list();

                result = list;

                LFGlobalObjectiveState lfGlobalObjectiveState = null;

                if (list.isEmpty()) {
                    FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_TREEIDANDMAPKEY,
                        finderArgs, list);
                } else {
                    lfGlobalObjectiveState = list.get(0);

                    cacheResult(lfGlobalObjectiveState);

                    if ((lfGlobalObjectiveState.getTreeID() != treeID) ||
                            (lfGlobalObjectiveState.getMapKey() == null) ||
                            !lfGlobalObjectiveState.getMapKey().equals(mapKey)) {
                        FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_TREEIDANDMAPKEY,
                            finderArgs, lfGlobalObjectiveState);
                    }
                }

                return lfGlobalObjectiveState;
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (result == null) {
                    FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_TREEIDANDMAPKEY,
                        finderArgs);
                }

                closeSession(session);
            }
        } else {
            if (result instanceof List<?>) {
                return null;
            } else {
                return (LFGlobalObjectiveState) result;
            }
        }
    }

    /**
     * Returns all the l f global objective states.
     *
     * @return the l f global objective states
     * @throws SystemException if a system exception occurred
     */
    public List<LFGlobalObjectiveState> findAll() throws SystemException {
        return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
    }

    /**
     * Returns a range of all the l f global objective states.
     *
     * <p>
     * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
     * </p>
     *
     * @param start the lower bound of the range of l f global objective states
     * @param end the upper bound of the range of l f global objective states (not inclusive)
     * @return the range of l f global objective states
     * @throws SystemException if a system exception occurred
     */
    public List<LFGlobalObjectiveState> findAll(int start, int end)
        throws SystemException {
        return findAll(start, end, null);
    }

    /**
     * Returns an ordered range of all the l f global objective states.
     *
     * <p>
     * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
     * </p>
     *
     * @param start the lower bound of the range of l f global objective states
     * @param end the upper bound of the range of l f global objective states (not inclusive)
     * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
     * @return the ordered range of l f global objective states
     * @throws SystemException if a system exception occurred
     */
    public List<LFGlobalObjectiveState> findAll(int start, int end,
        OrderByComparator orderByComparator) throws SystemException {
        FinderPath finderPath = null;
        Object[] finderArgs = new Object[] { start, end, orderByComparator };

        if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
                (orderByComparator == null)) {
            finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
            finderArgs = FINDER_ARGS_EMPTY;
        } else {
            finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
            finderArgs = new Object[] { start, end, orderByComparator };
        }

        List<LFGlobalObjectiveState> list = (List<LFGlobalObjectiveState>) FinderCacheUtil.getResult(finderPath,
                finderArgs, this);

        if (list == null) {
            StringBundler query = null;
            String sql = null;

            if (orderByComparator != null) {
                query = new StringBundler(2 +
                        (orderByComparator.getOrderByFields().length * 3));

                query.append(_SQL_SELECT_LFGLOBALOBJECTIVESTATE);

                appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
                    orderByComparator);

                sql = query.toString();
            } else {
                sql = _SQL_SELECT_LFGLOBALOBJECTIVESTATE;
            }

            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(sql);

                if (orderByComparator == null) {
                    list = (List<LFGlobalObjectiveState>) QueryUtil.list(q,
                            getDialect(), start, end, false);

                    Collections.sort(list);
                } else {
                    list = (List<LFGlobalObjectiveState>) QueryUtil.list(q,
                            getDialect(), start, end);
                }
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (list == null) {
                    FinderCacheUtil.removeResult(finderPath, finderArgs);
                } else {
                    cacheResult(list);

                    FinderCacheUtil.putResult(finderPath, finderArgs, list);
                }

                closeSession(session);
            }
        }

        return list;
    }

    /**
     * Removes all the l f global objective states where treeID = &#63; from the database.
     *
     * @param treeID the tree i d
     * @throws SystemException if a system exception occurred
     */
    public void removeByTreeID(Integer treeID) throws SystemException {
        for (LFGlobalObjectiveState lfGlobalObjectiveState : findByTreeID(
                treeID)) {
            remove(lfGlobalObjectiveState);
        }
    }

    /**
     * Removes the l f global objective state where treeID = &#63; and mapKey = &#63; from the database.
     *
     * @param treeID the tree i d
     * @param mapKey the map key
     * @return the l f global objective state that was removed
     * @throws SystemException if a system exception occurred
     */
    public LFGlobalObjectiveState removeByTreeIDAndMapKey(Integer treeID,
        String mapKey)
        throws NoSuchLFGlobalObjectiveStateException, SystemException {
        LFGlobalObjectiveState lfGlobalObjectiveState = findByTreeIDAndMapKey(treeID,
                mapKey);

        return remove(lfGlobalObjectiveState);
    }

    /**
     * Removes all the l f global objective states from the database.
     *
     * @throws SystemException if a system exception occurred
     */
    public void removeAll() throws SystemException {
        for (LFGlobalObjectiveState lfGlobalObjectiveState : findAll()) {
            remove(lfGlobalObjectiveState);
        }
    }

    /**
     * Returns the number of l f global objective states where treeID = &#63;.
     *
     * @param treeID the tree i d
     * @return the number of matching l f global objective states
     * @throws SystemException if a system exception occurred
     */
    public int countByTreeID(Integer treeID) throws SystemException {
        Object[] finderArgs = new Object[] { treeID };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_TREEID,
                finderArgs, this);

        if (count == null) {
            StringBundler query = new StringBundler(2);

            query.append(_SQL_COUNT_LFGLOBALOBJECTIVESTATE_WHERE);

            if (treeID == null) {
                query.append(_FINDER_COLUMN_TREEID_TREEID_NULL_2);
            } else {
                query.append(_FINDER_COLUMN_TREEID_TREEID_2);
            }

            String sql = query.toString();

            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(sql);

                QueryPos qPos = QueryPos.getInstance(q);

                if (treeID != null) {
                    qPos.add(treeID.intValue());
                }

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_TREEID,
                    finderArgs, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    /**
     * Returns the number of l f global objective states where treeID = &#63; and mapKey = &#63;.
     *
     * @param treeID the tree i d
     * @param mapKey the map key
     * @return the number of matching l f global objective states
     * @throws SystemException if a system exception occurred
     */
    public int countByTreeIDAndMapKey(Integer treeID, String mapKey)
        throws SystemException {
        Object[] finderArgs = new Object[] { treeID, mapKey };

        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_TREEIDANDMAPKEY,
                finderArgs, this);

        if (count == null) {
            StringBundler query = new StringBundler(3);

            query.append(_SQL_COUNT_LFGLOBALOBJECTIVESTATE_WHERE);

            if (treeID == null) {
                query.append(_FINDER_COLUMN_TREEIDANDMAPKEY_TREEID_NULL_2);
            } else {
                query.append(_FINDER_COLUMN_TREEIDANDMAPKEY_TREEID_2);
            }

            if (mapKey == null) {
                query.append(_FINDER_COLUMN_TREEIDANDMAPKEY_MAPKEY_1);
            } else {
                if (mapKey.equals(StringPool.BLANK)) {
                    query.append(_FINDER_COLUMN_TREEIDANDMAPKEY_MAPKEY_3);
                } else {
                    query.append(_FINDER_COLUMN_TREEIDANDMAPKEY_MAPKEY_2);
                }
            }

            String sql = query.toString();

            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(sql);

                QueryPos qPos = QueryPos.getInstance(q);

                if (treeID != null) {
                    qPos.add(treeID.intValue());
                }

                if (mapKey != null) {
                    qPos.add(mapKey);
                }

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_TREEIDANDMAPKEY,
                    finderArgs, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    /**
     * Returns the number of l f global objective states.
     *
     * @return the number of l f global objective states
     * @throws SystemException if a system exception occurred
     */
    public int countAll() throws SystemException {
        Long count = (Long) FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
                FINDER_ARGS_EMPTY, this);

        if (count == null) {
            Session session = null;

            try {
                session = openSession();

                Query q = session.createQuery(_SQL_COUNT_LFGLOBALOBJECTIVESTATE);

                count = (Long) q.uniqueResult();
            } catch (Exception e) {
                throw processException(e);
            } finally {
                if (count == null) {
                    count = Long.valueOf(0);
                }

                FinderCacheUtil.putResult(FINDER_PATH_COUNT_ALL,
                    FINDER_ARGS_EMPTY, count);

                closeSession(session);
            }
        }

        return count.intValue();
    }

    /**
     * Initializes the l f global objective state persistence.
     */
    public void afterPropertiesSet() {
        String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
                    com.liferay.util.service.ServiceProps.get(
                        "value.object.listener.com.arcusys.learn.persistence.liferay.model.LFGlobalObjectiveState")));

        if (listenerClassNames.length > 0) {
            try {
                List<ModelListener<LFGlobalObjectiveState>> listenersList = new ArrayList<ModelListener<LFGlobalObjectiveState>>();

                for (String listenerClassName : listenerClassNames) {
                    listenersList.add((ModelListener<LFGlobalObjectiveState>) InstanceFactory.newInstance(
                            listenerClassName));
                }

                listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
            } catch (Exception e) {
                _log.error(e);
            }
        }
    }

    public void destroy() {
        EntityCacheUtil.removeCache(LFGlobalObjectiveStateImpl.class.getName());
        FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
        FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
    }
}
