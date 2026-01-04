package com.zxtx.hummer.system.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OpenChannelExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OpenChannelExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIsNull() {
            addCriterion("operator_id is null");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIsNotNull() {
            addCriterion("operator_id is not null");
            return (Criteria) this;
        }

        public Criteria andOperatorIdEqualTo(String value) {
            addCriterion("operator_id =", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotEqualTo(String value) {
            addCriterion("operator_id <>", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdGreaterThan(String value) {
            addCriterion("operator_id >", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdGreaterThanOrEqualTo(String value) {
            addCriterion("operator_id >=", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLessThan(String value) {
            addCriterion("operator_id <", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLessThanOrEqualTo(String value) {
            addCriterion("operator_id <=", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLike(String value) {
            addCriterion("operator_id like", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotLike(String value) {
            addCriterion("operator_id not like", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIn(List<String> values) {
            addCriterion("operator_id in", values, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotIn(List<String> values) {
            addCriterion("operator_id not in", values, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdBetween(String value1, String value2) {
            addCriterion("operator_id between", value1, value2, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotBetween(String value1, String value2) {
            addCriterion("operator_id not between", value1, value2, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorNameIsNull() {
            addCriterion("operator_name is null");
            return (Criteria) this;
        }

        public Criteria andOperatorNameIsNotNull() {
            addCriterion("operator_name is not null");
            return (Criteria) this;
        }

        public Criteria andOperatorNameEqualTo(String value) {
            addCriterion("operator_name =", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameNotEqualTo(String value) {
            addCriterion("operator_name <>", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameGreaterThan(String value) {
            addCriterion("operator_name >", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameGreaterThanOrEqualTo(String value) {
            addCriterion("operator_name >=", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameLessThan(String value) {
            addCriterion("operator_name <", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameLessThanOrEqualTo(String value) {
            addCriterion("operator_name <=", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameLike(String value) {
            addCriterion("operator_name like", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameNotLike(String value) {
            addCriterion("operator_name not like", value, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameIn(List<String> values) {
            addCriterion("operator_name in", values, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameNotIn(List<String> values) {
            addCriterion("operator_name not in", values, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameBetween(String value1, String value2) {
            addCriterion("operator_name between", value1, value2, "operatorName");
            return (Criteria) this;
        }

        public Criteria andOperatorNameNotBetween(String value1, String value2) {
            addCriterion("operator_name not between", value1, value2, "operatorName");
            return (Criteria) this;
        }

        public Criteria andEmpIdIsNull() {
            addCriterion("emp_id is null");
            return (Criteria) this;
        }

        public Criteria andEmpIdIsNotNull() {
            addCriterion("emp_id is not null");
            return (Criteria) this;
        }

        public Criteria andEmpIdEqualTo(Long value) {
            addCriterion("emp_id =", value, "empId");
            return (Criteria) this;
        }

        public Criteria andEmpIdNotEqualTo(Long value) {
            addCriterion("emp_id <>", value, "empId");
            return (Criteria) this;
        }

        public Criteria andEmpIdGreaterThan(Long value) {
            addCriterion("emp_id >", value, "empId");
            return (Criteria) this;
        }

        public Criteria andEmpIdGreaterThanOrEqualTo(Long value) {
            addCriterion("emp_id >=", value, "empId");
            return (Criteria) this;
        }

        public Criteria andEmpIdLessThan(Long value) {
            addCriterion("emp_id <", value, "empId");
            return (Criteria) this;
        }

        public Criteria andEmpIdLessThanOrEqualTo(Long value) {
            addCriterion("emp_id <=", value, "empId");
            return (Criteria) this;
        }

        public Criteria andEmpIdIn(List<Long> values) {
            addCriterion("emp_id in", values, "empId");
            return (Criteria) this;
        }

        public Criteria andEmpIdNotIn(List<Long> values) {
            addCriterion("emp_id not in", values, "empId");
            return (Criteria) this;
        }

        public Criteria andEmpIdBetween(Long value1, Long value2) {
            addCriterion("emp_id between", value1, value2, "empId");
            return (Criteria) this;
        }

        public Criteria andEmpIdNotBetween(Long value1, Long value2) {
            addCriterion("emp_id not between", value1, value2, "empId");
            return (Criteria) this;
        }

        public Criteria andMd5KeyIsNull() {
            addCriterion("md5_key is null");
            return (Criteria) this;
        }

        public Criteria andMd5KeyIsNotNull() {
            addCriterion("md5_key is not null");
            return (Criteria) this;
        }

        public Criteria andMd5KeyEqualTo(String value) {
            addCriterion("md5_key =", value, "md5Key");
            return (Criteria) this;
        }

        public Criteria andMd5KeyNotEqualTo(String value) {
            addCriterion("md5_key <>", value, "md5Key");
            return (Criteria) this;
        }

        public Criteria andMd5KeyGreaterThan(String value) {
            addCriterion("md5_key >", value, "md5Key");
            return (Criteria) this;
        }

        public Criteria andMd5KeyGreaterThanOrEqualTo(String value) {
            addCriterion("md5_key >=", value, "md5Key");
            return (Criteria) this;
        }

        public Criteria andMd5KeyLessThan(String value) {
            addCriterion("md5_key <", value, "md5Key");
            return (Criteria) this;
        }

        public Criteria andMd5KeyLessThanOrEqualTo(String value) {
            addCriterion("md5_key <=", value, "md5Key");
            return (Criteria) this;
        }

        public Criteria andMd5KeyLike(String value) {
            addCriterion("md5_key like", value, "md5Key");
            return (Criteria) this;
        }

        public Criteria andMd5KeyNotLike(String value) {
            addCriterion("md5_key not like", value, "md5Key");
            return (Criteria) this;
        }

        public Criteria andMd5KeyIn(List<String> values) {
            addCriterion("md5_key in", values, "md5Key");
            return (Criteria) this;
        }

        public Criteria andMd5KeyNotIn(List<String> values) {
            addCriterion("md5_key not in", values, "md5Key");
            return (Criteria) this;
        }

        public Criteria andMd5KeyBetween(String value1, String value2) {
            addCriterion("md5_key between", value1, value2, "md5Key");
            return (Criteria) this;
        }

        public Criteria andMd5KeyNotBetween(String value1, String value2) {
            addCriterion("md5_key not between", value1, value2, "md5Key");
            return (Criteria) this;
        }

        public Criteria andAesKeyIsNull() {
            addCriterion("aes_key is null");
            return (Criteria) this;
        }

        public Criteria andAesKeyIsNotNull() {
            addCriterion("aes_key is not null");
            return (Criteria) this;
        }

        public Criteria andAesKeyEqualTo(String value) {
            addCriterion("aes_key =", value, "aesKey");
            return (Criteria) this;
        }

        public Criteria andAesKeyNotEqualTo(String value) {
            addCriterion("aes_key <>", value, "aesKey");
            return (Criteria) this;
        }

        public Criteria andAesKeyGreaterThan(String value) {
            addCriterion("aes_key >", value, "aesKey");
            return (Criteria) this;
        }

        public Criteria andAesKeyGreaterThanOrEqualTo(String value) {
            addCriterion("aes_key >=", value, "aesKey");
            return (Criteria) this;
        }

        public Criteria andAesKeyLessThan(String value) {
            addCriterion("aes_key <", value, "aesKey");
            return (Criteria) this;
        }

        public Criteria andAesKeyLessThanOrEqualTo(String value) {
            addCriterion("aes_key <=", value, "aesKey");
            return (Criteria) this;
        }

        public Criteria andAesKeyLike(String value) {
            addCriterion("aes_key like", value, "aesKey");
            return (Criteria) this;
        }

        public Criteria andAesKeyNotLike(String value) {
            addCriterion("aes_key not like", value, "aesKey");
            return (Criteria) this;
        }

        public Criteria andAesKeyIn(List<String> values) {
            addCriterion("aes_key in", values, "aesKey");
            return (Criteria) this;
        }

        public Criteria andAesKeyNotIn(List<String> values) {
            addCriterion("aes_key not in", values, "aesKey");
            return (Criteria) this;
        }

        public Criteria andAesKeyBetween(String value1, String value2) {
            addCriterion("aes_key between", value1, value2, "aesKey");
            return (Criteria) this;
        }

        public Criteria andAesKeyNotBetween(String value1, String value2) {
            addCriterion("aes_key not between", value1, value2, "aesKey");
            return (Criteria) this;
        }

        public Criteria andRelatedAppIdIsNull() {
            addCriterion("related_app_id is null");
            return (Criteria) this;
        }

        public Criteria andRelatedAppIdIsNotNull() {
            addCriterion("related_app_id is not null");
            return (Criteria) this;
        }

        public Criteria andRelatedAppIdEqualTo(String value) {
            addCriterion("related_app_id =", value, "relatedAppId");
            return (Criteria) this;
        }

        public Criteria andRelatedAppIdNotEqualTo(String value) {
            addCriterion("related_app_id <>", value, "relatedAppId");
            return (Criteria) this;
        }

        public Criteria andRelatedAppIdGreaterThan(String value) {
            addCriterion("related_app_id >", value, "relatedAppId");
            return (Criteria) this;
        }

        public Criteria andRelatedAppIdGreaterThanOrEqualTo(String value) {
            addCriterion("related_app_id >=", value, "relatedAppId");
            return (Criteria) this;
        }

        public Criteria andRelatedAppIdLessThan(String value) {
            addCriterion("related_app_id <", value, "relatedAppId");
            return (Criteria) this;
        }

        public Criteria andRelatedAppIdLessThanOrEqualTo(String value) {
            addCriterion("related_app_id <=", value, "relatedAppId");
            return (Criteria) this;
        }

        public Criteria andRelatedAppIdLike(String value) {
            addCriterion("related_app_id like", value, "relatedAppId");
            return (Criteria) this;
        }

        public Criteria andRelatedAppIdNotLike(String value) {
            addCriterion("related_app_id not like", value, "relatedAppId");
            return (Criteria) this;
        }

        public Criteria andRelatedAppIdIn(List<String> values) {
            addCriterion("related_app_id in", values, "relatedAppId");
            return (Criteria) this;
        }

        public Criteria andRelatedAppIdNotIn(List<String> values) {
            addCriterion("related_app_id not in", values, "relatedAppId");
            return (Criteria) this;
        }

        public Criteria andRelatedAppIdBetween(String value1, String value2) {
            addCriterion("related_app_id between", value1, value2, "relatedAppId");
            return (Criteria) this;
        }

        public Criteria andRelatedAppIdNotBetween(String value1, String value2) {
            addCriterion("related_app_id not between", value1, value2, "relatedAppId");
            return (Criteria) this;
        }

        public Criteria andRelatedLocationIsNull() {
            addCriterion("related_location is null");
            return (Criteria) this;
        }

        public Criteria andRelatedLocationIsNotNull() {
            addCriterion("related_location is not null");
            return (Criteria) this;
        }

        public Criteria andRelatedLocationEqualTo(String value) {
            addCriterion("related_location =", value, "relatedLocation");
            return (Criteria) this;
        }

        public Criteria andRelatedLocationNotEqualTo(String value) {
            addCriterion("related_location <>", value, "relatedLocation");
            return (Criteria) this;
        }

        public Criteria andRelatedLocationGreaterThan(String value) {
            addCriterion("related_location >", value, "relatedLocation");
            return (Criteria) this;
        }

        public Criteria andRelatedLocationGreaterThanOrEqualTo(String value) {
            addCriterion("related_location >=", value, "relatedLocation");
            return (Criteria) this;
        }

        public Criteria andRelatedLocationLessThan(String value) {
            addCriterion("related_location <", value, "relatedLocation");
            return (Criteria) this;
        }

        public Criteria andRelatedLocationLessThanOrEqualTo(String value) {
            addCriterion("related_location <=", value, "relatedLocation");
            return (Criteria) this;
        }

        public Criteria andRelatedLocationLike(String value) {
            addCriterion("related_location like", value, "relatedLocation");
            return (Criteria) this;
        }

        public Criteria andRelatedLocationNotLike(String value) {
            addCriterion("related_location not like", value, "relatedLocation");
            return (Criteria) this;
        }

        public Criteria andRelatedLocationIn(List<String> values) {
            addCriterion("related_location in", values, "relatedLocation");
            return (Criteria) this;
        }

        public Criteria andRelatedLocationNotIn(List<String> values) {
            addCriterion("related_location not in", values, "relatedLocation");
            return (Criteria) this;
        }

        public Criteria andRelatedLocationBetween(String value1, String value2) {
            addCriterion("related_location between", value1, value2, "relatedLocation");
            return (Criteria) this;
        }

        public Criteria andRelatedLocationNotBetween(String value1, String value2) {
            addCriterion("related_location not between", value1, value2, "relatedLocation");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLikeInsensitive(String value) {
            addCriterion("upper(operator_id) like", value.toUpperCase(), "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorNameLikeInsensitive(String value) {
            addCriterion("upper(operator_name) like", value.toUpperCase(), "operatorName");
            return (Criteria) this;
        }

        public Criteria andMd5KeyLikeInsensitive(String value) {
            addCriterion("upper(md5_key) like", value.toUpperCase(), "md5Key");
            return (Criteria) this;
        }

        public Criteria andAesKeyLikeInsensitive(String value) {
            addCriterion("upper(aes_key) like", value.toUpperCase(), "aesKey");
            return (Criteria) this;
        }

        public Criteria andRelatedAppIdLikeInsensitive(String value) {
            addCriterion("upper(related_app_id) like", value.toUpperCase(), "relatedAppId");
            return (Criteria) this;
        }

        public Criteria andRelatedLocationLikeInsensitive(String value) {
            addCriterion("upper(related_location) like", value.toUpperCase(), "relatedLocation");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}